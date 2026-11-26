package com.example.dotechuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import com.example.dotechuser.Model.Department_Doctor;
import com.example.dotechuser.Model.Doctor;
import com.example.dotechuser.Model.Hospital;
import com.example.dotechuser.adapter.DoctorListAdapter;
import com.example.dotechuser.adapter.HospitalDepartmentAdapter;
import com.example.dotechuser.adapter.HospitalDoctorListAdapter;
import com.example.dotechuser.databinding.ActivityHospitalDepartmentDoctorBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HospitalDepartmentDoctorActivity extends AppCompatActivity {

    ActivityHospitalDepartmentDoctorBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    List<Department_Doctor> list = new ArrayList<>();
    HospitalDoctorListAdapter adapter;

    String hospitalId = "";
    String doctorId = "";
    String departmentName = "";
    String fees = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHospitalDepartmentDoctorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.status)));


        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        SharedPreferences preferences = getSharedPreferences("hospitalDetails", MODE_PRIVATE);
        hospitalId = preferences.getString("hospitalId", "");

        Intent intent = getIntent();
        if (intent.hasExtra("departmentName")) {
            departmentName = intent.getStringExtra("departmentName");
            getSupportActionBar().setTitle(departmentName);
        }
        if (intent.hasExtra("fees")) {
            fees = intent.getStringExtra("fees");
        }

        binding.searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filterText(String.valueOf(editable));
            }
        });

        //SET ADAPTER

        adapter = new HospitalDoctorListAdapter(HospitalDepartmentDoctorActivity.this, list);
        binding.hospitalDoctorCategoryRecyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(HospitalDepartmentDoctorActivity.this);
        binding.hospitalDoctorCategoryRecyclerView.setLayoutManager(layoutManager);
        binding.hospitalDoctorCategoryRecyclerView.setHasFixedSize(true);


        //  fetch data
        Query query = FirebaseDatabase.getInstance().getReference("department_doctor").child(hospitalId).child(departmentName);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Department_Doctor hospital = dataSnapshot.getValue(Department_Doctor.class);
                    hospital.setFees(fees);
                    if (departmentName.equals(hospital.getCategory())) {
                        list.add(hospital);
                        hospital.setDoctorId(dataSnapshot.getKey());
                    }
                }
                adapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void filterText(String text) {
        List<Department_Doctor> filteredList = new ArrayList<>();
        for (Department_Doctor hospital : list) {
            if (hospital.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(hospital);
            }
        }

        if (!filteredList.isEmpty()) {
            adapter.setFilteredList(filteredList);
        }
    }
}