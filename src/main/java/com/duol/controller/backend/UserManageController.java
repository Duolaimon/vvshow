package com.duol.controller.backend;

import com.duol.common.Const;
import com.duol.common.ServerResponse;
import com.duol.pojo.User;
import com.duol.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @author Duolaimon
 * 18-7-17 下午3:42
 */
@Controller
@RequestMapping("/manage/user")
public class UserManageController {

    private final UserService userService;

    @Autowired
    public UserManageController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value="login.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> login(String username, String password, HttpSession session){
        ServerResponse<String> response = userService.login(username,password);
        if(response.isSuccess()){
            //todo
//            User user = response.getData();
            User user = new User();
            if(user.getRole() == Const.Role.ROLE_ADMIN){
                //说明登录的是管理员
                session.setAttribute(Const.CURRENT_USER,user);
                return response;
            }else{
                return ServerResponse.createByErrorMessage("不是管理员,无法登录");
            }
        }
        return response;
    }

}
