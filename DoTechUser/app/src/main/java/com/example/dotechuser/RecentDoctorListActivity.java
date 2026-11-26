package com.example.dotechuser;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dotechuser.Model.Consultation;
import com.example.dotechuser.Model.Doctor;
import com.example.dotechuser.Model.Following;
import com.example.dotechuser.adapter.RecentDoctorListAdapter;
import com.example.dotechuser.databinding.ActivityRecentDoctorListBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RecentDoctorListActivity extends AppCompatActivity {

    ActivityRecentDoctorListBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    List<Doctor> list = new ArrayList<>();
    RecentDoctorListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecentDoctorListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setTitle("Recent Doctor");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.status)));

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        adapter = new RecentDoctorListAdapter(RecentDoctorListActivity.this, list);
        binding.recentDoctorRecyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(RecentDoctorListActivity.this,LinearLayoutManager.VERTICAL,true);
        layoutManager.setStackFromEnd(true);
        binding.recentDoctorRecyclerView.setLayoutManager(layoutManager);
        binding.recentDoctorRecyclerView.setHasFixedSize(true);

        Query query = database.getReference("Users").child(auth.getUid()).child("following").child("Doctor");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Following following = dataSnapshot.getValue(Following.class);

                    Query queryPatientData = database.getReference("doctor and hospital");

                    queryPatientData.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                                Doctor doctor = dataSnapshot1.getValue(Doctor.class);
                                if (following.getFollowingBy().equals(dataSnapshot1.getKey())){
                                    doctor.setuId(dataSnapshot1.getKey());
                                    list.add(doctor);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}