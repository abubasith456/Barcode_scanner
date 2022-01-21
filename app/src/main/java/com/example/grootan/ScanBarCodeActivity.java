package com.example.grootan;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ScanBarCodeActivity extends AppCompatActivity {

    PreviewView previewView;

    ImageAnalysis imageAnalysis;
    private ListenableFuture cameraProviderFuture;
    private ExecutorService cameraExecutor;
    private MyImageAnalyzer analyzer;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    FrameLayout sideMenu;
    LinearLayout linear_layout_back, sideMenuClose;
    ImageView appCompatImageView;
    TextView textViewLogout;
    String timeStamp, indicate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_bar_code);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        timeStamp = "" + System.currentTimeMillis();
        previewView = findViewById(R.id.previewView);
        linear_layout_back = findViewById(R.id.linear_layout_back);
        sideMenu = findViewById(R.id.sideMenu);
        sideMenuClose = findViewById(R.id.sideMenuClose);
        textViewLogout = findViewById(R.id.textViewLogout);
        appCompatImageView = findViewById(R.id.imageviewBack);
        indicate = getIntent().getStringExtra("indicator");

        try {
            if (indicate.equals("new")) {
                appCompatImageView.setImageResource(R.drawable.icon_menu);
            } else {
                appCompatImageView.setImageResource(R.drawable.icon_back);
            }
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }

        this.getWindow().setFlags(1024, 1024);
        cameraExecutor = Executors.newSingleThreadExecutor();
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        analyzer = new MyImageAnalyzer(getSupportFragmentManager());
        cameraProviderFuture.addListener(() -> {
            try {
                if (ActivityCompat.checkSelfPermission(ScanBarCodeActivity.this, Manifest.permission.CAMERA) != (PackageManager.PERMISSION_GRANTED)) {
                    ActivityCompat.requestPermissions(ScanBarCodeActivity.this, new String[]{Manifest.permission.CAMERA}, 101);
                }
                ProcessCameraProvider processCameraProvider = (ProcessCameraProvider) cameraProviderFuture.get();
                bindPreview(processCameraProvider);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));

        linear_layout_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    try {
                        if (indicate.equals("new")) {
                            sideMenu.setVisibility(View.VISIBLE);
                        } else {
                            Intent intent = new Intent(ScanBarCodeActivity.this, DashboardActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_left,
                                    R.anim.slide_out_right);
                            finish();
                        }
                    } catch (Exception exception) {
                        Log.e("Error ==> ", "" + exception);
                    }

//                    sideMenu.setVisibility(View.VISIBLE);
                } catch (Exception exception) {
                    Log.e("Error ==> ", "" + exception);
                }
            }
        });

        sideMenuClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    sideMenu.setVisibility(View.GONE);
                } catch (Exception exception) {
                    Log.e("Error ==> ", "" + exception);
                }
            }
        });

        textViewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    sideMenu.setVisibility(View.GONE);
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(ScanBarCodeActivity.this);
                    builder1.setMessage("Are you sure.. do you want to logout?");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    FirebaseAuth.getInstance().signOut();
                                    Intent intent = new Intent(ScanBarCodeActivity.this, LoginActivity.class);
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

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101 && grantResults.length > 0) {
            ProcessCameraProvider processCameraProvider = null;
            try {
                processCameraProvider = (ProcessCameraProvider) cameraProviderFuture.get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
            assert processCameraProvider != null;
            bindPreview(processCameraProvider);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void bindPreview(ProcessCameraProvider processCameraProvider) {

        Preview preview = new Preview.Builder().build();
        CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());
        ImageCapture imageCapture = new ImageCapture.Builder().build();
        imageAnalysis = new ImageAnalysis.Builder().setTargetResolution(new Size(1208, 720))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build();
        imageAnalysis.setAnalyzer(cameraExecutor, analyzer);
        processCameraProvider.unbindAll();
        processCameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture, imageAnalysis);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ScanBarCodeActivity.this, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);
        finish();
    }

    public class MyImageAnalyzer implements ImageAnalysis.Analyzer {

        public MyImageAnalyzer(androidx.fragment.app.FragmentManager supportFragmentManager) {
        }

        @Override
        public void analyze(@NonNull ImageProxy image) {
            scanBarcode(image);
        }

        private void scanBarcode(ImageProxy image) {

            @SuppressLint("UnsafeOptInUsageError") Image image1 = image.getImage();
            assert image1 != null;
            InputImage inputImage = InputImage.fromMediaImage(image1, image.getImageInfo().getRotationDegrees());
            BarcodeScannerOptions options =
                    new BarcodeScannerOptions.Builder()
                            .setBarcodeFormats(
                                    Barcode.FORMAT_QR_CODE,
                                    Barcode.FORMAT_AZTEC)
                            .build();
            BarcodeScanner scanner = BarcodeScanning.getClient();

            Task<List<Barcode>> result = scanner.process(inputImage)
                    .addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                        @Override
                        public void onSuccess(List<Barcode> barcodes) {
                            readerBarcodeData(barcodes);
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Task failed with an exception
                    })
                    .addOnCompleteListener(task -> image.close());
        }

        private void readerBarcodeData(List<Barcode> barCodes) {
            if (barCodes.size() == 0)
                return;
            for (Barcode barcode : barCodes) {
                imageAnalysis.setAnalyzer(null, null);
                imageAnalysis = null;
                String rawValue = barcode.getRawValue();
                Toast.makeText(getApplicationContext(), rawValue, Toast.LENGTH_SHORT).show();
                uploadDataToFirebase(rawValue);
                Intent backIntent = new Intent(getApplicationContext(), DashboardActivity.class);
                backIntent.putExtra("QR_value", rawValue);
                startActivity(backIntent);
                overridePendingTransition(R.anim.slide_in_left,
                        R.anim.slide_out_right);
                finish();
//                Intent intent = new Intent();
//                intent.putExtra("BarCode Value", rawValue);
//                setResult(2, intent);
//                finish();
            }
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
}
