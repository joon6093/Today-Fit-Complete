package com.SJY.TodayFitComplete_Backend.config.security.token;


import com.SJY.TodayFitComplete_Backend.exception.MemberNotFoundException;
import com.SJY.TodayFitComplete_Backend.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public UserDetails loadUserByUsername(String token) throws UsernameNotFoundException {
        String username = jwtTokenUtil.getUsernameFromToken(token);
        return memberRepository.findByEmail(username).orElseThrow(() -> new MemberNotFoundException(username));
    }
}
