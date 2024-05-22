package org.basis.framework.encryption.desensitization.interceptor;

import org.basis.framework.encryption.desensitization.annotation.SensitiveField;
import org.basis.framework.encryption.desensitization.constant.SensitiveTypeEnum;
import org.basis.framework.encryption.desensitization.util.SensitiveUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import java.io.IOException;
import java.util.Objects;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ChenJie
 * jackson 敏感的拦截器
 */
public class SensitiveJacksonInterceptor extends JsonSerializer<String> implements ContextualSerializer {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(SensitiveJacksonInterceptor.class);
    private SensitiveTypeEnum sensitiveTypeEnum;
    private Integer prefixLen;
    private Integer suffixLen;

    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        switch (this.sensitiveTypeEnum) {
            case DEFAULT:
                jsonGenerator.writeString(SensitiveUtil.desValue(s, this.prefixLen, this.suffixLen));
                break;
            case NAME:
                jsonGenerator.writeString(SensitiveUtil.chineseName(s));
                break;
            case ID_CARD_NUM:
                jsonGenerator.writeString(SensitiveUtil.idCardNum(s));
                break;
            case PHONE_NUM:
                jsonGenerator.writeString(SensitiveUtil.mobilePhone(s));
                break;
            case BANK_CARD_NUM:
                jsonGenerator.writeString(SensitiveUtil.bankCard(s));
                break;
            case EMAIL:
                jsonGenerator.writeString(SensitiveUtil.email(s));
                break;
            default:
                throw new IllegalArgumentException("unknown sensitive type enum " + this.sensitiveTypeEnum);
        }

    }

    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        if (beanProperty != null) {
            if (Objects.equals(beanProperty.getType().getRawClass(), String.class)) {
                SensitiveField sensitive = (SensitiveField)beanProperty.getAnnotation(SensitiveField.class);
                if (sensitive == null) {
                    sensitive = (SensitiveField)beanProperty.getContextAnnotation(SensitiveField.class);
                }

                if (sensitive != null) {
                    return new SensitiveJacksonInterceptor(sensitive.type(), sensitive.prefixLen(), sensitive.suffixLen());
                }
            }

            return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
        } else {
            return serializerProvider.findNullValueSerializer((BeanProperty)null);
        }
    }

    @Generated
    public SensitiveJacksonInterceptor(SensitiveTypeEnum sensitiveTypeEnum, Integer prefixLen, Integer suffixLen) {
        this.sensitiveTypeEnum = sensitiveTypeEnum;
        this.prefixLen = prefixLen;
        this.suffixLen = suffixLen;
    }

    @Generated
    public SensitiveJacksonInterceptor() {
    }
}
