package com.SJY.TodayFitComplete_Backend.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
/**
 * JWT 토큰의 유효성을 검사하고, 인증
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService; // 사용자 상세 서비스
    private final JwtTokenUtil jwtTokenUtil; // JWT 토큰 유틸리티

    @Value("${jwt.header}")
    private String jwtHeader; // JWT 헤더 이름
    @Value("${jwt.prefix}")
    private String jwtPrefix; // JWT 토큰 접두사

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // HTTP 요청에서 JWT 헤더를 추출합니다.
        String header = request.getHeader(jwtHeader);
        String username = null;
        String authToken = null;

        // 헤더 검사 및 JWT 토큰 추출
        if (header != null && header.startsWith(jwtPrefix)) {
            authToken = header.replace(jwtPrefix, "").trim();
            try {
                // 토큰에서 사용자 이름을 추출합니다.
                username = jwtTokenUtil.getUsernameFromToken(authToken);
            } catch (IllegalArgumentException ex) {
                log.error("사용자 ID를 가져오는 데 실패하였습니다.", ex);
            } catch (ExpiredJwtException ex) {
                log.error("토큰이 만료되었습니다.", ex);
            } catch (MalformedJwtException ex) {
                log.error("유효하지 않은 JWT 토큰입니다.", ex);
            } catch (Exception e) {
                log.error("JWT 토큰을 가져오는 데 실패하였습니다.", e);
            }
        } else {
            log.info("요청 헤더에 JWT Bearer 토큰이 없습니다.");
        }

        // 사용자 인증 처리
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                // 인증 토큰 생성 및 보안 컨텍스트에 설정
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                log.info("인증된 사용자: {}", username);
            } else {
                log.info("유효하지 않은 JWT 토큰입니다.");
            }
        } else {
            log.info("인증 정보가 없거나 이미 인증된 요청입니다.");
        }

        filterChain.doFilter(request, response); // 요청을 다음 필터로 전달
    }
}
