package org.basis.framework.excel.annotation;

import java.lang.annotation.*;

/**
 * 列忽略，不参与表单生成
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ExcelIgnore {
}
