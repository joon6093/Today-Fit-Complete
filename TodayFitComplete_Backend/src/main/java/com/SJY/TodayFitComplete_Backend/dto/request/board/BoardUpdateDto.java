package com.SJY.TodayFitComplete_Backend.dto.request.board;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 게시글 수정을 위한 요청 DTO.
 */
@Getter
@Setter
@NoArgsConstructor
public class BoardUpdateDto {
    private String title;
    private String content;

}

