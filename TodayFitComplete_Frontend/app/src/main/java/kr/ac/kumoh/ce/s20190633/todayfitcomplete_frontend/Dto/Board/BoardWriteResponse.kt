package kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.Board

// BoardWriteResponse 클래스: 게시글 작성 후 서버에서 반환되는 응답 데이터를 나타냅니다.
data class BoardWriteResponse(
    val boardId: Long,                   // 생성된 게시글의 고유 ID
    val title: String,                   // 게시글 제목
    val content: String,                 // 게시글 내용
    val writerName: String,              // 작성자 이름
    val createdDate: String              // 작성일
)