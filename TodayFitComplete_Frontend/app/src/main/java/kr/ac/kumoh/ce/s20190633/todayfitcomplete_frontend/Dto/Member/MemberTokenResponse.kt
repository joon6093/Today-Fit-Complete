package kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.Member

// MemberTokenResponse 클래스: 사용자 로그인 후 서버에서 발급된 토큰과 사용자 이메일을 클라이언트로 전달하는 데이터 전송 객체 (DTO)
data class MemberTokenResponse(
    val email: String,  // 사용자 이메일 주소
    val token: String   // 사용자 인증을 위한 토큰
)