package org.basis.framework.utils;

import lombok.extern.slf4j.Slf4j;
import org.basis.framework.entity.BaseEntity;

import java.lang.reflect.Field;
import java.util.Date;

/**
 * @Description 反射调用工具类
 * @Author ChenWenJie
 * @Data 2021/8/27 2:52 下午
 **/
@Slf4j
public class ReflectionInvokeUtils {
    /**
     *
     * @return
     * @Title: getObjectValue @Description: 通过反射获取字段值 @param @return @throws
     */
    public static Object getObjectValue(Object object, String columnName){
        try {
            if (object != null) {
                Field field = object.getClass().getDeclaredField(columnName);
                if(field != null){
                    field.setAccessible(true);
                    return field.get(object);
                }
            }
        } catch (Exception e) {
            log.error("获取字段属性出现异常");
            return null;
        }
        return null;
    }

    /**
     *
     * @return
     * @Title: getObjectValue @Description: 通过反射获取字段值 @param @return @throws
     */
    public static void setObjectValue(Object object, String columnName,Object value){
        try {
            if (object != null) {
                Field field = object.getClass().getDeclaredField(columnName);
                if(field != null){
                    field.setAccessible(true);
                    field.set(object, value);
                }else{
                    log.warn("字段不存在警告：class={},feild={}",object.getClass().getName(),columnName);
                }
            }
        } catch (Exception e) {
            log.error("设置字段值出现异常"+e);
        }
    }

    public static void setObjectMultiValue(Object object, String columnName, Class type) {
        try {
            if (object != null) {
                Field field = object.getClass().getDeclaredField(columnName);
                field.setAccessible(true);
                Object keyValue = field.get(object).toString();
                StringBuilder desc=new StringBuilder();
                if(keyValue instanceof String) {
                    String values = (String)keyValue;
                    String [] valArray = values.split("\\|");
                    for(String value :valArray){
                        BaseEnum t = EnumUtils.getEnumValue(value, type);
                        if (t != null)
                            desc.append(t.getName()+"|");
                    }
                    //除去最有一个“|”
                    desc.deleteCharAt(desc.length()-1);
                    ReflectionInvokeUtils.setObjectValue(object, field.getName() + "Desc", desc.toString());
                }else{
                    log.error("多选枚举不是字符串，不能转义，请注意");
                    return ;
                }
            }
        } catch (Exception e) {
            log.error("获取字段属性出现异常");

        }
    }

    public static void main(String[] args) {
        BaseEntity baseEntity = new BaseEntity();
        ReflectionInvokeUtils.setObjectValue(baseEntity,"createTime",new Date());
        // 类型不一致会set失败
        ReflectionInvokeUtils.setObjectValue(baseEntity,"createUserId",1);
        System.out.println("baseEntity："+baseEntity.toString());
    }
}
