package org.basis.framework.excel.easypoi;

/**
 * @Description
 * @Author ChenWenJie
 * @Data 2022/5/31 15:47
 **/
@FunctionalInterface
public interface ImportConverter<E> {
    Object convert(E var1, Object var2);
}