package com.example.grootan.repositories;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.PrivateKey;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class BarCodeRepository {

    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    private Application application;
    private String timeStamp;

    public BarCodeRepository() {

    }

    public BarCodeRepository(Application application) {
        this.application = application;
        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        timeStamp = "" + System.currentTimeMillis();
    }


    public void uploadDataToFirebase(String data) {
        try {

            String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

            HashMap<String, Object> addFieldInfo = new HashMap<>();
            addFieldInfo.put("id", "" + timeStamp);
            addFieldInfo.put("uploadedDate", "" + currentDate);
            addFieldInfo.put("uploadedTime", "" + currentTime);
            addFieldInfo.put("uploadedData", "" + data);
            DocumentReference databaseReference = firebaseFirestore.collection(auth.getUid()).document(timeStamp);

            databaseReference.set(addFieldInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(application.getApplicationContext(), "Data uploaded", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(application.getApplicationContext(), "" + task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }
}
