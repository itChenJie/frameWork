package org.basis.framework.validation;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.basis.framework.error.BizCodeEnume;
import org.basis.framework.json.JsonReturnApi;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Description 校验工具类
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

    /**
     * 校验参数
     *
     * @param bindingResult
     * @return
     */
    public static JsonReturnApi validate(BindingResult bindingResult) {
        JsonReturnApi responseVo = JsonReturnApi.successData(null);
        if (bindingResult.hasErrors()) {
            List<String> errorMsg = Lists.newArrayList();
            for (FieldError fe : bindingResult.getFieldErrors()) {
                errorMsg.add(fe.getDefaultMessage());
            }
            return JsonReturnApi.error(String.valueOf(BizCodeEnume.PARAM_VALIDATE_ERROR.getCode()), errorMsg.toString());
        }

        return responseVo;
    }
}
