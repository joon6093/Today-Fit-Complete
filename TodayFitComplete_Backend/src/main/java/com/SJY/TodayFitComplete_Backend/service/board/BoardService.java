package com.SJY.TodayFitComplete_Backend.service.board;

import com.SJY.TodayFitComplete_Backend.dto.board.*;
import com.SJY.TodayFitComplete_Backend.entity.board.Board;
import com.SJY.TodayFitComplete_Backend.entity.member.Member;
import com.SJY.TodayFitComplete_Backend.exception.BoardNotFoundException;
import com.SJY.TodayFitComplete_Backend.exception.MemberNotFoundException;
import com.SJY.TodayFitComplete_Backend.repository.board.BoardRepository;
import com.SJY.TodayFitComplete_Backend.repository.member.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    /**
     * 모든 게시글 조회 및 페이징 처리
     */
    public Page<BoardListResponse> getAllBoards(Pageable pageable) {
        Page<Board> boards = boardRepository.findAllBoardsWithMember(pageable);
        List<BoardListResponse> list = boards.getContent().stream()
                .map(BoardListResponse::fromEntity)
                .collect(Collectors.toList());
        return new PageImpl<>(list, pageable, boards.getTotalElements());
    }

    /**
     * 게시글 검색 및 페이징 처리
     */
    public Page<BoardListResponse> search(SearchData searchData, Pageable pageable) {
        Page<Board> result = null;
        if (!searchData.getTitle().isEmpty()) {
            result = boardRepository.findBoardsByTitleContaining(searchData.getTitle(), pageable);
        } else if (!searchData.getContent().isEmpty()) {
            result = boardRepository.findBoardsByContentContaining(searchData.getContent(), pageable);
        } else if (!searchData.getWriterName().isEmpty()) {
            result = boardRepository.findBoardsByAuthorUsernameContaining(searchData.getWriterName(), pageable);
        }
        List<BoardListResponse> list = result.getContent().stream()
                .map(BoardListResponse::fromEntity)
                .collect(Collectors.toList());
        return new PageImpl<>(list, pageable, result.getTotalElements());
    }

    /**
     * 게시글 등록
     */
    public BoardWriteResponse write(BoardWriteRequest boardDTO) {
        Member member = memberRepository.findById(boardDTO.getMemberId()).orElseThrow(
                () -> new MemberNotFoundException(boardDTO.getMemberId().toString()));
        Board board = Board.builder()
                .title(boardDTO.getTitle())
                .content(boardDTO.getContent())
                .member(member)
                .build();
        Board saveBoard = boardRepository.save(board);
        return BoardWriteResponse.fromEntity(saveBoard);
    }

    /**
     * 게시글 상세보기
     */
    public BoardDetailsResponse detail(Long boardId) {
       Board board = boardRepository.findBoardWithMemberById(boardId).orElseThrow(
               () -> new BoardNotFoundException(boardId.toString()));
        board.upViewCount();
       return BoardDetailsResponse.fromEntity(board);
    }

    /**
     * 게시글 수정
     */
    @PreAuthorize("@boardAccessHandler.check(#boardId)")
    public BoardDetailsResponse update(@Param("boardId")Long boardId, BoardUpdateRequest boardDTO) {
        Board board = boardRepository.findBoardWithMemberById(boardId).orElseThrow(
                () -> new BoardNotFoundException(boardId.toString()));
        board.update(boardDTO.getTitle(), boardDTO.getContent());
        return BoardDetailsResponse.fromEntity(board);
    }

    /**
     * 게시글 삭제
     */
    @PreAuthorize("@boardAccessHandler.check(#boardId)")
    public void delete(@Param("boardId")Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new BoardNotFoundException(boardId.toString()));
        boardRepository.delete(board);
    }
}
