package org.basis.framework.error;

/**
 * @Description
 * @Author ChenWenJie
 * @Data 2021/12/7 4:59 下午
 **/
public class ServiceException extends BaseException{

    public ServiceException() { super("Service Error",BizCodeEnume.DEFAULT.getCode()); }

    public ServiceException(String message) { super(message,BizCodeEnume.DEFAULT.getCode()); }

    public ServiceException(String message, int code) { super(message, code); }

    public ServiceException(String message, Throwable throwable) { super(message,BizCodeEnume.DEFAULT.getCode(),throwable); }

    public ServiceException(String message,int code, Throwable throwable) { super(message,code,throwable); }
}
