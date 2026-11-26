package com.example.dotechuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.example.dotechuser.Model.Hospital;
import com.example.dotechuser.adapter.HospitalFragmentAdapter;
import com.example.dotechuser.databinding.ActivityHospitalBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class HospitalActivity extends AppCompatActivity {

    ActivityHospitalBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    String strHospitalName = "";
    String strEmailId = "";
    String strHospitalId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHospitalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.home)));
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        Intent intent = getIntent();
        if (intent.hasExtra("hospitalName")) {
            strHospitalName = intent.getStringExtra("hospitalName");
            binding.hospitalName.setText(strHospitalName);
        }
        if (intent.hasExtra("emailId")) {
            strEmailId = intent.getStringExtra("emailId");
        }
        if (intent.hasExtra("hospitalId")) {
            strHospitalId = intent.getStringExtra("hospitalId");
        }


        SharedPreferences.Editor pref = getSharedPreferences("hospitalDetails", MODE_PRIVATE).edit();
        pref.putString("hospitalId",strHospitalId);
        pref.putString("hospitalName",strHospitalName);
        pref.putString("strEmailId",strEmailId);
        pref.apply();

        Query query = FirebaseDatabase.getInstance().getReference("doctor and hospital").orderByChild("emailId").equalTo(strEmailId);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Hospital hospital = dataSnapshot.getValue(Hospital.class);
                    if (!hospital.getProfileImage().equals("")) {
                        Picasso.get()
                                .load(hospital.getProfileImage())
                                .placeholder(R.drawable.placeholder)
                                .into(binding.hospitalImage);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        binding.viewpager.setAdapter(new HospitalFragmentAdapter(getSupportFragmentManager()));
        binding.tabLayout.setupWithViewPager(binding.viewpager);
    }
}