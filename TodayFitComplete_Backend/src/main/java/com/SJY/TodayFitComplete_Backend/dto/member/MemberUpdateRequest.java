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

    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$")
    private String password;

    @NotBlank
    @Size(min=2)
    @Pattern(regexp = "^[A-Za-z가-힣]+$")
    private String nickname;
}
