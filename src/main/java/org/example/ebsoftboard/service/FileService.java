package org.example.ebsoftboard.service;

import org.example.ebsoftboard.entity.File;
import org.example.ebsoftboard.entity.Post;
import org.example.ebsoftboard.repository.FileRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileService {

    private final FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public void saveFiles(Post post, List<MultipartFile> multipartFiles) {
        if (multipartFiles == null || multipartFiles.isEmpty()) {
            return;
        }

        List<File> files = multipartFiles.stream()
                .filter(file -> !file.isEmpty())
                .map(file -> {
                    try {
                        return File.builder()
                                .originalFilename(file.getOriginalFilename())
                                .fileData(file.getBytes())
                                .post(post)
                                .build();
                    } catch (IOException e) {
                        throw new UncheckedIOException("파일 읽기 실패: " + file.getOriginalFilename(), e);
                    }
                }).collect(Collectors.toList());

        fileRepository.saveAll(files);
    }

    public File getFileById(Long id) {
        return fileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("파일을 찾을 수 없습니다."));
    }
}
