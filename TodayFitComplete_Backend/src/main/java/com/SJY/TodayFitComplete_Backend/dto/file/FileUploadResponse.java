package com.SJY.TodayFitComplete_Backend.dto.file;

import com.SJY.TodayFitComplete_Backend.entity.file.FileEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 파일 업로드 후 응답 DTO.
 */
@Getter
@Setter
@NoArgsConstructor
public class FileUploadResponse {

    private Long fileId;
    private String originFileName;
    private String filePath;
    private String fileType;

    @Builder
    public FileUploadResponse(Long fileId, String originFileName, String filePath, String fileType) {
        this.fileId = fileId;
        this.originFileName = originFileName;
        this.filePath = filePath;
        this.fileType = fileType;
    }

    public static FileUploadResponse fromEntity(FileEntity file) {
        return FileUploadResponse.builder()
                .fileId(file.getId())
                .originFileName(file.getOriginFileName())
                .filePath(file.getFilePath())
                .fileType(file.getFileType())
                .build();
    }
}
