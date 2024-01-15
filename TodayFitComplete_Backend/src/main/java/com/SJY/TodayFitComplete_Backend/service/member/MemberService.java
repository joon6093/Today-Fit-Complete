package com.SJY.TodayFitComplete_Backend.service.member;

import com.SJY.TodayFitComplete_Backend.config.security.token.JwtTokenUtil;
import com.SJY.TodayFitComplete_Backend.dto.member.*;
import com.SJY.TodayFitComplete_Backend.entity.member.Member;
import com.SJY.TodayFitComplete_Backend.exception.LoginFailureException;
import com.SJY.TodayFitComplete_Backend.exception.MemberEmailAlreadyExistsException;
import com.SJY.TodayFitComplete_Backend.exception.MemberNotFoundException;
import com.SJY.TodayFitComplete_Backend.exception.RegisterFailureException;
import com.SJY.TodayFitComplete_Backend.repository.member.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        Member member = memberRepository.findByEmail(loginDto.getEmail()).orElseThrow(() -> new LoginFailureException());
        checkEncodePassword(loginDto.getPassword(), member.getPassword());
        String token = jwtTokenUtil.generateToken(member);
        return MemberTokenResponse.fromEntity(member, token);
    }

    /**
     * 회원 정보 체크
     */
    public MemberResponse check(Member currentMember, String password) {
        Member member = memberRepository.findById(currentMember.getId()).orElseThrow(() -> new MemberNotFoundException(currentMember.getEmail()));
        checkEncodePassword(password, member.getPassword());
        return MemberResponse.fromEntity(member);
    }

    /**
     * 회원 정보 변경
     */
    public MemberResponse update(Member member, MemberUpdateDto updateDto) {
        String encodePwd = pwdEncoder.encode(updateDto.getPassword());
        Member updateMember =  memberRepository.findByEmail(member.getEmail()).orElseThrow(
                () -> new MemberNotFoundException(member.getEmail()));
        updateMember.update(encodePwd, updateDto.getUsername());
        return MemberResponse.fromEntity(updateMember);
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
