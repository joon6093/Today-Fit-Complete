package com.SJY.TodayFitComplete_Backend.dto.member;

import com.SJY.TodayFitComplete_Backend.entity.member.type.RoleType;
import com.SJY.TodayFitComplete_Backend.entity.member.Member;
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
                .roles(RoleType.USER)
                .build();
    }
}
