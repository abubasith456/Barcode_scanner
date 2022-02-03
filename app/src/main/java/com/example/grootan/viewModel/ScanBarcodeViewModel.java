package com.example.grootan.viewModel;

import android.app.Application;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.grootan.ScanBarCodeActivity;
import com.example.grootan.databinding.ActivityScanBarCodeBinding;
import com.example.grootan.models.ScannedDataModel;
import com.example.grootan.repositories.BarCodeRepository;

public class ScanBarcodeViewModel extends AndroidViewModel {

    private MutableLiveData<ScannedDataModel> scannedDataMutableLiveData;
    private MutableLiveData<String> stringMutableLiveData;
    private Application application;
    public BarCodeRepository barCodeRepository;
    public String scannedData;
    private ActivityScanBarCodeBinding activityScanBarCodeBinding;
    private ScanBarCodeActivity scanBarCodeActivity;

    public ScanBarcodeViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        barCodeRepository = new BarCodeRepository(application);
    }

    public void getScannedData(String scannedData) {
        this.scannedData = scannedData;
        if (scannedData != null) {
            barCodeRepository.uploadDataToFirebase(scannedData);
//            Intent backIntent = new Intent(application.getApplicationContext(), DashboardActivity.class);
//            backIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            backIntent.putExtra("QR_value", scannedData);
//            application.startActivity(backIntent);
//            ((Activity) application.getApplicationContext()).overridePendingTransition(R.anim.slide_in_left,
//                    R.anim.slide_out_right);
            scanBarCodeActivity.finish();

        }
    }

    public void imageViewChange(ScanBarCodeActivity scanBarCodeActivity, ActivityScanBarCodeBinding activityScanBarCodeBinding, String indicate) {
        this.activityScanBarCodeBinding = activityScanBarCodeBinding;
        this.scanBarCodeActivity = scanBarCodeActivity;
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
        scanBarCodeActivity.finish();
    }

    public MutableLiveData<ScannedDataModel> getScannedDataMutableLiveData() {
        try {


        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
        return scannedDataMutableLiveData;
    }

}
