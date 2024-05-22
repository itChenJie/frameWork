package org.basis.framework.encryption.desensitization.interceptor;

import org.basis.framework.encryption.desensitization.annotation.SensitiveField;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.JavaBeanSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import org.basis.framework.encryption.desensitization.util.SensitiveUtil;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Objects;

/**
 * @author ChenJie
 * Fastjson 序列化进行脱敏
 */
public class SensitiveFastjsonInterceptor implements ObjectSerializer {
    public SensitiveFastjsonInterceptor() {
    }

    public void write(JSONSerializer jsonSerializer, Object o, Object fieldName, Type type, int i) throws IOException {
        if (Objects.isNull(o)) {
            jsonSerializer.writeNull();
        } else {
            Class<?> clazz = o.getClass();
            Field[] declaredFields = clazz.getDeclaredFields();
            int length = declaredFields.length;

            for(int y = 0; y < length; ++y) {
                Field field = declaredFields[y];

                try {
                    if (field.isAnnotationPresent(SensitiveField.class)) {
                        field.setAccessible(true);
                        Object value = field.get(o);
                        if (!Objects.isNull(value) && value instanceof String) {
                            SensitiveField sensitiveField = (SensitiveField)field.getAnnotation(SensitiveField.class);
                            String val = (String)value;
                            switch (sensitiveField.type()) {
                                case DEFAULT:
                                    field.set(o, SensitiveUtil.desValue(val, sensitiveField.prefixLen(), sensitiveField.suffixLen()));
                                    break;
                                case NAME:
                                    field.set(o, SensitiveUtil.chineseName(val));
                                    break;
                                case ID_CARD_NUM:
                                    field.set(o, SensitiveUtil.idCardNum(val));
                                    break;
                                case PHONE_NUM:
                                    field.set(o, SensitiveUtil.mobilePhone(val));
                                    break;
                                case BANK_CARD_NUM:
                                    field.set(o, SensitiveUtil.bankCard(val));
                                    break;
                                case EMAIL:
                                    field.set(o, SensitiveUtil.email(val));
                                    break;
                                default:
                                    throw new IllegalArgumentException("unknown sensitive type enum " + sensitiveField.type());
                            }
                        }
                    }
                } catch (IllegalAccessException var15) {
                    throw new RuntimeException(var15);
                }
            }

            JavaBeanSerializer javaBeanSerializer = new JavaBeanSerializer(clazz);
            javaBeanSerializer.write(jsonSerializer, o, fieldName, type, i);
        }
    }
}
