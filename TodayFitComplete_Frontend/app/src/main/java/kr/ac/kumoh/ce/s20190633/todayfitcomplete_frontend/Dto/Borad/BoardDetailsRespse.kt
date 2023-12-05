package kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.Board

data class BoardDetailsResponse(
    val boardId: Long,
    val title: String,
    val content: String,
    val viewCount: Int,
    val writerName: String,
    val createdDate: String,
    val modifiedDate: String,
    val comments: List<CommentResponse> = listOf(),
    val files: List<BoardDetailsFileResponse> = listOf()
)
data class CommentResponse(
    val commentId: Long,
    val content: String,
    val writerName: String,
    val createdDate: String,
    val modifiedDate: String
)

data class BoardDetailsFileResponse(
    val fileId: Long,
    val originFileName: String,
    val fileType: String,
    val filePath: String
)