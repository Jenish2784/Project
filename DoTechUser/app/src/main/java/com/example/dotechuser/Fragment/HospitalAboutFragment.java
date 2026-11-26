package com.example.dotechuser.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dotechuser.Model.Hospital;
import com.example.dotechuser.R;
import com.example.dotechuser.databinding.FragmentHospitalAboutBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class HospitalAboutFragment extends Fragment {

    FragmentHospitalAboutBinding binding;

    String strEmailId = "";

    public HospitalAboutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHospitalAboutBinding.inflate(inflater, container, false);

        SharedPreferences preferences = getContext().getSharedPreferences("hospitalDetails", MODE_PRIVATE);
        strEmailId = preferences.getString("strEmailId", "");

        Query query = FirebaseDatabase.getInstance().getReference("doctor and hospital").orderByChild("emailId").equalTo(strEmailId);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Hospital hospital = dataSnapshot.getValue(Hospital.class);
                    binding.hospitalAddress.setText(hospital.getAddress());
                    binding.hospitalMobile.setText(hospital.getMobileNumber());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return binding.getRoot();
    }
}