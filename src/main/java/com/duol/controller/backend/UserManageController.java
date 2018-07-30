package com.duol.controller.backend;

import com.duol.common.Const;
import com.duol.common.ServerResponse;
import com.duol.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Objects;

/**
 * @author Duolaimon
 * 18-7-17 下午3:42
 */
@Controller
@RequestMapping("/manage")
public class UserManageController {

    private final UserService userService;

    @Autowired
    public UserManageController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/session")
    @ResponseBody
    public ServerResponse<String> login(String username, String password, HttpSession session) {
        if (Objects.nonNull(session.getAttribute(Const.CURRENT_ADMIN))) {
            return ServerResponse.createBySuccessMessage("用户已登录,不要重复登录");
        }
        ServerResponse<String> response = userService.login(username, password);
        if (response.isSuccess()) {
            String result = response.getData();
            session.setAttribute(Const.CURRENT_ADMIN, result);
        } else {
            return ServerResponse.createByErrorMessage("不是管理员,无法登录");
        }
        return response;
    }

}
