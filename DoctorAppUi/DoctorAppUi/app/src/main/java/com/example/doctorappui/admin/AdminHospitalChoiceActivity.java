package com.example.doctorappui.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import com.example.doctorappui.R;
import com.example.doctorappui.databinding.ActivityAdminHospitalChoiceBinding;

public
class AdminHospitalChoiceActivity extends AppCompatActivity {

    ActivityAdminHospitalChoiceBinding binding;
    String strEmail="",strName="",strMobile="",strImage="",strAddress="",strUId="",strStatus="";

    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminHospitalChoiceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        Intent intent = getIntent();
        if (intent.hasExtra("email")){
            strEmail = intent.getStringExtra("email");
        }
        if (intent.hasExtra("name")){
            strName = intent.getStringExtra("name");
            getSupportActionBar().setTitle(strName);
        }
        if (intent.hasExtra("mobile")){
            strMobile = intent.getStringExtra("mobile");
        }
        if (intent.hasExtra("image")){
            strImage = intent.getStringExtra("image");
        }
        if (intent.hasExtra("address")){
            strAddress = intent.getStringExtra("address");
        }
        if (intent.hasExtra("uId")){
            strUId = intent.getStringExtra("uId");
        }
        if (intent.hasExtra("status")){
            strStatus = intent.getStringExtra("status");
        }

        binding.departmentCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                Intent intent = new Intent(AdminHospitalChoiceActivity.this,AdminHospitalDepartmentActivity.class);
                intent.putExtra("email",strEmail);
                intent.putExtra("name",strName);
                intent.putExtra("mobile",strMobile);
                intent.putExtra("image",strImage);
                intent.putExtra("address",strAddress);
                intent.putExtra("uId",strUId);
                startActivity(intent);
            }
        });

        binding.profileCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                Intent intent = new Intent(AdminHospitalChoiceActivity.this,HospitalDataActivity.class);
                intent.putExtra("email",strEmail);
                intent.putExtra("name",strName);
                intent.putExtra("mobile",strMobile);
                intent.putExtra("image",strImage);
                intent.putExtra("address",strAddress);
                intent.putExtra("uId",strUId);
                intent.putExtra("status",strStatus);
                startActivity(intent);
            }
        });


    }
}