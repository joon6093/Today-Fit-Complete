package kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.Board

// BoardWriteDto 클래스: 게시글을 작성할 때 필요한 데이터를 나타냅니다.
data class BoardWriteDto(
    val title: String,                   // 게시글 제목
    val content: String                  // 게시글 내용
)
