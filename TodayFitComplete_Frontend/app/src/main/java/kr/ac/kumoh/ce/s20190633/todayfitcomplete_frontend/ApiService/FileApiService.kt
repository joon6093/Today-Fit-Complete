package kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.ApiService
import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.File.FileUploadResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface FileApiService {
    @Multipart
    @POST("/board/{boardId}/file/upload")
    fun uploadFile(@Header("Authorization") token: String, @Path("boardId") boardId: Long, @Part file: MultipartBody.Part): Call<List<FileUploadResponse>>
}