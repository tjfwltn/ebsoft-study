package org.example.ebsoftboard.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.ebsoftboard.entity.Comment;
import org.example.ebsoftboard.entity.Post;

import java.time.LocalDateTime;

import static org.example.ebsoftboard.constants.ValidationConstants.*;

@Getter
@Setter
@NoArgsConstructor
public class CommentRequestDTO {

    private Long postId;

    @NotBlank
    @Size(min = COMMENT_CONTENT_MIN_LENGTH, max = COMMENT_CONTENT_MAX_LENGTH,
            message = COMMENT_CONTENT_SIZE_MESSAGE)
    private String content;

    public Comment toEntity(Post post) {
        return Comment.builder()
                .contents(content)
                .createdAt(LocalDateTime.now())
                .post(post)
                .build();
    }
}
