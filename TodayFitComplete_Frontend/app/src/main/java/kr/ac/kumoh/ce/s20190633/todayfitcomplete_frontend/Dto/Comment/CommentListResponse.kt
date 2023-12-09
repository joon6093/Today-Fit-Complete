package kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.Comment

// CommentListResponse: 서버에서 받아온 개별 댓글 데이터를 담는 데이터 클래스
data class CommentListResponse(
    val commentId: Long,          // 댓글 고유 식별자
    val content: String,          // 댓글 내용
    val commentWriterName: String, // 댓글 작성자 이름
    val createdDate: String,      // 댓글 작성 일자
    val modifiedDate: String      // 댓글 수정 일자
)