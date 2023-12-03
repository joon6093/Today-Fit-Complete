package com.SJY.TodayFitComplete_Backend.security;

import com.SJY.TodayFitComplete_Backend.security.jwt.JwtAuthenticationEntryPoint;
import com.SJY.TodayFitComplete_Backend.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CorsConfigurationSource corsConfigurationSource;
    /**
     * Spring Security의 인증 관리자를 빈으로 등록합니다.
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    /**
     * HTTP 요청에 대한 보안 설정을 정의합니다.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic(httpBasic -> httpBasic.disable())
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource))

                .authorizeHttpRequests(authorize
                        -> authorize
                        .requestMatchers("/board/list",
                                         "/board/{boardId}",
                                         "/board/search",
                                         "/user/checkId",
                                         "/user/register",
                                         "/user/login",
                                         "/board/{boardId}/comment/list/**",
                                         "/board/{boardId}/file/download/**",
                                         "/files/**")
                                         .permitAll()

                        .requestMatchers("/user/**",
                                         "/board/**",
                                         "/board/{boardId}/comment/**",
                                         "/board/{boardId}/file/**")
                                         .hasRole("USER"))

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // 세션을 사용하지 않는 상태로 설정합니다.
                .exceptionHandling(excep -> excep.authenticationEntryPoint(jwtAuthenticationEntryPoint)) // 인증 실패 시 처리를 정의합니다.
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)  // JWT 필터를 추가합니다.
                .build();
    }
}


