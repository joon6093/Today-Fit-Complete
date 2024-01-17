package com.SJY.TodayFitComplete_Backend.repository.comment;

import com.SJY.TodayFitComplete_Backend.entity.comment.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c JOIN FETCH c.member JOIN FETCH c.board b WHERE b.id = :boardId")
    Page<Comment> findCommentsWithMemberAndBoardByBoardId(@Param("boardId")Long boardId, Pageable pageable);

    @Query("SELECT c FROM Comment c JOIN FETCH c.member m JOIN FETCH c.board b WHERE c.id = :commentId")
    Optional<Comment> findCommentWithMemberAndBoardById(@Param("commentId")Long commentId);
}
