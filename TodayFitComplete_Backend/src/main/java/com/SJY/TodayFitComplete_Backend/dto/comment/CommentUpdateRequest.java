package com.SJY.TodayFitComplete_Backend.dto.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 댓글 등록 및 수정 요청 DTO.
 */
@Getter
@Setter
@NoArgsConstructor
public class CommentUpdateRequest {

    @NotBlank
    private String content;
}

