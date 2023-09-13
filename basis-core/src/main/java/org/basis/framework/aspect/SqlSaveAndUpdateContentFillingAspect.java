package org.basis.framework.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.basis.framework.base.AbstractBaseUtil;
import org.basis.framework.entity.BaseEntity;
import org.basis.framework.entity.BaseTimeEntity;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Description
 *  新增、更新sql
 *  切面注入：创建人、更新人、创建时间、更新时间
 * @Author ChenWenJie
 * @Data 2021/9/26 4:42 下午
 **/
@Aspect
@Component
public class SqlSaveAndUpdateContentFillingAspect {
    @Pointcut("execution(* com.baomidou.mybatisplus.core.mapper.BaseMapper.insert(..))")
    private void savePointCut(){}

    @Pointcut("execution(* com.baomidou.mybatisplus.core.mapper.BaseMapper.update*(..))")
    private void updatePointCut(){}

    @Before("savePointCut()")
    public void doBefore(JoinPoint joinPoint){
        saveFilling(joinPoint);
    }

    @Before("updatePointCut()")
    public void doUpdateBefore(JoinPoint joinPoint){
        updateFilling(joinPoint);
    }

    /**
     * 新增sql 填充创建人、创建时间
     * @param joinPoint
     */
    private void saveFilling(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args!=null){
            for (Object arg : args) {
                if (arg instanceof BaseEntity) {
                    BaseEntity baseEntity = (BaseEntity) arg;
                    baseEntity.setCreateUserId(AbstractBaseUtil.getUserId());
                    baseEntity.setCreateTime(new Date());
                }else if(arg instanceof BaseTimeEntity ){
                    BaseTimeEntity baseTimeEntity = (BaseTimeEntity) arg;
                    baseTimeEntity.setCreateTime(new Date());
                }
            }
        }
    }

    /**
     * 更新sql填充 更新人、更新时间
     * @param joinPoint
     */
    private void updateFilling(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args!=null){
            for (Object arg : args) {
                if (arg instanceof BaseEntity) {
                    BaseEntity baseEntity = (BaseEntity) arg;
                    baseEntity.setUpdateUserId(AbstractBaseUtil.getUserId());
                    baseEntity.setUpdateTime(new Date());
                }else if(arg instanceof BaseTimeEntity ){
                    BaseTimeEntity baseTimeEntity = (BaseTimeEntity) arg;
                    baseTimeEntity.setUpdateTime(new Date());
                }
            }
        }
    }
}
