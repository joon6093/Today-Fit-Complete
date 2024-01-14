package com.SJY.TodayFitComplete_Backend.dto.file;

import com.SJY.TodayFitComplete_Backend.entity.file.FileEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 게시글 상세 정보에 포함될 파일 정보.
 */
@Getter
@Setter
@NoArgsConstructor
public class BoardDetailsFileResponse {

    private Long fileId;
    private String originFileName;
    private String fileType;
    private String filePath;

    @Builder
    public BoardDetailsFileResponse(Long fileId, String originFileName, String fileType, String filePath) {
        this.fileId = fileId;
        this.originFileName = originFileName;
        this.fileType = fileType;
        this.filePath = filePath;
    }

    public static BoardDetailsFileResponse fromEntity(FileEntity file) {
        return BoardDetailsFileResponse.builder()
                .fileId(file.getId())
                .originFileName(file.getOriginFileName())
                .fileType(file.getFileType())
                .filePath(file.getFilePath())
                .build();
    }
}
