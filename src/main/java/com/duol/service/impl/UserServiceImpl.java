package com.duol.service.impl;

import com.duol.cache.ObjectCache;
import com.duol.cache.ValueCache;
import com.duol.common.Const;
import com.duol.common.ServerResponse;
import com.duol.dao.UserMapper;
import com.duol.pojo.User;
import com.duol.service.UserService;
import com.duol.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

/**
 * @author Duolaimon
 * 18-2-18 下午9:33
 */
@Service
public class UserServiceImpl implements UserService {

    private UserMapper userMapper;
    private ObjectCache objectCache;

    @Autowired
    public UserServiceImpl(UserMapper userMapper, ObjectCache objectCache) {
        this.userMapper = userMapper;
        this.objectCache = objectCache;
    }

    @Override
    public ServerResponse<String> login(String username, String password) {
        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username, md5Password);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户名不存在或密码错误");
        }

        return ServerResponse.createBySuccess("登录成功", loginSuccess(user));
    }


    @Override
    public ServerResponse<String> register(User user) {
        ServerResponse<String> validResponse = this.checkValid(user.getUsername(), Const.USERNAME);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }
        validResponse = this.checkValid(user.getEmail(), Const.EMAIL);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }
        user.setRole(Const.Role.ROLE_CUSTOMER);
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        if (userMapper.insert(user) < 1) {
            return ServerResponse.createByErrorMessage("注册失败");
        }
        user.setId(userMapper.selectIdByUsername(user.getUsername()));
        return ServerResponse.createBySuccess("注册成功",loginSuccess(user));
    }

    /**
     * 登陆成功
     * @param user  登录用户
     * @return  session id
     */
    private String loginSuccess(User user) {
        Integer userId = user.getId();
        user.setPassword(StringUtils.EMPTY);
        String sessionID = ValueCache.cacheSessionID(userId.toString());
        ValueCache.cache(ValueCache.USERNAME_PREFIX + user.getUsername(), userId.toString());
        objectCache.cacheObject(userId.toString(), user);//缓存用户信息
        return userId + "-" + sessionID;
    }

    @Override
    public void logout(String userId) {
        ValueCache.removeSessionID(userId);
        String username = objectCache.getProperty(userId, "username");
        ValueCache.delete(ValueCache.USERNAME_PREFIX + username);
        objectCache.deleteCache(userId);
    }

    @Override
    public ServerResponse<String> checkValid(String str, String type) {
        if (StringUtils.isNotBlank(type)) {
            if (Const.USERNAME.equals(type)) {
//                todo
                if (Objects.nonNull(ValueCache.get(ValueCache.USERNAME_PREFIX + str)) || userMapper.checkUsername(str) > 0) {
                    return ServerResponse.createByErrorMessage("用户名已存在");
                }
            }
            if (Const.EMAIL.equals(type)) {
                if (userMapper.checkEmail(str) > 0) {
                    return ServerResponse.createByErrorMessage("email已存在");
                }
            }
        } else {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        return ServerResponse.createBySuccessMessage("校验成功");
    }

    @Override
    public ServerResponse<String> selectQuestion(String username) {
        ServerResponse<String> validResponse = this.checkValid(username, Const.USERNAME);
        if (validResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }
//        String userId = ValueCache.get(username);
//        String question = objectCache.getProperty(userId,"question");
//        if (Objects.isNull(question)) {
//            question = userMapper.selectQuestionByUsername(username);
//        }
        String question;
        question = userMapper.selectQuestionByUsername(username);
        if (StringUtils.isNotBlank(question)) {
            return ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createByErrorMessage("找回密码的问题是空的");
    }

    @Override
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        if (userMapper.checkAnswer(username, question, answer) > 0) {
            String forgetToken = UUID.randomUUID().toString();
//            TokenCache.setKey(TokenCache.TOKEN_PREFIX + username, forgetToken);
            ValueCache.cache(ValueCache.TOKEN_PREFIX + username, forgetToken);
            return ServerResponse.createBySuccess(forgetToken);
        }
        return ServerResponse.createByErrorMessage("问题答案错误");
    }

    @Override
    public ServerResponse<String> forgetResetPassword(String username, String newPassword, String forgetToken) {
        if (StringUtils.isBlank(forgetToken)) {
            return ServerResponse.createByErrorMessage("参数错误,token需要传递");
        }
        ServerResponse<String> validResponse = this.checkValid(username, Const.USERNAME);
        if (validResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }
//        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX + username);
        String token = ValueCache.get(ValueCache.TOKEN_PREFIX + username);
        if (StringUtils.isBlank(token)) {
            return ServerResponse.createByErrorMessage("token无效或过期");
        }
        if (StringUtils.equals(forgetToken, token)) {
            String md5Password = MD5Util.MD5EncodeUtf8(newPassword);
            if (userMapper.updatePasswordByUsername(username, md5Password) > 0) {
                return ServerResponse.createBySuccessMessage("修改密码成功");
            }
        }
        return ServerResponse.createByErrorMessage("修改密码失败");
    }

    @Override
    public ServerResponse<String> resetPassword(String oldPassword, String newPassword, String userId) {
        if (!checkPassword(oldPassword, userId)) {
            return ServerResponse.createByErrorMessage("旧密码错误");
        }
        if (userMapper.updatePasswordById(Integer.valueOf(userId), MD5Util.MD5EncodeUtf8(newPassword)) > 0) {
            objectCache.cacheProperty(userId, "password", MD5Util.MD5EncodeUtf8(newPassword));
            return ServerResponse.createBySuccessMessage("密码更新成功");
        }
        return ServerResponse.createByErrorMessage("密码更新失败");
    }

    private boolean checkPassword(String password, String userId) {
        String name = "password";
        String oldPassword = objectCache.getProperty(userId, name);
        if (StringUtils.equals(MD5Util.MD5EncodeUtf8(password), oldPassword)) return true;
        return userMapper.checkPassword(MD5Util.MD5EncodeUtf8(oldPassword), Integer.valueOf(userId)) > 0;
    }

    @Override
    public ServerResponse<User> updateInformation(User user) {
        int resultCount = userMapper.checkEmailByUserId(user.getEmail(), user.getId());
        if (resultCount > 0) {
            return ServerResponse.createByErrorMessage("email已被使用");
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());

        if (userMapper.updateByPrimaryKeySelective(updateUser) > 0) {
            objectCache.cacheObject(updateUser.getId().toString(), updateUser);
            return ServerResponse.createBySuccess("更新个人信息成功", updateUser);
        }
        return ServerResponse.createByErrorMessage("更新个人信息失败");
    }

    @Override
    public ServerResponse<User> getInformation(Integer userId) {
        //查找缓存
        User user = (User) objectCache.entries(userId.toString());
        if (user == null) {
            user = userMapper.selectByPrimaryKey(userId);
            if (user == null) {
                return ServerResponse.createByErrorMessage("找不到当前用户");
            }
        }
        user.setPassword(StringUtils.EMPTY);
        objectCache.cacheObject(userId.toString(), user);
        return ServerResponse.createBySuccess(user);
    }

    @Override
    public ServerResponse checkAdminRole(User user) {
        if (user != null && Const.Role.ROLE_ADMIN.equals(user.getRole())) {
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }

}
