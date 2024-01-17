package com.SJY.TodayFitComplete_Backend.dto.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 사용자 정보 및 토큰 정보 응답 DTO.
 */
@Getter
@Setter
@NoArgsConstructor
public class MemberTokenResponse {
    private String email;
    private String token;

    @Builder
    public MemberTokenResponse(String email, String token) {
        this.email = email;
        this.token = token;
    }

    public static MemberTokenResponse fromEntity(UserDetails member, String token) {
        return MemberTokenResponse.builder()
                .email(member.getUsername())
                .token(token)
                .build();
    }
}
