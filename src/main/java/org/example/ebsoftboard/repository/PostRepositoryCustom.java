package org.example.ebsoftboard.repository;

import org.example.ebsoftboard.dto.PostListResponseDTO;
import org.example.ebsoftboard.dto.PostSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {
    Page<PostListResponseDTO> searchPosts(PostSearchCondition condition, Pageable pageable);
}
