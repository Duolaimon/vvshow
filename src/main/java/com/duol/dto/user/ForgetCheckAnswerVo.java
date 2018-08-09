package com.duol.dto.user;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Duolaimon
 * 18-8-1 下午8:48
 */
public class ForgetCheckAnswerVo {
    @ApiModelProperty(required = true,position = 1)
    private String username;

    @ApiModelProperty(required = true,position = 2)
    private String question;

    @ApiModelProperty(required = true,position = 3)
    private String answer;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
