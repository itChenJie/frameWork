package org.basis.framework.error;

/**
 * @Description
 * @Author ChenJie
 **/
public class ServiceException extends BaseException{

    public ServiceException() { super("Service Error",BizCodeEnume.DEFAULT.getCode()); }
    public ServiceException(BizCodeEnume bizCodeEnume) { super(bizCodeEnume.getMsg(),bizCodeEnume.getCode()); }

    public ServiceException(String message) { super(message,BizCodeEnume.DEFAULT.getCode()); }

    public ServiceException(String message, int code) { super(message, code); }

    public ServiceException(String message, Throwable throwable) { super(message,BizCodeEnume.DEFAULT.getCode(),throwable); }

    public ServiceException(String message,int code, Throwable throwable) { super(message,code,throwable); }
}
