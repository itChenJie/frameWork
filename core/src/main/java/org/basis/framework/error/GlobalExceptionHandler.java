package org.basis.framework.error;


import lombok.extern.slf4j.Slf4j;
import org.basis.framework.json.JsonReturnApi;
import org.basis.framework.validation.ValidationUtils;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @异常拦截
 * @Author ChenWenJie
 * @Data 2021/8/27 5:56 下午
 **/
@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {


    /**
     * 捕获自定义异常信息
     *
     * @param request
     * @param exception
     * @return
     */
    @ExceptionHandler
    public JsonReturnApi bizExceptionHandler(HttpServletRequest request, RRException exception) {
        log.warn("捕获自定义异常信息：{}", exception);
        return JsonReturnApi.error(String.valueOf(exception.getCode()), exception.getMessage());
    }


    /**
     * 捕获自定义异常信息
     *
     * @param request
     * @param bizException
     * @return
     */
    @ExceptionHandler
    public JsonReturnApi bizExceptionHandler(HttpServletRequest request,
                                             IllegalArgumentException bizException) {
        log.warn("捕获自定义异常信息：{}", bizException);

        return JsonReturnApi.errorMsg(bizException.getMessage());
    }

    /**
     * 捕获参数校验异常
     *
     * @param request
     * @param bindException
     * @return
     */
    @ExceptionHandler
    public JsonReturnApi methodArgumentNotValidExceptionHandler(HttpServletRequest request,
                                                                BindException bindException) {
        log.warn("捕获参数校验异常信息：{}", bindException);

        return ValidationUtils.validate(bindException.getBindingResult());
    }

    /**
     * 捕获未处理的异常信息
     *
     * @param request
     * @param exception
     * @return
     */
    @ExceptionHandler
    public JsonReturnApi allExceptionHandler(HttpServletRequest request,
                                             Exception exception) {
        log.warn("捕获异常信息：{}", exception);

        return JsonReturnApi.errorMsg("系统内部错误，请联系管理员！");
    }


}
