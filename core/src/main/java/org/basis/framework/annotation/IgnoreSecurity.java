package org.basis.framework.annotation;

import java.lang.annotation.*;

/**
 * 忽略登录检查
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoreSecurity {
}
