package com.campus.legal.common;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ApiResult<Void> handleBiz(BusinessException e) {
        return ApiResult.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResult<Void> handleValid(Exception e) {
        String msg = "参数错误";
        if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException ex = (MethodArgumentNotValidException) e;
            if (ex.getBindingResult().getFieldError() != null) {
                msg = ex.getBindingResult().getFieldError().getDefaultMessage();
            }
        }
        return ApiResult.fail(msg);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiResult<Void> handleDenied(AccessDeniedException e) {
        return ApiResult.fail(403, "无权限");
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ApiResult<Void> handleBadCred(BadCredentialsException e) {
        return ApiResult.fail(401, "账号或密码错误");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResult<Void> handleOther(Exception e) {
        e.printStackTrace();
        return ApiResult.fail(500, "服务器异常");
    }
}
