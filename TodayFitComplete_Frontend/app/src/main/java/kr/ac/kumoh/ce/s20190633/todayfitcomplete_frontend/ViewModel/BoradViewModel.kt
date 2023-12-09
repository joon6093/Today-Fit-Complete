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
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.ApiService.BoardApiService
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.ApiService.FileApiService
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.Board.BoardDetailsResponse
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.Board.BoardListResponse
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.Board.BoardWriteDto
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

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        boardApi = retrofit.create(BoardApiService::class.java)
        fileApi = retrofit.create(FileApiService::class.java)
        fetchBoardData()
    }

    // 게시판 목록을 LiveData로 관리
    val boardList: LiveData<List<BoardListResponse>>
        get() = _boardList

    // 게시판 목록을 가져오는 함수
    private fun fetchBoardData() {
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                boardApi.getBoardList().execute()
            }
            if (response.isSuccessful) {
                _boardList.value = response.body()?.content
            } else {
                Log.e("fetchBoardData()", "Response not successful")
            }
        }
    }

    // 게시글 상세 정보를 LiveData로 관리
    private val _boardDetails = MutableLiveData<BoardDetailsResponse>()
    val boardDetails: LiveData<BoardDetailsResponse> = _boardDetails

    // 특정 게시글의 상세 정보를 가져오는 함수
    fun fetchBoardDetails(boardId: Long) {
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                boardApi.getBoardDetails(boardId).execute()
            }
            if (response.isSuccessful && response.body() != null) {
                _boardDetails.postValue(response.body())
            }
        }
    }

    // 게시글 작성 및 파일 업로드 함수
    fun writePostWithFiles(title: String, content: String, fileUris: List<Uri>, onPostComplete: () -> Unit) {
        val token = SharedPreferencesUtils.getToken(getApplication<Application>().applicationContext)
        val boardWriteDto = BoardWriteDto(title, content)

        viewModelScope.launch {
            // 파일 정보 생성
            val fileDetails = fileUris.mapNotNull { uri ->
                val fileName = getFileName(uri)
                val mimeType = getApplication<Application>().contentResolver.getType(uri) ?: "application/octet-stream"
                val file = uriToFile(uri, fileName)
                FileDetail(file, mimeType, fileName)
            }
            // 게시글 작성 API 호출
            val postResponse = withContext(Dispatchers.IO) {
                boardApi.writePost("Bearer $token", boardWriteDto).execute()
            }
            if (postResponse.isSuccessful) {
                onPostComplete()
                fetchBoardData()
                val createdBoardId = postResponse.body()?.boardId
                fileDetails.forEach { fileDetail ->
                    if (createdBoardId != null) {
                        uploadFile(createdBoardId, fileDetail, token)
                    }
                }
            }
        }
    }

    // Uri를 File 객체로 변환하는 함수
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

    // 파일 업로드 함수
    private suspend fun uploadFile(boardId: Long, fileDetail: FileDetail, token: String?) {
        val requestBody = fileDetail.file.asRequestBody(fileDetail.mimeType.toMediaTypeOrNull())
        val multipartBody = MultipartBody.Part.createFormData("file", fileDetail.fileName, requestBody)

        val response = withContext(Dispatchers.IO) {
            fileApi.uploadFile("Bearer $token", boardId, multipartBody).execute()
        }
    }

    // Uri에서 파일 이름을 가져오는 함수
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

    // 게시글 삭제 함수
    fun deleteBoard(boardId: Long) {
        val token = SharedPreferencesUtils.getToken(getApplication<Application>().applicationContext)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                boardApi.deleteBoard("Bearer $token", boardId).execute()
            }
            fetchBoardData()
        }
    }
}
