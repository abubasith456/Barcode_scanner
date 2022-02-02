package com.example.grootan.repositories;

import android.app.Activity;
import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.grootan.DashboardActivity;
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
}
