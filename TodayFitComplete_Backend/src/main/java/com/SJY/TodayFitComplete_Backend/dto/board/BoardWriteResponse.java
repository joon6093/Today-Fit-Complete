package com.SJY.TodayFitComplete_Backend.dto.board;

import com.SJY.TodayFitComplete_Backend.entity.board.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 게시글 작성 응답 정보를 위한 DTO.
 */
@Getter
@Setter
@NoArgsConstructor
public class BoardWriteResponse {

    private Long boardId;
    private String title;
    private String content;
    private String writerName;
    private String createdDate;

    @Builder
    public BoardWriteResponse(Long boardId, String title, String content, String writerName, String createdDate) {
        this.boardId = boardId;
        this.title = title;
        this.content = content;
        this.writerName = writerName;
        this.createdDate = createdDate;
    }

    public static BoardWriteResponse fromEntity(Board board, String writerName) {
        return BoardWriteResponse.builder()
                .boardId(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .writerName(writerName)
                .createdDate(board.getCreatedDate())
                .build();
    }
}
