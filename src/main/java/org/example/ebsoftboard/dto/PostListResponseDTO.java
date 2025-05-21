package org.example.ebsoftboard.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@Builder
public class PostListResponseDTO {

    private Page<PostResponseDTO> posts;
    private int currentPage;
    private int totalPages;
    private long totalCount;
    private PostSearchCondition condition;

}
