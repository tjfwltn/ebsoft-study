package org.example.ebsoftboard.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.ebsoftboard.dto.*;
import org.example.ebsoftboard.entity.Category;
import org.example.ebsoftboard.entity.Post;
import org.example.ebsoftboard.repository.CategoryRepository;
import org.example.ebsoftboard.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class PostService {

    private final FileService fileService;
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;

    public PostService(FileService fileService, PostRepository postRepository, CategoryRepository categoryRepository) {
        this.fileService = fileService;
        this.postRepository = postRepository;
        this.categoryRepository = categoryRepository;
    }

    public Page<PostListResponseDTO> getPaginatedPostList(PostSearchCondition condition, Pageable pageable) {
        return postRepository.searchPosts(condition, pageable);
    }

    @Transactional
    public void savePostWithFiles(PostRequestDTO postRequestDTO) {
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
        fileService.saveFiles(post, postRequestDTO.getFiles());
    }

    @Transactional
    public PostDetailResponseDTO getPostAndIncreaseViewCount(Long id) {
        postRepository.increaseViewCount(id);
        return getPost(id);
    }

    public PostDetailResponseDTO getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 게시글이 없습니다."));

        return PostDetailResponseDTO.from(post);
    }

    @Transactional
    public void updatePostWithFiles(Long id, List<Long> deleteFileIds, List<MultipartFile> files, PostUpdateRequestDTO dto) {
        Post post = postRepository.findPostOnlyById(id)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        post.update(dto.getUsername(), dto.getPassword(), dto.getTitle(), dto.getContent());

        fileService.deleteFilesByIds(deleteFileIds);

        fileService.saveFiles(post, files);
    }

    @Transactional(readOnly = true)
    public boolean verifyPassword(PostPasswordVerificationRequestDTO requestDTO) {
        Post post = postRepository.findPostOnlyById(requestDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        return post.getPassword().equals(requestDTO.getPassword());
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}
