package org.example.ebsoftboard.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.ebsoftboard.dto.PostPasswordVerificationRequestDTO;
import org.example.ebsoftboard.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/post")
public class PostApiController {

    private final PostService postService;

    public PostApiController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/verify-password")
    public ResponseEntity<?> verifyPassword(
            @Valid @RequestBody PostPasswordVerificationRequestDTO requestDTO,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }

        boolean isValid = postService.verifyPassword(requestDTO);
        if (isValid) {
            return ResponseEntity.ok().body(new PasswordVerificationResponse(true, "비밀번호 일치"));
        }
        return ResponseEntity.badRequest().body(new PasswordVerificationResponse(false, "비밀번호 불일치"));
    }

    @Getter
    @AllArgsConstructor
    private static class PasswordVerificationResponse {
        private boolean valid;
        private String message;
    }
}
