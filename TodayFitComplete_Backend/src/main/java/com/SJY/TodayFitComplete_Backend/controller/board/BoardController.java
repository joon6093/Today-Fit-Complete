package com.SJY.TodayFitComplete_Backend.controller.board;

import com.SJY.TodayFitComplete_Backend.aop.AssignMemberId;
import com.SJY.TodayFitComplete_Backend.dto.board.*;
import com.SJY.TodayFitComplete_Backend.dto.response.Response;
import com.SJY.TodayFitComplete_Backend.entity.member.Member;
import com.SJY.TodayFitComplete_Backend.service.board.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService boardService;

    /**
     * 게시글 목록을 반환합니다.
     *
     * @param pageable 페이징 정보 (크기, 정렬 등)
     * @return 게시글 목록
     */
    @GetMapping("/list")
    public ResponseEntity<Response> boardList(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<BoardListResponse> listDTO = boardService.getAllBoards(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(Response.success(listDTO));
    }

    /**
     * 제목, 내용, 작성자명으로 게시글을 검색합니다.
     *
     * @param pageable 페이징 정보
     * @param title 제목
     * @param content 내용
     * @param writerName 작성자명
     * @return 검색 결과에 해당하는 게시글 목록
     */
    @GetMapping("/search")
    public ResponseEntity<Response> search(
            @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam String writerName) {
        SearchData searchData = SearchData.builder()
                .title(title)
                .content(content)
                .writerName(writerName)
                .build();
        Page<BoardListResponse> searchBoard = boardService.search(searchData, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(Response.success(searchBoard));
    }

    /**
     * 새로운 게시글을 작성합니다.
     *
     * @param boardDTO 게시글 작성 데이터
     * @return 생성된 게시글 정보
     */
    @PostMapping("/write")
    @AssignMemberId
    public ResponseEntity<Response> write(
            @Valid @RequestBody BoardWriteRequest boardDTO) {
        BoardWriteResponse saveBoardDTO = boardService.write(boardDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(Response.success(saveBoardDTO));
    }

    /**
     * 특정 게시글의 상세 정보를 반환합니다.
     *
     * @param boardId 게시글 ID
     * @return 게시글 상세 정보
     */
    @GetMapping("/{boardId}")
    public ResponseEntity<Response> detail(@PathVariable("boardId") Long boardId) {
        BoardDetailsResponse findBoardDTO = boardService.detail(boardId);
        return ResponseEntity.status(HttpStatus.OK).body(Response.success(findBoardDTO));
    }

    /**
     * 특정 게시글을 수정합니다.
     *
     * @param boardId 수정할 게시글 ID
     * @param boardDTO 수정할 게시글 정보
     * @param member 인증된 사용자 정보
     * @return 수정된 게시글 정보
     */
    @PatchMapping("/update/{boardId}")
    public ResponseEntity<Response> update(
            @PathVariable("boardId") Long boardId,
            @Valid @RequestBody BoardUpdateRequest boardDTO,
            @AuthenticationPrincipal Member member) {
        BoardDetailsResponse updateBoardDTO = boardService.update(boardId, boardDTO, member);
        return ResponseEntity.status(HttpStatus.OK).body(Response.success(updateBoardDTO));
    }

    /**
     * 특정 게시글을 삭제합니다.
     *
     * @param boardId 삭제할 게시글 ID
     * @param member 인증된 사용자 정보
     * @return 삭제된 게시글 ID
     */
    @DeleteMapping("/delete/{boardId}")
    public ResponseEntity<Response> delete(@PathVariable("boardId") Long boardId, @AuthenticationPrincipal Member member) {
        boardService.delete(boardId, member);
        return ResponseEntity.status(HttpStatus.OK).body(Response.success(boardId));
    }
}

