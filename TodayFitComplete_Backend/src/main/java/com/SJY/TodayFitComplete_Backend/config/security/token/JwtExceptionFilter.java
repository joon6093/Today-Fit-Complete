package com.SJY.TodayFitComplete_Backend.config.security.token;

import com.SJY.TodayFitComplete_Backend.dto.response.Response;
import com.SJY.TodayFitComplete_Backend.exception.MemberNotFoundException;
import com.SJY.TodayFitComplete_Backend.exception.handler.ResponseHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.SJY.TodayFitComplete_Backend.exception.type.ExceptionType.*;


@RequiredArgsConstructor
@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

    private final ResponseHandler responseHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (MemberNotFoundException e) {
            handleMemberNotFoundException(response, e);
        } catch (ExpiredJwtException e) {
            handleExpiredJwtException(response, e);
        } catch (JwtException e) {
            handleJwtException(response);
        }
    }

    private void handleMemberNotFoundException(HttpServletResponse response, MemberNotFoundException e) throws IOException {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(convertToJson(responseHandler.getFailureResponse(MEMBER_NOT_FOUND_EXCEPTION, e.getMessage())));
    }

    private void handleExpiredJwtException(HttpServletResponse response, ExpiredJwtException e) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(convertToJson(responseHandler.getFailureResponse(EXPIRED_JWT_EXCEPTION)));
    }

    private void handleJwtException(HttpServletResponse response) throws IOException {  // 보안상에 이유로 정보 제공하지 않음.
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(convertToJson(responseHandler.getFailureResponse(JWT_TOKEN_FAILURE_EXCEPTION)));
    }

    private String convertToJson(Response response) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(response);
    }
}