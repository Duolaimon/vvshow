package com.duol.controller.backend;

import com.duol.common.ServerResponse;
import com.duol.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * @author Duolaimon
 * 18-2-24 下午2:18
 */
@RestController
@RequestMapping("/manage/categories")
public class CategoryManageController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryManageController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ServerResponse addCategory(String categoryName, @RequestParam(value = "parentId", defaultValue = "0") int parentId, HttpSession session) {
        return categoryService.addCategory(categoryName, parentId);
    }

    @PutMapping("/{categoryId}")
    public ServerResponse updateCategoryName(@PathVariable("categoryId") Integer categoryId, String categoryName, HttpSession session) {
        return categoryService.updateCategoryName(categoryId, categoryName);
    }

    @GetMapping("/parallel/{categoryId}")
    public ServerResponse getChildrenParallelCategory(@PathVariable(value = "categoryId") Integer categoryId, HttpSession session) {
        return categoryService.getChildrenParallelCategory(categoryId);
    }

    @GetMapping("/deep/{categoryId}")
    public ServerResponse getCategoryAndDeepChildrenCategory(@PathVariable("categoryId") Integer categoryId, HttpSession session) {
        return categoryService.selectCategoryAndChildrenById(categoryId);
    }

}
