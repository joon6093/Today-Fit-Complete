package kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.ApiService

import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.Member.MemberLoginDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.Member.MemberRegisterDto
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.Member.MemberResponse
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.Member.MemberTokenResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MemberApiService {
    @POST("/user/register")
    fun register(@Body memberRegisterDto: MemberRegisterDto): Call<MemberResponse>
    @GET("/user/checkId")
    fun checkIdDuplicate(@Query("email") email: String): Call<Boolean>
    @POST("/user/login")
    fun login(@Body memberLoginDto: MemberLoginDto): Call<MemberTokenResponse>
}
