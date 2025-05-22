package org.example.ebsoftboard.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.ebsoftboard.dto.CommentRequestDTO;
import org.example.ebsoftboard.entity.Comment;
import org.example.ebsoftboard.entity.Post;
import org.example.ebsoftboard.repository.CommentRepository;
import org.example.ebsoftboard.repository.PostRepository;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    public void saveComment(CommentRequestDTO commentRequestDTO) {
        Post post = postRepository.findPostOnlyById(commentRequestDTO.getPostId())
                .orElseThrow(() -> new EntityNotFoundException("게시글이 존재하지 않습니다."));

        Comment comment = commentRequestDTO.toEntity(post);
        commentRepository.save(comment);
    }
}
