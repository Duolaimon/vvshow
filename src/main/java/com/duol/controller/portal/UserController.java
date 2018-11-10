package com.duol.controller.portal;

import com.duol.common.Const;
import com.duol.common.ServerResponse;
import com.duol.dto.user.*;
import com.duol.pojo.User;
import com.duol.service.UserService;
import com.duol.util.BaseVOUtil;
import com.duol.util.SCaptcha;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

import static com.duol.util.SessionUtil.verifyUserLogin;

/**
 * @author Duolaimon
 * 18-2-16 下午6:38
 */
@RestController
@Api(value = "用户api", consumes = "application/json", produces = "http")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private static final String VERIFY_CODE = "verification";

    private final UserService userService;

    @Autowired
    public UserController(@Qualifier("userServiceImpl") UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "用户登录")
    @PostMapping("/session")
    public ServerResponse<String> login(@RequestBody UserPwdVo user,
                                        HttpSession session, HttpServletResponse servletResponse, HttpServletRequest servletRequest) {
        String username = user.getUsername();
        String password = user.getPassword();
        Cookie[] cookies = servletRequest.getCookies();
        if (cookies != null)
            for (Cookie cookie :
                    cookies) {
                if ("userId".equals(cookie.getName()) && Objects.nonNull(session.getAttribute(Const.CURRENT_USER))) {
                    return ServerResponse.createBySuccessMessage("用户已登录,不要重复登录");
                }
            }
        ServerResponse<String> response = userService.login(username, password);
        return doLogin(session, servletResponse, response);
    }

    @ApiOperation("用户注册")
    @ApiImplicitParam(name = "loginVO", value = "用户注册信息", required = true, dataType = "LoginVO")
    @ApiResponse(code = 201, message = "注册成功")
    @PostMapping("/user")
    public ServerResponse<String> register(@RequestBody LoginVO loginVO, HttpSession session, HttpServletResponse servletResponse) {
        logger.info("register: username:{}", loginVO.getUsername());
        ServerResponse<String> response = userService.register(BaseVOUtil.parse(loginVO, User.class));
        return doLogin(session, servletResponse, response);
    }

    private ServerResponse<String> doLogin(HttpSession session, HttpServletResponse servletResponse, ServerResponse<String> response) {
        if (response.isSuccess()) {
            String[] info = response.getData().split("-");
            Cookie cookie = new Cookie("userId", info[0]);
            cookie.setHttpOnly(false);
            cookie.setMaxAge(30 * 60 * 10);
            cookie.setPath("/");
            servletResponse.addCookie(cookie);
            session.setAttribute(Const.CURRENT_USER, response.getData());
            servletResponse.setStatus(HttpServletResponse.SC_CREATED);
        }
        return ServerResponse.createBySuccess(response, response.getData().split("-")[0]);
    }


    @ApiOperation(value = "用户注销")
    @DeleteMapping("/session")
    public ServerResponse<String> logout(HttpSession session, HttpServletResponse response) {
        String[] info = verifyUserLogin(session);
        if (Objects.nonNull(info)) {
            userService.logout(info[0]);
            session.removeAttribute(Const.CURRENT_USER);
            doLogout(response);
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createBySuccessMessage("用户未登录或已过期");
    }

    private void doLogout(HttpServletResponse response) {
        Cookie cookie = new Cookie("userId", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @ApiOperation(value = "用户校验")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "str", value = "内容", required = true, paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型", required = true, paramType = "query")})
    @GetMapping("/user/valid")
    public ServerResponse<String> checkValid(String str, String type) {
        return userService.checkValid(str, type);
    }

    @ApiOperation(value = "个人信息")
    @ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "path")
    @GetMapping("/user/{userId}")
    public ServerResponse<UserInfoVO> getUserInfo(HttpSession session, @PathVariable("userId") String userId) {
        User user = userService.getInformation(Integer.valueOf(userId)).getData();
        return ServerResponse.createBySuccess(BaseVOUtil.parse(user, UserInfoVO.class));
    }

    @ApiOperation(value = "获得忘记密码问题")
    @ApiImplicitParam(name = "username", value = "用户名", required = true, paramType = "query")
    @GetMapping("/password/forget-password/question")
    public ServerResponse<String> forgetGetQuestion(String username) {
        return userService.selectQuestion(username);
    }

    @ApiOperation(value = "忘记密码问题答案")
    @ApiImplicitParam(name = "vo", value = "问题答案", dataType = "ForgetCheckAnswerVo")
    @PostMapping("/password/forget-password/answer")
    public ServerResponse<String> forgetCheckAnswer(@RequestBody ForgetCheckAnswerVo vo) {
        return userService.checkAnswer(vo.getUsername(), vo.getQuestion(), vo.getAnswer());
    }

    @ApiOperation(value = "携带token更改密码")
    @ApiImplicitParam(name = "forgetPasswordVo", value = "修改密码", dataType = "ForgetPasswordVo")
    @PutMapping("/password/forget-password")
    public ServerResponse<String> forgetResetPassword(@RequestBody ForgetPasswordVo forgetPasswordVo) {
        return userService.forgetResetPassword(forgetPasswordVo.getUsername(),
                forgetPasswordVo.getNewPassword(), forgetPasswordVo.getToken());
    }

    @ApiOperation(value = "更改密码")
    @PutMapping("/user/{userId}/password")
    public ServerResponse<String> resetPassword(HttpSession session, @RequestBody ResetPwdVo resetPwdVo, @PathVariable("userId") String userId) {
        return userService.resetPassword(resetPwdVo.getOldPassword(), resetPwdVo.getNewPassword(), userId);
    }

    @ApiOperation(value = "更新个人信息")
    @PutMapping("/user/{userId}")
    public ServerResponse<UserInfoVO> updateInformation(HttpSession session, @RequestBody UserInfoVO userInfoVO) {
        User user = userService.updateInformation(BaseVOUtil.parse(userInfoVO, User.class)).getData();
        return ServerResponse.createBySuccess(BaseVOUtil.parse(user, UserInfoVO.class));
    }

    @GetMapping("/verify-code")
    public void verification(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        // 设置响应的类型格式为图片格式
        response.setContentType("image/jpeg");
        // 禁止图像缓存。
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        //实例生成验证码对象
        SCaptcha instance = new SCaptcha();
        //将验证码存入session
        session.setAttribute(VERIFY_CODE, instance.getCode());
        //向页面输出验证码图片
        instance.write(response.getOutputStream());
    }

}
