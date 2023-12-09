package kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.Member

// MemberRegisterDto 클래스: 사용자 회원 가입에 필요한 데이터를 나타내는 데이터 전송 객체 (DTO)
data class MemberRegisterDto(
    val email: String,        // 사용자 이메일 주소
    val password: String,     // 사용자 비밀번호
    val passwordCheck: String, // 비밀번호 확인을 위한 필드 (비밀번호와 일치해야 함)
    val username: String      // 사용자의 사용자 이름
)
