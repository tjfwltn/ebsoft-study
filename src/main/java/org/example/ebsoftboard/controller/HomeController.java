package org.example.ebsoftboard.controller;

import jakarta.validation.Valid;
import org.example.ebsoftboard.dto.*;
import org.example.ebsoftboard.entity.Category;
import org.example.ebsoftboard.service.CategoryService;
import org.example.ebsoftboard.service.FileService;
import org.example.ebsoftboard.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/board/free")
public class HomeController {

    private static final String TEMPLATE_PATH = "board/free/";
    private static final String REDIRECT = "redirect:/";
    private final CategoryService categoryService;
    private final PostService postService;

    public HomeController(CategoryService categoryService, PostService postService) {
        this.categoryService = categoryService;
        this.postService = postService;
    }

    @GetMapping("/list")
    public String list(@ModelAttribute PostSearchCondition condition,
                       @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                       Model model) {
        List<Category> categories = categoryService.getCategories();
        Page<PostListResponseDTO> postList = postService.getPaginatedPostList(condition, pageable);

        PageGroupDTO pageGroup = calculatePageGroupInfo(postList); // 이전, 다음 버튼을 위한 페이징 그룹 계산
        model.addAttribute("categories", categories);
        model.addAttribute("postList", postList);
        model.addAttribute("condition", condition);
        model.addAttribute("pageGroup", pageGroup);

        return TEMPLATE_PATH + "list";
    }

    @GetMapping("/write")
    public String write(Model model) {
        List<Category> categories = categoryService.getCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("postRequestDTO", new PostRequestDTO());
        return TEMPLATE_PATH + "write";
    }

    @PostMapping("/write")
    public String write(@Valid @ModelAttribute PostRequestDTO postRequestDTO,
                        BindingResult bindingResult, Model model) {
        List<Category> categories = categoryService.getCategories();
        if (bindingResult.hasErrors()) {
            model.addAttribute("postRequestDTO", postRequestDTO);
            model.addAttribute("categories", categories);
            return TEMPLATE_PATH + "write";
        }

        try {
            postService.savePostWithFiles(postRequestDTO);
            return REDIRECT + TEMPLATE_PATH + "list";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("postRequestDTO", postRequestDTO);
            model.addAttribute("categories", categories);
            return TEMPLATE_PATH + "write";
        }
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, Model model) {
        PostDetailResponseDTO post = postService.getPost(id);
        model.addAttribute("post", post);
        return TEMPLATE_PATH + "view";
    }


    private PageGroupDTO calculatePageGroupInfo(Page<PostListResponseDTO> postList) {
        int pageGroupSize = 10;
        int currentPage = postList.getNumber() + 1;
        int currentGroup = (currentPage - 1) * pageGroupSize;

        int startPage = currentGroup * pageGroupSize + 1;
        int endPage = Math.min(startPage + pageGroupSize - 1, postList.getTotalPages());

        return new PageGroupDTO(startPage, endPage, pageGroupSize);
    }
}
