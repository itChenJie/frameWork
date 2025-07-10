package org.basis.framework.encryption.desensitization.annotation;

import com.alibaba.fastjson.annotation.JSONType;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.basis.framework.encryption.desensitization.constant.SensitiveTypeEnum;
import org.basis.framework.encryption.desensitization.interceptor.SensitiveJacksonInterceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * author: ChenJie
 * 反系列化时用来标识敏感字段 和 敏感字段类型
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(
    using = SensitiveJacksonInterceptor.class
)
@JSONType
public @interface SensitiveField {
    SensitiveTypeEnum type() default SensitiveTypeEnum.DEFAULT;

    int prefixLen() default 0;

    int suffixLen() default 0;
}
