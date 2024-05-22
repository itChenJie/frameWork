package org.basis.framework.annotation;

import java.lang.annotation.*;

/**
 * @Description 系统日志注解
 * @Author ChenWenJie
 * @Data 2021/7/6 4:47 下午
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {
    String value() default "";
}
