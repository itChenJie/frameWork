package org.basis.framework.excel.easypoi;


import java.util.Optional;

/**
 * @Description
 * @Author ChenJie
 **/
public class IEnumConverter implements Converter<IEnum> {
    public IEnumConverter() {
    }

    public Object convert(IEnum value) {
        return Optional.ofNullable(value).map(IEnum::getDescription).orElse("");
    }
}