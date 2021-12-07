package org.basis.framework.error;

import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

/**
 * @Description
 * 分配锁异常
 * @Author ChenWenJie
 * @Data 2021/9/29 3:07 下午
 **/
@EqualsAndHashCode(callSuper=false)
public class DistributeLockException extends BaseException{
    private static final long serialVersionUID = 1L;

    public DistributeLockException(){ super("分布式锁，使用异常！",HttpStatus.UNAUTHORIZED.value()); }

    public DistributeLockException(String msg){ super(msg,HttpStatus.UNAUTHORIZED.value()); }

    public DistributeLockException(String msg,int code){ super(msg,code);}

    public DistributeLockException(String msg, Throwable e) { super(msg,HttpStatus.UNAUTHORIZED.value(), e); }

    public DistributeLockException(String msg, int code, Throwable e) { super(msg,code, e); }
}
