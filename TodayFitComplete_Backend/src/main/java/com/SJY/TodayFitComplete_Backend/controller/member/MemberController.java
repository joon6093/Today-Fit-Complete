package com.SJY.TodayFitComplete_Backend.controller.member;

import com.SJY.TodayFitComplete_Backend.dto.member.*;
import com.SJY.TodayFitComplete_Backend.dto.response.Response;
import com.SJY.TodayFitComplete_Backend.entity.member.Member;
import com.SJY.TodayFitComplete_Backend.service.member.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/member")
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
    public ResponseEntity<Response> checkIdDuplicate(@RequestParam("email") String email) {
        memberService.checkIdDuplicate(email);
        return ResponseEntity.status(HttpStatus.OK).body(Response.success(email));
    }

    /**
     * 새로운 회원을 등록합니다.
     *
     * @param memberRegisterDTO 회원 등록 정보
     * @return 등록된 회원 정보
     */
    @PostMapping("/register")
    public ResponseEntity<Response> register(@Valid @RequestBody MemberRegisterRequest memberRegisterDTO) {
        MemberResponse registeredMember = memberService.register(memberRegisterDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(Response.success(registeredMember));
    }

    /**
     * 회원 로그인을 처리합니다.
     *
     * @param memberLoginDTO 로그인 정보
     * @return 로그인 성공 시 토큰 및 사용자 정보
     */
    @PostMapping("/login")
    public ResponseEntity<Response> login(@Valid @RequestBody MemberLoginRequest memberLoginDTO) {
        MemberTokenResponse loginResult = memberService.login(memberLoginDTO);
        return ResponseEntity.status(HttpStatus.OK).body(Response.success(loginResult));
    }

    /**
     * 회원 정보를 업데이트합니다.
     *
     * @param memberUpdateDTO 업데이트 정보
     * @param member 인증된 사용자
     * @return 업데이트된 회원 정보
     */
    @PutMapping("/update")
    public ResponseEntity<Response> update(@Valid @RequestBody MemberUpdateRequest memberUpdateDTO, @AuthenticationPrincipal Member member) {
        MemberResponse updatedMember = memberService.update(memberUpdateDTO, member);
        return ResponseEntity.status(HttpStatus.OK).body(Response.success(updatedMember));
    }

    /**
     * 회원 정보를 삭제합니다.
     *
     * @param memberId 삭제할 사용자 ID
     * @return 업데이트된 회원 정보
     */
    @DeleteMapping("/delete/{memberId}")
    public ResponseEntity<Response> delete(@PathVariable("memberId")Long memberId) {
        memberService.delete(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(Response.success());
    }
}

