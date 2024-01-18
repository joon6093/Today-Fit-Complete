package com.SJY.TodayFitComplete_Backend.exception;

public class FileDeleteFailureException extends RuntimeException {
    public FileDeleteFailureException(String message) {
        super(message);
    }
}