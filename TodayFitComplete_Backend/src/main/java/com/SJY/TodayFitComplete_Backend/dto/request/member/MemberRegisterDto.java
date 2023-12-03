package com.SJY.TodayFitComplete_Backend.dto.request.member;

import com.SJY.TodayFitComplete_Backend.common.Role;
import com.SJY.TodayFitComplete_Backend.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 회원 가입 요청 DTO.
 */
@Getter
@Setter
@NoArgsConstructor
public class MemberRegisterDto {
    private String email;
    private String password;
    private String passwordCheck;
    private String username;

    public static Member ofEntity(MemberRegisterDto dto) {
        return Member.builder()
                .email(dto.getEmail())
                .password(dto.getPassword())
                .username(dto.getUsername())
                .roles(Role.USER)
                .build();
    }
}
