package org.example.ebsoftboard.service;

import org.example.ebsoftboard.dto.PostResponseDTO;
import org.example.ebsoftboard.dto.PostSearchCondition;
import org.example.ebsoftboard.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Page<PostResponseDTO> getPaginatedPostList(PostSearchCondition condition, Pageable pageable) {
        return postRepository.searchPosts(condition, pageable);
    }
}
