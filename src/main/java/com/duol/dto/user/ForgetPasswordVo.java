package com.duol.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author Duolaimon
 * 18-8-1 下午8:43
 */
@ApiModel
public class ForgetPasswordVo {
    @ApiModelProperty(required = true,position = 1)
    private String username;

    @ApiModelProperty(required = true,position = 2)
    private String newPassword;

    @ApiModelProperty(required = true,position = 3)
    private String token;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
