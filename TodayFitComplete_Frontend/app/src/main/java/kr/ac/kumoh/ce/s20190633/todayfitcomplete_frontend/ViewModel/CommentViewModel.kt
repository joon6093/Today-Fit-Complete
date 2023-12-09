package kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.ApiService.CommentApiService
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.Comment.CommentDto
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.Comment.CommentListResponse
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.SharedPreferencesUtils
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CommentViewModel(application: Application) : AndroidViewModel(application) {
    // 서버의 기본 URL
    private val SERVER_URL = "https://port-0-todayfitcomplete-1drvf2llomgqfda.sel5.cloudtype.app"

    // Retrofit을 사용하여 API 서비스를 생성
    private val apiService: CommentApiService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(CommentApiService::class.java)
    }

    // 게시판의 댓글 목록을 저장하는 MutableLiveData
    private val _comments = MutableLiveData<List<CommentListResponse>>()
    val commentsLiveData: LiveData<List<CommentListResponse>>
        get() = _comments

    // 게시판의 댓글 목록을 가져오는 함수
    fun fetchComments(boardId: Long) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    apiService.getComments(boardId).execute()
                }
                if (response.isSuccessful) {
                    _comments.value = response.body()?.content
                } else {
                    Log.e("fetchComments()", "Response not successful: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("fetchComments()", "Error fetching comments: ${e.message}")
            }
        }
    }

    // 댓글 작성 결과를 저장하는 MutableLiveData
    private val _commentResponse = MutableLiveData<CommentListResponse?>()
    val commentResponseLiveData: LiveData<CommentListResponse?>
        get() = _commentResponse

    // 댓글을 게시판에 작성하는 함수
    fun postComment(boardId: Long, commentDto: CommentDto) {
        viewModelScope.launch {
            val token = SharedPreferencesUtils.getToken(getApplication())

            // API 서비스를 사용하여 댓글 작성 요청을 실행
            val response = withContext(Dispatchers.IO) {
                apiService.postComment(boardId, "Bearer $token", commentDto).execute()
            }

            if (response.isSuccessful) {
                _commentResponse.postValue(response.body())
                fetchComments(boardId) // 댓글 작성 후 댓글 목록 다시 가져오기
            }
        }
    }

    // 댓글을 삭제하는 함수
    fun deleteComment(commentId: Long) {
        viewModelScope.launch {
            val token = SharedPreferencesUtils.getToken(getApplication())

            // API 서비스를 사용하여 댓글 삭제 요청을 실행
            val response = withContext(Dispatchers.IO) {
                apiService.deleteComment(commentId, "Bearer $token").execute()
            }
        }
    }
}
