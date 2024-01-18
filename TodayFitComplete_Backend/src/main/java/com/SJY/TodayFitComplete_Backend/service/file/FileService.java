package com.SJY.TodayFitComplete_Backend.service.file;

import com.SJY.TodayFitComplete_Backend.dto.file.FileDownloadResponse;
import com.SJY.TodayFitComplete_Backend.dto.file.FileUploadResponse;
import com.SJY.TodayFitComplete_Backend.entity.board.Board;
import com.SJY.TodayFitComplete_Backend.entity.file.FileEntity;
import com.SJY.TodayFitComplete_Backend.exception.*;
import com.SJY.TodayFitComplete_Backend.repository.board.BoardRepository;
import com.SJY.TodayFitComplete_Backend.repository.file.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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

        return multipartFiles.stream().map(multipartFile -> {
            try {
                return saveFile(board, multipartFile);
            } catch (IOException e) {
                throw new FileUploadFailureException(e);
            }
        }).collect(Collectors.toList());
    }

    private FileUploadResponse saveFile(Board board, MultipartFile multipartFile) throws IOException {
        String originalFileName = multipartFile.getOriginalFilename();
        String filePath = createFilePath(originalFileName, board.getId());

        String fileResourcePath = FOLDER_PATH + File.separator + filePath;
        File directory = new File(FOLDER_PATH);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        Files.copy(multipartFile.getInputStream(), Paths.get(fileResourcePath));
        FileEntity saveFile = FileEntity.builder()
                .originFileName(originalFileName)
                .filePath(filePath)
                .fileType(multipartFile.getContentType())
                .build();
        saveFile.setBoard(board);
        return FileUploadResponse.fromEntity(fileRepository.save(saveFile));
    }

    private String createFilePath(String originalFilename, Long boardId) {
        int lastIndexOfDot = originalFilename.lastIndexOf(".");
        String extension = originalFilename.substring(lastIndexOfDot);
        String randomId = UUID.randomUUID().toString();
        return boardId + "_" + randomId + extension;
    }

    /**
     * 파일 다운로드
     */
    public FileDownloadResponse download(Long fileId){
        FileEntity file = fileRepository.findById(fileId).orElseThrow(
                () -> new FileNotFoundException(fileId.toString()));
        try {
            String filePath = FOLDER_PATH + file.getFilePath();
            String contentType = file.getFileType();
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
    public void delete(Long boardId, @Param("fileId") Long fileId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException("Board not found with id: " + boardId));
        FileEntity file = fileRepository.findById(fileId)
                .orElseThrow(() -> new FileNotFoundException("File not found with id: " + fileId));
        deleteLocalFile(file);
        file.delBoard(board);
        fileRepository.delete(file);
    }

    public void deleteLocalFile(FileEntity file) {
        String filePath = FOLDER_PATH + File.separator + file.getFilePath();
        File localFile = new File(filePath);
        if (localFile.exists() && !localFile.delete()) {
            throw new FileDeleteFailureException(filePath);
        }
    }
}
