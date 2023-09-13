package org.basis.framework.annotation;

import java.lang.annotation.*;

@Target({
        ElementType.METHOD,
        ElementType.TYPE
})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    String value() default "";
}
