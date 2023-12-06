package kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.ApiService

import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.Comment.CommentDto
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.Comment.CommentResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path


interface CommentApiService {
    @POST("/board/{boardId}/comment/write")
    fun postComment(@Path("boardId") boardId: Long, @Header("Authorization") token: String?, @Body commentDto: CommentDto): Call<CommentResponse>

    @GET("/board/{boardId}/comment/list")
    fun getComments(@Path("boardId") boardId: Long): Call<List<CommentResponse>>

    @PATCH("/board/{boardId}/comment/update/{commentId}")
    fun updateComment(@Path("commentId") commentId: Long, @Header("Authorization") token: String?, @Body commentDto: CommentDto): Call<CommentResponse>

    @DELETE("/board/{boardId}/comment/delete/{commentId}")
    fun deleteComment(@Path("commentId") commentId: Long, @Header("Authorization") token: String?): Call<Void>
}
