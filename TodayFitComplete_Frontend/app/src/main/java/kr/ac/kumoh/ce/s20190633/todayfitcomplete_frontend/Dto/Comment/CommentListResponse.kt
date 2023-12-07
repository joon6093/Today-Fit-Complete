package kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.Comment

data class CommentListResponse(
    val commentId: Long,
    val content: String,
    val commentWriterName: String,
    val createdDate: String,
    val modifiedDate: String
)
