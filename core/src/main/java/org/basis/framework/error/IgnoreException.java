package org.basis.framework.error;

public class IgnoreException extends BaseException {

    public IgnoreException(String message) {
        super(message,BizCodeEnume.DEFAULT.getCode());
    }

    public IgnoreException(String message, int code) {
        super(message,code);
    }

    public IgnoreException(String message, Throwable cause) {
        super(message,BizCodeEnume.DEFAULT.getCode(), cause);
    }

    public IgnoreException(String message,int code, Throwable cause) { super(message,code, cause); }


}
