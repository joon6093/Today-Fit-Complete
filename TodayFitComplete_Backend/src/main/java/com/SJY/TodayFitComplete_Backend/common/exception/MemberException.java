package com.SJY.TodayFitComplete_Backend.common.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
public class MemberException extends RuntimeException {

    private HttpStatus status; // HTTP 상태 코드
    private String message; // 예외 메시지

    /**
     * MemberException 생성자.
     *
     * @param message 예외 메시지
     * @param status HTTP 상태 코드
     */
    public MemberException(String message, HttpStatus status) {
        super(message);
        this.message = message;
        this.status = status;
    }
}

