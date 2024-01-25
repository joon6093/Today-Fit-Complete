package com.SJY.TodayFitComplete_Backend.dto.board;

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

    @NotBlank(message = "{boardRequest.title.notBlank}")
    private String title;

    @NotBlank(message = "{boardRequest.content.notBlank}")
    private String content;

    @Null
    private Long memberId;
}

