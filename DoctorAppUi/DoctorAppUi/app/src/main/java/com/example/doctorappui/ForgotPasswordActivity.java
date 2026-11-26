package com.example.doctorappui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.doctorappui.databinding.ActivityForgotPasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public
class ForgotPasswordActivity extends AppCompatActivity {

    ActivityForgotPasswordBinding binding;
    String strEmail = "";
    FirebaseAuth auth;

    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.home)));
        getSupportActionBar().setTitle("Forgot Password");

        binding.btnForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                strEmail = binding.edtEmail.getText().toString();
                if (strEmail.equals("")) {
                    Toast.makeText(ForgotPasswordActivity.this, "Enter email id", Toast.LENGTH_SHORT).show();
                } else {
                    auth.sendPasswordResetEmail(strEmail)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public
                                void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(ForgotPasswordActivity.this, "Check your email", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(ForgotPasswordActivity.this,SignInActivity.class);
                                        startActivity(intent);
                                    }else {
                                        Toast.makeText(ForgotPasswordActivity.this, "Error : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        binding.tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                Intent intent = new Intent(ForgotPasswordActivity.this,SignInActivity.class);
                startActivity(intent);
            }
        });

    }
}