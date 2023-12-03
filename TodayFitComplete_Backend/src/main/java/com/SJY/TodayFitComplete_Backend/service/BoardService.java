package com.SJY.TodayFitComplete_Backend.service;

import com.SJY.TodayFitComplete_Backend.common.exception.ResourceNotFoundException;
import com.SJY.TodayFitComplete_Backend.dto.request.board.BoardUpdateDto;
import com.SJY.TodayFitComplete_Backend.dto.request.board.BoardWriteDto;
import com.SJY.TodayFitComplete_Backend.dto.request.board.SearchData;
import com.SJY.TodayFitComplete_Backend.dto.response.board.BoardDetailsResponse;
import com.SJY.TodayFitComplete_Backend.dto.response.board.BoardListResponse;
import com.SJY.TodayFitComplete_Backend.dto.response.board.BoardWriteResponse;
import com.SJY.TodayFitComplete_Backend.entity.Board;
import com.SJY.TodayFitComplete_Backend.entity.Member;
import com.SJY.TodayFitComplete_Backend.repository.BoardRepository;
import com.SJY.TodayFitComplete_Backend.repository.MemberRepository;
import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
    public BoardWriteResponse write(BoardWriteDto boardDTO, Member member) {

        Board board = BoardWriteDto.ofEntity(boardDTO);
        Member writerMember = memberRepository.findByEmail(member.getEmail()).orElseThrow(
                () -> new ResourceNotFoundException("Member", "Email", member.getEmail())
        );
        board.setMappingMember(writerMember);
        Board saveBoard = boardRepository.save(board);
        return BoardWriteResponse.fromEntity(saveBoard, writerMember.getUsername());
    }

    /**
     * 게시글 상세보기
     */
    public BoardDetailsResponse detail(Long boardId) {
       Board findBoard = boardRepository.findBoardWithMemberById(boardId).orElseThrow(
               () -> new ResourceNotFoundException("Board", "Id", String.valueOf(boardId))
       );
       findBoard.upViewCount();
       return BoardDetailsResponse.fromEntity(findBoard);
    }

    /**
     * 게시글 수정
     */
    public BoardDetailsResponse update(Long boardId, BoardUpdateDto boardDTO) {
        Board updateBoard = boardRepository.findBoardWithMemberById(boardId).orElseThrow(
                () -> new ResourceNotFoundException("Board", "Id", String.valueOf(boardId))
        );
        updateBoard.update(boardDTO.getTitle(), boardDTO.getContent());
        return BoardDetailsResponse.fromEntity(updateBoard);
    }

    /**
     * 게시글 삭제
     */
    public void delete(Long boardId) {
        boardRepository.deleteById(boardId);
    }
}
