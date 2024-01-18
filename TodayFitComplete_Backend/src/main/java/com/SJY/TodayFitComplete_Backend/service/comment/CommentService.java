package com.SJY.TodayFitComplete_Backend.service.comment;

import com.SJY.TodayFitComplete_Backend.dto.comment.CommentResponse;
import com.SJY.TodayFitComplete_Backend.dto.comment.CommentUpdateRequest;
import com.SJY.TodayFitComplete_Backend.dto.comment.CommentWriteRequest;
import com.SJY.TodayFitComplete_Backend.entity.board.Board;
import com.SJY.TodayFitComplete_Backend.entity.comment.Comment;
import com.SJY.TodayFitComplete_Backend.entity.member.Member;
import com.SJY.TodayFitComplete_Backend.exception.BoardNotFoundException;
import com.SJY.TodayFitComplete_Backend.exception.CommentNotFoundException;
import com.SJY.TodayFitComplete_Backend.exception.MemberNotFoundException;
import com.SJY.TodayFitComplete_Backend.repository.board.BoardRepository;
import com.SJY.TodayFitComplete_Backend.repository.comment.CommentRepository;
import com.SJY.TodayFitComplete_Backend.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    /**
     * 모든 댓글 조회
     */
    public Page<CommentResponse> getAllComments(Pageable pageable, Long boardId) {
        Page<Comment> comments = commentRepository.findAllWithMemberAndBoardByBoardId(boardId, pageable);
        List<CommentResponse> commentList = comments.getContent().stream()
                .map(CommentResponse::fromEntity)
                .collect(Collectors.toList());
        return new PageImpl<>(commentList, pageable, comments.getTotalElements());
    }

    /**
     * 댓글 작성
     */
    @Transactional
    public CommentResponse write(Long boardId, CommentWriteRequest writeDto) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new BoardNotFoundException(boardId.toString()));
        Member member = memberRepository.findById(writeDto.getMemberId()).orElseThrow(
                () -> new MemberNotFoundException(writeDto.getMemberId().toString()));
        Comment comment = Comment.builder()
                .content(writeDto.getContent())
                .board(board)
                .member(member)
                .build();
        Comment saveComment = commentRepository.save(comment);
        return CommentResponse.fromEntity(saveComment);
    }

    /**
     * 댓글 수정
     */
    @Transactional
    @PreAuthorize("@commentAccessHandler.check(#commentId)")
    public CommentResponse update(@Param("commentId")Long commentId, CommentUpdateRequest commentDto) {
        Comment comment = commentRepository.findByIdWithMemberAndBoard(commentId).orElseThrow(
                () -> new CommentNotFoundException(commentId.toString()));
        comment.update(commentDto.getContent());
        return CommentResponse.fromEntity(comment);
    }

    /**
     * 댓글 삭제
     */
    @Transactional
    @PreAuthorize("@commentAccessHandler.check(#commentId)")
    public void delete(@Param("commentId")Long commentId) {
        Comment comment = commentRepository.findByIdWithMemberAndBoard(commentId).orElseThrow(
                () -> new CommentNotFoundException(commentId.toString()));
        commentRepository.delete(comment);
    }
}
