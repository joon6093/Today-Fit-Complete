package com.SJY.TodayFitComplete_Backend.dto.response.file;

import com.SJY.TodayFitComplete_Backend.entity.FileEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 파일 다운로드 후 응답.
 */
@Getter
@Setter
@NoArgsConstructor
public class FileDownloadResponse {

    private String filename;
    private String fileType;
    private byte[] content;

    @Builder
    public FileDownloadResponse(String filename, String fileType, byte[] content) {
        this.filename = filename;
        this.fileType = fileType;
        this.content = content;
    }

    public static FileDownloadResponse fromFileResource(FileEntity file, String contentType, byte[] content) {
        return FileDownloadResponse.builder()
                .filename(file.getOriginFileName())
                .fileType(contentType)
                .content(content)
                .build();
    }
}
