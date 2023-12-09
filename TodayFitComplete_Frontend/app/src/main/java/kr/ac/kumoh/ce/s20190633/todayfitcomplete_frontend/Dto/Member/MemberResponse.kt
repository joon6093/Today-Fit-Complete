package kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.Member

// MemberResponse 클래스: 서버에서 클라이언트로 전달되는 사용자 정보를 나타내는 데이터 전송 객체 (DTO)
data class MemberResponse(
    val email: String,    // 사용자 이메일 주소
    val username: String // 사용자의 사용자 이름
)