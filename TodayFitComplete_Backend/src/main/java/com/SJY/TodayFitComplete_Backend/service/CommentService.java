package com.SJY.TodayFitComplete_Backend.service;

import com.SJY.TodayFitComplete_Backend.common.exception.MemberException;
import com.SJY.TodayFitComplete_Backend.common.exception.ResourceNotFoundException;
import com.SJY.TodayFitComplete_Backend.dto.request.comment.CommentDto;
import com.SJY.TodayFitComplete_Backend.dto.response.comment.CommentResponse;
import com.SJY.TodayFitComplete_Backend.entity.Board;
import com.SJY.TodayFitComplete_Backend.entity.Comment;
import com.SJY.TodayFitComplete_Backend.entity.Member;
import com.SJY.TodayFitComplete_Backend.repository.BoardRepository;
import com.SJY.TodayFitComplete_Backend.repository.CommentRepository;
import com.SJY.TodayFitComplete_Backend.repository.MemberRepository;
import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
    public CommentResponse write(Long boardId, Member member, CommentDto writeDto) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new ResourceNotFoundException("Board", "Board id", String.valueOf(boardId))
        );
        Member commentWriter = memberRepository.findById(member.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Member", "Member id", String.valueOf(member.getId()))
        );
        Comment comment = CommentDto.ofEntity(writeDto);
        comment.setBoard(board);
        comment.setMember(commentWriter);

        Comment saveComment = commentRepository.save(comment);
        return CommentResponse.fromEntity(saveComment);
    }

    /**
     * 댓글 수정
     */
    public CommentResponse update(Long commentId, CommentDto commentDto, Member currentMember) {
        Comment comment = commentRepository.findCommentWithMemberAndBoardById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("Comment", "Comment Id", String.valueOf(commentId))
        );

        if (!comment.getMember().equals(currentMember)) {
            throw new MemberException("You do not have permission to edit this comment.", HttpStatus.FORBIDDEN);
        }

        comment.update(commentDto.getContent());
        return CommentResponse.fromEntity(comment);
    }

    /**
     * 댓글 삭제
     */
    public void delete(Long commentId, Member currentMember) {
        Comment comment = commentRepository.findCommentWithMemberAndBoardById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("Comment", "Comment Id", String.valueOf(commentId))
        );

        if (!comment.getMember().equals(currentMember)) {
            throw new MemberException("You do not have permission to delete this comment.", HttpStatus.FORBIDDEN);
        }

        commentRepository.deleteById(commentId);
    }
}
