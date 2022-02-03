package com.example.grootan;

import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.grootan.databinding.ActivityWelcomeBinding;
import com.example.grootan.viewModel.WelcomeViewModel;
import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomeActivity extends AppIntro {

    FirebaseAuth firebaseAuth;
    private ActivityWelcomeBinding activityWelcomeBinding;
    private WelcomeViewModel welcomeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_welcome);
        welcomeViewModel = ViewModelProviders.of(this).get(WelcomeViewModel.class);
        welcomeViewModel.getCurrentUser().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null) {
                    Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                    startActivity(intent);
                }
            }
        });
//        activityWelcomeBinding = DataBindingUtil.setContentView(this, R.layout.activity_welcome);
//        activityWelcomeBinding.setWelcomeViewModel(welcomeViewModel);
//        activityWelcomeBinding.setLifecycleOwner(this);
        firebaseAuth = FirebaseAuth.getInstance();
        addSlide(AppIntroFragment.newInstance("Welcome", "This application created by the Google firebase to authenticate the user logins.", R.drawable.firebase, ContextCompat.getColor(getApplicationContext(), R.color.colorAccent)));
        addSlide(AppIntroFragment.newInstance("Scan QR", "Scan the QR code to find the result. And that scanned result value stored to firebase.", R.drawable.qr_code_scan, ContextCompat.getColor(getApplicationContext(), R.color.colorAccent)));
        addSlide(AppIntroFragment.newInstance("Retrieve data", "This application shows the value that previously scanned.", R.drawable.data_storage, ContextCompat.getColor(getApplicationContext(), R.color.colorAccent)));
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        startActivity(new Intent(getApplicationContext(), LoginRegisterActivity.class));
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        startActivity(new Intent(getApplicationContext(), LoginRegisterActivity.class));

    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
            if (currentUser != null) {
                Intent intent = new Intent(WelcomeActivity.this, DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("userId", String.valueOf(userId));
                startActivity(intent);
                finish();
            }
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }
}