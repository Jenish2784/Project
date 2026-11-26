package com.example.dotechuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.dotechuser.databinding.ActivityForgeotPasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgeotPasswordActivity extends AppCompatActivity {

    ActivityForgeotPasswordBinding binding;
    FirebaseAuth auth;
    String strEmail = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgeotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strEmail = binding.edtEmail.getText().toString();
                if (strEmail.equals("")) {
                    Toast.makeText(ForgeotPasswordActivity.this, "Enter email id", Toast.LENGTH_SHORT).show();
                } else {
                    auth.sendPasswordResetEmail(strEmail)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ForgeotPasswordActivity.this, "Check your email", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(ForgeotPasswordActivity.this, SignInActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(ForgeotPasswordActivity.this, "Error : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });


       /* binding.backToSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ForgeotPasswordActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });*/
    }
}