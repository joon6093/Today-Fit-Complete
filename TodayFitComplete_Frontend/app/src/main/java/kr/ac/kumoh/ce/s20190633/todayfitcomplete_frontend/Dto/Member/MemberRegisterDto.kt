package kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.Member

data class MemberRegisterDto(
    val email: String,
    val password: String,
    val passwordCheck: String,
    val username: String
)
