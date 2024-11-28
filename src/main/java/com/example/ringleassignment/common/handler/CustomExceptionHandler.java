package com.example.ringleassignment.common.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RequiredArgsConstructor
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponseEntity> handleCustomException(CustomException e) {
        e.printStackTrace();
        return ErrorResponseEntity.toResponseEntity(e.getAuthCode());
    }

    @ExceptionHandler(Exception.class)
    protected RuntimeException handleException(Exception e) {
        e.printStackTrace();
        return new RuntimeException(e);
    }
}
