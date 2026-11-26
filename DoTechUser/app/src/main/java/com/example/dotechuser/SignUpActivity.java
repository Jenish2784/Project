package com.example.dotechuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.dotechuser.Model.User;
import com.example.dotechuser.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    String strName = "", strEmail = "", strMobile = "", strPass = "", strConPass = "", strStatus = "1";
    String profileImage = "", gender = "", bloodGroup = "", birthDate = "", height = "", weight = "";

    ProgressDialog progressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Account Creating");
        progressDialog.setMessage("Please wait.");

        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strName = binding.edtName.getText().toString();
                strEmail = binding.edtEmail.getText().toString();
                strMobile = binding.edtMobileNo.getText().toString();
                strPass = binding.edtPassword.getText().toString();
                strConPass = binding.edtConPassword.getText().toString();

                if (strName.isEmpty()) {
                    binding.edtName.setError("Enter Your Name");
                } else if (strEmail.isEmpty()) {
                    binding.edtEmail.setError("Enter Your Email");
                } else if (strMobile.isEmpty()) {
                    binding.edtMobileNo.setError("Enter Your Mobile number");
                } else if (strPass.isEmpty()) {
                    binding.edtPassword.setError("Enter Your Password");
                } else if (strConPass.isEmpty()) {
                    binding.edtConPassword.setError("Enter your Password");
                } else {

                    if (strPass.equals(strConPass)) {
                        SharedPreferences.Editor pref = getSharedPreferences("userDetails", MODE_PRIVATE).edit();
                        pref.putString("email", strEmail);
                        pref.putString("name", strName);
                        pref.putString("mobile", strMobile);
                        pref.putString("password", strPass);
                        pref.putString("status", strStatus);
                        pref.apply();
                        progressDialog.show();

                        auth.createUserWithEmailAndPassword(strEmail, strPass)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        progressDialog.dismiss();
                                        if (task.isSuccessful()) {
                                            User user = new User(strEmail, strName, strMobile, strPass, strStatus, profileImage, gender, bloodGroup, birthDate, height, weight);
                                            String id = task.getResult().getUser().getUid();
                                            database.getReference().child("Users").child(id).setValue(user);
                                            auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        binding.edtName.setText("");
                                                        binding.edtEmail.setText("");
                                                        binding.edtMobileNo.setText("");
                                                        binding.edtPassword.setText("");
                                                        binding.edtConPassword.setText("");

                                                        Toast.makeText(SignUpActivity.this, "User signup successfully. \n Please verify your email id.", Toast.LENGTH_SHORT).show();
                                                        /*Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                                        startActivity(intent);*/
                                                    } else {
                                                        Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        } else {
                                            String errMsg = task.getException() != null ? task.getException().getMessage() : "Registration failed";
                                            Toast.makeText(SignUpActivity.this, errMsg, Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(SignUpActivity.this, "Enter same password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        binding.txtSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
        startActivity(intent);
    }
}