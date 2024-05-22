package org.basis.framework.encryption.desensitization.constant;

/**
 * @author chenjie
 * 脱敏字段类型
 */
public enum SensitiveTypeEnum {
    /**
     * 姓名
     */
    NAME,
    /**
     * 身份证号
     */
    ID_CARD_NUM,
    /**
     * 手机号
     */
    PHONE_NUM,
    /**
     * 银行卡号
     */
    BANK_CARD_NUM,
    /**
     * 邮箱
     */
    EMAIL,
    /**
     * 默认
     */
    DEFAULT;

    private SensitiveTypeEnum() {
    }
}
