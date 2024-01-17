package com.SJY.TodayFitComplete_Backend.service.file;

import com.SJY.TodayFitComplete_Backend.dto.file.FileDownloadResponse;
import com.SJY.TodayFitComplete_Backend.dto.file.FileUploadResponse;
import com.SJY.TodayFitComplete_Backend.entity.board.Board;
import com.SJY.TodayFitComplete_Backend.entity.file.FileEntity;
import com.SJY.TodayFitComplete_Backend.exception.BoardNotFoundException;
import com.SJY.TodayFitComplete_Backend.exception.FileDownloadFailureException;
import com.SJY.TodayFitComplete_Backend.exception.FileNotFoundException;
import com.SJY.TodayFitComplete_Backend.exception.FileUploadFailureException;
import com.SJY.TodayFitComplete_Backend.repository.board.BoardRepository;
import com.SJY.TodayFitComplete_Backend.repository.file.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileService {

    private final BoardRepository boardRepository;
    private final FileRepository fileRepository;

    @Value("${project.folderPath}")
    private String FOLDER_PATH; // 파일 저장 폴더 경로

    /**
     * 파일 업로드 및 저장
     */
    @Transactional
    public List<FileUploadResponse> upload(Long boardId, List<MultipartFile> multipartFiles){
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new BoardNotFoundException(boardId.toString()));
        try {
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

                    saveFile.setBoard(board);
                    fileEntitys.add(fileRepository.save(saveFile));

            }
            return fileEntitys.stream()
                    .map(FileUploadResponse::fromEntity)
                    .collect(Collectors.toList());
        } catch(IOException e) {
            throw new FileUploadFailureException(e);
        }
    }

    /**
     * 파일 다운로드
     */
    public FileDownloadResponse download(Long fileId){
        FileEntity file = fileRepository.findById(fileId).orElseThrow(
                () -> new FileNotFoundException(fileId.toString()));
        try {
            String filePath = FOLDER_PATH + file.getFilePath();
            String contentType = determineContentType(file.getFileType());
            byte[] content = Files.readAllBytes(new File(filePath).toPath());
            return FileDownloadResponse.fromFileResource(file, contentType, content);
        }
        catch(IOException e) {
            throw new FileDownloadFailureException(e);
        }
    }

    /**
     * 파일 삭제
     */
    @Transactional
    @PreAuthorize("@fileAccessHandler.check(#fileId)")
    public void delete(Long boardId, @Param("fileId")Long fileId){
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new BoardNotFoundException(boardId.toString()));
        FileEntity file = fileRepository.findById(fileId).orElseThrow(
                () -> new FileNotFoundException(fileId.toString()));
        String filePath = FOLDER_PATH + File.separator + file.getFilePath();
        File localFile = new File(filePath);
        if (localFile.exists()) {
            localFile.delete();
        }
        file.delBoard(board);
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
