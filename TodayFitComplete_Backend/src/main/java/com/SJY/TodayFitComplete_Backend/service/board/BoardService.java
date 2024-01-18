package com.SJY.TodayFitComplete_Backend.service.board;

import com.SJY.TodayFitComplete_Backend.dto.board.*;
import com.SJY.TodayFitComplete_Backend.entity.board.Board;
import com.SJY.TodayFitComplete_Backend.entity.member.Member;
import com.SJY.TodayFitComplete_Backend.exception.BoardNotFoundException;
import com.SJY.TodayFitComplete_Backend.exception.MemberNotFoundException;
import com.SJY.TodayFitComplete_Backend.repository.board.BoardRepository;
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
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    /**
     * 모든 게시글 조회
     */
    public Page<BoardListResponse> getAllBoards(Pageable pageable) {
        Page<Board> boards = boardRepository.findAllBoardsWithMember(pageable);
        List<BoardListResponse> list = boards.getContent().stream()
                .map(BoardListResponse::fromEntity)
                .collect(Collectors.toList());
        return new PageImpl<>(list, pageable, boards.getTotalElements());
    }

    /**
     * 게시글 검색
     */
    public Page<BoardListResponse> search(String title, String content, String writerName, Pageable pageable) {
        boolean isTitleValid = title != null && !title.trim().isEmpty();
        boolean isContentValid = content != null && !content.trim().isEmpty();
        boolean isWriterNameValid = writerName != null && !writerName.trim().isEmpty();
        Page<Board> result;
        if (isTitleValid) {
            result = boardRepository.findBoardsByTitleContaining(title, pageable);
        } else if (isContentValid) {
            result = boardRepository.findBoardsByContentContaining(content, pageable);
        } else if (isWriterNameValid) {
            result = boardRepository.findBoardsByAuthorUsernameContaining(writerName, pageable);
        } else {
            result = boardRepository.findAll(pageable);
        }
        List<BoardListResponse> list = result.getContent().stream()
                .map(BoardListResponse::fromEntity)
                .collect(Collectors.toList());
        return new PageImpl<>(list, pageable, result.getTotalElements());
    }

    /**
     * 게시글 등록
     */
    @Transactional
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
       Board board = boardRepository.findBoardWithMemberAndFilesById(boardId).orElseThrow(
               () -> new BoardNotFoundException(boardId.toString()));
        board.upViewCount();
       return BoardDetailsResponse.fromEntity(board);
    }

    /**
     * 게시글 수정
     */
    @Transactional
    @PreAuthorize("@boardAccessHandler.check(#boardId)")
    public BoardDetailsResponse update(@Param("boardId")Long boardId, BoardUpdateRequest boardDTO) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new BoardNotFoundException(boardId.toString()));
        board.update(boardDTO.getTitle(), boardDTO.getContent());
        return BoardDetailsResponse.fromEntity(board);
    }

    /**
     * 게시글 삭제
     */
    @Transactional
    @PreAuthorize("@boardAccessHandler.check(#boardId)")
    public void delete(@Param("boardId")Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new BoardNotFoundException(boardId.toString()));
        boardRepository.delete(board);
    }
}
