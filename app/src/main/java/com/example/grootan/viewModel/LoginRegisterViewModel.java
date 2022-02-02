package com.example.grootan.viewModel;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.grootan.databinding.ActivityLoginBinding;
import com.example.grootan.repositories.AuthenticationRepository;
import com.google.firebase.auth.FirebaseUser;

public class LoginRegisterViewModel extends AndroidViewModel {

    public MutableLiveData<String> EmailLogin = new MutableLiveData<>();
    public MutableLiveData<String> PasswordLogin = new MutableLiveData<>();
    public MutableLiveData<String> EmailRegister = new MutableLiveData<>();
    public MutableLiveData<String> PasswordRegister = new MutableLiveData<>();
    public MutableLiveData<String> NameRegister = new MutableLiveData<>();
    private AuthenticationRepository repository;
    private MutableLiveData<FirebaseUser> userLoginData;
    private MutableLiveData<Boolean> loggedStatus;
    private Application application;

    public MutableLiveData<FirebaseUser> getUserLoginData() {
        return userLoginData;
    }

    public MutableLiveData<Boolean> getLoggedStatus() {
        return loggedStatus;
    }

    public LoginRegisterViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        userLoginData = repository.getFirebaseLoginUserMutableLiveData();
        loggedStatus = repository.getUserLoggedMutableLiveData();
    }


    public void onLoginClick(View view) {
        try {
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
                repository.register(EmailRegister.getValue(), PasswordRegister.getValue());
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
        boolean valid = true;
        try {
            if (email == null) {
                Toast.makeText(application.getApplicationContext(), "Please Enter the email", Toast.LENGTH_SHORT).show();
                valid = false;
            } else if (password == null) {
                Toast.makeText(application.getApplicationContext(), "Please enter the password", Toast.LENGTH_SHORT).show();
                valid = false;
            }
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
        return valid;
    }

    public boolean validateRegister(String name, String email, String password) {
        boolean valid = true;
        try {

            if (name == null) {
                Toast.makeText(application.getApplicationContext(), "Please enter the name", Toast.LENGTH_SHORT).show();
                valid = false;
            } else if (email == null) {
                Toast.makeText(application.getApplicationContext(), "Please enter the email", Toast.LENGTH_SHORT).show();
                valid = false;
            } else if (password == null) {
                Toast.makeText(application.getApplicationContext(), "Please enter the password", Toast.LENGTH_SHORT).show();
                valid = false;
            }

        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
        return valid;
    }


}
