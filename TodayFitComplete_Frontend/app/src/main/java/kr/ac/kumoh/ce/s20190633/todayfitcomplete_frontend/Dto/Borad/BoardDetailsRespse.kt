package kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.Board

import kr.ac.kumoh.ce.s20190633.todayfitcomplete_frontend.Dto.Comment.CommentListResponse


data class BoardDetailsResponse(
    val boardId: Long,
    val title: String,
    val content: String,
    val viewCount: Int,
    val writerName: String,
    val createdDate: String,
    val modifiedDate: String,
    val comments: List<CommentListResponse> = listOf(),
    val files: List<BoardDetailsFileResponse> = listOf()
)

data class BoardDetailsFileResponse(
    val fileId: Long,
    val originFileName: String,
    val fileType: String,
    val filePath: String
)