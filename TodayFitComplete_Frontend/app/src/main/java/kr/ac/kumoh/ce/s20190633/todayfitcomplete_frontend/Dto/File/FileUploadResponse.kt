package kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.File

data class FileUploadResponse(
    val fileId: Long,
    val originFileName: String,
    val filePath: String,
    val fileType: String
)