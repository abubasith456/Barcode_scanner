package com.example.grootan.repositories;

import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.MutableLiveData;

import com.example.grootan.DashboardActivity;
import com.example.grootan.LoginRegisterActivity;
import com.example.grootan.ScanBarCodeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthenticationRepository {
    private Application application;
    private MutableLiveData<FirebaseUser> firebaseLoginUserMutableLiveData;

    private MutableLiveData<Boolean> userLoggedMutableLiveData;
    private FirebaseAuth auth;


    public MutableLiveData<FirebaseUser> getFirebaseLoginUserMutableLiveData() {
        return firebaseLoginUserMutableLiveData;
    }

    public MutableLiveData<Boolean> getUserLoggedMutableLiveData() {
        return userLoggedMutableLiveData;
    }

    public AuthenticationRepository(Application application) {
        this.application = application;
        firebaseLoginUserMutableLiveData = new MutableLiveData<>();
        userLoggedMutableLiveData = new MutableLiveData<>();
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            firebaseLoginUserMutableLiveData.postValue(auth.getCurrentUser());
        }
    }

    public void register(String email, String pass) {
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(application.getApplicationContext(), ScanBarCodeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    application.startActivities(new Intent[]{intent});
                } else {
                    Toast.makeText(application, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void login(String email, String pass) {
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    firebaseLoginUserMutableLiveData.postValue(auth.getCurrentUser());
                } else {
                    Toast.makeText(application, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

//    public void signOut() {
//        try {
//            AlertDialog.Builder builder1 = new AlertDialog.Builder(application.getApplicationContext());
//            builder1.setMessage("Are you sure.. do you want to logout?");
//            builder1.setCancelable(true);
//
//            builder1.setPositiveButton(
//                    "Yes",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            auth.signOut();
//                            Intent intent = new Intent(application.getApplicationContext(), LoginRegisterActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                            application.startActivity(intent);
//                        }
//                    });
//
//            builder1.setNegativeButton(
//                    "No",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            dialog.cancel();
//                        }
//                    });
//
//            AlertDialog alert11 = builder1.create();
//            alert11.show();
//        } catch (Exception exception) {
//            Log.e("Error ==> ", "" + exception);
//        }
//        userLoggedMutableLiveData.postValue(true);
//    }

}
