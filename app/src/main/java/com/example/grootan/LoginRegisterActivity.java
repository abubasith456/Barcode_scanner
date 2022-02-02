package com.example.grootan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grootan.databinding.ActivityLoginBinding;
import com.example.grootan.utils.EmailValidator;
import com.example.grootan.viewModel.LoginRegisterViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;


public class LoginRegisterActivity extends AppCompatActivity {

    EditText editTextEmailInput, editTextPasswordInput, editTextSignUpUserName, editTextSignUpEmail, editTextSignUpPassword;
    TextView mTextViewErrorEmail, mTextViewErrorPassword, mTextViewUserNameError, mTextViewSignupEmailError, mTextViewSignupMobileNumberError, mTextViewPasswordError;
    LinearLayout layoutForgotPassword, layoutSignIn, layoutRegister, linearLayoutClose, layoutSignUp;
    FrameLayout frameLayoutLogin, frameLayoutRegister;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    EmailValidator emailValidator;
    Spinner spinnerLoggedUser;
    ArrayList<String> spinnerArrayList;
    ArrayAdapter<String> spinnerAdapter;

    private ActivityLoginBinding activityLoginBinding;
    private LoginRegisterViewModel loginRegisterViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//        loginRegisterViewModel=new LoginRegisterViewModel(this,activityLoginBinding);
        loginRegisterViewModel = ViewModelProviders.of(this).get(LoginRegisterViewModel.class);
        activityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        activityLoginBinding.setLifecycleOwner(this);
        activityLoginBinding.setLoginregisterViewModel(loginRegisterViewModel);
        loginRegisterViewModel.getUserLoginData().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser!=null){
                    Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                    startActivity(intent);
                }else {

                }

            }
        });

//        editTextEmailInput = findViewById(R.id.editTextEmailInput);
//        editTextPasswordInput = findViewById(R.id.editTextPasswordInput);
        layoutForgotPassword = findViewById(R.id.layoutForgotPassword);
        layoutSignIn = findViewById(R.id.layoutSignIn);
        layoutRegister = findViewById(R.id.layoutRegister);
        mTextViewErrorEmail = findViewById(R.id.mTextViewErrorMobileNUmber);
        mTextViewErrorPassword = findViewById(R.id.mTextViewErrorPassword);
        linearLayoutClose = findViewById(R.id.linearLayoutClose);
        frameLayoutLogin = findViewById(R.id.frameLayoutLogin);
        frameLayoutRegister = findViewById(R.id.frameLayoutRegister);
        spinnerLoggedUser = findViewById(R.id.spinnerLoggedUser);

        //Register
        editTextSignUpUserName = findViewById(R.id.editTextSignUpUserName);
        editTextSignUpEmail = findViewById(R.id.editTextSignUpEmail);
        mTextViewUserNameError = findViewById(R.id.mTextViewUserNameError);
        mTextViewSignupEmailError = findViewById(R.id.mTextViewSignupEmailError);
        mTextViewPasswordError = findViewById(R.id.mTextViewPasswordError);
        editTextSignUpPassword = findViewById(R.id.editTextSignUpPassword);
        layoutSignUp = findViewById(R.id.layoutSignUp);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        emailValidator = new EmailValidator();
        spinnerArrayList = new ArrayList<>();
        spinnerAdapter = new ArrayAdapter<>(LoginRegisterActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerArrayList);
        spinnerLoggedUser.setAdapter(spinnerAdapter);
        loadUsers();

