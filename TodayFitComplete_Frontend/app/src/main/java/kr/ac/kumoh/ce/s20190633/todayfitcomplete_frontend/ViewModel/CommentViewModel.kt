package kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.ApiService.CommentApiService
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.Comment.CommentDto
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.Comment.CommentResponse
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.SharedPreferencesUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CommentViewModel(application: Application) : AndroidViewModel(application) {
    private val SERVER_URL = "https://port-0-todayfitcomplete-1drvf2llomgqfda.sel5.cloudtype.app"
    private val apiService: CommentApiService

    private val comments = MutableLiveData<List<CommentResponse>>()
    private val commentResponse = MutableLiveData<CommentResponse?>()

    val commentsLiveData: LiveData<List<CommentResponse>>
        get() = comments
    val commentResponseLiveData: LiveData<CommentResponse?>
        get() = commentResponse

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(CommentApiService::class.java)
    }
    fun fetchComments(boardId: Long) {
        apiService.getComments(boardId)?.enqueue(object : Callback<List<CommentResponse>> {
            override fun onResponse(call: Call<List<CommentResponse>>, response: Response<List<CommentResponse>>) {
                if (response.isSuccessful) {
                    comments.postValue(response.body())
                } else {
                    // 오류 처리
                }
            }
            override fun onFailure(call: Call<List<CommentResponse>>, t: Throwable) {
                // 네트워크 오류 처리
            }
        })
    }

    fun postComment(boardId: Long, commentDto: CommentDto) {
        val token = SharedPreferencesUtils.getToken(getApplication())
        apiService.postComment(boardId, "Bearer $token", commentDto).enqueue(object : Callback<CommentResponse> {
            override fun onResponse(call: Call<CommentResponse>, response: Response<CommentResponse>) {
                if (response.isSuccessful) {
                    commentResponse.postValue(response.body())
                } else {
                    // 오류 처리
                }
            }

            override fun onFailure(call: Call<CommentResponse>, t: Throwable) {
                // 네트워크 오류 처리
            }
        })
    }
    fun updateComment(commentId: Long, commentDto: CommentDto) {
        val token = SharedPreferencesUtils.getToken(getApplication())
        apiService.updateComment(commentId, "Bearer $token", commentDto).enqueue(object : Callback<CommentResponse> {
            override fun onResponse(call: Call<CommentResponse>, response: Response<CommentResponse>) {
                if (response.isSuccessful) {
                    // 댓글 수정 성공
                } else {
                    // 오류 처리
                }
            }
            override fun onFailure(call: Call<CommentResponse>, t: Throwable) {
                // 네트워크 오류 처리
            }
        })
    }

    fun deleteComment(commentId: Long) {
        val token = SharedPreferencesUtils.getToken(getApplication())
        apiService.deleteComment(commentId, "Bearer $token").enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    // 댓글 삭제 성공
                } else {
                    // 오류 처리
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                // 네트워크 오류 처리
            }
        })
    }
}