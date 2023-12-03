package com.SJY.TodayFitComplete_Backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class PwdEncoderConfig {

    /**
     * BCryptPasswordEncoder를 빈으로 등록합니다.
     * BCrypt는 비밀번호 해싱을 위한 강력한 방법을 제공합니다.
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
