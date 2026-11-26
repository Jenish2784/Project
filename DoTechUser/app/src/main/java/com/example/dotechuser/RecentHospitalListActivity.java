package com.example.dotechuser;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.dotechuser.Model.Following;
import com.example.dotechuser.Model.Hospital;
import com.example.dotechuser.adapter.HospitalListAdapter;
import com.example.dotechuser.adapter.RecentDoctorListAdapter;
import com.example.dotechuser.adapter.RecentHospitalListAdapter;
import com.example.dotechuser.databinding.ActivityRecentHospitalListBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RecentHospitalListActivity extends AppCompatActivity {

    ActivityRecentHospitalListBinding binding;

    FirebaseAuth auth;
    FirebaseDatabase database;
    ArrayList<Hospital> list = new ArrayList<>();
    RecentHospitalListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecentHospitalListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setTitle("Recent Hospital");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.status)));

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        adapter = new RecentHospitalListAdapter(RecentHospitalListActivity.this, list);
        binding.recentHospitalRecyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(RecentHospitalListActivity.this,LinearLayoutManager.VERTICAL,true);
        layoutManager.setStackFromEnd(true);
        binding.recentHospitalRecyclerView.setLayoutManager(layoutManager);
        binding.recentHospitalRecyclerView.setHasFixedSize(true);

        Query query = database.getReference("Users").child(auth.getUid()).child("following").child("Hospital") ;

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Following following = dataSnapshot.getValue(Following.class);

                    Query queryPatientData = database.getReference("doctor and hospital");

                    queryPatientData.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                                Hospital hospital = dataSnapshot1.getValue(Hospital.class);
                                if (following.getFollowingBy().equals(dataSnapshot1.getKey())) {
                                    hospital.setHospitalId(dataSnapshot1.getKey());
                                    list.add(hospital);
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