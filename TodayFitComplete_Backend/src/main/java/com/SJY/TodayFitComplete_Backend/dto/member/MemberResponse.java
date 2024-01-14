package com.SJY.TodayFitComplete_Backend.dto.member;

import com.SJY.TodayFitComplete_Backend.entity.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 사용자 정보 반환.
 */
@Getter
@Setter
@NoArgsConstructor
public class MemberResponse {
    private String email;
    private String username;

    @Builder
    public MemberResponse(String email, String username) {
        this.email = email;
        this.username = username;
    }

    public static MemberResponse fromEntity(Member member) {
        return MemberResponse.builder()
                .email(member.getEmail())
                .username(member.getUsername())
                .build();
    }
}
