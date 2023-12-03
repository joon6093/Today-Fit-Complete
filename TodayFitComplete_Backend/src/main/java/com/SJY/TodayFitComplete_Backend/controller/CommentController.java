package com.SJY.TodayFitComplete_Backend.controller;


import com.SJY.TodayFitComplete_Backend.dto.request.comment.CommentDto;
import com.SJY.TodayFitComplete_Backend.dto.response.comment.CommentResponse;
import com.SJY.TodayFitComplete_Backend.entity.Member;
import com.SJY.TodayFitComplete_Backend.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/board/{boardId}/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /**
     * 특정 게시글에 대한 모든 댓글을 페이지네이션 형태로 반환합니다.
     *
     * @param boardId 게시글 ID
     * @param pageable 페이징 정보
     * @return 페이지네이션된 댓글 목록
     */
    @GetMapping("/list")
    public ResponseEntity<Page<CommentResponse>> commentList(
            @PathVariable Long boardId,
            @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<CommentResponse> commentList = commentService.getAllComments(pageable, boardId);
        return ResponseEntity.status(HttpStatus.OK).body(commentList);
    }

    /**
     * 새로운 댓글을 작성합니다.
     *
     * @param member 인증된 사용자 정보
     * @param boardId 게시글 ID
     * @param commentDto 댓글 작성 데이터
     * @return 생성된 댓글 정보
     */
    @PostMapping("/write")
    public ResponseEntity<CommentResponse> write(
            @AuthenticationPrincipal Member member,
            @PathVariable Long boardId,
            @RequestBody CommentDto commentDto) {

        CommentResponse saveCommentDTO = commentService.write(boardId, member, commentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saveCommentDTO);
    }

    /**
     * 특정 댓글을 수정합니다.
     *
     * @param commentId 수정할 댓글 ID
     * @param commentDto 수정할 댓글 데이터
     * @return 수정된 댓글 정보
     */
    @PatchMapping("/update/{commentId}")
    public ResponseEntity<CommentResponse> update(
            @PathVariable Long commentId,
            @RequestBody CommentDto commentDto) {

        CommentResponse updateCommentDTO = commentService.update(commentId, commentDto);
        return ResponseEntity.status(HttpStatus.OK).body(updateCommentDTO);
    }

    /**
     * 특정 댓글을 삭제합니다.
     *
     * @param commentId 삭제할 댓글 ID
     * @return 삭제된 댓글 ID
     */
    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<Long> delete(@PathVariable Long commentId) {

        commentService.delete(commentId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}

