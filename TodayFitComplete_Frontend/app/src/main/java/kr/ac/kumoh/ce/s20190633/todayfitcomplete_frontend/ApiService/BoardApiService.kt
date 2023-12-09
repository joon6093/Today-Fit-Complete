package kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.ApiService

import BoardListPageResponse
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.Board.BoardDetailsResponse
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.Board.BoardWriteDto
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.Board.BoardWriteResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface BoardApiService {
    // 게시판 목록을 가져오는 API 엔드포인트입니다.
    @GET("/board/list")
    fun getBoardList(): Call<BoardListPageResponse>

    // 특정 게시글의 상세 정보를 가져오는 API 엔드포인트입니다.
    @GET("/board/{boardId}")
    fun getBoardDetails(@Path("boardId") boardId: Long): Call<BoardDetailsResponse>

    // 게시글을 작성하는 API 엔드포인트입니다.
    @POST("/board/write")
    fun writePost(@Header("Authorization") token: String?, @Body boardWriteDto: BoardWriteDto): Call<BoardWriteResponse>

    // 게시글을 삭제하는 API 엔드포인트입니다.
    @DELETE("/board/{boardId}/delete")
    fun deleteBoard(@Header("Authorization") token: String?, @Path("boardId") boardId: Long): Call<Void>
}