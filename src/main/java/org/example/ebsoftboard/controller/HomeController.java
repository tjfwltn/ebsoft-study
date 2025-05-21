package org.example.ebsoftboard.controller;

import org.example.ebsoftboard.dto.PageGroupDTO;
import org.example.ebsoftboard.dto.PostRequestDTO;
import org.example.ebsoftboard.dto.PostResponseDTO;
import org.example.ebsoftboard.dto.PostSearchCondition;
import org.example.ebsoftboard.entity.Category;
import org.example.ebsoftboard.service.CategoryService;
import org.example.ebsoftboard.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
                       @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                       Model model) {
        List<Category> categories = categoryService.getCategories();
        Page<PostResponseDTO> postList = postService.getPaginatedPostList(condition, pageable);

        PageGroupDTO pageGroup = calculatePageGroupInfo(postList);
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
        return TEMPLATE_PATH + "write";
    }

    @PostMapping("/write")
    public String write(@ModelAttribute PostRequestDTO postRequestDTO) {

    }
//    @GetMapping("/view/*")
//    public String view(@RequestParam Long id, Model model) {
//        PostResponseDTO post = postService.getPost(id);
//    }

    private PageGroupDTO calculatePageGroupInfo(Page<PostResponseDTO> postList) {
        int pageGroupSize = 10;
        int currentPage = postList.getNumber() + 1;
        int currentGroup = (currentPage - 1) * pageGroupSize;

        int startPage = currentGroup * pageGroupSize + 1;
        int endPage = Math.min(startPage + pageGroupSize - 1, postList.getTotalPages());

        return new PageGroupDTO(startPage, endPage, pageGroupSize);
    }
}
