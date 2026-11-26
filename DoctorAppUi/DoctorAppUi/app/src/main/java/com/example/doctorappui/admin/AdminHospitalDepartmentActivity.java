package com.example.doctorappui.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Toast;

import com.example.doctorappui.R;
import com.example.doctorappui.adapter.AdminHospitalDepartmentAdapter;
import com.example.doctorappui.adapter.DepartmentAdapter;
import com.example.doctorappui.databinding.ActivityAdminHospitalDepartmentBinding;
import com.example.doctorappui.modal.Department;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public
class AdminHospitalDepartmentActivity extends AppCompatActivity {

    ActivityAdminHospitalDepartmentBinding binding;
    String strEmail="",strName="",strMobile="",strImage="",strAddress="",strUId="";
    AdminHospitalDepartmentAdapter adapter;
    ArrayList<Department> list = new ArrayList<>();

    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminHospitalDepartmentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.home)));

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



        adapter = new AdminHospitalDepartmentAdapter(list, AdminHospitalDepartmentActivity.this);
        binding.departmentRecyclerview.setAdapter(adapter);

        LinearLayoutManager layoutManager = new GridLayoutManager(AdminHospitalDepartmentActivity.this, 2, LinearLayoutManager.VERTICAL, false);
        binding.departmentRecyclerview.setLayoutManager(layoutManager);
        binding.departmentRecyclerview.setHasFixedSize(true);

        Query query = FirebaseDatabase.getInstance().getReference("doctor and hospital").child(strUId).child("department");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public
            void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Department department = dataSnapshot.getValue(Department.class);
                    department.setUserId(strUId);
                    list.add(department);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public
            void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}