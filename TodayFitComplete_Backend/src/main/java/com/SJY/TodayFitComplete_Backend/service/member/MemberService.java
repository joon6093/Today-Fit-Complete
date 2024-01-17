package com.SJY.TodayFitComplete_Backend.service.member;

import com.SJY.TodayFitComplete_Backend.config.security.token.JwtTokenUtil;
import com.SJY.TodayFitComplete_Backend.dto.member.*;
import com.SJY.TodayFitComplete_Backend.entity.member.Member;
import com.SJY.TodayFitComplete_Backend.entity.member.type.RoleType;
import com.SJY.TodayFitComplete_Backend.exception.LoginFailureException;
import com.SJY.TodayFitComplete_Backend.exception.MemberEmailAlreadyExistsException;
import com.SJY.TodayFitComplete_Backend.exception.MemberNotFoundException;
import com.SJY.TodayFitComplete_Backend.exception.RegisterFailureException;
import com.SJY.TodayFitComplete_Backend.repository.member.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberService {

    private final PasswordEncoder pwdEncoder;
    private final MemberRepository memberRepository;
    private final JwtTokenUtil jwtTokenUtil;

    /**
     * 이메일 중복 체크
     */
    public void checkIdDuplicate(String email) {
        isExistUserEmail(email);
    }

    /**
     * 회원 가입
     */
    public MemberResponse register(MemberRegisterRequest registerDto) {
        isExistUserEmail(registerDto.getEmail());
        checkPassword(registerDto.getPassword(), registerDto.getPasswordCheck());

        String encodePwd = pwdEncoder.encode(registerDto.getPassword());
        registerDto.setPassword(encodePwd);

        Member member = Member.builder()
                .email(registerDto.getEmail())
                .password(registerDto.getPassword())
                .nickname(registerDto.getNickname())
                .roles(RoleType.ROLE_USER)
                .build();
        memberRepository.save(member);
        return MemberResponse.fromEntity(member);
    }

    /**
     * 로그인
     */
    public MemberTokenResponse login(MemberLoginRequest loginDto) {
        Member member = memberRepository.findByEmail(loginDto.getEmail()).orElseThrow(() -> new LoginFailureException());
        checkEncodePassword(loginDto.getPassword(), member.getPassword());
        String token = jwtTokenUtil.generateToken(member);
        return MemberTokenResponse.fromEntity(member, token);
    }

    /**
     * 회원 정보 변경
     */
    public MemberResponse update(MemberUpdateRequest updateDto, Member currMember) {
        Member member = memberRepository.findById(currMember.getId()).orElseThrow(() -> new MemberNotFoundException(currMember.getId().toString()));
        String encodePwd = pwdEncoder.encode(updateDto.getPassword());
        member.update(encodePwd, updateDto.getNickname());
        return MemberResponse.fromEntity(member);
    }

    /**
     * 회원 정보 삭제
     */
    @PreAuthorize("@memberAccessHandler.check(#memberId)")
    public void delete(@Param("memberId")Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException(memberId.toString()));
        memberRepository.delete(member);
    }

    /**
     * 이메일 중복 체크
     */
    private void isExistUserEmail(String email) {
        if (memberRepository.findByEmail(email).isPresent()) {
            throw new MemberEmailAlreadyExistsException(email);
        }
    }

    /**
     * 비밀번호 확인 체크
     */
    private void checkPassword(String password, String passwordCheck) {
        if (!password.equals(passwordCheck)) {
            throw new RegisterFailureException();
        }
    }

    /**
     * 비밀번호 검증
     */
    private void checkEncodePassword(String rawPassword, String encodedPassword) {
        if (!pwdEncoder.matches(rawPassword, encodedPassword)) {
            throw new LoginFailureException();
        }
    }
}
