package com.SJY.TodayFitComplete_Backend.dto.request.comment;

import com.SJY.TodayFitComplete_Backend.entity.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 댓글 등록 및 수정 요청 DTO.
 */
@Getter
@Setter
@NoArgsConstructor
public class CommentDto {
    private String content;

    public static Comment ofEntity(CommentDto dto) {
        return Comment.builder()
                .content(dto.getContent())
                .build();
    }
}

