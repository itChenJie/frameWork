package org.basis.framework.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description
 * @Author ChenWenJie
 * @Data 2021/12/7 4:59 下午
 **/
public class BaseException extends RuntimeException{
    private static final Logger log = LoggerFactory.getLogger(BaseException.class);
    private int code;

    public BaseException(String message, int code) {
        super(message);
        this.code = code;
        log.warn(this.toString());
    }

    public BaseException(String message, int code, Throwable cause) {
        super(message);
        this.code = code;
        log.warn(this.toString(), cause);
    }

    public String toString() {
        return "BaseException{code=" + this.code + ", message=" + super.getMessage() + "}";
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
