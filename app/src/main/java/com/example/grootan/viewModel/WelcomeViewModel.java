package com.example.grootan.viewModel;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.grootan.models.UserModel;
import com.example.grootan.repositories.AuthenticationRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class WelcomeViewModel extends AndroidViewModel {
    private FirebaseAuth auth;
    private MutableLiveData<FirebaseUser> userLoginData;
    private AuthenticationRepository repository;
    private UserModel userModel;
    private Application application;

    public WelcomeViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        userModel = new UserModel();
        repository = new AuthenticationRepository(application);
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
