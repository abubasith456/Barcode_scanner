package com.example.grootan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grootan.adapter.ScannedDataAdapter;
import com.example.grootan.databinding.ActivityDashboardBinding;
import com.example.grootan.models.ScannedDataModel;
import com.example.grootan.models.UserModel;
import com.example.grootan.viewModel.DashboardViewModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private ScannedDataAdapter scannedDataAdapter;
    private DashboardViewModel dashboardViewModel;
    private ActivityDashboardBinding activityDashboardBinding;
    private UserModel userModel;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        activityDashboardBinding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);
        activityDashboardBinding.setLifecycleOwner(this);
        activityDashboardBinding.setDashboardViewModel(dashboardViewModel);
        dashboardViewModel.getActivity(DashboardActivity.this);
        userModel = new UserModel();
        activityDashboardBinding.setUser(userModel);
        activityDashboardBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        activityDashboardBinding.recyclerView.setHasFixedSize(true);
        scannedDataAdapter = new ScannedDataAdapter();
        activityDashboardBinding.recyclerView.setAdapter(scannedDataAdapter);
        dashboardViewModel.passBinding(activityDashboardBinding);
        getScannedData();
        getUserData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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

    private void getUserData() {
        dashboardViewModel.getCurrentUser().observe(this, new Observer<UserModel>() {
            @Override
            public void onChanged(UserModel userModel) {
                if (userModel.getUsername() != null) {
                    activityDashboardBinding.textViewUserName.setText("Hi.." + userModel.getUsername());
                } else {
                    activityDashboardBinding.textViewUserName.setText("Hi... Grootan user");
                }
            }
        });
    }
}