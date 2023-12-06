package kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.ViewModel

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.Borad.BoardListResponse
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.ApiService.BoardApiService
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.ApiService.FileApiService
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.Board.BoardDetailsResponse
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.Borad.BoardWriteDto
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.SharedPreferencesUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileOutputStream

class BoardViewModel(application: Application) : AndroidViewModel(application) {
    private val SERVER_URL = "https://port-0-todayfitcomplete-1drvf2llomgqfda.sel5.cloudtype.app"
    private val boardApi: BoardApiService
    private val fileApi: FileApiService
    private val _boardList = MutableLiveData<List<BoardListResponse>>()
    data class FileDetail(val file: File, val mimeType: String, val fileName: String)

    val boardList: LiveData<List<BoardListResponse>>
        get() = _boardList

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        boardApi = retrofit.create(BoardApiService::class.java)
        fileApi = retrofit.create(FileApiService::class.java)
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

    // 게시글 작성 함수
    fun writePost(title: String, content: String) {
        // 토큰을 가져오는 로직
        val token = SharedPreferencesUtils.getToken(getApplication<Application>().applicationContext)

        // 게시글 작성 DTO 생성
        val boardWriteDto = BoardWriteDto(title, content)

        // 게시글 작성 API 호출 로직
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    boardApi.writePost("Bearer $token", boardWriteDto).execute()
                }
                if (response.isSuccessful) {
                    // 게시글 작성 성공 처리
                } else {
                    // 게시글 작성 실패 처리
                }
            } catch (e: Exception) {
                // 예외 처리
            }
        }
    }
    // BoardViewModel 클래스 내부
    fun writePostWithFiles(title: String, content: String, fileUris: List<Uri>) {
        val token = SharedPreferencesUtils.getToken(getApplication<Application>().applicationContext)
        val boardWriteDto = BoardWriteDto(title, content)

        viewModelScope.launch {
            // 파일 정보 생성
            val fileDetails = fileUris.mapNotNull { uri ->
                try {
                    val fileName = getFileName(uri)
                    val mimeType = getApplication<Application>().contentResolver.getType(uri) ?: "application/octet-stream"
                    val file = uriToFile(uri, fileName)

                    FileDetail(file, mimeType, fileName)
                } catch (e: Exception) {
                    null // 파일 처리 중 문제가 발생하면 null 반환
                }
            }

            // 게시글 작성 API 호출
            val postResponse = withContext(Dispatchers.IO) {
                boardApi.writePost("Bearer $token", boardWriteDto).execute()
            }

            if (postResponse.isSuccessful) {
                val createdBoardId = postResponse.body()?.boardId
                fileDetails.forEach { fileDetail ->
                    if (createdBoardId != null) {
                        uploadFile(createdBoardId, fileDetail, token)
                    }
                }
            } else {
                // 게시글 작성 실패 처리
            }
        }
    }

    private fun uriToFile(uri: Uri, fileName: String): File {
        val inputStream = getApplication<Application>().contentResolver.openInputStream(uri)
        val file = File(getApplication<Application>().cacheDir, fileName)
        inputStream.use { input ->
            FileOutputStream(file).use { output ->
                input?.copyTo(output)
            }
        }
        return file
    }
    // 파일 업로드 함수 (Context 대신 필요한 정보를 받음)
    private suspend fun uploadFile(boardId: Long, fileDetail: FileDetail, token: String?) {
        try {
            // 파일 업로드 API 호출
            val requestBody = fileDetail.file.asRequestBody(fileDetail.mimeType.toMediaTypeOrNull())
            val multipartBody = MultipartBody.Part.createFormData("file", fileDetail.fileName, requestBody)

            val response = withContext(Dispatchers.IO) {
                fileApi.uploadFile("Bearer $token", boardId, multipartBody).execute()
            }

            if (response.isSuccessful) {
                // 파일 업로드 성공 처리
            } else {
                // 파일 업로드 실패 처리
            }
        } catch (e: Exception) {
            // 예외 처리
        }
    }

    // 확장 함수로 파일 이름 가져오기
    private fun getFileName(uri: Uri): String {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = getApplication<Application>().contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    result = it.getString(it.getColumnIndexOrThrow("_display_name"))
                }
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result?.lastIndexOf('/')
            if (cut != null && cut != -1) {
                result = result?.substring(cut + 1)
            }
        }
        return result ?: "unknown"
    }
}