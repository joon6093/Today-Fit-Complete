package kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.File

// FileUploadResponse 클래스: 파일 업로드 후 서버에서 반환되는 응답 데이터를 나타냅니다.
data class FileUploadResponse(
    val fileId: Long,             // 파일의 고유 ID
    val originFileName: String,   // 업로드된 파일의 원본 파일 이름
    val filePath: String,         // 업로드된 파일의 서버 상의 저장 경로
    val fileType: String          // 파일의 유형 (예: 이미지, 문서 등)
)
