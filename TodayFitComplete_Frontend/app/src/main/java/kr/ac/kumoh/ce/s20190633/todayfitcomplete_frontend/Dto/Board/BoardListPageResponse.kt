import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.Board.BoardListResponse

// BoardListPageResponse 클래스: 게시글 목록을 페이지로 나누어 응답할 때 사용됩니다.
data class BoardListPageResponse(
    val content: List<BoardListResponse>,  // 게시글 목록
)