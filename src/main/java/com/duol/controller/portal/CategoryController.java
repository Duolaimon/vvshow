package com.duol.controller.portal;

import com.duol.common.ServerResponse;
import com.duol.pojo.Category;
import com.duol.service.CategoryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Duolaimon
 * 18-8-9 下午2:52
 */
@RestController
@RequestMapping("/categories")
public class CategoryController {
    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/deep/{categoryId}")
    @ApiOperation(value = "产品类别所有子类id")
    public ServerResponse<List<Integer>> getCategoryAndDeepChildrenCategory(@PathVariable("categoryId") Integer categoryId) {
        return categoryService.selectCategoryAndChildrenById(categoryId);
    }

    @GetMapping("/parallel/{categoryId}")
    @ApiOperation(value = "产品类别子集")
    public ServerResponse<List<Category>> getChildrenParallelCategory(@PathVariable(value = "categoryId") Integer categoryId) {
        return categoryService.getChildrenParallelCategory(categoryId);
    }
}
