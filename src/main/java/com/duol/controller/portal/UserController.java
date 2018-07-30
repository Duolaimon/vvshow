package com.duol.controller.portal;

import com.duol.common.Const;
import com.duol.common.ServerResponse;
import com.duol.pojo.User;
import com.duol.service.UserService;
import com.duol.vo.UserResponse;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Objects;

import static com.duol.util.SessionUtil.verifyUserLogin;

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
    @PostMapping("/session")
    public ServerResponse<String> login(String username, String password, HttpSession session, HttpServletResponse servletResponse) {
        if (Objects.nonNull(session.getAttribute(Const.CURRENT_USER))) {
            return ServerResponse.createBySuccessMessage("用户已登录,不要重复登录");
        }
        ServerResponse<String> response = userService.login(username, password);
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());
            servletResponse.setStatus(HttpServletResponse.SC_CREATED);
        }
        return response;
    }

    @ApiOperation("用户注册")
    @ApiImplicitParam(name = "user", value = "用户注册信息", required = true, dataType = "user")
    @PostMapping("/user")
    public ServerResponse<String> register(@RequestBody User user, HttpSession session) {
        ServerResponse<String> response = userService.register(user);
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    }

    @DeleteMapping("/session")
    public ServerResponse<String> logout(HttpSession session) {
        String[] info = verifyUserLogin(session);
        if (Objects.nonNull(info)) {
            userService.logout(info[0]);
            session.removeAttribute(Const.CURRENT_USER);
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createBySuccessMessage("用户未登录或已过期");
    }

    @GetMapping("/user/valid")
    public ServerResponse<String> checkValid(String str, String type) {
        return userService.checkValid(str, type);
    }

    @GetMapping("/user/{userId}")
    public ServerResponse<User> getUserInfo(HttpSession session, @PathVariable("userId") String userId) {
        return userService.getInformation(Integer.valueOf(userId));
    }

    @GetMapping("/password/forget-password/question")
    public ServerResponse<String> forgetGetQuestion(String username) {

        return userService.selectQuestion(username);
    }

    @GetMapping("/password/forget-password/answer")
    public ServerResponse<String> forgetCheckAnswer(String username, String question, String answer) {
        return userService.checkAnswer(username, question, answer);
    }

    @PutMapping("/password/forget-password")
    public ServerResponse<String> forgetResetPassword(@RequestBody Map<String, String> map) {
        return userService.forgetResetPassword(map.get("username"), map.get("newPassword"), map.get("token"));
    }

    @PutMapping("/user/{userId}/password")
    public ServerResponse<String> resetPassword(HttpSession session, String oldPassword, String newPassword, @PathVariable("userId") String userId) {
                return userService.resetPassword(oldPassword, newPassword, userId);
    }

    @PutMapping("/user/{userId}")
    public ServerResponse<User> updateInformation(HttpSession session, User user) {
                return userService.updateInformation(user);
    }

    @GetMapping("/user/{userId}/info")
    public ServerResponse<User> getInformation(HttpSession session, @PathVariable("userId") String userId) {
                return userService.getInformation(Integer.valueOf(userId));
    }


}
