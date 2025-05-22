package org.example.ebsoftboard.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.ebsoftboard.dto.PostDetailResponseDTO;
import org.example.ebsoftboard.dto.PostRequestDTO;
import org.example.ebsoftboard.dto.PostListResponseDTO;
import org.example.ebsoftboard.dto.PostSearchCondition;
import org.example.ebsoftboard.entity.Category;
import org.example.ebsoftboard.entity.Post;
import org.example.ebsoftboard.repository.CategoryRepository;
import org.example.ebsoftboard.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
