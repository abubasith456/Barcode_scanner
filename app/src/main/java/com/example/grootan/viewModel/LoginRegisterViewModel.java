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
    public MutableLiveData<Boolean> onClickResult = new MutableLiveData<>();
    private AuthenticationRepository repository;
    private MutableLiveData<FirebaseUser> userLoginData;
    private MutableLiveData<Boolean> loggedStatus;
    private Application application;
    private ActivityLoginBinding activityLoginBinding;

    public MutableLiveData<FirebaseUser> getUserLoginData() {
        return userLoginData;
    }

//    public MutableLiveData<Boolean> getLoggedStatus() {
//        return loggedStatus;
//    }

    public MutableLiveData<Boolean> onclickHide(View view) {
        onClickResult.setValue(false);
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
        loggedStatus = repository.getUserLoggedMutableLiveData();
    }


    public void onLoginClick(View view) {
        try {
//            if (!TextUtils.isEmpty(EmailLogin.get()) ){
//                activityLoginBinding.mTextViewLoginErrorEmail.setVisibility(View.VISIBLE);
//                activityLoginBinding.editTextEmailInput.setBackground(application.getResources().getDrawable(R.drawable.background_rounded_edit_text_error));
//                activityLoginBinding.mTextViewLoginErrorEmail.setText(application.getResources().getString(R.string.error_username));
//            } else {
//                activityLoginBinding.mTextViewLoginErrorEmail.setVisibility(View.GONE);
//                activityLoginBinding.editTextEmailInput.setBackground(application.getResources().getDrawable(R.drawable.background_rounded_edit_text_gray));
//            }
            if (validateLogin(EmailLogin.getValue(), PasswordLogin.getValue())) {
                repository.login(EmailLogin.getValue(), PasswordLogin.getValue());
            }
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    public void onRegisterClick(View view) {
        try {
            if (validateRegister(NameRegister.getValue(), EmailRegister.getValue(), PasswordRegister.getValue())) {
                repository.register(EmailRegister.getValue(), PasswordRegister.getValue(), NameRegister.getValue());
            }
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

//    public void onSignOut(View view) {
//        try {
//            repository.signOut();
//        } catch (Exception exception) {
//            Log.e("Error ==> ", "" + exception);
//        }
//    }

    public boolean validateLogin(String email, String password) {
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

//            if (!TextUtils.isEmpty(email)) {
//                activityLoginBinding.mTextViewLoginErrorEmail.setVisibility(View.VISIBLE);
//                activityLoginBinding.editTextEmailInput.setBackground(application.getResources().getDrawable(R.drawable.background_rounded_edit_text_error));
//                activityLoginBinding.mTextViewLoginErrorEmail.setText(application.getResources().getString(R.string.error_username));
//                valid = false;
//            } else {
//                activityLoginBinding.mTextViewLoginErrorEmail.setVisibility(View.GONE);
//                activityLoginBinding.editTextEmailInput.setBackground(application.getResources().getDrawable(R.drawable.background_rounded_edit_text_gray));
//            }
//            if (password == null) {
//                activityLoginBinding.mTextViewLoginErrorPassword.setVisibility(View.VISIBLE);
//                activityLoginBinding.editTextPasswordInput.setBackground(application.getResources().getDrawable(R.drawable.background_rounded_edit_text_error));
//                activityLoginBinding.mTextViewLoginErrorPassword.setText(application.getResources().getString(R.string.email_error));
//                valid = false;
//            } else {
//                activityLoginBinding.mTextViewLoginErrorPassword.setVisibility(View.GONE);
//                activityLoginBinding.editTextSignUpEmail.setBackground(application.getResources().getDrawable(R.drawable.background_rounded_edit_text_gray));
//            }
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
        return valid;
    }

    public boolean validateRegister(String name, String email, String password) {
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


//            if (name == null) {
//
//                valid = false;
//            } else if (email == null) {
//                Toast.makeText(application.getApplicationContext(), "Please enter the email", Toast.LENGTH_SHORT).show();
//                valid = false;
//            } else if (password == null) {
//                Toast.makeText(application.getApplicationContext(), "Please enter the password", Toast.LENGTH_SHORT).show();
//                valid = false;
//            }

        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
        return valid;
    }

    public boolean isEmailValid(String value) {
        return Patterns.EMAIL_ADDRESS.matcher(value).matches();
    }

}
