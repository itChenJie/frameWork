package org.basis.framework.validation;

import com.google.common.base.Joiner;
import org.apache.commons.collections.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.HashSet;
import java.util.Set;

/**
 * @Description
 * @Author ChenWenJie
 * @Data 2021/5/26 5:34 下午
 **/
public class ValidationUtils {
    public static final String VALIDATOR_PASS = "PASS";
    private static Validator validator;

    static {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    /**
     * 校验对象
     * @param obj 待校验对象
     */
    public static <T> String validate(T obj){
        Set<ConstraintViolation<T>> validate = validator.validate(obj);
        if (CollectionUtils.isNotEmpty(validate)) {
            Set<String> validatorResultList = new HashSet<>(16);
            validate.forEach(item -> {
                validatorResultList.add(item.getMessage());
            });
            return Joiner.on(",").join(validatorResultList);
        }
        return VALIDATOR_PASS;
    }


    /**
     * 校验对象
     * @param object 待校验对象
     * @param groups 待校验的组
     */
    public static <T> String validateGroups(Object object,Class< ? >... groups){
        Set<ConstraintViolation<Object>> validate = validator.validate(object, groups);
        if (CollectionUtils.isNotEmpty(validate)) {
            Set<String> validatorResultList = new HashSet<>(16);
            validate.forEach(item -> {
                validatorResultList.add(item.getMessage());
            });
            return Joiner.on(",").join(validatorResultList);
        }
        return VALIDATOR_PASS;
    }
}
