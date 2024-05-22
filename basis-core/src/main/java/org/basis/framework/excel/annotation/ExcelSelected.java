package org.basis.framework.excel.annotation;

import java.lang.annotation.*;

/**
 * Excel 单元格 下拉选项
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelSelected {
    /**
     * 固定下拉内容
     */
    String[] source() default {};

    Class<? extends ExcelDynamicSelect>[] sourceClass() default {};
    /**
     * 字典key
     * @return
     */
    String key() default "";
    /**
     * 设置下拉框的起始行，默认为第二行
     */
    int firstRow() default 1;
 
    /**
     * 设置下拉框的结束行，默认为最后一行
     */
    int lastRow() default 0x10000;
}