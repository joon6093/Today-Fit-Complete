package com.SJY.TodayFitComplete_Backend.repository.board;

import com.SJY.TodayFitComplete_Backend.entity.board.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    @Query(value = "SELECT b FROM Board b JOIN FETCH b.member WHERE b.id = :boardID")
    Optional<Board> findBoardWithMemberById(Long boardID);

    @Query(value = "SELECT b FROM Board b JOIN FETCH b.member")
    Page<Board> findAllBoardsWithMember(Pageable pageable);

    @Query(value = "SELECT b FROM Board b JOIN FETCH b.member WHERE b.title LIKE %:title%")
    Page<Board> findBoardsByTitleContaining(String title, Pageable pageable);

    @Query(value = "SELECT b FROM Board b JOIN FETCH b.member WHERE b.content LIKE %:content%")
    Page<Board> findBoardsByContentContaining(String content, Pageable pageable);

    @Query(value = "SELECT b FROM Board b JOIN FETCH b.member WHERE b.member.username LIKE %:username%")
    Page<Board> findBoardsByAuthorUsernameContaining(String username, Pageable pageable);
}
