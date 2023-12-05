package kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.ViewModel.Board

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.Borad.BoardListResponse
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.ApiService.BoardApiService
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.Board.BoardDetailsResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BoardViewModel() : ViewModel() {
    private val SERVER_URL = "https://port-0-todayfitcomplete-1drvf2llomgqfda.sel5.cloudtype.app"
    private val boardApi: BoardApiService
    private val _boardList = MutableLiveData<List<BoardListResponse>>()
    val boardList: LiveData<List<BoardListResponse>>
        get() = _boardList

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        boardApi = retrofit.create(BoardApiService::class.java)
        fetchBoardData()
    }

    private fun fetchBoardData() {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) { // 비동기적으로 네트워크 요청 수행
                    boardApi.getBoardList().execute()
                }
                if (response.isSuccessful) {
                    _boardList.value = response.body()?.content
                } else {
                    Log.e("fetchBoardData()", "Response not successful")
                }
            } catch (e: Exception) {
                Log.e("fetchBoardData()", e.toString())
            }
        }
    }

    private val _boardDetails = MutableLiveData<BoardDetailsResponse>()
    val boardDetails: LiveData<BoardDetailsResponse> = _boardDetails
    fun fetchBoardDetails(boardId: Long) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    // Retrofit을 사용한 API 호출
                    boardApi.getBoardDetails(boardId).execute()
                }
                if (response.isSuccessful && response.body() != null) {
                    _boardDetails.postValue(response.body())
                } else {
                    // 오류 처리: API 응답 실패
                }
            } catch (e: Exception) {
                // 오류 처리: 예외 발생
            }
        }
    }
}

