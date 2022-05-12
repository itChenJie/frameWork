package org.basis.framework.annotation;

import java.lang.annotation.*;

/**
 * 分布式锁注解
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DistributeLock {
    /**
     * @author 锁的key前缀
     */
    String lockKeyPrefix() default "";
    /**
     * @author 业务code
     */
    String businessCode() default "";
    /**
     * 特定参数识别，默认不取业务字段
     */
    int lockFiled() default -1;
    /**
     * 超时重试次数
     */
    int tryCount() default 3;
    /**
     * @author 获取锁的等待时间(毫秒)
     */
    long waitTime() default 0;
    /**
     * @author 释放的时间(毫秒)
     */
    long leaseTime() default 0;
}
