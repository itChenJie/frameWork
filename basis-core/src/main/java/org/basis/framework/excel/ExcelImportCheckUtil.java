package org.basis.framework.excel;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.extern.slf4j.Slf4j;
import org.basis.framework.excel.annotation.ExcelFormat;
import org.basis.framework.excel.annotation.ExcelSelected;
import org.basis.framework.utils.DateUtil;
import org.basis.framework.regexp.RegexpUtil;
import org.basis.framework.validation.ValidationUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Description Excel 导入检查工具
 * 先进行 JSR303校验错误 ，通过反射 获取字段上的 ExcelProperty、ExcelFormat、ExcelSelected
 * 进行 校验（类型、格式(正则)）
 *
 **/
@Slf4j
public class ExcelImportCheckUtil {
    private static final String DATE_PARSE_MSG = "日期格式无效（应为 %s）";
    private static final String INVALID_NUMBER_MSG = "非有效数字格式";

    /**
     * 校验信息
     * @param target 校验对象
     * @param selectionSource 下拉框数据源
     * @return
     */
    public static String formatCheck(Object target,Map<String,List<String>> selectionSource) {
        Assert.notNull(target, "校验对象不能为空");

        List<String> errors = new ArrayList<>();
        collectValidationErrors(target, errors);
        processAnnotatedFields(target, selectionSource, errors);
        return String.join(", ", errors);
    }

    /**
     * 收集JSR303校验错误
     * @param target 校验对象
     * @param errors
     */
    private static void collectValidationErrors(Object target, List<String> errors) {
        String validateResult = ValidationUtils.validate(target);
        Optional.ofNullable(validateResult).filter(StrUtil::isNotBlank).ifPresent(errors::add);
    }

    /**
     * 处理带 ExcelProperty 注解的字段
     * @see com.alibaba.excel.annotation.ExcelProperty
     * @param target 校验对象
     * @param selectionSource 下拉框数据源
     * @param errors
     */
    private static void processAnnotatedFields(Object target, Map<String, List<String>> selectionSource,
                                               List<String> errors) {
        Arrays.stream(target.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(ExcelProperty.class))
                .forEach(field -> processField(field, target, selectionSource, errors));
    }

    /**
     * 处理每个字段
     * @param field 字段
     * @param target 校验对象
     * @param selectionSource 下拉框数据源
     * @param errors
     */
    private static void processField(Field field, Object target, Map<String, List<String>> selectionSource,
                                     List<String> errors) {
        try {
            Object value = getFieldValue(field, target);

            if (value==null)
                return;

            ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
            String columnTitle = Arrays.toString(excelProperty.value());

            validateFieldFormat(field, value, columnTitle, errors);
            validateFieldSelection(field, value, columnTitle, selectionSource, errors);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("字段访问失败: " + field.getName(), e);
        }
    }

    /**
     * 校验每个字段的格式
     * @param field 字段
     * @param value 校验对象
     * @param columnTitle 标题
     * @param errors
     */
    private static void validateFieldFormat(Field field, Object value, String columnTitle, List<String> errors) {
        Optional.ofNullable(field.getAnnotation(ExcelFormat.class)).ifPresent(anno -> {
            String error = null;
            String simpleName = anno.checkClass().getSimpleName();
            switch (simpleName) {
                case "Date":
                    error = validateDate((String) value, anno.matcher());
                    break;
                case "BigDecimal":
                    error = validateBigDecimal((String) value);
                    break;
                case "String":
                    error = validateStringFormat((String) value, anno.matcher(), anno.matcherDescription());
                    break;
                default:
                    log.warn(" not support validate type :{}",simpleName);
                    break;
            }
            Optional.ofNullable(error).ifPresent(e -> errors.add(columnTitle + e));
        });
    }

    /**
     * 下拉选择校验
     * @param field 字段
     * @param value 内容
     * @param columnTitle 标题
     * @param source 数据源
     * @param errors 错误信息
     */
    private static void validateFieldSelection(Field field, Object value, String columnTitle
            , Map<String, List<String>> source, List<String> errors) {

        Optional.ofNullable(field.getAnnotation(ExcelSelected.class))
                .ifPresent(anno -> {
                    List<String> allowedValues = Optional.ofNullable(source.get(anno.key()))
                            .orElseGet(() -> Arrays.asList(anno.source()));

            if (!allowedValues.contains(value.toString())) {
                errors.add(columnTitle + "的值不在允许范围内");
            }
        });
    }

    /**
     * 获取字段值
     * @param field 字段
     * @param target 校验对象
     * @return value
     * @throws IllegalAccessException
     */
    private static Object getFieldValue(Field field, Object target) throws IllegalAccessException {
        field.setAccessible(true);
        return field.get(target);
    }

    /**
     * 日期校验
     * @param value 字段值
     * @param format 日期格式
     * @return errorMsg
     */
    private static String validateDate(String value, String format) {
        if (StrUtil.isBlank(format))
            return null;

        Date parseDate = DateUtil.hmsParseDate(value);
        if (Objects.isNull(parseDate)) {
            return String.format(DATE_PARSE_MSG,  format);
        }
        return null;
    }

    /**
     * 数字校验
     * @param value
     * @return errorMsg
     */
    private static String validateBigDecimal(String value) {
        try {
            new BigDecimal(value);
            return null;
        } catch (NumberFormatException e) {
            return INVALID_NUMBER_MSG;
        }
    }

    /**
     * 字符串格式校验
     * @param value 字段值
     * @param regex 正则
     * @param errorMsg
     * @return errorMsg
     */
    private static String validateStringFormat(String value, String regex, String errorMsg) {
        if (StrUtil.isBlank(regex) || RegexpUtil.isMatch(regex, value)) return null;

        return StrUtil.isBlank(errorMsg) ? "格式限制：" + regex : errorMsg;
    }
}
