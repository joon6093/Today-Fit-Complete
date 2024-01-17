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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    /**
     * 모든 댓글 조회 및 페이징 처리
     */
    public Page<CommentResponse> getAllComments(Pageable pageable, Long boardId) {
        Page<Comment> comments = commentRepository.findCommentsWithMemberAndBoardByBoardId(pageable, boardId);
        List<CommentResponse> commentList = comments.getContent().stream()
                .map(CommentResponse::fromEntity)
                .collect(Collectors.toList());
        return new PageImpl<>(commentList, pageable, comments.getTotalElements());
    }

    /**
     * 댓글 작성
     */
    public CommentResponse write(Long boardId, CommentWriteRequest writeDto) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new BoardNotFoundException(boardId.toString()));
        Member member = memberRepository.findById(writeDto.getMemberId()).orElseThrow(
                () -> new MemberNotFoundException(writeDto.getMemberId().toString()));
        Comment comment = CommentWriteRequest.ofEntity(writeDto);
        comment.setBoard(board);
        comment.setMember(member);
        Comment saveComment = commentRepository.save(comment);
        return CommentResponse.fromEntity(saveComment);
    }

    /**
     * 댓글 수정
     */
    public CommentResponse update(Long commentId, CommentUpdateRequest commentDto, Member currentMember) {
        Comment comment = commentRepository.findCommentWithMemberAndBoardById(commentId).orElseThrow(
                () -> new CommentNotFoundException(commentId.toString()));

        if (!comment.getMember().equals(currentMember)) {
            throw new AccessDeniedException("AccessDeniedException");
        }

        comment.update(commentDto.getContent());
        return CommentResponse.fromEntity(comment);
    }

    /**
     * 댓글 삭제
     */
    public void delete(Long commentId, Member currentMember) {
        Comment comment = commentRepository.findCommentWithMemberAndBoardById(commentId).orElseThrow(
                () -> new CommentNotFoundException(commentId.toString()));

        if (!comment.getMember().equals(currentMember)) {
            throw new AccessDeniedException("AccessDeniedException");
        }

        commentRepository.deleteById(commentId);
    }
}
