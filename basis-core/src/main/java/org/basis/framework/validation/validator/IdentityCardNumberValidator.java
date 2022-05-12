package org.basis.framework.validation.validator;

import org.basis.framework.utils.IdCardValidatorUtils;
import org.basis.framework.validation.group.IdentityCardNumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @Description
 * @Author ChenWenJie
 * @Data 2021/9/26 3:29 下午
 **/
public class IdentityCardNumberValidator implements ConstraintValidator<IdentityCardNumber, Object> {

    @Override
    public void initialize(IdentityCardNumber identityCardNumber) {
    }


    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        return IdCardValidatorUtils.isValidate18Idcard(obj.toString());
    }
}
