package org.basis.framework.aspect;

import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.basis.framework.annotation.DistributeLock;
import org.basis.framework.error.RRException;
import org.basis.framework.utils.SnowFlakeUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @Description
 * 采用注解+Aop的形式使用分布式锁
 * @Author ChenWenJie
 * @Data 2021/9/29 2:57 下午
 **/
@Slf4j
@Aspect
@Component
public class DistributeLockAspect {
    @Autowired
    private RedissonClient redissonClient;

    @Pointcut(value = "@annotation(org.basis.framework.annotation.DistributeLock)")
    private void pointcut(){}

    @Around("pointcut()")
    public Object lock(ProceedingJoinPoint joinPoint)throws Throwable {
       MethodSignature signature= (MethodSignature)joinPoint.getSignature();
        Method method = signature.getMethod();
        DistributeLock distributeLock = method.getAnnotation(DistributeLock.class);
        String lockKey=getLockKeyName(distributeLock,joinPoint);
        // 默认过期时间是30秒
        RLock rLock=redissonClient.getLock(lockKey);
        boolean b=getLock(rLock,distributeLock);
        try {
            if(b){
                return joinPoint.proceed();
            }else {
                throw new RRException("网络繁忙,请稍后再试");
            }
        }catch (Exception e){
            throw new RRException("服务器繁忙,请稍后再试");
        }finally {
            if(b){
                if(rLock.isLocked()){
                    rLock.unlock();
                }
            }
        }
    }

    /**
     * 获取锁
     * @param rLock
     * @param distributeLock
     * @return
     * @throws InterruptedException
     */
    private boolean getLock(RLock rLock, DistributeLock distributeLock) throws InterruptedException {
        long waitTime = distributeLock.waitTime();
        long leaseTime = distributeLock.leaseTime();
        boolean result = false;
        int count = 1;
        while (!result){
            if (waitTime>0&&leaseTime>0){
                result = rLock.tryLock(waitTime,leaseTime, TimeUnit.MICROSECONDS);
            }else if (leaseTime>0){
                result = rLock.tryLock(0,leaseTime,TimeUnit.MILLISECONDS);
            }else if (waitTime>0){
                result = rLock.tryLock(waitTime,TimeUnit.MILLISECONDS);
            }else {
                result = rLock.tryLock();
            }
            count++;
            if (count>distributeLock.tryCount())
                throw new RRException("获取分布式锁失败！");
            Thread.sleep(distributeLock.tryCountWaitTime());
        }
        return result;
    }

    /**
     * 获取锁的key name
     * @param distributeLock 分发锁注解
     * @param joinPoint
     * @return
     */
    private String getLockKeyName(DistributeLock distributeLock, ProceedingJoinPoint joinPoint) {
        String keyPrefix = distributeLock.lockKeyPrefix();
        String businessCode = distributeLock.businessCode();
        String keyName = "";
        String ukString = "";
        if (distributeLock.lockFiled()>-1){
            Object[] args = joinPoint.getArgs();
            ukString = args[distributeLock.lockFiled()].toString();
        }
        if (StringUtils.isNotBlank(ukString)){
            CollectionUtil.join(Arrays.asList(keyPrefix,businessCode, ukString),":");
        }else {
            CollectionUtil.join(Arrays.asList(keyPrefix,businessCode),":");
        }
        return keyName;
    }
}
