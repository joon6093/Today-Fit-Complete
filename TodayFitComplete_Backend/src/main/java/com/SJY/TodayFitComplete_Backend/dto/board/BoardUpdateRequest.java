package com.SJY.TodayFitComplete_Backend.dto.board;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 게시글 수정을 위한 요청 DTO.
 */
@Getter
@Setter
@NoArgsConstructor
public class BoardUpdateRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

}

