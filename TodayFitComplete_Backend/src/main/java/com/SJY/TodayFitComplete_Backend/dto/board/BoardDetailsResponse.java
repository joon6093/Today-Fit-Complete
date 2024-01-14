package com.SJY.TodayFitComplete_Backend.dto.board;

import com.SJY.TodayFitComplete_Backend.dto.comment.CommentResponse;
import com.SJY.TodayFitComplete_Backend.dto.file.BoardDetailsFileResponse;
import com.SJY.TodayFitComplete_Backend.entity.board.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 게시글 상세 정보 및 수정 응답 DTO.
 */
@Getter
@Setter
@NoArgsConstructor
public class BoardDetailsResponse {

    private Long boardId;
    private String title;
    private String content;
    private int viewCount;
    private String writerName;
    private String createdDate;
    private String modifiedDate;

    private List<CommentResponse> comments;
    private List<BoardDetailsFileResponse> files;

    @Builder
    public BoardDetailsResponse(Long boardId, String title, String content, int viewCount, String writerName, String createdDate, String modifiedDate, List<CommentResponse> comments, List<BoardDetailsFileResponse> files) {
        this.boardId = boardId;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.writerName = writerName;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.comments = comments;
        this.files = files;
    }

    public static BoardDetailsResponse fromEntity(Board board) {
        return BoardDetailsResponse.builder()
                .boardId(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .viewCount(board.getViewCount())
                .writerName(board.getMember().getUsername())
                .createdDate(board.getCreatedDate())
                .modifiedDate(board.getModifiedDate())
                .comments(board.getComments().stream()
                        .map(CommentResponse::fromEntity)
                        .collect(Collectors.toList()))
                .files(board.getFiles().stream()
                        .map(BoardDetailsFileResponse::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }
}
