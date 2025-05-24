package org.example.ebsoftboard.controller;

import jakarta.validation.Valid;
import org.example.ebsoftboard.dto.*;
import org.example.ebsoftboard.entity.Category;
import org.example.ebsoftboard.service.CategoryService;
import org.example.ebsoftboard.service.PostService;
import org.example.ebsoftboard.util.PaginationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/board/free")
public class PostController {

    private static final String TEMPLATE_PATH = "board/free/";
    private static final String REDIRECT = "redirect:/";
    private final CategoryService categoryService;
    private final PostService postService;

    public PostController(CategoryService categoryService, PostService postService) {
        this.categoryService = categoryService;
        this.postService = postService;
    }

    @GetMapping("/list")
    public String list(@ModelAttribute PostSearchCondition condition,
                       @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                       Model model) {
        List<Category> categories = categoryService.getCategories();
        Page<PostListResponseDTO> postList = postService.getPaginatedPostList(condition, pageable);

        PageGroupDTO pageGroup = PaginationUtil.calculatePageGroupInfo(postList); // 이전, 다음 버튼을 위한 페이징 그룹 계산
        model.addAttribute("categories", categories);
        model.addAttribute("postList", postList);
        model.addAttribute("condition", condition);
        model.addAttribute("pageGroup", pageGroup);

        return TEMPLATE_PATH + "list";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, Model model) {
        PostDetailResponseDTO post = postService.getPostAndIncreaseViewCount(id);
        model.addAttribute("post", post);
        return TEMPLATE_PATH + "view";
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

    /**
     * 지금 상세 게시글 조회를 위해 getPost() 메서드 내에서 findById() 메서드를 한번에 files, category, comment를
     * 한번에 가져오는 메서드를 사용하는데, modify에서는 comment가 필요 없음. 이걸 어떻게 해야 할까?
     */
    @GetMapping("/modify/{id}")
    public String modify(@PathVariable Long id, Model model) {
        PostDetailResponseDTO post = postService.getPost(id);
        model.addAttribute("post", post);

        return TEMPLATE_PATH + "modify";
    }

    @PostMapping("/modify/{id}")
    public String modify(@PathVariable Long id,
                         @RequestParam(required = false) List<Long> deleteFileIds,
                         @RequestParam(required = false) List<MultipartFile> files,
                         @Valid @ModelAttribute PostUpdateRequestDTO postUpdateRequestDTO,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            PostDetailResponseDTO post = postService.getPost(id);
            model.addAttribute("post", post);
            return TEMPLATE_PATH + "modify" + id;
        }
        postService.updatePostWithFiles(id, deleteFileIds, files, postUpdateRequestDTO);
        return REDIRECT + TEMPLATE_PATH + "view/" + id;
    }

    @PostMapping("delete")
    public String delete(@RequestParam("id") Long id) {
        postService.deletePost(id);
        return REDIRECT + TEMPLATE_PATH + "list";
    }

}
