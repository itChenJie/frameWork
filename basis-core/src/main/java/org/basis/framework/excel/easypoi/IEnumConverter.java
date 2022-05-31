package org.basis.framework.excel.easypoi;


import java.util.Optional;

/**
 * @Description
 * @Author ChenWenJie
 * @Data 2022/5/31 15:47
 **/
public class IEnumConverter implements Converter<IEnum> {
    public IEnumConverter() {
    }

    public Object convert(IEnum value) {
        return Optional.ofNullable(value).map(IEnum::getDescription).orElse("");
    }
}