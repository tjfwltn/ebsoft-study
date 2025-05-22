package org.example.ebsoftboard.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    @NotBlank
    @Size(min = 1, max = 1999,
            message = "댓글은 1글자 이상, 2000글자 미만이어야 합니다.")
    private String content;

    public Comment toEntity(Post post) {
        return Comment.builder()
                .contents(content)
                .createdAt(LocalDateTime.now())
                .post(post)
                .build();
    }
}
