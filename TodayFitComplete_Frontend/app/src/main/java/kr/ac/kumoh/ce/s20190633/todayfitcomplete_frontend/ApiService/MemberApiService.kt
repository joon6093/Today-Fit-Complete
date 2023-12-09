package kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.ApiService

import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.Member.MemberLoginDto
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.Member.MemberRegisterDto
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.Member.MemberResponse
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.Member.MemberTokenResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MemberApiService {
    // 회원 가입을 위한 POST 요청을 정의한 함수
    @POST("/user/register")
    fun register(@Body memberRegisterDto: MemberRegisterDto): Call<MemberResponse>

    // 이메일 중복 검사를 위한 GET 요청을 정의한 함수
    @GET("/user/checkId")
    fun checkIdDuplicate(@Query("email") email: String): Call<Boolean>

    // 로그인을 위한 POST 요청을 정의한 함수
    @POST("/user/login")
    fun login(@Body memberLoginDto: MemberLoginDto): Call<MemberTokenResponse>
}
