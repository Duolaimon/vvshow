package com.duol.dto.user;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Duolaimon
 * 18-8-18 下午2:08
 */
public class UserInfoVO {
    @ApiModelProperty(required = true,position = 1)
    private String username;

    @ApiModelProperty(required = true,position = 3)
    private String email;

    @ApiModelProperty(position = 5)
    private String phone;

    @ApiModelProperty(position = 6)
    private String question;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
