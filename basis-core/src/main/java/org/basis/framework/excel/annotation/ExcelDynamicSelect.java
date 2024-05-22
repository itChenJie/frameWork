package org.basis.framework.excel.annotation;

import java.util.List;
import java.util.Map;

/**
 * Excel 单元格 动态下拉值
 */
public interface ExcelDynamicSelect {
    /**
     * 获取动态生成的下拉框可选数据
     * @return 动态生成的下拉框可选数据
     */
    String[] getSource(String key, Map<String, List<String>> map);
}