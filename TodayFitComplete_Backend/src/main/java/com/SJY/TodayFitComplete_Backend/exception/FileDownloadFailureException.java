package com.SJY.TodayFitComplete_Backend.exception;

public class FileDownloadFailureException extends RuntimeException {
    public FileDownloadFailureException(Throwable cause) {
        super(cause);
    }
}