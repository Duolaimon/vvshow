package com.duol.dto.user;

/**
 * @author Duolaimon
 * 18-8-1 下午9:06
 */
public class ResetPwdVo {
    private String newPassword;
    private String oldPassword;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
}
