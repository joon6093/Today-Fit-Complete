package com.SJY.TodayFitComplete_Backend.dto.board;

import com.SJY.TodayFitComplete_Backend.entity.board.Board;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 새 게시글 등록을 위한 요청 DTO.
 */
@Getter
@Setter
@NoArgsConstructor
public class BoardWriteRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @Null
    private Long memberId;

    public static Board ofEntity(BoardWriteRequest dto) {
        return Board.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .build();
    }
}

