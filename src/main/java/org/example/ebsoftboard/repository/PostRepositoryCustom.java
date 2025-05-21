package org.example.ebsoftboard.repository;

import org.example.ebsoftboard.dto.PostResponseDTO;
import org.example.ebsoftboard.dto.PostSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {
    Page<PostResponseDTO> searchPosts(PostSearchCondition condition, Pageable pageable);
}
