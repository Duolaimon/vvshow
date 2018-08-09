package com.duol.dto.user;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Duolaimon
 * 18-8-1 下午8:12
 */
public class UserPwdVo {
    @ApiModelProperty(value = "用户名",position = 1)
    private String username;
    @ApiModelProperty(value = "密码",position = 2)
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
