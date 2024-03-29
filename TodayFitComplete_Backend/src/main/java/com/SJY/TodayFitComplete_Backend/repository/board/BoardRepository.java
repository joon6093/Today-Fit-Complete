package com.SJY.TodayFitComplete_Backend.repository.board;

import com.SJY.TodayFitComplete_Backend.entity.board.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    @Query(value = "SELECT b FROM Board b JOIN FETCH b.member")
    Page<Board> findAllWithMembers(Pageable pageable);

    @Query(value = "SELECT b FROM Board b JOIN FETCH b.member LEFT JOIN FETCH b.files WHERE b.id = :boardId")
    Optional<Board> findByIdWithMemberAndFiles(@Param("boardId") Long boardId);

    @Query("SELECT b FROM Board b LEFT JOIN FETCH b.files WHERE b.id = :boardId")
    Optional<Board> findByIdWithFiles(@Param("boardId") Long boardId);

    @Query(value = "SELECT b FROM Board b JOIN FETCH b.member WHERE b.title LIKE %:title%")
    Page<Board> findAllByTitleWithMembers(@Param("title")String title, Pageable pageable);

    @Query(value = "SELECT b FROM Board b JOIN FETCH b.member WHERE b.content LIKE %:content%")
    Page<Board> findAllByContentWithMembers(@Param("content")String content, Pageable pageable);

    @Query(value = "SELECT b FROM Board b JOIN FETCH b.member WHERE b.member.nickname LIKE %:username%")
    Page<Board> findAllByUsernameWithMembers(@Param("username")String username, Pageable pageable);
}
