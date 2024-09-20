package com.example.banking_app.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AccountException.class)
    public ResponseEntity<ErrorDetail> handleAccountException(AccountException accountException,
                                                              WebRequest webRequest){
        ErrorDetail errorDetail = new ErrorDetail(
                LocalDateTime.now(),
                accountException.getMessage(),
                webRequest.getDescription(false),
                "ACCOUNT_NOT_FOUNT"
        );
        return new ResponseEntity<>(errorDetail, HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<ErrorDetail> handleGenericException(Exception exception,
                                                              WebRequest webRequest){
        ErrorDetail errorDetail = new ErrorDetail(
                LocalDateTime.now(),
                exception.getMessage(),
                webRequest.getDescription(false),
                "INTERNAL_SERVER_ERROR"
        );
        return new ResponseEntity<>(errorDetail, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
