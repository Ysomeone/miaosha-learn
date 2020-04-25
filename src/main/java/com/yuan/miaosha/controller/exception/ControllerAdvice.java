package com.yuan.miaosha.controller.exception;

import com.yuan.miaosha.controller.common.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 异常捕获
 *
 * @author yuan
 */
@RestControllerAdvice
public class ControllerAdvice {

    /**
     * 接口异常抛出
     *
     * @param request
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(ApiException.class)
    public Result<String> apiException(HttpServletRequest request, ApiException e) {
        String uri = request.getRequestURI();
        return Result.jsonStringError(e.getMsg(), e.getCode());
    }
}
