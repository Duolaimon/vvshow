package com.duol.service;

import com.duol.common.ServerResponse;
import com.duol.pojo.User;

/**
 * 用户操作
 *
 *
 * @author Duolaimon
 * 18-2-16 下午6:39
 */
public interface UserService {

    ServerResponse<String> login(String username, String password);

    ServerResponse<String> register(User user);

    /**
     * 检验唯一性字段内容是否已经存在
     * @param str   字段内容
     * @param type  字段类型,如username,email
     * @return      检验结果
     */
    ServerResponse<String> checkValid(String str, String type);

    /**
     * 用户是否有找回密码的问题和答案
     * @param username  用户名
     */
    ServerResponse<String> selectQuestion(String username);
    /**
     * 检验问题答案是否正确
     * @param username  用户名
     * @param question  问题
     * @param answer    答案
     */
    ServerResponse<String> checkAnswer(String username, String question, String answer);

    /**
     * 通过忘记问题答案修改密码
     * @param username      用户名
     * @param newPassword   新密码
     * @param forgetToken   验证token
     */
    ServerResponse<String> forgetResetPassword(String username, String newPassword, String forgetToken);

    /**
     * 通过旧密码修改密码
     * @param oldPassword   旧密码
     * @param newPassword   新密码
     */
    ServerResponse<String> resetPassword(String oldPassword, String newPassword, String userId);

    ServerResponse<User> updateInformation(User user);

    ServerResponse<User> getInformation(Integer userId);

    /**
     * 检验是否是管理员
     */
    ServerResponse checkAdminRole(User user);

    void logout(String userId);
}
