package com.SJY.TodayFitComplete_Backend.config.security.token;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@Slf4j
public class JwtTokenUtil {

    @Value("${jwt.prefix}")
    private String jwtPrefix;
    @Value("${jwt.tokenExpirationTime}")
    private Integer tokenExpirationTime;
    @Value("${jwt.key}")
    private String key;

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }


    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = parse(token);
        return claimsResolver.apply(claims);
    }

    public Claims parse(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(key.getBytes())
                    .parseClaimsJws(unType(token))
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.warn("Expired JWT token - {}", e.getMessage());
            throw e;
        } catch (JwtException e) {
            log.warn("JWT error - {}", e.getMessage());
            throw e;
        }
    }

    private String unType(String token) {
        return token.substring(jwtPrefix.length());
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpirationTime * 1000))
                .signWith(SignatureAlgorithm.HS256, key.getBytes())
                .compact();
    }
}