//        layoutSignIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    hideKeyboard(view);
//                    firebaseLogin();
//                } catch (Exception exception) {
//                    Log.e("Error ==> ", "" + exception);
//                }
//            }
//        });

        layoutRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    frameLayoutLogin.setVisibility(View.GONE);
                    frameLayoutRegister.setVisibility(View.VISIBLE);
                    linearLayoutClose.setVisibility(View.VISIBLE);
                    invalidateErrorMessagesLogin();
                } catch (Exception exception) {
                    Log.e("Error ==> ", "" + exception);
                }
            }
        });

        linearLayoutClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    frameLayoutLogin.setVisibility(View.VISIBLE);
                    frameLayoutRegister.setVisibility(View.GONE);
                    linearLayoutClose.setVisibility(View.GONE);
                    invalidateErrorMessagesRegister();

                } catch (Exception exception) {
                    Log.e("Error ==> ", "" + exception);
                }
            }
        });

//        layoutSignUp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    hideKeyboard(view);
//                    firebaseRegister();
//                } catch (Exception exception) {
//                    Log.e("Error ==> ", "" + exception);
//                }
//            }
//        });

        spinnerLoggedUser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (adapterView.getItemAtPosition(i).toString().equals("Select user")) {
//                    editTextEmailInput.setText("");
                } else {
                    String item = adapterView.getItemAtPosition(i).toString();
                    editTextEmailInput.setText(item);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void loadUsers() {
        try {
            firebaseFirestore.collection("Users").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            spinnerArrayList.add("Select user");
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                    String data = documentSnapshot.getString("userEmailAddress");
                                    spinnerArrayList.add(data);
                                }
                                spinnerAdapter.notifyDataSetChanged();
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

    public void firebaseLogin() {
        try {
            if (loginValidate(editTextEmailInput.getText().toString(), editTextPasswordInput.getText().toString())) {
                firebaseAuth.signInWithEmailAndPassword(editTextEmailInput.getText().toString(), editTextPasswordInput.getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Login Successfully", Toast.LENGTH_SHORT).show();
                                    String userId = task.getResult().getUser().getUid();
                                    Intent intent = new Intent(LoginRegisterActivity.this, DashboardActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra("userId", String.valueOf(userId));
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    public void firebaseRegister() {
        try {
            if (registerValidate(editTextSignUpUserName.getText().toString(), editTextSignUpEmail.getText().toString(), editTextSignUpPassword.getText().toString())) {
                firebaseAuth.createUserWithEmailAndPassword(editTextSignUpEmail.getText().toString(), editTextSignUpPassword.getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Register Successfully", Toast.LENGTH_SHORT).show();
                                    String userId = firebaseAuth.getCurrentUser().getUid();
                                    storeUserInfo(userId, editTextSignUpUserName.getText().toString(), editTextSignUpEmail.getText().toString());
                                    String indicator = "new";
                                    Intent intent = new Intent(getApplicationContext(), ScanBarCodeActivity.class);
                                    intent.putExtra("indicator", indicator);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }


    private void storeUserInfo(String userId, String userName, String email) {
        try {
            HashMap<String, Object> addFieldInfo = new HashMap<>();
            addFieldInfo.put("userId", "" + userId);
            addFieldInfo.put("userName", "" + userName);
            addFieldInfo.put("userEmailAddress", "" + email);
            DocumentReference databaseReference = firebaseFirestore.collection("Users").document(userId);
            databaseReference.set(addFieldInfo);
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    private void invalidateErrorMessagesLogin() {
        try {
            mTextViewErrorEmail.setText("");
            mTextViewErrorPassword.setText("");
            mTextViewErrorEmail.setVisibility(View.GONE);
            mTextViewErrorPassword.setVisibility(View.GONE);
            editTextEmailInput.setText("");
            editTextPasswordInput.setText("");
            editTextEmailInput.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_gray));
            editTextPasswordInput.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_gray));
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    private void invalidateErrorMessagesRegister() {
        try {
            mTextViewUserNameError.setText("");
            mTextViewSignupMobileNumberError.setText("");
            mTextViewSignupEmailError.setText("");
            mTextViewPasswordError.setText("");
            mTextViewUserNameError.setVisibility(View.GONE);
            mTextViewSignupMobileNumberError.setVisibility(View.GONE);
            mTextViewSignupEmailError.setVisibility(View.GONE);
            mTextViewPasswordError.setVisibility(View.GONE);
            editTextSignUpUserName.setText("");
//            editTextSignUpMobileNumber.setText("");
            editTextSignUpEmail.setText("");
            editTextSignUpPassword.setText("");
            editTextSignUpUserName.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_gray));
//            editTextSignUpMobileNumber.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_gray));
            editTextSignUpEmail.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_gray));
            editTextSignUpPassword.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_gray));
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    private void hideKeyboard(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
    }

    public boolean loginValidate(String strEmail, String password) {
        boolean valid = true;
        try {
            if (strEmail.isEmpty()) {
                editTextEmailInput.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_error));
                mTextViewErrorEmail.setVisibility(View.VISIBLE);
                mTextViewErrorEmail.setText(getResources().getString(R.string.email_error));
                valid = false;
            } else if (!emailValidator.validate(strEmail)) {
                mTextViewErrorEmail.setVisibility(View.VISIBLE);
                editTextEmailInput.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_error));
                mTextViewErrorEmail.setText(getResources().getString(R.string.email_invalid));
                valid = false;
            } else {
                editTextEmailInput.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_gray));
                mTextViewErrorEmail.setVisibility(View.GONE);
            }
            if (password.isEmpty()) {
                editTextPasswordInput.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_error));
                mTextViewErrorPassword.setVisibility(View.VISIBLE);
                mTextViewErrorPassword.setText(getResources().getString(R.string.password_error));
                valid = false;
            } else {
                editTextPasswordInput.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_gray));
                mTextViewErrorPassword.setVisibility(View.GONE);
            }
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
        return valid;
    }

    public boolean registerValidate(String strFirstName, String strEmail, String strPassword) {
        boolean valid = true;
        try {
            if (strFirstName.isEmpty()) {
                mTextViewUserNameError.setVisibility(View.VISIBLE);
                editTextSignUpUserName.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_error));
                mTextViewUserNameError.setText(getResources().getString(R.string.error_username));
                valid = false;
            } else {
                mTextViewUserNameError.setVisibility(View.GONE);
                editTextSignUpUserName.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_gray));
            }
