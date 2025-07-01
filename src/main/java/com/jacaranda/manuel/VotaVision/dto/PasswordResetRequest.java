package com.jacaranda.manuel.VotaVision.dto;

public class PasswordResetRequest {
    private String password;
    private String confirmPassword;

    public PasswordResetRequest() {
    }

    public PasswordResetRequest(String password, String confirmPassword) {
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
