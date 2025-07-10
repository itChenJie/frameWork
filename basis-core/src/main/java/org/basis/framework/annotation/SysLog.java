package org.basis.framework.annotation;

import java.lang.annotation.*;

/**
 * @Description 系统日志注解
 * @Author ChenJie
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {
    String value() default "";
}
