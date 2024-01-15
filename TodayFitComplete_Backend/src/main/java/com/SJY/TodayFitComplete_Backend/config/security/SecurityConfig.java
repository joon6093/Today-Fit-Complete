package com.SJY.TodayFitComplete_Backend.config.security;

import com.SJY.TodayFitComplete_Backend.config.security.token.JwtAuthenticationFilter;
import com.SJY.TodayFitComplete_Backend.config.security.token.JwtExceptionFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtExceptionFilter jwtExceptionFilter;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CorsConfigurationSource corsConfigurationSource;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer.disable())
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling((exceptionConfig) ->
                        exceptionConfig.authenticationEntryPoint(customAuthenticationEntryPoint).accessDeniedHandler(customAccessDeniedHandler)
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class)
                .authorizeHttpRequests(authorize
                        -> authorize
                        .requestMatchers(HttpMethod.POST, "/api/member/register", "/api/member/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/member/checkPwd").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/member/update").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/board/write").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/board/update/{boardId}").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/board/delete/{boardId}").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/board/{boardId}/file/upload").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/board/{boardId}/file/delete").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/board/{boardId}/comment/write").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/board/{boardId}/comment/update/{commentId}").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/board/{boardId}/comment/delete/{commentId}").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/**","/files/**").permitAll()
                        .anyRequest().hasAnyRole("ADMIN"))
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}