//            if (strMobible.isEmpty()) {
//                mTextViewSignupMobileNumberError.setVisibility(View.VISIBLE);
//                editTextSignUpMobileNumber.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_error));
//                mTextViewSignupMobileNumberError.setText(getResources().getString(R.string.error_number));
//                valid = false;
//            } else {
//                mTextViewSignupMobileNumberError.setVisibility(View.GONE);
//                editTextSignUpMobileNumber.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_gray));
//            }
            if (strEmail.isEmpty()) {
                mTextViewSignupEmailError.setVisibility(View.VISIBLE);
                editTextSignUpEmail.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_error));
                mTextViewSignupEmailError.setText(getResources().getString(R.string.email_error));
                valid = false;
            } else {
                mTextViewSignupEmailError.setVisibility(View.GONE);
                editTextSignUpEmail.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_gray));
            }
            if (!emailValidator.validate(strEmail)) {
                mTextViewSignupEmailError.setVisibility(View.VISIBLE);
                editTextSignUpEmail.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_error));
                mTextViewSignupEmailError.setText(getResources().getString(R.string.email_invalid));
                valid = false;
            } else {
                mTextViewSignupEmailError.setVisibility(View.GONE);
                editTextSignUpEmail.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_gray));
            }
            if (strPassword.isEmpty()) {
                mTextViewPasswordError.setVisibility(View.VISIBLE);
                editTextSignUpPassword.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_error));
                mTextViewPasswordError.setText(getResources().getString(R.string.password_error));
                valid = false;
            } else {
                mTextViewPasswordError.setVisibility(View.GONE);
                editTextSignUpPassword.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_gray));
            }
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
        return valid;
    }

}