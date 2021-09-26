package org.basis.framework.error;

import org.basis.framework.utils.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
        logger.error("Run time exception :{}",e.getMessage());
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
        logger.error("Business exception：code：{} mes:{}",e.getCode(),e.getMessage());
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
        logger.error("Unauthorized exception：code：{} mes:{}",e.getCode(),e.getMessage());
        return R.error(e.getCode(),e.getMessage());
    }
    /**
     * 方法参数校验异常
     * @param e
     * @return
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public R constraintViolationException(MethodArgumentNotValidException e){
        logger.error("Parameter Violation Exception :{}",e.getBindingResult().getFieldError().getDefaultMessage());
        return R.error(BizCodeEnume.PARAM_VALIDATE_ERROR.getCode(),e.getBindingResult().getFieldError().getDefaultMessage());
    }

    protected void printStackTrace(Exception e){
        e.printStackTrace();
    }
}
