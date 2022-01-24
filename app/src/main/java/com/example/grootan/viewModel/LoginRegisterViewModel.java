package com.example.grootan.viewModel;

import android.app.Application;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.grootan.LoginActivity;
import com.example.grootan.databinding.ActivityLoginBinding;
import com.example.grootan.models.LoginModel;
import com.example.grootan.models.RegisterModel;
import com.example.grootan.repositories.AuthenticationRepository;
import com.google.firebase.auth.FirebaseUser;

public class LoginRegisterViewModel extends AndroidViewModel {

    public MutableLiveData<String> EmailLogin = new MutableLiveData<>();
    public MutableLiveData<String> PasswordLogin = new MutableLiveData<>();
    public MutableLiveData<String> EmailRegister = new MutableLiveData<>();
    public MutableLiveData<String> PasswordRegister = new MutableLiveData<>();
    public MutableLiveData<String> NameRegister = new MutableLiveData<>();
    private MutableLiveData<LoginModel> userLoginMutableLiveData;
    private MutableLiveData<RegisterModel> userRegisterMutableLiveData;
    private ActivityLoginBinding activityLoginBinding;

    private AuthenticationRepository repository;
    private MutableLiveData<FirebaseUser> userData;
    private MutableLiveData<Boolean> loggedStatus;
    private Application application;

    public MutableLiveData<FirebaseUser> getUserData() {
        return userData;
    }

    public MutableLiveData<Boolean> getLoggedStatus() {
        return loggedStatus;
    }

    public LoginRegisterViewModel(@NonNull  Application application) {
        super(application);
        this.application=application;
        repository = new AuthenticationRepository(application);
        userData = repository.getFirebaseUserMutableLiveData();
        loggedStatus = repository.getUserLoggedMutableLiveData();
    }

//    public void register(String email , String pass){
//        repository.register(email, pass);
//    }
//    public void signIn(String email , String pass){
//        repository.login(email, pass);
//    }
//    public void signOut(){
//        repository.signOut();
//    }

//    public MutableLiveData<LoginModel> getLoginUser() {
//
//        if (userLoginMutableLiveData == null) {
//            userLoginMutableLiveData = new MutableLiveData<>();
//
//        }
//        return userLoginMutableLiveData;
//
//    }
//
//    public MutableLiveData<RegisterModel> getRegisterUser() {
//        if (userRegisterMutableLiveData == null) {
//            userRegisterMutableLiveData = new MutableLiveData<>();
//        }
//        return userRegisterMutableLiveData;
//    }

    public void onLoginClick(View view) {
        try {
            if (validateLogin(EmailLogin.getValue(), PasswordLogin.getValue())) {
                repository.login(EmailLogin.getValue(), PasswordLogin.getValue());

            }
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }

    }

    public boolean validateLogin(String email, String password) {
        boolean valid = true;
        try {
            if (email.equals("")) {
                valid = false;
//                activityLoginBinding.mTextViewErrorMobileNUmber.setVisibility(View.VISIBLE);
//                activityLoginBinding.mTextViewErrorMobileNUmber.setText("Please enter the email");
            } else if (password.equals("")) {
                valid = false;
            }
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
        return valid;
    }


}
