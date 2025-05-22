package org.example.ebsoftboard.service;

import org.example.ebsoftboard.dto.PostRequestDTO;
import org.example.ebsoftboard.dto.PostResponseDTO;
import org.example.ebsoftboard.dto.PostSearchCondition;
import org.example.ebsoftboard.entity.Category;
import org.example.ebsoftboard.entity.File;
import org.example.ebsoftboard.entity.Post;
import org.example.ebsoftboard.repository.CategoryRepository;
import org.example.ebsoftboard.repository.FileRepository;
import org.example.ebsoftboard.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final FileRepository fileRepository;

    public PostService(PostRepository postRepository, CategoryRepository categoryRepository, FileRepository fileRepository) {
        this.postRepository = postRepository;
        this.categoryRepository = categoryRepository;
        this.fileRepository = fileRepository;
    }

    public Page<PostResponseDTO> getPaginatedPostList(PostSearchCondition condition, Pageable pageable) {
        return postRepository.searchPosts(condition, pageable);
    }

    public PostResponseDTO getPost(Long id) {
//        postRepository.findById(id).ifPresent(post -> {
//            PostResponseDTO.builder()
//                    .id(post.getId())
//                    .title(post.getTitle())
//        })

        return null;
    }

    @Transactional
    public void savePost(PostRequestDTO postRequestDTO) {
        Category category = categoryRepository.findById(postRequestDTO.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 카테고리입니다."));

        Post post = Post.builder()
                .title(postRequestDTO.getTitle())
                .contents(postRequestDTO.getContent())
                .username(postRequestDTO.getUsername())
                .password(postRequestDTO.getPassword())
                .category(category)
                .build();

        postRepository.save(post);

        List<MultipartFile> multipartFiles = postRequestDTO.getFiles();
        if (multipartFiles != null && !multipartFiles.isEmpty()) {
            List<File> fileList = multipartFiles.stream()
                    .filter(file -> !file.isEmpty()) // 빈 배열 파일 리스트 에러 발생, 이를 위한 조건
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
                    })
                    .collect(Collectors.toList());
            fileRepository.saveAll(fileList);
        }
    }
}
