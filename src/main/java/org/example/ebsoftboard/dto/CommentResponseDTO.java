package org.example.ebsoftboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.example.ebsoftboard.entity.Comment;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class CommentResponseDTO {

    private Long id;
    private String contents;
    private LocalDateTime createdAt;

    public static CommentResponseDTO from(Comment comment) {
        return new CommentResponseDTO(
                comment.getId(),
                comment.getContents(),
                comment.getCreatedAt()
        );
    }
}
