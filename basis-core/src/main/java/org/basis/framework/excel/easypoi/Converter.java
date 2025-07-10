package org.basis.framework.excel.easypoi;

/**
 * @Description
 * @Author ChenJie
 **/
@FunctionalInterface
public interface Converter<T> {
    Object convert(T var1);
}