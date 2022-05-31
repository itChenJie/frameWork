package org.basis.framework.excel.easypoi;

import cn.afterturn.easypoi.handler.impl.ExcelDataHandlerDefaultImpl;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * @Description
 *  数据类型转换
 * @Author ChenWenJie
 * @Data 2022/5/31 15:47
 **/
@Slf4j
public class ConverterDataHandler<T> extends ExcelDataHandlerDefaultImpl<T> {
    private Map<String, Converter> exportConvertMap = new HashMap();
    private Map<String, ImportConverter> importConvertMap = new HashMap();

    public ConverterDataHandler() {
    }

    public Object exportHandler(T obj, String name, Object value) {
        return this.exportConvertMap.containsKey(name) ? ((Converter)this.exportConvertMap.get(name)).convert(value) : super.exportHandler(obj, name, value);
    }

    public Object importHandler(T obj, String name, Object value) {
        return this.importConvertMap.containsKey(name) ? ((ImportConverter)this.importConvertMap.get(name)).convert(obj, value) : super.importHandler(obj, name, value);
    }

    public String[] getNeedHandlerFields() {
        Set<String> keys = this.exportConvertMap.keySet();
        HashSet<String> newKeys = new HashSet(keys);
        newKeys.addAll(this.importConvertMap.keySet());
        return (String[])newKeys.toArray(new String[0]);
    }

    public <E> ConverterDataHandler<T> addExportConvert(Converter<E> value, String... key) {
        Arrays.asList(key).forEach((s) -> {
            Converter converter = (Converter)this.exportConvertMap.put(s, value);
        });
        return this;
    }

    public ConverterDataHandler<T> addImportConvert(ImportConverter<T> value, String... key) {
        Arrays.asList(key).forEach((s) -> {
            ImportConverter converter = (ImportConverter)this.importConvertMap.put(s, value);
        });
        return this;
    }
}
