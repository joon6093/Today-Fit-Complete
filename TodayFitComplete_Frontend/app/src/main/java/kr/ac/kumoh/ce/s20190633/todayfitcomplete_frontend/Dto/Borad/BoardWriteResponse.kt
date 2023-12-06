package kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.Borad

data class BoardWriteResponse(
    val boardId: Long,
    val title: String,
    val content: String,
    val writerName: String,
    val createdDate: String
)