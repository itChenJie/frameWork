package org.basis.framework.validation.validator;

import org.basis.framework.validation.group.Phone;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description 自定义手机号约束注解关联验证器
 * @Author ChenWenJie
 * @Data 2021/9/26 3:29 下午
 **/
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