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
    private val SERVER_URL = "https://port-0-todayfitcomplete-1drvf2llomgqfda.sel5.cloudtype.app"
    private val apiService: CommentApiService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(CommentApiService::class.java)
    }

    private val _comments = MutableLiveData<List<CommentListResponse>>()
    val commentsLiveData: LiveData<List<CommentListResponse>>
        get() = _comments
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

    private val _commentResponse = MutableLiveData<CommentListResponse?>()
    val commentResponseLiveData: LiveData<CommentListResponse?>
        get() = _commentResponse

    fun postComment(boardId: Long, commentDto: CommentDto) {
        viewModelScope.launch {
            val token = SharedPreferencesUtils.getToken(getApplication())
            val response = withContext(Dispatchers.IO) {
                apiService.postComment(boardId, "Bearer $token", commentDto).execute()
            }
            if (response.isSuccessful) {
                _commentResponse.postValue(response.body())
                fetchComments(boardId)
            }
        }
    }

    fun deleteComment(commentId: Long) {
        viewModelScope.launch {
            val token = SharedPreferencesUtils.getToken(getApplication())
            val response = withContext(Dispatchers.IO) {
                apiService.deleteComment(commentId, "Bearer $token").execute()
            }
        }
    }
}
