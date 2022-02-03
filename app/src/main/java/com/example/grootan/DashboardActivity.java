package com.example.grootan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grootan.adapter.ScannedDataAdapter;
import com.example.grootan.databinding.ActivityDashboardBinding;
import com.example.grootan.models.ScannedData;
import com.example.grootan.viewModel.DashboardViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class DashboardActivity extends AppCompatActivity {

    FrameLayout sideMenu;
    LinearLayout linear_layout_menu, sideMenuClose;
    TextView textViewUserName, textViewLogout;
    RecyclerView recyclerView;
    ArrayList<ScannedData> scannedDataArrayList;
    FloatingActionButton floatingButtonOpenCamera;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    String timeStamp, QR_value;
    ScannedDataAdapter scannedDataAdapter;
    DashboardViewModel dashboardViewModel;
    ActivityDashboardBinding activityDashboardBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_dashboard);
        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        activityDashboardBinding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);
        activityDashboardBinding.setLifecycleOwner(this);
        activityDashboardBinding.setDashboardViewModel(dashboardViewModel);
        activityDashboardBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        activityDashboardBinding.recyclerView.setHasFixedSize(true);
        scannedDataAdapter = new ScannedDataAdapter();
        activityDashboardBinding.recyclerView.setAdapter(scannedDataAdapter);
        dashboardViewModel.passBinding(activityDashboardBinding);
        getScannedData();

//        linear_layout_menu = findViewById(R.id.linear_layout_menu);
        sideMenu = findViewById(R.id.sideMenu);
        sideMenuClose = findViewById(R.id.sideMenuClose);
        textViewUserName = findViewById(R.id.textViewUserName);
        textViewLogout = findViewById(R.id.textViewLogout);
        recyclerView = findViewById(R.id.recyclerView);
        floatingButtonOpenCamera = findViewById(R.id.floatingButtonOpenCamera);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        timeStamp = "" + System.currentTimeMillis();
//        QR_value = getIntent().getStringExtra("QR_value");
        loadUserInfo();
//        linear_layout_menu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    sideMenu.setVisibility(View.VISIBLE);
//                } catch (Exception exception) {
//                    Log.e("Error ==> ", "" + exception);
//                }
//            }
//        });
//
//        sideMenuClose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    sideMenu.setVisibility(View.GONE);
//                } catch (Exception exception) {
//                    Log.e("Error ==> ", "" + exception);
//                }
//            }
//        });

        textViewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    sideMenu.setVisibility(View.GONE);
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(DashboardActivity.this);
                    builder1.setMessage("Are you sure.. do you want to logout?");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    FirebaseAuth.getInstance().signOut();
                                    Intent intent = new Intent(DashboardActivity.this, LoginRegisterActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
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
            }
        });

//        floatingButtonOpenCamera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//
//                    Intent intent = new Intent(DashboardActivity.this, ScanBarCodeActivity.class);
//                    intent.putExtra("indicator", "exist");
//                    startActivity(intent);
////                    finish();
////                    Intent intent = new Intent(getApplicationContext(), ScanBarCodeActivity.class);
////                    startActivityForResult(intent, 2);
//                } catch (Exception exception) {
//                    Log.e("Error ==> ", "" + exception);
//                }
//            }
//        });
    }

    private void getScannedData() {
        dashboardViewModel.getScannedData().observe(this, new Observer<List<ScannedData>>() {
            private Object ArrayList;

            @Override
            public void onChanged(List<ScannedData> scannedData) {
                scannedDataAdapter.getScannedData((ArrayList<ScannedData>) scannedData);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) ;
        {
            String barCode_value = data.getStringExtra("BarCode Value");
            Toast.makeText(getApplicationContext(), "" + barCode_value, Toast.LENGTH_SHORT).show();
            uploadDataToFirebase(barCode_value);
        }
    }

    private void loadUserInfo() {
        try {
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            DocumentReference documentReference = firebaseFirestore.collection("Users").document(firebaseAuth.getUid());
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        if (task.getResult().exists()) {
                            String nameResult = task.getResult().getString("userName");
                            String mobileNumberResult = task.getResult().getString("userMobileNumber");
                            String emailAddressResult = task.getResult().getString("userEmailAddress");
                            textViewUserName.setText("Hi... " + nameResult);
                        }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    private void uploadDataToFirebase(String data) {
        try {

            String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

            HashMap<String, Object> addFieldInfo = new HashMap<>();
            addFieldInfo.put("id", "" + timeStamp);
            addFieldInfo.put("uploadedDate", "" + currentDate);
            addFieldInfo.put("uploadedTime", "" + currentTime);
            addFieldInfo.put("uploadedData", "" + data);
            DocumentReference databaseReference = firebaseFirestore.collection(firebaseAuth.getUid()).document(timeStamp);

            databaseReference.set(addFieldInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Data uploaded", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            loadData();
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    private void loadData() {
        try {

            scannedDataArrayList = new ArrayList<>();
            firebaseFirestore.collection(firebaseAuth.getUid()).get().
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
//                                scannedDataAdapter = new ScannedDataAdapter(scannedDataArrayList, getApplicationContext());
//                                LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
//                                recyclerView.setLayoutManager(mLayoutManager);
//                                recyclerView.setAdapter(scannedDataAdapter);
//                                scannedDataAdapter.notifyDataSetChanged();
                            }


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

//    public static class ScannedDataAdapter extends RecyclerView.Adapter<ScannedDataViewHolder> {
//        private ArrayList<ScannedData> scannedDataArrayList;
//        private Context context;
//
//        public ScannedDataAdapter(ArrayList<ScannedData> scannedDataArrayList, Context context) {
//            this.scannedDataArrayList = scannedDataArrayList;
//            context = context;
//        }
//
//        public void setBookMarksList(ArrayList<ScannedData> scannedDataArrayList) {
//            this.scannedDataArrayList = scannedDataArrayList;
//            notifyDataSetChanged();
//        }
//
//        @NonNull
//        @Override
//        public ScannedDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            return new ScannedDataViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_view_data, parent, false));
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull ScannedDataViewHolder holder, int position) {
//            holder.bind(scannedDataArrayList.get(position), context);
//        }
//
//        @Override
//        public int getItemCount() {
//            return scannedDataArrayList.size();
//        }
//    }

//    public static class ScannedDataViewHolder extends RecyclerView.ViewHolder {
//
//        TextView textViewScannedData, textViewDate, textViewTime;
//
//        public ScannedDataViewHolder(View itemView) {
//            super(itemView);
//            try {
//
//                textViewScannedData = itemView.findViewById(R.id.textViewScannedData);
//                textViewDate = itemView.findViewById(R.id.textViewDate);
//                textViewTime = itemView.findViewById(R.id.textViewTime);
//
//            } catch (Exception exception) {
//                Log.e("Error ==> ", "" + exception);
//            }
//        }
//
//        public void bind(final ScannedData response, final Context context) {
//            try {
//                if (response != null) {
//
//                    textViewScannedData.setText(response.getScanned_data());
//                    textViewDate.setText(response.getDate());
//                    textViewTime.setText(response.getTime());
//
//                }
//
//            } catch (Exception exception) {
//                Log.e("Error ==> ", "" + exception);
//            }
//        }
//
//    }

}