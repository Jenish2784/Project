package com.example.dotechuser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.dotechuser.Model.ContactUs;
import com.example.dotechuser.databinding.ActivityConditionsBinding;
import com.example.dotechuser.databinding.ActivityContactUsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ContactUsActivity extends AppCompatActivity {

    ActivityContactUsBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;

    String userId="";
    String strProfileImage="";
    String strName = "";
    String strEmail = "";
    String strMobile = "";
    String strSubject = "";
    String strMessage = "";
    String currentDate = "";
    String currentTime = "";
    String status = "0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContactUsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();


        Intent intent = getIntent();
        if (intent.hasExtra("email")) {
            strEmail = intent.getStringExtra("email");
        }
        if (intent.hasExtra("name")) {
            strName = intent.getStringExtra("name");
        }
        if (intent.hasExtra("mobile")) {
            strMobile = intent.getStringExtra("mobile");
        }
        if (intent.hasExtra("profileImage")) {
            strProfileImage = intent.getStringExtra("profileImage");
        }

        userId = auth.getUid();

        binding.edtName.setText(strName);
        binding.edtEmail.setText(strEmail);

        currentDate = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

        currentTime = new SimpleDateFormat("KK : mm aaa").format(Calendar.getInstance().getTime());

        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strSubject  = binding.edtSub.getText().toString();
                strMessage=binding.edtMsg.getText().toString();
                if (binding.edtSub.getText().toString().equals("") &&
                        binding.edtMsg.getText().toString().equals("")) {
                    Toast.makeText(ContactUsActivity.this, "Please fill up all details", Toast.LENGTH_SHORT).show();
                } else {
                    ContactUs contactUs = new ContactUs(userId,strProfileImage,strName, strEmail, strMobile, strSubject, strMessage,currentDate,currentTime,status);
                    database.getReference().child("contactUs").push().setValue(contactUs);

                    Toast.makeText(ContactUsActivity.this, "msg sended...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}