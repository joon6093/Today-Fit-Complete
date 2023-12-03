package com.SJY.TodayFitComplete_Backend.dto.request.board;

import com.SJY.TodayFitComplete_Backend.entity.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 새 게시글 등록을 위한 요청 DTO.
 */
@Getter
@Setter
@NoArgsConstructor
public class BoardWriteDto {
    private String title;
    private String content;

    public static Board ofEntity(BoardWriteDto dto) {
        return Board.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .build();
    }
}

