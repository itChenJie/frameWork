package org.basis.framework.error;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

/**
 * @Description
 * 分配锁异常
 * @Author ChenWenJie
 * @Data 2021/9/29 3:07 下午
 **/
@Data
@EqualsAndHashCode(callSuper=false)
public class DistributeLockException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    private String msg;
    private int code = HttpStatus.UNAUTHORIZED.value();

    public DistributeLockException(){
        super("分布式锁，使用异常！");
        this.msg = "分布式锁，使用异常！";
    }

    public DistributeLockException(String msg){
        super(msg);
        this.msg = msg;
    }

    public DistributeLockException(String msg,int code){
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public DistributeLockException(String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
    }
}
