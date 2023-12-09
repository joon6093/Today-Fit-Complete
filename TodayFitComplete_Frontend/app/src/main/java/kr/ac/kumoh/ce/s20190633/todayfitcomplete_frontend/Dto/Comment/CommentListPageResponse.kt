package kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.Comment

// CommentListPageResponse: 서버에서 받아온 여러 댓글 목록 페이지 데이터를 담는 데이터 클래스
data class CommentListPageResponse(
    val content: List<CommentListResponse> // 댓글 목록 페이지에 있는 댓글 목록을 저장하는 리스트
)