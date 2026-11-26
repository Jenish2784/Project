package com.example.doctorappui.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.example.doctorappui.R;
import com.example.doctorappui.databinding.ActivityAdminHospitalDepartmentDoctorDataBinding;
import com.example.doctorappui.modal.DepartmentDoctor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public
class AdminHospitalDepartmentDoctorDataActivity extends AppCompatActivity {

    ActivityAdminHospitalDepartmentDoctorDataBinding binding;
    String strEmail = "", strUId = "", strName = "", strMobile = "", strGender = "", strDepartmentName = "", strProfileImage = "";

    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminHospitalDepartmentDoctorDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.home)));

        Intent intent = getIntent();

        if (intent.hasExtra("email")) {
            strEmail = intent.getStringExtra("email");
        }
        if (intent.hasExtra("uId")) {
            strUId = intent.getStringExtra("uId");
        }
        if (intent.hasExtra("departmentName")) {
            strDepartmentName = intent.getStringExtra("departmentName");
        }

        Query query = FirebaseDatabase.getInstance().getReference("department_doctor").child(strUId).child(strDepartmentName).orderByChild("emailId").equalTo(strEmail);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public
            void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DepartmentDoctor departmentDoctor = dataSnapshot.getValue(DepartmentDoctor.class);
                    strMobile = departmentDoctor.getMobile_number();
                    strName = departmentDoctor.getName();
                    strGender = departmentDoctor.getGender();

                    binding.edtName.setText(strName);
                    binding.edtEmail.setText(strEmail);
                    binding.edtMobile.setText(strMobile);

                    if (!departmentDoctor.getProfileImage().equals("")) {
                        Picasso.get()
                                .load(departmentDoctor.getProfileImage())
                                .placeholder(R.drawable.placeholder)
                                .into(binding.profileImage);
                    }

                    if (strGender.equals("male")) {
                        binding.btnMale.setChecked(true);
                    } else if (strGender.equals("female")) {
                        binding.btnFemale.setChecked(true);
                    } else {
                        binding.btnOther.setChecked(true);
                    }
                    getSupportActionBar().setTitle(strName);
                }
            }

            @Override
            public
            void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}