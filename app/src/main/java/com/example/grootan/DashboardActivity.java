package com.example.grootan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.example.grootan.adapter.ScannedDataAdapter;
import com.example.grootan.databinding.ActivityDashboardBinding;
import com.example.grootan.models.ScannedDataModel;
import com.example.grootan.models.UserModel;
import com.example.grootan.viewModel.DashboardViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private ScannedDataAdapter scannedDataAdapter;
    private DashboardViewModel dashboardViewModel;
    private ActivityDashboardBinding activityDashboardBinding;
    private UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_dashboard);
        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        activityDashboardBinding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);
        activityDashboardBinding.setLifecycleOwner(this);
        activityDashboardBinding.setDashboardViewModel(dashboardViewModel);

        userModel = new UserModel();
        activityDashboardBinding.setUser(userModel);
        activityDashboardBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        activityDashboardBinding.recyclerView.setHasFixedSize(true);
        scannedDataAdapter = new ScannedDataAdapter();
        activityDashboardBinding.recyclerView.setAdapter(scannedDataAdapter);
        dashboardViewModel.passBinding(activityDashboardBinding);
        getScannedData();
    }

    private void getScannedData() {
        dashboardViewModel.getScannedData().observe(this, new Observer<List<ScannedDataModel>>() {
            private Object ArrayList;

            @Override
            public void onChanged(List<ScannedDataModel> scannedDatumModels) {
                scannedDataAdapter.getScannedData((ArrayList<ScannedDataModel>) scannedDatumModels);

            }
        });
    }
}