package com.example.doctorappui.hospital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.doctorappui.R;
import com.example.doctorappui.adapter.DepartmentDoctorAdapter;
import com.example.doctorappui.databinding.ActivityDepartmentDoctorBinding;
import com.example.doctorappui.modal.DepartmentDoctor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public
class DepartmentDoctorActivity extends AppCompatActivity {

    ActivityDepartmentDoctorBinding binding;
    String strDepartmentName="",strUId="";
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    DepartmentDoctorAdapter adapter;
    ArrayList<DepartmentDoctor> list = new ArrayList<>();

    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDepartmentDoctorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.home)));

        Intent intent = getIntent();

        if (intent.hasExtra("department")){
            strDepartmentName = intent.getStringExtra("department");
            getSupportActionBar().setTitle(strDepartmentName);
        }
        if (intent.hasExtra("uId")){
            strUId = intent.getStringExtra("uId");
        }


        //fetch department doctor
        adapter = new DepartmentDoctorAdapter(list,DepartmentDoctorActivity.this);
        binding.departmentDoctorRecyclerview.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(DepartmentDoctorActivity.this);
        binding.departmentDoctorRecyclerview.setLayoutManager(layoutManager);
        binding.departmentDoctorRecyclerview.setHasFixedSize(true);

        Query query = FirebaseDatabase.getInstance().getReference("department_doctor").child(auth.getUid())
                        .child(strDepartmentName);

        //Toast.makeText(this,strUId, Toast.LENGTH_SHORT).show();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public
            void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    DepartmentDoctor departmentDoctor = dataSnapshot.getValue(DepartmentDoctor.class);
                    departmentDoctor.setUserId(dataSnapshot.getKey());
                    list.add(departmentDoctor);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public
            void onCancelled(@NonNull DatabaseError error) {

            }
        });


        binding.addDepartmentDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                Intent intent = new Intent(DepartmentDoctorActivity.this, AddDepartmentDoctorActivity.class);
                intent.putExtra("department",strDepartmentName);
                intent.putExtra("uId",strUId);
                startActivity(intent);
            }
        });

    }
}