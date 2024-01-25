package com.SJY.TodayFitComplete_Backend.dto.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 로그인 요청 DTO.
 */
@Getter
@Setter
@NoArgsConstructor
public class MemberLoginRequest {

    @Email(message = "{memberRequest.email.email}")
    @NotBlank(message = "{memberRequest.email.notBlank}")
    private String email;

    @NotBlank(message = "{memberRequest.password.notBlank}")
    private String password;
}
