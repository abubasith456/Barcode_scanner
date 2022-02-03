package com.example.grootan.viewModel;

import android.app.Application;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.grootan.DashboardActivity;
import com.example.grootan.ScanBarCodeActivity;
import com.example.grootan.databinding.ActivityDashboardBinding;
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
    private ActivityDashboardBinding activityDashboardBinding;

    public DashboardViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        scannedDataRepository = new ScannedDataRepository(application);
        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    public LiveData<List<ScannedData>> getScannedData() {
        return scannedDataRepository.getMutableLiveData();
    }

    public void passBinding(ActivityDashboardBinding activityDashboardBinding) {
        this.activityDashboardBinding = activityDashboardBinding;
    }

    public void onMenuOpenClick(View view) {
        activityDashboardBinding.sideMenu.setVisibility(View.VISIBLE);
    }

    public void onMenuCloseClick(View view) {
        activityDashboardBinding.sideMenu.setVisibility(View.GONE);
    }

    public void onFloatingButtonClick(View view){
        Intent intent = new Intent(application.getApplicationContext(), ScanBarCodeActivity.class);
        intent.putExtra("indicate","old");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        application.startActivity(intent);
    }

    public void onSignOut(View view) {
        try {
            scannedDataRepository.signOut();
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }
}
