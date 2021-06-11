package org.basis.framework.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * @Description 枚举
 * @Author ChenWenJie
 * @Data 2021/6/11 11:20 上午
 */
@Getter
@AllArgsConstructor
public enum ProcessType {
    TYPE_CHAR("char","String"),
    TYPE_BIGINT("bigint","Long"),
    TYPE_INT("int","Integer"),
    TYPE_DATE("date","java.util.Date"),
    TYPE_TEXT("text","String"),
    TYPE_TIMESTAMP("timestamp","java.util.Date"),
    TYPE_BIT("bit","Boolean"),
    TYPE_DECIMAL("decimal","java.math.BigDecimal"),
    TYPE_BLOB("blob","byte[]");
    /**
     * 描述
     */
    private String description;
    /**
     * 编码
     */
    private String code;

    public static String ofDescription(String description){
        Objects.requireNonNull(description);
        return Stream.of(values())
                .filter(bean -> bean.description.contains(description))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(description + " not exists")).getCode();
    }

    public static ProcessType ofCode(String code) {
        Objects.requireNonNull(code);
        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(code + " not exists"));
    }
}
