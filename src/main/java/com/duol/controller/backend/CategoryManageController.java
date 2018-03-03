package com.duol.controller.backend;

import com.duol.common.Const;
import com.duol.common.ResponseCode;
import com.duol.common.ServerResponse;
import com.duol.pojo.User;
import com.duol.service.CategoryService;
import com.duol.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * @author Duolaimon
 * 18-2-24 下午2:18
 */
@RestController
@RequestMapping("/manage/category")
public class CategoryManageController {
    private final UserService userService;
    private final CategoryService categoryService;

    @Autowired
    public CategoryManageController(UserService userService, CategoryService categoryService) {
        this.userService = userService;
        this.categoryService = categoryService;
    }

    @PostMapping
    public ServerResponse addCategory(String categoryName, @RequestParam(value = "parentId", defaultValue = "0") int parentId, HttpSession session) {
        ServerResponse checkValid = checkValid(session);
        if (!checkValid.isSuccess()){
            return checkValid;
        }
        return categoryService.addCategory(categoryName, parentId);
    }

    @PutMapping("/{categoryId}")
    public ServerResponse updateCategoryName(@PathVariable("categoryId") Integer categoryId, String categoryName, HttpSession session) {
        ServerResponse checkValid = checkValid(session);
        if (!checkValid.isSuccess()) {
            return checkValid;
        }
        return categoryService.updateCategoryName(categoryId, categoryName);
    }

    @GetMapping("/parallel/{categoryId}")
    public ServerResponse getChildrenParallelCategory(@PathVariable(value = "categoryId") Integer categoryId, HttpSession session) {
        ServerResponse checkValid = checkValid(session);
        if (!checkValid.isSuccess()) {
            return checkValid;
        }
        return categoryService.getChildrenParallelCategory(categoryId);
    }

    @GetMapping("/deep/{categoryId}")
    public ServerResponse getCategoryAndDeepChildrenCategory(@PathVariable("categoryId") Integer categoryId, HttpSession session) {
        ServerResponse checkValid = checkValid(session);
        if (!checkValid.isSuccess()) {
            return checkValid;
        }
        return categoryService.selectCategoryAndChildrenById(categoryId);
    }

    private ServerResponse checkValid(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录");
        }
        //是否是管理员
        if (!userService.checkAdminRole(user).isSuccess()) {
            return ServerResponse.createByErrorMessage("无权限操作,需要管理员权限");
        }
        return ServerResponse.createBySuccess();
    }
}
