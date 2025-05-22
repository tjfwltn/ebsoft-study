package org.example.ebsoftboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.example.ebsoftboard.entity.Post;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class PostDetailResponseDTO {

    private Long id;
    private String title;
    private String content;
    private String categoryTitle;
    private String username;
    private Integer viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<CommentResponseDTO> comments;
    private List<FileResponseDTO> files;

    public static PostDetailResponseDTO from(Post post) {
        return PostDetailResponseDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .username(post.getUsername())
                .content(post.getContents())
                .categoryTitle(post.getCategory().getTitle())
                .createdAt(post.getCreatedAt())
                .comments(post.getComments().stream().map(CommentResponseDTO::from).toList())
                .files(post.getFiles().stream().map(FileResponseDTO::from).toList())
                .viewCount(post.getViewCount())
                .build();
    }
}
