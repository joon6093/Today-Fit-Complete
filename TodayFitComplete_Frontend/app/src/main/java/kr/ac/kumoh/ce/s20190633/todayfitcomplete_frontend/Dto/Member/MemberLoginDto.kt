package kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.Member

// MemberLoginDto 클래스: 사용자 로그인에 필요한 데이터를 나타내는 데이터 전송 객체 (DTO)
data class MemberLoginDto(
    val email: String,     // 사용자 이메일 주소
    val password: String   // 사용자 비밀번호
)