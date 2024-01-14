package com.SJY.TodayFitComplete_Backend.dto.comment;

import com.SJY.TodayFitComplete_Backend.entity.comment.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 댓글 등록 및 수정 응답.
 */
@Getter
@Setter
@NoArgsConstructor
public class CommentResponse {

    private Long commentId;
    private String content;
    private String createdDate;
    private String modifiedDate;
    private String commentWriterName;

    @Builder
    public CommentResponse(Long commentId, String content, String createdDate, String modifiedDate, String commentWriterName) {
        this.commentId = commentId;
        this.content = content;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.commentWriterName = commentWriterName;
    }

    public static CommentResponse fromEntity(Comment comment) {
        return CommentResponse.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .createdDate(comment.getCreatedDate())
                .modifiedDate(comment.getModifiedDate())
                .commentWriterName(comment.getMember().getUsername())
                .build();
    }
}
