package com.SJY.TodayFitComplete_Backend.service;

import com.SJY.TodayFitComplete_Backend.common.exception.ResourceNotFoundException;
import com.SJY.TodayFitComplete_Backend.dto.response.file.FileDownloadResponse;
import com.SJY.TodayFitComplete_Backend.dto.response.file.FileUploadResponse;
import com.SJY.TodayFitComplete_Backend.entity.Board;
import com.SJY.TodayFitComplete_Backend.entity.FileEntity;
import com.SJY.TodayFitComplete_Backend.repository.BoardRepository;
import com.SJY.TodayFitComplete_Backend.repository.FileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FileService {

    private final BoardRepository boardRepository;
    private final FileRepository fileRepository;

    @Value("${project.folderPath}")
    private String FOLDER_PATH; // 파일 저장 폴더 경로

    /**
     * 파일 업로드 및 저장
     */
    public List<FileUploadResponse> upload(Long boardId, List<MultipartFile> multipartFiles) throws IOException {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new ResourceNotFoundException("Board", "Board Id", String.valueOf(boardId))
        );
        List<FileEntity> fileEntitys = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            String fileName = multipartFile.getOriginalFilename();
            String randomId = UUID.randomUUID().toString();
            String filePath =
                    "POST_" + board.getId() + "_" + randomId.concat(fileName.substring(fileName.indexOf(".")));

            String fileResourcePath = FOLDER_PATH + File.separator + filePath;
            File f = new File(FOLDER_PATH);
            if (!f.exists()) {
                f.mkdir();
            }
            Files.copy(multipartFile.getInputStream(), Paths.get(fileResourcePath));
            FileEntity saveFile = FileEntity.builder()
                    .originFileName(multipartFile.getOriginalFilename())
                    .filePath(filePath)
                    .fileType(multipartFile.getContentType())
                    .build();

            saveFile.setMappingBoard(board);
            fileEntitys.add(fileRepository.save(saveFile));
        }

        return fileEntitys.stream()
                .map(FileUploadResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 파일 다운로드
     */
    public FileDownloadResponse download(Long fileId) throws IOException {
        FileEntity file = fileRepository.findById(fileId).orElseThrow(
                FileNotFoundException::new
        );
        String filePath = FOLDER_PATH + file.getFilePath();
        String contentType = determineContentType(file.getFileType());
        byte[] content = Files.readAllBytes(new File(filePath).toPath());
        return FileDownloadResponse.fromFileResource(file, contentType, content);
    }

    /**
     * 파일 삭제
     */
    public void delete(Long fileId) {
        FileEntity file = fileRepository.findById(fileId).orElseThrow(
                () -> new ResourceNotFoundException("File", "File Id", String.valueOf(fileId))
        );

        // local 파일을 삭제
        String filePath = FOLDER_PATH + File.separator + file.getFilePath();
        File physicalFile = new File(filePath);
        if (physicalFile.exists()) {
            physicalFile.delete();
        }
        fileRepository.delete(file);
    }

    private String determineContentType(String contentType) {
        switch (contentType) {
            case "image/png":
                return MediaType.IMAGE_PNG_VALUE;
            case "image/jpeg":
                return MediaType.IMAGE_JPEG_VALUE;
            case "text/plain":
                return MediaType.TEXT_PLAIN_VALUE;
            default:
                return MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
    }
}
