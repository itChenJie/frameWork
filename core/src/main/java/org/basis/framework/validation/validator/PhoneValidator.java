package org.basis.framework.validation.validator;

import org.basis.framework.validation.group.Phone;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author ChenWenJie
 * @Classname PhoneValidator
 * Describe: 自定义手机号约束注解关联验证器
 * @Date 2020/4/18 15:15
 */
public class PhoneValidator implements ConstraintValidator<Phone,String> {

    @Override
    public boolean isValid(String value,
                           ConstraintValidatorContext constraintValidatorContext) {
        //手机号验证规则：158后头随便
        String check = "1\\d{11}";
        Pattern compile = Pattern.compile(check);
        String phone = Optional.ofNullable(value).orElse("");
        Matcher matcher = compile.matcher(phone);
        return matcher.matches();
    }
}