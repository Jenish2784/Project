package com.example.doctorappui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.doctorappui.databinding.ActivityContactUsBinding;
import com.example.doctorappui.modal.ContactUs;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public
class ContactUsActivity extends AppCompatActivity {

    ActivityContactUsBinding binding;
    String strName="",strEmail="",strMobile="",strProfileImage="";
    String strSubject="",strMessage="",currentDate;
    FirebaseAuth auth;
    FirebaseDatabase database;
    Calendar calendar;

    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContactUsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setTitle("Contact Us");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.home)));

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        calendar = Calendar.getInstance();

        SharedPreferences preferences = this.getSharedPreferences("userData",MODE_PRIVATE);
        strName = preferences.getString("name","");
        strEmail = preferences.getString("email","");
        strMobile = preferences.getString("mobile","");
        strProfileImage = preferences.getString("profileImage","");

        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = simpleDate.format(calendar.getTime()).toString();

        binding.edtName.setText(strName);
        binding.edtEmail.setText(strEmail);
        binding.edtMobile.setText(strMobile);

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                strSubject = binding.edtSubject.getText().toString();
                strMessage = binding.edtMessage.getText().toString();
                if (binding.edtName.getText().toString().equals("")){
                    Toast.makeText(ContactUsActivity.this, "Please enter name", Toast.LENGTH_SHORT).show();
                }else if (binding.edtEmail.getText().toString().equals("")){
                    Toast.makeText(ContactUsActivity.this, "Please enter email", Toast.LENGTH_SHORT).show();
                }else if (binding.edtMobile.getText().toString().equals("")){
                    Toast.makeText(ContactUsActivity.this, "Please enter mobile number", Toast.LENGTH_SHORT).show();
                }else if (strSubject.equals("")){
                    Toast.makeText(ContactUsActivity.this, "Please enter subject", Toast.LENGTH_SHORT).show();
                }else if (strMessage.equals("")){
                    Toast.makeText(ContactUsActivity.this, "Please enter message", Toast.LENGTH_SHORT).show();
                }else {
                    binding.edtSubject.setText("");
                    binding.edtMessage.setText("");
                    long time = new Date().getTime();
                    ContactUs contactUs = new ContactUs(auth.getUid(),binding.edtName.getText().toString(),binding.edtEmail.getText().toString(),binding.edtMobile.getText().toString(),strSubject,strMessage,strProfileImage,time,currentDate,"0");
                    database.getReference().child("contactUs").push().setValue(contactUs);
                    Toast.makeText(ContactUsActivity.this, "Request submit successfully!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}