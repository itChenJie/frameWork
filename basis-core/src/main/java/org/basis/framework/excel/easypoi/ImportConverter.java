package org.basis.framework.excel.easypoi;

/**
 * @Description
 * @Author ChenJie
 **/
@FunctionalInterface
public interface ImportConverter<E> {
    Object convert(E var1, Object var2);
}