package com.example.grootan.repositories;

import android.app.Activity;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.grootan.DashboardActivity;
import com.example.grootan.LoginRegisterActivity;
import com.example.grootan.adapter.ScannedDataAdapter;
import com.example.grootan.models.ScannedData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ScannedDataRepository {
    private static final String TAG = "Scanned Repository";
    private ArrayList<ScannedData> scannedDataArrayList = new ArrayList<>();
    private MutableLiveData<List<ScannedData>> mutableLiveData = new MutableLiveData<>();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private Application application;

    public ScannedDataRepository(Application application) {
        this.application = application;
    }

    public MutableLiveData<List<ScannedData>> getMutableLiveData() {
        try {
            scannedDataArrayList = new ArrayList<>();
            firebaseFirestore.collection(auth.getUid()).get().
                    addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                    ScannedData data = new ScannedData(documentSnapshot.getString("id"),
                                            documentSnapshot.getString("uploadedDate"),
                                            documentSnapshot.getString("uploadedTime"),
                                            documentSnapshot.getString("uploadedData"));
                                    scannedDataArrayList.add(data);
                                }
                            } else {
                                Log.e(TAG, "" + task.getException().toString());
                            }
                            mutableLiveData.setValue(scannedDataArrayList);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
        return mutableLiveData;
    }

    public void signOut() {
        try {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(application.getApplicationContext());
            builder1.setMessage("Are you sure.. do you want to logout?");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            auth.signOut();
                            Intent intent = new Intent(application.getApplicationContext(), LoginRegisterActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            application.startActivity(intent);
                        }
                    });

            builder1.setNegativeButton(
                    "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
//        userLoggedMutableLiveData.postValue(true);
    }
}
