package com.SJY.TodayFitComplete_Backend.dto.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * 회원 정보 변경 요청 DTO.
 */
@Getter
@Setter
public class MemberUpdateRequest {

    @NotBlank(message = "{memberRequest.password.notBlank}")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$", message = "{memberRegisterRequest.password.pattern}")
    private String password;

    @NotBlank(message = "{memberRequest.nickname.notBlank}")
    @Size(min=2, message = "{memberRequest.nickname.size}")
    @Pattern(regexp = "^[A-Za-z가-힣]+$", message = "{memberRegisterRequest.nickname.pattern}")
    private String nickname;
}
