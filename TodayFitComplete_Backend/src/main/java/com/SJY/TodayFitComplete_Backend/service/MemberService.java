package com.SJY.TodayFitComplete_Backend.service;

import com.SJY.TodayFitComplete_Backend.common.exception.MemberException;
import com.SJY.TodayFitComplete_Backend.common.exception.ResourceNotFoundException;
import com.SJY.TodayFitComplete_Backend.dto.request.member.MemberLoginDto;
import com.SJY.TodayFitComplete_Backend.dto.request.member.MemberRegisterDto;
import com.SJY.TodayFitComplete_Backend.dto.request.member.MemberUpdateDto;
import com.SJY.TodayFitComplete_Backend.dto.response.member.MemberResponse;
import com.SJY.TodayFitComplete_Backend.dto.response.member.MemberTokenResponse;
import com.SJY.TodayFitComplete_Backend.entity.Member;
import com.SJY.TodayFitComplete_Backend.repository.MemberRepository;
import com.SJY.TodayFitComplete_Backend.security.jwt.CustomUserDetailsService;
import com.SJY.TodayFitComplete_Backend.security.jwt.JwtTokenUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberService {

    private final PasswordEncoder pwdEncoder; // 비밀번호 인코더
    private final MemberRepository memberRepository; // 멤버 리포지토리

    private final AuthenticationManager authManager; // 인증 관리자
    private final CustomUserDetailsService userDetailsService; // 사용자 상세 서비스
    private final JwtTokenUtil jwtTokenUtil; // JWT 토큰 유틸리티

    /**
     * 이메일 중복 체크
     */
    public HttpStatus checkIdDuplicate(String email) {
        isExistUserEmail(email);
        return HttpStatus.OK;
    }

    /**
     * 회원 가입
     */
    public MemberResponse register(MemberRegisterDto registerDto) {
        isExistUserEmail(registerDto.getEmail());
        checkPassword(registerDto.getPassword(), registerDto.getPasswordCheck());

        String encodePwd = pwdEncoder.encode(registerDto.getPassword());
        registerDto.setPassword(encodePwd);

        Member saveMember = memberRepository.save(MemberRegisterDto.ofEntity(registerDto));

        return MemberResponse.fromEntity(saveMember);
    }

    /**
     * 로그인
     */
    public MemberTokenResponse login(MemberLoginDto loginDto) {
        authenticate(loginDto.getEmail(), loginDto.getPassword());
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginDto.getEmail());
        checkEncodePassword(loginDto.getPassword(), userDetails.getPassword());
        String token = jwtTokenUtil.generateToken(userDetails);
        return MemberTokenResponse.fromEntity(userDetails, token);
    }

    /**
     * 회원 정보 체크
     */
    public MemberResponse check(Member member, String password) {
        Member checkMember = (Member) userDetailsService.loadUserByUsername(member.getEmail());
        checkEncodePassword(password, checkMember.getPassword());
        return MemberResponse.fromEntity(checkMember);
    }

    /**
     * 회원 정보 변경
     */
    public MemberResponse update(Member member, MemberUpdateDto updateDto) {
        checkPassword(updateDto.getPassword(), updateDto.getPasswordCheck());
        String encodePwd = pwdEncoder.encode(updateDto.getPassword());
        Member updateMember =  memberRepository.findByEmail(member.getEmail()).orElseThrow(
                () -> new ResourceNotFoundException("Member", "Email", member.getEmail())
        );
        updateMember.update(encodePwd, updateDto.getUsername());
        return MemberResponse.fromEntity(updateMember);
    }

    /**
     * 사용자 인증
     */
    private void authenticate(String email, String pwd) {
        try {
            authManager.authenticate(new UsernamePasswordAuthenticationToken(email, pwd));
        } catch (DisabledException e) {
            throw new MemberException("User is disabled.", HttpStatus.BAD_REQUEST);
        } catch (BadCredentialsException e) {
            throw new MemberException("Invalid credentials.", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 이메일 중복 체크
     */
    private void isExistUserEmail(String email) {
        if (memberRepository.findByEmail(email).isPresent()) {
            throw new MemberException("Email is already in use.", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 비밀번호 확인 체크
     */
    private void checkPassword(String password, String passwordCheck) {
        if (!password.equals(passwordCheck)) {
            throw new MemberException("Password and password confirmation do not match.", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 비밀번호 검증
     */
    private void checkEncodePassword(String rawPassword, String encodedPassword) {
        if (!pwdEncoder.matches(rawPassword, encodedPassword)) {
            throw new MemberException("Incorrect password.", HttpStatus.BAD_REQUEST);
        }
    }
}
