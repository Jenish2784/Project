package com.example.doctorappui.hospital;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import com.example.doctorappui.ManageTimeSlotActivity;
import com.example.doctorappui.R;
import com.example.doctorappui.databinding.ActivityManageDepartmentDoctorBinding;

public
class ManageDepartmentDoctorActivity extends AppCompatActivity {

    ActivityManageDepartmentDoctorBinding binding;
    String strEmail = "",strUId = "",strDepartmentName = "";

    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageDepartmentDoctorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.home)));

        Intent intent = getIntent();

        if (intent.hasExtra("email")){
            strEmail = intent.getStringExtra("email");
        }
        if (intent.hasExtra("uId")){
            strUId = intent.getStringExtra("uId");
        }
        if (intent.hasExtra("departmentName")){
            strDepartmentName = intent.getStringExtra("departmentName");
        }
        if (intent.hasExtra("name")){
            String name = intent.getStringExtra("name");
            getSupportActionBar().setTitle(name);
        }

        binding.timeSlotCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                Intent intent = new Intent(ManageDepartmentDoctorActivity.this, ManageTimeSlotActivity.class);
                intent.putExtra("uId",strUId);
                startActivity(intent);
            }
        });

        binding.profileCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                Intent intent = new Intent(ManageDepartmentDoctorActivity.this,DepartmentDoctorDataActivity.class);
                intent.putExtra("email",strEmail);
                intent.putExtra("uId",strUId);
                intent.putExtra("departmentName",strDepartmentName);
                startActivity(intent);
            }
        });
    }
}