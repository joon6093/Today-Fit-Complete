package kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.Board

// BoardListResponse 클래스: 게시글 목록에서 각 게시글의 요약 정보를 나타냅니다.
data class BoardListResponse(
    val boardId: Long,                   // 게시글 고유 ID
    val title: String,                   // 게시글 제목
    val content: String,                 // 게시글 내용
    val viewCount: Int,                  // 게시글 조회수
    val createdDate: String,             // 작성일
    val modifiedDate: String,            // 수정일
    val writerName: String               // 작성자 이름
)