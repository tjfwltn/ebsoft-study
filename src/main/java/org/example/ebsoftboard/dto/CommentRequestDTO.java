package org.example.ebsoftboard.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.ebsoftboard.entity.Comment;
import org.example.ebsoftboard.entity.Post;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CommentRequestDTO {

    private Long postId;
    private String content;

    public Comment toEntity(Post post) {
        return Comment.builder()
                .contents(content)
                .createdAt(LocalDateTime.now())
                .post(post)
                .build();
    }
}
