package org.example.ebsoftboard.controller;

import org.example.ebsoftboard.dto.PostListResponseDTO;
import org.example.ebsoftboard.dto.PostSearchCondition;
import org.example.ebsoftboard.entity.Category;
import org.example.ebsoftboard.service.CategoryService;
import org.example.ebsoftboard.service.PostService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/board/free")
public class HomeController {

    private static final String TEMPLATE_PATH = "board/free/";
    private final CategoryService categoryService;
    private final PostService postService;

    public HomeController(CategoryService categoryService, PostService postService) {
        this.categoryService = categoryService;
        this.postService = postService;
    }

    @GetMapping("/index")
    public String home(Model model) {
        List<Category> categories = categoryService.getCategories();
        model.addAttribute("categories", categories);
        return TEMPLATE_PATH + "index";
    }

    @GetMapping("/list")
    public String list(@ModelAttribute PostSearchCondition condition,
                       @PageableDefault(size = 10, direction = Sort.Direction.DESC) Pageable pageable,
                       Model model) {
        PostListResponseDTO response = postService.getPaginatedPostList(condition, pageable);
        model.addAttribute("response", response);
        return TEMPLATE_PATH + "list";
    }
}
