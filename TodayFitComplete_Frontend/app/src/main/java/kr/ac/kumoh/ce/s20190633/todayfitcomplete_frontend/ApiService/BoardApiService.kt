package kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.ApiService

import BoardListPageResponse
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.Board.BoardDetailsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface BoardApiService {
    @GET("/board/list")
    fun getBoardList(): Call<BoardListPageResponse>
    @GET("/board/{boardId}")
    fun getBoardDetails(@Path("boardId") boardId: Long): Call<BoardDetailsResponse>
}
