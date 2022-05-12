package org.basis.framework.error;

import org.basis.framework.annotation.Permissions;

/**
 * @Annotation
 *  * 错误码和错误信息定义类
 *  * 1. 错误码定义规则为5为数字
 *  * 2. 前两位表示业务场景，最后三位表示错误码。例如：100001。10:通用 001:系统未知异常
 *  * 3. 维护错误码后需要维护错误描述，将他们定义为枚举形式
 *  * 错误码列表：
 *  *  10: 通用
 *  *      001：参数格式校验
 *  *  11: 商品
 *  *  12: 订单
 *  *  13: 购物车
 *  *  14: 物流
 * @ClassName BizCodeEnume
 * @Author ChenWenJie
 * @Data 2020/7/8 11:27 下午
 * @Version 1.0
 **/
public enum BizCodeEnume {
    USER_UN_LOGIN(00000, "用户未登录"),
    PARAM_VALIDATE_ERROR(00001, "参数校验错误"),
    UNKNOW_EXCEPTION(10000,"系统未知异常"),
    VAILD_EXCEPTION(10001,"参数格式校验失败"),
    NO_DATA(00003, "无数据"),
    NO_ACCOUNT(00004, "未维护用户信息，请联系管理员"),
    DEFAULT(500,"通用错误"),
    PERMISSIONS(00005,"未授权");
    
    private int code;
    private String msg;
    BizCodeEnume(int code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
