package com.example.grootan.models;

import android.util.Log;

public class LoginModel {

    private String loginEmail;
    private String loginPassword;
    private String registerEmail;
    private String registerPassword;
    private String registerName;

    public LoginModel(String loginEmail, String loginPassword, String registerEmail, String registerPassword, String registerName) {
        this.loginEmail = loginEmail;
        this.loginPassword = loginPassword;
        this.registerEmail = registerEmail;
        this.registerPassword = registerPassword;
        this.registerName = registerName;
    }

    public String getLoginEmail() {
        return loginEmail;
    }

    public String getLoginPassword() {
        return loginPassword;
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
