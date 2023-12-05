package com.SJY.TodayFitComplete_Backend.common;

import com.SJY.TodayFitComplete_Backend.common.exception.MemberException;
import com.SJY.TodayFitComplete_Backend.common.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MemberException.class)
    public ResponseEntity<String> handleMemberException(MemberException e) {
        return new ResponseEntity<>(e.getMessage(), e.getStatus());
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException e) {
        String errorMessage = e.getMessage();
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }
}