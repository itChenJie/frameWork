package org.basis.framework.error;

import java.util.Map;

/**
 * @Description
 * @Author ChenWenJie
 * @Data 2021/6/28 4:56 下午
 **/
public class ValidationException extends RuntimeException{
    private int code = BizCodeEnume.VAILD_EXCEPTION.getCode();

    private transient Map<String, Object> data;

    public ValidationException(int code, String message) {
        super(message);
        this.code = code;
    }

    public ValidationException(int code, String message, Map<String, Object> data) {
        super(message);
        this.code = code;
        this.data = data;
    }


    public ValidationException(String message) {
        super(message);
    }

    public Integer getCode() {
        return code;
    }

    public Map<String, Object> getData() {
        return data;
    }
}
