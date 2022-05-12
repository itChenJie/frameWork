package org.basis.framework.error;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

/**
 * @Description
 * 未经授权 异常
 * @Author ChenWenJie
 * @Data 2021/9/24 4:52 下午
 **/
@Data
@EqualsAndHashCode(callSuper=false)
public class UnauthorizedException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    private String msg;
    private int code = HttpStatus.UNAUTHORIZED.value();

    public UnauthorizedException(String msg){
        super(msg);
        this.msg = msg;
    }

    public UnauthorizedException(String msg,int code){
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public UnauthorizedException(String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
    }
}
