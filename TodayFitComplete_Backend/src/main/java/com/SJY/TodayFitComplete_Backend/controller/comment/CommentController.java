package com.SJY.TodayFitComplete_Backend.controller.comment;

import com.SJY.TodayFitComplete_Backend.aop.AssignMemberId;
import com.SJY.TodayFitComplete_Backend.dto.comment.CommentResponse;
import com.SJY.TodayFitComplete_Backend.dto.comment.CommentUpdateRequest;
import com.SJY.TodayFitComplete_Backend.dto.comment.CommentWriteRequest;
import com.SJY.TodayFitComplete_Backend.dto.response.Response;
import com.SJY.TodayFitComplete_Backend.service.comment.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/board/{boardId}/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /**
     * 특정 게시글에 대한 모든 댓글을 반환합니다.
     *
     * @param boardId 게시글 ID
     * @param pageable 페이징 정보
     * @return 댓글 목록
     */
    @GetMapping("/list")
    public ResponseEntity<Response> commentList(
            @PathVariable("boardId") Long boardId,
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<CommentResponse> commentList = commentService.getAllComments(pageable, boardId);
        return ResponseEntity.status(HttpStatus.OK).body(Response.success(commentList));
    }

    /**
     * 새로운 댓글을 작성합니다.
     *
     * @param boardId 게시글 ID
     * @param commentDto 댓글 작성 데이터
     * @return 생성된 댓글 정보
     */
    @PostMapping("/write")
    @AssignMemberId
    public ResponseEntity<Response> write(
            @PathVariable("boardId") Long boardId,
            @Valid @RequestBody CommentWriteRequest commentDto) {
        CommentResponse saveCommentDTO = commentService.write(boardId, commentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(Response.success(saveCommentDTO));
    }

    /**
     * 특정 댓글을 수정합니다.
     *
     * @param commentId 수정할 댓글 ID
     * @param commentDto 수정할 댓글 데이터
     * @return 수정된 댓글 정보
     */
    @PutMapping("/update/{commentId}")
    public ResponseEntity<Response> update(
            @PathVariable("boardId") Long commentId,
            @Valid @RequestBody CommentUpdateRequest commentDto) {
        CommentResponse updateCommentDTO = commentService.update(commentId, commentDto);
        return ResponseEntity.status(HttpStatus.OK).body(Response.success(updateCommentDTO));
    }


    /**
     * 특정 댓글을 삭제합니다.
     *
     * @param commentId 삭제할 댓글 ID
     * @return 삭제된 댓글 ID
     */
    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<Response> delete(
            @PathVariable("boardId") Long commentId) {
        commentService.delete(commentId);
        return ResponseEntity.status(HttpStatus.OK).body(Response.success());
    }
}

