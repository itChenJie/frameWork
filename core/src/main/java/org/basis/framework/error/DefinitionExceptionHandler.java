package org.basis.framework.error;

import com.aliyun.oss.ServiceException;
import org.basis.framework.utils.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @Description
 * 默认统一异常处理
 * @Author ChenWenJie
 * @Data 2021/9/24 4:32 下午
 **/
public class DefinitionExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 未知异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public R exception(Exception e){
        printStackTrace(e);
        e.printStackTrace();
        logger.error("运行时常 :{}",e.getMessage());
        return R.error();
    }

    /**
     * 业务异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(RRException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ResponseBody
    public R serviceException(RRException e){
        e.printStackTrace();
        logger.error("业务异常：code：{} mes:{}",e.getCode(),e.getMessage());
        return R.error(e.getCode(),e.getMessage());
    }

    /**
     * 未经授权异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public R unauthorizedException(UnauthorizedException e){
        logger.error("未经授权异常：code：{} mes:{}",e.getCode(),e.getMessage());
        return R.error(e.getCode(),e.getMessage());
    }

    protected void printStackTrace(Exception e){
        e.printStackTrace();
    }
}
