package com.example.grootan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomeActivity extends AppIntro {

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_welcome);
        firebaseAuth=FirebaseAuth.getInstance();
        addSlide(AppIntroFragment.newInstance("Welcome", "Welcome you all", R.drawable.welcome, ContextCompat.getColor(getApplicationContext(), R.color.colorAccent)));
        addSlide(AppIntroFragment.newInstance("Second page", "This is the Second Page Description", R.drawable.welcome2, ContextCompat.getColor(getApplicationContext(), R.color.colorAccent)));
        addSlide(AppIntroFragment.newInstance("Third page", "This is the Third Page Description", R.drawable.come, ContextCompat.getColor(getApplicationContext(), R.color.colorAccent)));
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));

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