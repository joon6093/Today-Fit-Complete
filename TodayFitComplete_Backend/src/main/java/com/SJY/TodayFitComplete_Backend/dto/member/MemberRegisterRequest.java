package com.SJY.TodayFitComplete_Backend.dto.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 회원 가입 요청 DTO.
 */
@Getter
@Setter
@NoArgsConstructor
public class MemberRegisterRequest {

    @Email(message = "{memberRequest.email.email}")
    @NotBlank(message = "{memberRequest.email.notBlank}")
    private String email;

    @NotBlank(message = "{memberRequest.password.notBlank}")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$", message = "{memberRegisterRequest.password.pattern}")
    private String password;

    @NotBlank(message = "{memberRequest.password.notBlank}")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$", message = "{memberRegisterRequest.password.pattern}")
    private String passwordCheck;

    @NotBlank(message = "{memberRequest.nickname.notBlank}")
    @Size(min=2, message = "{memberRequest.nickname.size}")
    @Pattern(regexp = "^[A-Za-z가-힣]+$", message = "{memberRegisterRequest.nickname.pattern}")
    private String nickname;

}
