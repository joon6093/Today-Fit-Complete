package kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.Board

import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.Comment.CommentListResponse


// BoardDetailsResponse 클래스: 특정 게시글의 상세 정보를 나타냅니다.
data class BoardDetailsResponse(
    val boardId: Long,                    // 게시글 고유 ID
    val title: String,                   // 게시글 제목
    val content: String,                 // 게시글 내용
    val viewCount: Int,                  // 게시글 조회수
    val writerName: String,              // 작성자 이름
    val createdDate: String,             // 작성일
    val modifiedDate: String,            // 수정일
    val comments: List<CommentListResponse> = listOf(), // 댓글 목록 (기본값은 빈 목록)
    val files: List<BoardDetailsFileResponse> = listOf()  // 파일 목록 (기본값은 빈 목록)
)

// BoardDetailsFileResponse 클래스: 게시글에 첨부된 파일의 정보를 나타냅니다.
data class BoardDetailsFileResponse(
    val fileId: Long,                   // 파일 고유 ID
    val originFileName: String,         // 원본 파일 이름
    val fileType: String,               // 파일 유형
    val filePath: String                // 파일 경로
)