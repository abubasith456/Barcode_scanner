package com.example.grootan.viewModel;

import android.app.Application;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.grootan.models.ScannedData;
import com.example.grootan.repositories.ScannedDataRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DashboardViewModel extends AndroidViewModel {
    private ScannedDataRepository scannedDataRepository;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth auth;
    private Application application;


    public DashboardViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        scannedDataRepository=new ScannedDataRepository(application);
        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    public LiveData<List<ScannedData>> getScannedData() {
        return scannedDataRepository.getMutableLiveData();
    }

    public void onSignOut(View view) {
        try {
            scannedDataRepository.signOut();
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }
}
