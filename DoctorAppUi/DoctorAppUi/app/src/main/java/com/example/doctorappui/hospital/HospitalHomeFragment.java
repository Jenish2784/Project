package com.example.doctorappui.hospital;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.doctorappui.R;
import com.example.doctorappui.databinding.FragmentHospitalHomeBinding;
import com.example.doctorappui.modal.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

public
class HospitalHomeFragment extends Fragment {

    FragmentHospitalHomeBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    long departmentCount = 0;
    FirebaseStorage storage;
    String strName = "";
    String strStatus = "";


    public
    HospitalHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public
    void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    @Override
    public
    View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHospitalHomeBinding.inflate(inflater, container, false);

        SharedPreferences preferences = this.getContext().getSharedPreferences("userData", Context.MODE_PRIVATE);
        strStatus = preferences.getString("status", "");
        strName = preferences.getString("name", "");

        binding.doctorName.setText("Hello, " + strName);


        database.getReference().child("doctor and hospital").child(auth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public
                    void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            Users users = snapshot.getValue(Users.class);
                            if (!users.getProfileImage().equals("")){
                                Picasso.get()
                                        .load(users.getProfileImage())
                                        .placeholder(R.drawable.placeholder)
                                        .into(binding.profileImage);
                            }
                        }
                    }

                    @Override
                    public
                    void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        //count department
        Query departmentQuery = FirebaseDatabase.getInstance().getReference("doctor and hospital").child(auth.getUid()).child("department");
        departmentQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public
            void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    departmentCount = snapshot.getChildrenCount();
                    binding.departmentCount.setText(departmentCount+"");
                }
            }

            @Override
            public
            void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.departmentCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                Intent intent = new Intent(getContext(), DepartmentActivity.class);
                startActivity(intent);
            }
        });


        return binding.getRoot();
    }
}