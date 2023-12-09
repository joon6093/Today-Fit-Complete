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
    // 멀티파트 요청을 사용하여 파일 업로드 API를 정의합니다.
    @Multipart
    @POST("/board/{boardId}/file/upload")
    fun uploadFile(
        // 헤더에 인증 토큰을 추가하여 사용자를 인증합니다.
        @Header("Authorization") token: String,
        // 경로 변수 {boardId}를 동적으로 설정하여 업로드할 게시판을 지정합니다.
        @Path("boardId") boardId: Long,
        // 멀티파트 요청의 일부로 업로드할 파일을 첨부합니다.
        @Part file: MultipartBody.Part
    ): Call<List<FileUploadResponse>>
}