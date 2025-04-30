package org.basis.framework.excel.annotation;
import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ExcelFormat {
    /**
     * 字段类型
     * @return
     */
    Class<?> checkClass() default Object.class;

    /**
     * 格式
     * @return
     */
    String pattern() default "";

    /**
     * 正则
     * @return
     */
    String matcher() default "";

    /**
     * 正则不对 错误描述
     * @return
     */
    String matcherDescription() default "";
}
