package com.SJY.TodayFitComplete_Backend.entity.member;

import com.SJY.TodayFitComplete_Backend.entity.board.Board;
import com.SJY.TodayFitComplete_Backend.entity.comment.Comment;
import com.SJY.TodayFitComplete_Backend.entity.common.BaseTimeEntity;
import com.SJY.TodayFitComplete_Backend.entity.member.type.RoleType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Member extends BaseTimeEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, length = 30, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 20)
    private String username;

    @Enumerated(EnumType.STRING)
    private RoleType roles;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public List<Comment> comments = new ArrayList<>();

    @Builder
    public Member(String email, String password, String username, RoleType roles) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.roles = roles;
    }

    public void update(String password, String username) {
        this.password = password;
        this.username = username;
    }

    /**
     * Token을 고유한 Email 값으로 생성합니다
     * @return email;
     */
    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add( new SimpleGrantedAuthority("ROLE_" + this.roles.name()));
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {  // 니 계정 만료되지는 않았니?
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {   // 니 계정 잠겨있지 않니?
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {    // 니 계정 비밀번호 갈아낄때 되지 않았니?
        return true;
    }

    @Override
    public boolean isEnabled() {    // 니 계정이 활성화 되어있니?
        // 우리 사이트에서 1년동안 로그인을 안하면 휴면 계정으로 변환하기로 했다면?
        // 현재 시간 - 마지막 로그인 시간으로 계산 => 1년 초과하면 false 로 return.
        // 나머지 비어있는 함수들도 다 비슷하게 구현해주면 된다.
        return true;
    }
}
