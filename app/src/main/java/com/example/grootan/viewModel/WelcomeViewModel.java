package com.example.grootan.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomeViewModel extends AndroidViewModel {
    private FirebaseAuth auth;
    private MutableLiveData<FirebaseUser> userLoginData;

    public WelcomeViewModel(@NonNull Application application) {
        super(application);
        userLoginData = new MutableLiveData<>();
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            userLoginData.setValue(auth.getCurrentUser());
        }
    }

    public MutableLiveData<FirebaseUser> getCurrentUser() {
        return userLoginData;
    }
}
