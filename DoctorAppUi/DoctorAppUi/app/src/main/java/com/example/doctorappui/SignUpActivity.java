package com.example.doctorappui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.doctorappui.databinding.ActivitySignUpActivityBinding;

public
class SignUpActivity extends AppCompatActivity {

    ActivitySignUpActivityBinding binding;

    String strEmail="",strMobile="",strPass="",strConfPass="";

    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        binding.txtSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this,SignInActivity.class);
                startActivity(intent);
            }
        });

        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                strEmail = binding.edtEmail.getText().toString();
                strMobile = binding.edtMobile.getText().toString();
                strPass = binding.edtPass.getText().toString();
                strConfPass = binding.edtConfPass.getText().toString();

                if (strEmail.equals("")){
                    Toast.makeText(SignUpActivity.this, "Please Enter Email Id", Toast.LENGTH_SHORT).show();
                } else if(strMobile.equals("")){
                    Toast.makeText(SignUpActivity.this, "Please Enter Mobile number", Toast.LENGTH_SHORT).show();
                } else if (strPass.equals("")){
                    Toast.makeText(SignUpActivity.this, "Please Enter password", Toast.LENGTH_SHORT).show();
                } else if (strConfPass.equals("")){
                    Toast.makeText(SignUpActivity.this, "Please Enter password", Toast.LENGTH_SHORT).show();
                }else {
                    if (strPass.equals(strConfPass)){
                        if (strPass.length() > 5){
                            Intent intent = new Intent(SignUpActivity.this,SignUpVerifyActivity.class);
                            intent.putExtra("email",strEmail);
                            intent.putExtra("mobile",strMobile);
                            intent.putExtra("pass",strPass);
                            startActivity(intent);
                        }else {
                            Toast.makeText(SignUpActivity.this, "Password must be greater than 6 character", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(SignUpActivity.this, "Password not match", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}