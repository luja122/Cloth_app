package com.spring.CloathingStore.exception;

import com.spring.CloathingStore.dtos.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleUsernotfoundException(UserNotFoundException exception){
        ExceptionResponse response = new ExceptionResponse(exception.getMessage(), HttpStatus.NOT_FOUND,404);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleRoleNotFoundException(RoleNotFoundException exception){
        ExceptionResponse response = new ExceptionResponse(exception.getMessage(),HttpStatus.NOT_FOUND,404);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    @ExceptionHandler(UserNotEnable.class)
    public ResponseEntity<ExceptionResponse> handleUserNotEnable (UserNotEnable exception){
        ExceptionResponse response = new ExceptionResponse(exception.getMessage(),HttpStatus.FORBIDDEN,403);
        return  ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleBadCredentialException(BadCredentialsException exception){
        ExceptionResponse response = new ExceptionResponse(exception.getMessage(),HttpStatus.NOT_FOUND,404);
        return ResponseEntity.status(404).body(response);
    }
}
