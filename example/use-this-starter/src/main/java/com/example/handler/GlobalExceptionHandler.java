package com.example.handler;

import cn.authing.exception.AuthingException;
import com.example.dto.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice(basePackages = "com.example.controller")
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {
    
    @ExceptionHandler(AuthingException.class)
    public ResponseVO handleAuthingException(AuthingException e) {
        log.error("log:*token error {},exception class:{}",e.getMessage(),e.getClass());
        return ResponseVO.fail(403,"wrong token");
    }
}
