package com.SJY.TodayFitComplete_Backend.controller;


import com.SJY.TodayFitComplete_Backend.dto.request.board.BoardUpdateDto;
import com.SJY.TodayFitComplete_Backend.dto.request.board.BoardWriteDto;
import com.SJY.TodayFitComplete_Backend.dto.request.board.SearchData;
import com.SJY.TodayFitComplete_Backend.dto.response.board.BoardDetailsResponse;
import com.SJY.TodayFitComplete_Backend.dto.response.board.BoardListResponse;
import com.SJY.TodayFitComplete_Backend.dto.response.board.BoardWriteResponse;
import com.SJY.TodayFitComplete_Backend.entity.Member;
import com.SJY.TodayFitComplete_Backend.service.BoardService;
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
@RequestMapping("/board")
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService boardService;

    /**
     * 게시글 목록을 페이지네이션으로 반환합니다.
     *
     * @param pageable 페이징 정보 (크기, 정렬 등)
     * @return 페이지네이션된 게시글 목록
     */
    @GetMapping("/list")
    public ResponseEntity<Page<BoardListResponse>> boardList(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<BoardListResponse> listDTO = boardService.getAllBoards(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(listDTO);
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
    public ResponseEntity<Page<BoardListResponse>> search(
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
        return ResponseEntity.status(HttpStatus.OK).body(searchBoard);
    }

    /**
     * 새로운 게시글을 작성합니다.
     *
     * @param boardDTO 게시글 작성 데이터
     * @param member 인증된 사용자 정보
     * @return 생성된 게시글 정보
     */
    @PostMapping("/write")
    public ResponseEntity<BoardWriteResponse> write(
            @RequestBody BoardWriteDto boardDTO,
            @AuthenticationPrincipal Member member) {
        BoardWriteResponse saveBoardDTO = boardService.write(boardDTO, member);
        return ResponseEntity.status(HttpStatus.CREATED).body(saveBoardDTO);
    }

    /**
     * 특정 게시글의 상세 정보를 반환합니다.
     *
     * @param boardId 게시글 ID
     * @return 게시글 상세 정보
     */
    @GetMapping("/{boardId}")
    public ResponseEntity<BoardDetailsResponse> detail(@PathVariable("boardId") Long boardId) {
        BoardDetailsResponse findBoardDTO = boardService.detail(boardId);
        return ResponseEntity.status(HttpStatus.OK).body(findBoardDTO);
    }

    /**
     * 특정 게시글을 수정합니다.
     *
     * @param boardId 수정할 게시글 ID
     * @param boardDTO 수정할 게시글 정보
     * @param member 인증된 사용자 정보
     * @return 수정된 게시글 정보
     */
    @PatchMapping("/{boardId}/update")
    public ResponseEntity<BoardDetailsResponse> update(
            @PathVariable Long boardId,
            @RequestBody BoardUpdateDto boardDTO,
            @AuthenticationPrincipal Member member) {
        BoardDetailsResponse updateBoardDTO = boardService.update(boardId, boardDTO, member);
        return ResponseEntity.status(HttpStatus.OK).body(updateBoardDTO);
    }

    /**
     * 특정 게시글을 삭제합니다.
     *
     * @param boardId 삭제할 게시글 ID
     * @param member 인증된 사용자 정보
     * @return 삭제된 게시글 ID
     */
    @DeleteMapping("/{boardId}/delete")
    public ResponseEntity<Long> delete(@PathVariable Long boardId, @AuthenticationPrincipal Member member) {
        boardService.delete(boardId, member);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}

