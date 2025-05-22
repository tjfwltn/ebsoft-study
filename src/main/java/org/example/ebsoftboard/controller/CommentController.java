package org.example.ebsoftboard.controller;

import org.example.ebsoftboard.dto.CommentRequestDTO;
import org.example.ebsoftboard.service.CommentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/write")
    public String writeComment(@ModelAttribute CommentRequestDTO commentRequestDTO) {
        commentService.saveComment(commentRequestDTO);
        return "redirect:/board/free/view/" + commentRequestDTO.getPostId();
    }

}
