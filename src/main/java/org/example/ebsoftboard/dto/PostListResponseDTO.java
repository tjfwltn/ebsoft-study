package org.example.ebsoftboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class PostListResponseDTO {

    private Long id;
    private String title;
    private String categoryTitle;
    private String username;
    private int viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private boolean hasFiles;
}
