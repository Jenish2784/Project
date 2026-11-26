package com.example.doctorappui.hospital;

import static android.content.Context.MODE_PRIVATE;

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

import com.example.doctorappui.ProfileActivity;
import com.example.doctorappui.R;
import com.example.doctorappui.SignInActivity;
import com.example.doctorappui.databinding.FragmentHospitalHomeBinding;
import com.example.doctorappui.databinding.FragmentHospitalProfileBinding;
import com.example.doctorappui.modal.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

public
class HospitalProfileFragment extends Fragment {


    FragmentHospitalProfileBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    String strName="",strEmail="",strMobile="";


    public
    HospitalProfileFragment() {
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

        binding = FragmentHospitalProfileBinding.inflate(inflater,container,false);

        SharedPreferences preferences = this.getContext().getSharedPreferences("userData", Context.MODE_PRIVATE);
        strName = preferences.getString("name","");
        strEmail = preferences.getString("email","");
        strMobile = preferences.getString("mobile","");

        binding.tvName.setText(strName);
        binding.tvEmail.setText(strEmail);
        binding.tvCall.setText(strMobile);

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

        binding.profileCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                Intent intent = new Intent(getContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        binding.logoutCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                SharedPreferences.Editor pref=getContext().getSharedPreferences("userData",MODE_PRIVATE).edit();
                pref.putString("message","logout");
                pref.apply();
                Intent intent = new Intent(getContext(), SignInActivity.class);
                startActivity(intent);
            }
        });

        return binding.getRoot();
    }
}