package com.example.demo._core.handler;

import com.example.demo._core.utils.Resp;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 모든 RuntimeException 처리 (REST API용)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException e) {
        return Resp.fail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    // 데이터가 존재하지 않을 때 처리
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
        return Resp.fail(HttpStatus.NOT_FOUND, e.getMessage());
    }
}
