package com.SJY.TodayFitComplete_Backend.dto.response.board;

import com.SJY.TodayFitComplete_Backend.entity.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 게시글 목록 응답을 위한 DTO. 양방향 관계로 인해 JSON 직렬화 루프를 피하기 위함.
 */
@Getter
@Setter
@NoArgsConstructor
public class BoardListResponse {

    private Long boardId;
    private String title;
    private String content;
    private int viewCount;
    private String createdDate;
    private String modifiedDate;
    private String writerName;

    @Builder
    public BoardListResponse(Long boardId, String title, String content, int viewCount, String createdDate, String modifiedDate, String writerName) {
        this.boardId = boardId;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.writerName = writerName;
    }

    public static BoardListResponse fromEntity(Board board) {
        return BoardListResponse.builder()
                .boardId(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .viewCount(board.getViewCount())
                .createdDate(board.getCreatedDate())
                .modifiedDate(board.getModifiedDate())
                .writerName(board.getMember().getUsername())
                .build();
    }
}
