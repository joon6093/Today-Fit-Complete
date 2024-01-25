package com.SJY.TodayFitComplete_Backend.exception.type;

import lombok.Getter;

@Getter
public enum ExceptionType {
    EXCEPTION("exception.code", "exception.msg"),
    AUTHENTICATION_ENTRY_POINT_EXCEPTION("authenticationEntryPointException.code", "authenticationEntryPointException.msg"),
    ACCESS_DENIED_EXCEPTION("accessDeniedException.code", "accessDeniedException.msg"),
    EXPIRED_JWT_EXCEPTION("expiredJwtException.code", "expiredJwtException.msg"),
    JWT_TOKEN_FAILURE_EXCEPTION("jwtTokenFailureException.code", "jwtTokenFailureException.msg"),
    METHOD_ARGUMENT_NOT_VALID_EXCEPTION("methodArgumentNotValidException.code", "methodArgumentNotValidException.msg"),
    REGISTER_FAILURE_EXCEPTION("registerFailureException.code", "registerFailureException.msg"),
    LOGIN_FAILURE_EXCEPTION("loginFailureException.code", "loginFailureException.msg"),
    MEMBER_EMAIL_ALREADY_EXISTS_EXCEPTION("memberEmailAlreadyExistsException.code", "memberEmailAlreadyExistsException.msg"),
    MEMBER_NOT_FOUND_EXCEPTION("memberNotFoundException.code", "memberNotFoundException.msg"),
    BOARD_NOT_FOUND_EXCEPTION("boardNotFoundException.code", "boardNotFoundException.msg"),
    FILE_NOT_FOUND_EXCEPTION("fileNotFoundException.code", "fileNotFoundException.msg"),
    COMMENT_NOT_FOUND_EXCEPTION("commentNotFoundException.code", "commentNotFoundException.msg"),
    FILE_UPLOAD_FAILURE_EXCEPTION("fileUploadFailureException.code", "fileUploadFailureException.msg"),
    FILE_DOWNLOAD_FAILURE_EXCEPTION("fileDownloadFailureException.code", "fileDownloadFailureException.msg"),
    FILE_DELETE_FAILURE_EXCEPTION("fileDeleteFailureException.code", "fileDeleteFailureException.msg");

    private final String code;
    private final String message;

    ExceptionType(String code, String message) {
        this.code = code;
        this.message = message;
    }
}