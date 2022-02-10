package com.example.grootan.viewModel;

import android.app.Application;
import android.util.Log;
import android.util.Patterns;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.grootan.databinding.ActivityLoginBinding;
import com.example.grootan.repositories.AuthenticationRepository;
import com.google.firebase.auth.FirebaseUser;

public class LoginRegisterViewModel extends AndroidViewModel {

    public MutableLiveData<String> EmailLogin = new MutableLiveData<>();
    public MutableLiveData<String> PasswordLogin = new MutableLiveData<>();
    public MutableLiveData<String> EmailError = new MutableLiveData<>();
    public MutableLiveData<String> PasswordError = new MutableLiveData<>();
    public MutableLiveData<String> EmailRegister = new MutableLiveData<>();
    public MutableLiveData<String> PasswordRegister = new MutableLiveData<>();
    public MutableLiveData<String> NameRegister = new MutableLiveData<>();
    public MutableLiveData<String> EmailRegisterError = new MutableLiveData<>();
    public MutableLiveData<String> PasswordRegisterError = new MutableLiveData<>();
    public MutableLiveData<String> NameRegisterError = new MutableLiveData<>();
    public MutableLiveData<String> ForgotPassword = new MutableLiveData<>();
    public MutableLiveData<String> ForgotError = new MutableLiveData<>();
    public MutableLiveData<Boolean> onClickResult = new MutableLiveData<>();
    public MutableLiveData<Boolean> onClickRegister = new MutableLiveData<>();
    public MutableLiveData<Boolean> onClickForgotResult = new MutableLiveData<>();

    private AuthenticationRepository repository;
    private MutableLiveData<FirebaseUser> userLoginData;
    private Application application;
    private ActivityLoginBinding activityLoginBinding;

    public MutableLiveData<FirebaseUser> getUserLoginData() {
        return userLoginData;
    }

    public MutableLiveData<Boolean> onclickHide(View view) {
        EmailRegisterError.setValue(null);
        PasswordRegisterError.setValue(null);
        NameRegisterError.setValue(null);
        ForgotError.setValue(null);

        EmailRegister.setValue(null);
        PasswordRegister.setValue(null);
        NameRegister.setValue(null);
        ForgotPassword.setValue(null);

        onClickResult.setValue(false);
        onClickRegister.setValue(false);
        onClickForgotResult.setValue(false);
        return onClickResult;
    }

    public MutableLiveData<Boolean> onClickShow(View view) {
        EmailError.setValue(null);
        PasswordError.setValue(null);

        EmailLogin.setValue(null);
        PasswordLogin.setValue(null);

        onClickResult.setValue(true);
        onClickRegister.setValue(true);
        return onClickResult;
    }

    public MutableLiveData<Boolean> onForgotPasswordClick(View view) {
        EmailError.setValue(null);
        PasswordError.setValue(null);
        onClickResult.setValue(true);
        onClickRegister.setValue(false);
        onClickForgotResult.setValue(true);
        return onClickResult;
    }

    public void getBinding(ActivityLoginBinding activityLoginBinding) {
        this.activityLoginBinding = activityLoginBinding;
    }

    public LoginRegisterViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        repository = new AuthenticationRepository(application);
        userLoginData = repository.getFirebaseLoginUserMutableLiveData();
//        loadUserDetails();
    }

    public void onLoginClick(View view) {
        try {
            if (validateLogin()) {
                repository.login(EmailLogin.getValue(), PasswordLogin.getValue());
            }
        } catch (Exception exception) {
            Log.e("Error login==> ", "" + exception);
        }
    }

    public void onRegisterClick(View view) {
        try {
            if (validateRegister()) {
                repository.register(EmailRegister.getValue(), PasswordRegister.getValue(), NameRegister.getValue());
            }
        } catch (Exception exception) {
            Log.e("Error register ==> ", "" + exception);
        }
    }

    public void onForgotClick(View view) {
        try {
            if (validateForgot()) {
                repository.forgot(ForgotPassword.getValue());
            }
        } catch (Exception exception) {
            Log.e("Error forgot==> ", "" + exception);
        }
    }

    public boolean validateLogin() {
        EmailError.setValue(null);
        PasswordError.setValue(null);
        boolean valid = true;
        try {
            if (EmailLogin.getValue() == null || EmailLogin.getValue().isEmpty()) {
                EmailError.setValue("Please enter email address.");
                valid = false;
            }
            if (PasswordLogin.getValue() == null || PasswordLogin.getValue().isEmpty()) {
                PasswordError.setValue("Please enter the password.");
                valid = false;
            }
            if (!isEmailValid(EmailLogin.getValue())) {
                EmailError.setValue("Please enter a valid email address.");
                valid = false;
            }
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
        return valid;
    }

    public boolean validateRegister() {
        EmailRegisterError.setValue(null);
        NameRegisterError.setValue(null);
        PasswordRegisterError.setValue(null);
        boolean valid = true;
        try {
            if (NameRegister.getValue() == null || NameRegister.getValue().isEmpty()) {
                NameRegisterError.setValue("Please enter the Username");
                valid = false;
            }
            if (EmailRegister.getValue() == null || EmailRegister.getValue().isEmpty()) {
                EmailRegisterError.setValue("Please enter the email");
                valid = false;
            }
            if (PasswordRegister.getValue() == null || PasswordRegister.getValue().isEmpty()) {
                PasswordRegisterError.setValue("Please enter the password.");
                valid = false;
            }
            if (!isEmailValid(EmailRegister.getValue())) {
                EmailRegisterError.setValue("Please enter a valid email address.");
                valid = false;
            }
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
        return valid;
    }

    public boolean validateForgot() {
        boolean valid = true;
        try {
            if (ForgotPassword.getValue() == null || ForgotPassword.getValue().isEmpty()) {
                ForgotError.setValue("Please enter email address.");
                valid = false;
            }
            if (!isEmailValid(ForgotPassword.getValue())) {
                ForgotError.setValue("Please enter a valid email address.");
                valid = false;
            }
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }

        return valid;
    }

    public boolean isEmailValid(String value) {
        return Patterns.EMAIL_ADDRESS.matcher(value).matches();
    }

}
