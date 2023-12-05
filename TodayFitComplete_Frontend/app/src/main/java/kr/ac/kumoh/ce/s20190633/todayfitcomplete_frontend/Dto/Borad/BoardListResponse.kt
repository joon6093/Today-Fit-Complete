package kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.Borad

data class BoardListResponse(
    val boardId: Long,
    val title: String,
    val content: String,
    val viewCount: Int,
    val createdDate: String,
    val modifiedDate: String,
    val writerName: String
)
