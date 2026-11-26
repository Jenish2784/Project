package com.example.doctorappui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Toast;

import com.example.doctorappui.adapter.PatientListAdapter;
import com.example.doctorappui.databinding.ActivityPatientListBinding;
import com.example.doctorappui.modal.Follow;
import com.example.doctorappui.modal.SystemUsers;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public
class PatientListActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseDatabase database;
    String strType = "";
    ActivityPatientListBinding binding;
    PatientListAdapter adapter;
    ArrayList<SystemUsers> list = new ArrayList<>();

    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPatientListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.home)));
        getSupportActionBar().setTitle("Patient List");

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        SharedPreferences preferences = getSharedPreferences("userData", MODE_PRIVATE);
        strType = preferences.getString("type", "");


        if (strType.equals("doctor")) {
            adapter = new PatientListAdapter(list, PatientListActivity.this);
            binding.patientRecyclerView.setAdapter(adapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PatientListActivity.this);
            binding.patientRecyclerView.setLayoutManager(linearLayoutManager);
            binding.patientRecyclerView.setHasFixedSize(true);

            Query queryPatient = FirebaseDatabase.getInstance().getReference("doctor and hospital").child(auth.getUid()).child("followers");
            queryPatient.addValueEventListener(new ValueEventListener() {
                @Override
                public
                void onDataChange(@NonNull DataSnapshot snapshot) {
                    list.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Follow follow = dataSnapshot.getValue(Follow.class);

                        //fetch user data
                        Query query = FirebaseDatabase.getInstance().getReference("Users");
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public
                            void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                                    SystemUsers systemUsers = dataSnapshot1.getValue(SystemUsers.class);
                                    if (follow.getFollowedBy().equals(dataSnapshot1.getKey())) {
                                        list.add(systemUsers);
                                    }
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

                @Override
                public
                void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else if (strType.equals("hospital")) {
            //fetch hospital patient
            adapter = new PatientListAdapter(list, PatientListActivity.this);
            binding.patientRecyclerView.setAdapter(adapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PatientListActivity.this);
            binding.patientRecyclerView.setLayoutManager(linearLayoutManager);
            binding.patientRecyclerView.setHasFixedSize(true);

            Query queryPatient = FirebaseDatabase.getInstance().getReference("doctor and hospital").child(auth.getUid()).child("followers");
            queryPatient.addValueEventListener(new ValueEventListener() {
                @Override
                public
                void onDataChange(@NonNull DataSnapshot snapshot) {
                    list.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Follow follow = dataSnapshot.getValue(Follow.class);


                        //fetch user data
                        Query query = FirebaseDatabase.getInstance().getReference("Users");
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public
                            void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                                    SystemUsers systemUsers = dataSnapshot1.getValue(SystemUsers.class);
                                    if (follow.getFollowedBy().equals(dataSnapshot1.getKey())) {
                                        list.add(systemUsers);
                                    }
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

                @Override
                public
                void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}