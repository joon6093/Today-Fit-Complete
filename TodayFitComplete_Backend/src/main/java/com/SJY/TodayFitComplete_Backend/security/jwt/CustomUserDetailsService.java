package com.SJY.TodayFitComplete_Backend.security.jwt;


import com.SJY.TodayFitComplete_Backend.common.exception.ResourceNotFoundException;
import com.SJY.TodayFitComplete_Backend.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository; // MemberRepository 주입

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 사용자 이름 (이메일)을 사용하여 사용자 상세 정보를 로드합니다.
        // 사용자가 없는 경우, ResourceNotFoundException을 던집니다.
        return memberRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("Member", "Email", username));
    }
}
