package kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.ApiService

import BoardListPageResponse
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.Board.BoardDetailsResponse
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.Borad.BoardWriteDto
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.Borad.BoardWriteResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface BoardApiService {
    @GET("/board/list")
    fun getBoardList(): Call<BoardListPageResponse>
    @GET("/board/{boardId}")
    fun getBoardDetails(@Path("boardId") boardId: Long): Call<BoardDetailsResponse>
    @POST("/board/write")
    fun writePost(@Header("Authorization") token: String?, @Body boardWriteDto: BoardWriteDto): Call<BoardWriteResponse>
    @DELETE("/board/{boardId}/delete")
    fun deleteBoard(@Header("Authorization") token: String?, @Path("boardId") boardId: Long): Call<Void>
}
