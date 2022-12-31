package org.basis.framework.excel.annotation;

import java.lang.annotation.*;

/**
 * Excel 列属性 object
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ExcelProperty {
    /**
     * 列名称
     * @return
     */
    String[] value() default {""};


    int index() default -1;

    int order() default Integer.MAX_VALUE;

    @Deprecated
    String format() default "";

    /**
     * 列类型
     * @return
     */
    Class<?> typeClass() default String.class;
}
