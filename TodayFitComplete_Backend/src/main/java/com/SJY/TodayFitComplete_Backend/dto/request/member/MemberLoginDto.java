package com.SJY.TodayFitComplete_Backend.dto.request.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 로그인 요청 DTO.
 */
@Getter
@Setter
@NoArgsConstructor
public class MemberLoginDto {
    private String email;
    private String password;
}
