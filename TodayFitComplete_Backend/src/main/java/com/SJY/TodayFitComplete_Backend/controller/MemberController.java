package com.SJY.TodayFitComplete_Backend.controller;


import com.SJY.TodayFitComplete_Backend.dto.request.member.MemberLoginDto;
import com.SJY.TodayFitComplete_Backend.dto.request.member.MemberRegisterDto;
import com.SJY.TodayFitComplete_Backend.dto.request.member.MemberUpdateDto;
import com.SJY.TodayFitComplete_Backend.dto.response.member.MemberResponse;
import com.SJY.TodayFitComplete_Backend.dto.response.member.MemberTokenResponse;
import com.SJY.TodayFitComplete_Backend.entity.Member;
import com.SJY.TodayFitComplete_Backend.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    /**
     * 회원의 이메일 중복을 검사합니다.
     *
     * @param email 검사할 이메일
     * @return 검사 결과
     */
    @GetMapping("/checkId")
    public ResponseEntity<?> checkIdDuplicate(@RequestParam String email) {
        memberService.checkIdDuplicate(email);
        return ResponseEntity.ok().build();
    }

    /**
     * 새로운 회원을 등록합니다.
     *
     * @param memberRegisterDTO 회원 등록 정보
     * @return 등록된 회원 정보
     */
    @PostMapping("/register")
    public ResponseEntity<MemberResponse> register(@RequestBody MemberRegisterDto memberRegisterDTO) {
        MemberResponse registeredMember = memberService.register(memberRegisterDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredMember);
    }

    /**
     * 회원 로그인을 처리합니다.
     *
     * @param memberLoginDTO 로그인 정보
     * @return 로그인 성공 시 토큰 및 사용자 정보
     */
    @PostMapping("/login")
    public ResponseEntity<MemberTokenResponse> login(@RequestBody MemberLoginDto memberLoginDTO) {
        MemberTokenResponse loginResult = memberService.login(memberLoginDTO);
        return ResponseEntity.ok().header(loginResult.getToken()).body(loginResult);
    }

    /**
     * 회원의 비밀번호를 검사합니다.
     *
     * @param member 인증된 사용자
     * @param request 요청 데이터(비밀번호 포함)
     * @return 검사 결과 및 회원 정보
     */
    @PostMapping("/checkPwd")
    public ResponseEntity<MemberResponse> checkPassword(@AuthenticationPrincipal Member member,
                                                        @RequestBody Map<String, String> request) {
        String password = request.get("password");
        MemberResponse memberInfo = memberService.check(member, password);
        return ResponseEntity.ok(memberInfo);
    }

    /**
     * 회원 정보를 업데이트합니다.
     *
     * @param member 인증된 사용자
     * @param memberUpdateDTO 업데이트 정보
     * @return 업데이트된 회원 정보
     */
    @PutMapping("/update")
    public ResponseEntity<MemberResponse> update(@AuthenticationPrincipal Member member,
                                                 @RequestBody MemberUpdateDto memberUpdateDTO) {
        MemberResponse updatedMember = memberService.update(member, memberUpdateDTO);
        return ResponseEntity.ok(updatedMember);
    }
}

