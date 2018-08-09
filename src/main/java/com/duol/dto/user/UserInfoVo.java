package com.duol.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author Duolaimon
 * 18-8-1 下午7:54
 */
@ApiModel
public class UserInfoVo {

    @ApiModelProperty(required = true,position = 1)
    private String username;

    @ApiModelProperty(required = true,position = 2)
    private String password;

    @ApiModelProperty(required = true,position = 3)
    private String email;

    @ApiModelProperty(position = 5)
    private String phone;

    @ApiModelProperty(position = 6)
    private String question;

    @ApiModelProperty(position = 7)
    private String answer;


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

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
