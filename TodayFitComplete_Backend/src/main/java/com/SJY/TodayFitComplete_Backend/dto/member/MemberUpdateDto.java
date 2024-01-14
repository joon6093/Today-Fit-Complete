package com.SJY.TodayFitComplete_Backend.dto.member;

import lombok.*;

/**
 * 회원 정보 변경 요청 DTO.
 */
@Getter
@Setter
public class MemberUpdateDto {
    private String password;
    private String username;
}
