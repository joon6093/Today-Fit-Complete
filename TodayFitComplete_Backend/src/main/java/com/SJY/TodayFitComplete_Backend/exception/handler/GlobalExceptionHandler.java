package com.SJY.TodayFitComplete_Backend.exception.handler;

import com.SJY.TodayFitComplete_Backend.dto.response.Response;
import com.SJY.TodayFitComplete_Backend.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.FileNotFoundException;
import java.net.BindException;

import static com.SJY.TodayFitComplete_Backend.exception.type.ExceptionType.*;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    private final ResponseHandler responseHandler;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> exception(Exception e) {
        log.info("e = {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(responseHandler.getFailureResponse(EXCEPTION));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Response> accessDeniedException(AccessDeniedException e) {
        log.info("e = {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(responseHandler.getFailureResponse(ACCESS_DENIED_EXCEPTION));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<Response> bindException(BindException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(responseHandler.getFailureResponse(BIND_EXCEPTION, e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(responseHandler.getFailureResponse(BIND_EXCEPTION, e.getMessage()));
    }

    @ExceptionHandler(RegisterFailureException.class)
    public ResponseEntity<Response> registerFailureException() {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(responseHandler.getFailureResponse(REGISTER_FAILURE_EXCEPTION));
    }

    @ExceptionHandler(LoginFailureException.class)
    public ResponseEntity<Response> loginFailureException() {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(responseHandler.getFailureResponse(LOGIN_FAILURE_EXCEPTION));
    }

    @ExceptionHandler(MemberEmailAlreadyExistsException.class)
    public ResponseEntity<Response> memberEmailAlreadyExistsException(MemberEmailAlreadyExistsException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(responseHandler.getFailureResponse(MEMBER_EMAIL_ALREADY_EXISTS_EXCEPTION, e.getMessage()));
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<Response> memberNotFoundException(MemberNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(responseHandler.getFailureResponse(MEMBER_NOT_FOUND_EXCEPTION, e.getMessage()));
    }

    @ExceptionHandler(BoardNotFoundException.class)
    public ResponseEntity<Response> boardNotFoundException(BoardNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(responseHandler.getFailureResponse(BOARD_NOT_FOUND_EXCEPTION, e.getMessage()));
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<Response> fileNotFoundException(FileNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(responseHandler.getFailureResponse(FILE_NOT_FOUND_EXCEPTION, e.getMessage()));
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<Response> commentNotFoundException(CommentNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(responseHandler.getFailureResponse(COMMENT_NOT_FOUND_EXCEPTION, e.getMessage()));
    }

    @ExceptionHandler(FileUploadFailureException.class)
    public ResponseEntity<Response> fileUploadFailureException(FileUploadFailureException e) {
        log.info("e = {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(responseHandler.getFailureResponse(FILE_UPLOAD_FAILURE_EXCEPTION));
    }

    @ExceptionHandler(FileDownloadFailureException.class)
    public ResponseEntity<Response> fileDownloadFailureException(FileDownloadFailureException e) {
        log.info("e = {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(responseHandler.getFailureResponse(FILE_DOWNLOAD_FAILURE_EXCEPTION));
    }
}