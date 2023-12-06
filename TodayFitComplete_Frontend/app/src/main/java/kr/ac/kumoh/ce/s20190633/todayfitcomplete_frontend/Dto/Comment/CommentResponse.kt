package kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.Comment

data class CommentResponse(
    val commentId: Long,
    val content: String,
    val writerName: String,
    val createdDate: String,
    val modifiedDate: String
)
