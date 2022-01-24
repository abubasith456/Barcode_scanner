package com.example.grootan.models;

public class RegisterModel {
    private String registerEmail;
    private String registerPassword;
    private String registerName;

    public RegisterModel(String registerEmail, String registerPassword, String registerName) {
        this.registerEmail = registerEmail;
        this.registerPassword = registerPassword;
        this.registerName = registerName;
    }

    public String getRegisterEmail() {
        return registerEmail;
    }

    public String getRegisterPassword() {
        return registerPassword;
    }

    public String getRegisterName() {
        return registerName;
    }
}
