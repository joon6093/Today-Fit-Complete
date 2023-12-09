package kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.ApiService

import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.Comment.CommentDto
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.Comment.CommentListPageResponse
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.Comment.CommentListResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface CommentApiService {
    // 새로운 댓글을 게시판에 작성하는 HTTP POST 요청을 정의합니다.
    @POST("/board/{boardId}/comment/write")
    fun postComment(
        @Path("boardId") boardId: Long,         // 게시판의 고유 식별자
        @Header("Authorization") token: String?, // JWT 토큰 (사용자 인증)
        @Body commentDto: CommentDto             // 댓글 데이터
    ): Call<CommentListResponse>

    // 게시판의 댓글 목록을 가져오는 HTTP GET 요청을 정의합니다.
    @GET("/board/{boardId}/comment/list")
    fun getComments(@Path("boardId") boardId: Long): Call<CommentListPageResponse>

    // 게시판의 특정 댓글을 삭제하는 HTTP DELETE 요청을 정의합니다.
    @DELETE("/board/{boardId}/comment/delete/{commentId}")
    fun deleteComment(
        @Path("commentId") commentId: Long,     // 삭제할 댓글의 고유 식별자
        @Header("Authorization") token: String?  // JWT 토큰 (사용자 인증)
    ): Call<Void>
}
