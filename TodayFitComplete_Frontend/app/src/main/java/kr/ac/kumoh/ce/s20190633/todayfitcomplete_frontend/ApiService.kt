package kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend

import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.Member.MemberLoginDto
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.Member.MemberTokenResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/user/login")
    fun login(@Body loginRequest: MemberLoginDto): Call<MemberTokenResponse>
}
