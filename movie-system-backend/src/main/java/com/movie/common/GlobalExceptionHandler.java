package com.movie.common;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        // 捕获我们刚才抛出的 401
        if ("401".equals(e.getMessage())) {
            Result<?> result = Result.error("登录已过期，请重新登录");
            result.setCode(401); // 设置特定的状态码
            return result;
        }
        if ("403".equals(e.getMessage())) {
            Result<?> result = Result.error("权限不足");
            result.setCode(403);
            return result;
        }
        e.printStackTrace();
        return Result.error(e.getMessage());
    }
}