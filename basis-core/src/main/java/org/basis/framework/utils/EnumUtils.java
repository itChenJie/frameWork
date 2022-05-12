package org.basis.framework.utils;

/**
 * @Description
 * 枚举工具类
 * @Author ChenWenJie
 * @Data 2021/6/11 2:53 下午
 **/
public class EnumUtils {

    /**
     * 获取枚举值
     * @param value 值 -》code
     * @param type 枚举类型
     * @param <T>
     * @return
     */
    public static  <T extends  BaseEnum>  T getEnumValue(Object value, Class<T> type) {
        if(value == null || "".equals(value)){
            return null;
        }
        for (T constant : type.getEnumConstants()) {
            if ((constant.getCode()+"").equals(value+"")) {
                return constant;
            }
        }
        return null;
    }

    /**
     * @Title: getEnumValueIngoreType
     * @Description: 忽略枚举的code属性类型,统一做string处理
     * @param
     * @return
     * @throws
     */
    public static  <T extends BaseEnum>  T getEnumValueIngoreType(Object value, Class<T> type) {
        if(value == null || "".equals(value)){
            return null;
        }
        for (T constant : type.getEnumConstants()) {
            if (constant.getCode().toString().equals(value.toString())) {
                return constant;
            }
        }
        return null;
    }
}
