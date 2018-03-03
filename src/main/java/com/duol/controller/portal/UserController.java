package com.duol.controller.portal;

import com.duol.common.Const;
import com.duol.common.ResponseCode;
import com.duol.common.ServerResponse;
import com.duol.pojo.User;
import com.duol.service.UserService;
import com.duol.vo.UserResponse;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * @author Duolaimon
 * 18-2-16 下午6:38
 */
@Api("用户api")
@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(@Qualifier("userServiceImpl") UserService userService) {
        this.userService = userService;
    }


    @ApiOperation(value = "用户登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, paramType = "form"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, paramType = "form")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not Found!!!", response = UserResponse.class)})
    @GetMapping("/session")
    public ServerResponse<User> login(String username, String password, HttpSession session) {
        ServerResponse<User> response = userService.login(username, password);
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    }

    @ApiOperation("用户注册")
    @ApiImplicitParam(name = "user", value = "用户注册信息", required = true, dataType = "user")
    @PostMapping("/user")
    public ServerResponse<String> register(@RequestBody User user) {
        return userService.register(user);
    }

    @DeleteMapping("/session")
    public ServerResponse<String> logout(HttpSession session) {
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }

    @GetMapping("/user/valid")
    public ServerResponse<String> checkValid(String str, String type) {
        return userService.checkValid(str, type);
    }

    @GetMapping("/user/{userId}")
    public ServerResponse<User> getUserInfo(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user != null) {
            return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createByErrorMessage("用户未登录");
    }

    @GetMapping("/user/forget-password/question")
    public ServerResponse<String> forgetGetQuestion(String username) {

        return userService.selectQuestion(username);
    }

    @GetMapping("/user/forget-password/answer")
    public ServerResponse<String> forgetCheckAnswer(String username, String question, String answer) {
        return userService.checkAnswer(username, question, answer);
    }

    @PutMapping("/user/forget-password")
    public ServerResponse<String> forgetResetPassword(String username, String newPassword, String forgetToken) {
        return userService.forgetResetPassword(username, newPassword, forgetToken);
    }

    @PutMapping("/user/{userId}/password")
    public ServerResponse<String> resetPassword(HttpSession session, String oldPassword, String newPassword) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        return userService.resetPassword(oldPassword, newPassword, user);
    }

    @PutMapping("/user/{userId}")
    public ServerResponse<User> updateInformation(HttpSession session, User user) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());
        ServerResponse<User> response = userService.updateInformation(user);
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    }

    @GetMapping("/user/{userId}/info")
    public ServerResponse<User> getInformation(HttpSession session) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录,需要强制登录status=10");
        }
        return userService.getInformation(currentUser.getId());
    }


}
