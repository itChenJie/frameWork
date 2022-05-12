package org.basis.framework.validation;

import com.google.common.base.Joiner;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.validator.HibernateValidator;


import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.HashSet;
import java.util.Set;

/**
 * @Description
 * @Author ChenWenJie
 * @Data 2021/5/26 4:23 下午
 **/
public class HibernateValidationUtils {
    public static final String VALIDATOR_PASS = "PASS";
    private static Validator validator = Validation.byProvider(HibernateValidator.class)
            .configure().buildValidatorFactory().getValidator();
    /**
     *
     * @param obj 需要校验的实体。
     * @param <T>
     * @return  校验成功返回PASS；校验失败返回校验message。
     */
    public static <T> String validate(T obj) {
        // 01、参数校验
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(obj);
        // 02、校验结果收集。
        if (CollectionUtils.isNotEmpty(constraintViolations)) {
            Set<String> validatorResultList = new HashSet<>(16);
            constraintViolations.forEach(item -> {
                validatorResultList.add(item.getMessage());
            });
            return Joiner.on(",").join(validatorResultList);
        }

        return VALIDATOR_PASS;
    }
}
