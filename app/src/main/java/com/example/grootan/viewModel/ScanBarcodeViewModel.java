package com.example.grootan.viewModel;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.grootan.DashboardActivity;
import com.example.grootan.R;
import com.example.grootan.ScanBarCodeActivity;
import com.example.grootan.databinding.ActivityScanBarCodeBinding;
import com.example.grootan.models.ScannedData;
import com.example.grootan.repositories.BarCodeRepository;
import com.example.grootan.repositories.ScannedDataRepository;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ScanBarcodeViewModel extends AndroidViewModel {

    private MutableLiveData<ScannedData> scannedDataMutableLiveData;
    private MutableLiveData<String> stringMutableLiveData;
    private Application application;
    public BarCodeRepository barCodeRepository;
    public String scannedData;
    private ActivityScanBarCodeBinding activityScanBarCodeBinding;

    public ScanBarcodeViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        barCodeRepository = new BarCodeRepository(application);
    }

    public void getScannedData(String scannedData) {
        this.scannedData = scannedData;
        if (scannedData != null) {
            barCodeRepository.uploadDataToFirebase(scannedData);
            Intent backIntent = new Intent(application.getApplicationContext(), DashboardActivity.class);
            backIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            backIntent.putExtra("QR_value", scannedData);
            application.startActivity(backIntent);
//            ((Activity) application.getApplicationContext()).overridePendingTransition(R.anim.slide_in_left,
//                    R.anim.slide_out_right);
        }
    }

    public void imageViewChange(ActivityScanBarCodeBinding activityScanBarCodeBinding, String indicate) {
        this.activityScanBarCodeBinding = activityScanBarCodeBinding;
        try {
            if (indicate.equals("new")) {
                activityScanBarCodeBinding.frameLayoutBack.setVisibility(View.GONE);
                activityScanBarCodeBinding.frameLayoutMenu.setVisibility(View.VISIBLE);
            } else {
                activityScanBarCodeBinding.frameLayoutMenu.setVisibility(View.GONE);
                activityScanBarCodeBinding.frameLayoutBack.setVisibility(View.VISIBLE);
            }
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    public void onMenuOpenClick(View view) {
        activityScanBarCodeBinding.sideMenu.setVisibility(View.VISIBLE);
    }

    public void onMenuCloseClick(View view) {
        activityScanBarCodeBinding.sideMenu.setVisibility(View.GONE);
    }

    public void backIntent(View view) {
        Intent backIntent = new Intent(application.getApplicationContext(), DashboardActivity.class);
        backIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        application.startActivity(backIntent);
    }

    public MutableLiveData<ScannedData> getScannedDataMutableLiveData() {
        try {


        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
        return scannedDataMutableLiveData;
    }

}
