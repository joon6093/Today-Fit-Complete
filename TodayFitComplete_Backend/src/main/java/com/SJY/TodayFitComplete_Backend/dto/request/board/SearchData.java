package com.SJY.TodayFitComplete_Backend.dto.request.board;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 게시글 검색 요청을 위한 DTO.
 */
@Getter
@Setter
@Builder
public class SearchData {
    private String title;
    private String content;
    private String writerName;
}

