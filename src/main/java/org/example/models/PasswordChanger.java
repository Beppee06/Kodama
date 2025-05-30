package org.example.models;

public class PasswordChanger {
    private String oldPassword;
    private String newPassword;
    private String newPasswordConfirm;

    public PasswordChanger(String oldPassword, String newPassword, String newPasswordConfirm) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.newPasswordConfirm = newPasswordConfirm;
    }

    public String getOldPassword() {return oldPassword;}
    public String getNewPassword() {return newPassword;}
    public String getNewPasswordConfirm() {return newPasswordConfirm;}
}
