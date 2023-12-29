package com.SJY.TodayFitComplete_Backend.controller;

import com.SJY.TodayFitComplete_Backend.dto.response.file.FileDownloadResponse;
import com.SJY.TodayFitComplete_Backend.dto.response.file.FileUploadResponse;
import com.SJY.TodayFitComplete_Backend.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/board/{boardId}/file")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    /**
     * 특정 게시글에 파일을 업로드합니다.
     *
     * @param boardId 게시글 ID
     * @param files 업로드할 파일 목록
     * @return 업로드된 파일 정보 목록
     * @throws IOException 파일 업로드 과정에서 발생하는 예외
     */
    @PostMapping("/upload")
    public ResponseEntity<List<FileUploadResponse>> upload(@PathVariable Long boardId,
                                                           @RequestParam("file") List<MultipartFile> files) throws IOException {
        List<FileUploadResponse> uploadedFiles = fileService.upload(boardId, files);
        return ResponseEntity.status(HttpStatus.CREATED).body(uploadedFiles);
    }

    /**
     * 파일을 다운로드합니다.
     *
     * @param fileId 다운로드할 파일 ID
     * @return 다운로드할 파일 리소스
     * @throws IOException 파일 다운로드 과정에서 발생하는 예외
     */
    @GetMapping("/download")
    public ResponseEntity<Resource> download(@RequestParam("fileId") Long fileId) throws IOException {
        FileDownloadResponse downloadDto = fileService.download(fileId);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.parseMediaType(downloadDto.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName=\"" + downloadDto.getFilename() + "\"")
                .body(new ByteArrayResource(downloadDto.getContent()));
    }

    /**
     * 파일을 삭제합니다.
     *
     * @param fileId 삭제할 파일 ID
     * @return 삭제된 파일 ID
     */
    @DeleteMapping("/delete")
    public ResponseEntity<Long> delete(@RequestParam("fileId") Long fileId) {
        fileService.delete(fileId);
        return  ResponseEntity.status(HttpStatus.OK).body(fileId);
    }
}

