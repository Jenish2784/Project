package com.example.doctorappui.hospital;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.doctorappui.R;
import com.example.doctorappui.SignUpActivity;
import com.example.doctorappui.SignUpVerifyActivity;
import com.example.doctorappui.databinding.ActivityAddDepartmentDoctorBinding;
import com.example.doctorappui.modal.DepartmentDoctor;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;
import java.util.Locale;

public
class AddDepartmentDoctorActivity extends AppCompatActivity {

    ActivityAddDepartmentDoctorBinding binding;
    String strDepartmentName = "";
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    String strName = "", strEmail = "", strMobile = "", strGender = "", strUId = "",strPass = "",strConfPass = "";
    ProgressDialog progressDialog = null;

    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddDepartmentDoctorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.home)));

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        progressDialog = new ProgressDialog(AddDepartmentDoctorActivity.this);
        progressDialog.setTitle("Doctor account creating");
        progressDialog.setMessage("Please wait...");

        Intent intent = getIntent();
        if (intent.hasExtra("department")) {
            strDepartmentName = intent.getStringExtra("department");
            getSupportActionBar().setTitle("Add " + strDepartmentName.toLowerCase(Locale.ROOT) + " doctor");
        }
        if (intent.hasExtra("uId")) {
            strUId = intent.getStringExtra("uId");
        }

        binding.genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public
            void onCheckedChanged(RadioGroup radioGroup, int i) {
                binding.btnMale.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public
                    void onClick(View view) {
                        strGender = "male";
                    }
                });
                binding.btnFemale.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public
                    void onClick(View view) {
                        strGender = "female";
                    }
                });
                binding.btnOther.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public
                    void onClick(View view) {
                        strGender = "other";
                    }
                });
            }
        });


        binding.btnAddDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                strName = binding.edtName.getText().toString();
                strEmail = binding.edtEmail.getText().toString();
                strMobile = binding.edtMobile.getText().toString();
                if (strName.equals("")) {
                    Toast.makeText(AddDepartmentDoctorActivity.this, "Please enter name", Toast.LENGTH_SHORT).show();
                } else if (strEmail.equals("")) {
                    Toast.makeText(AddDepartmentDoctorActivity.this, "Please enter email id", Toast.LENGTH_SHORT).show();
                } else if (strMobile.equals("")) {
                    Toast.makeText(AddDepartmentDoctorActivity.this, "Please enter mobile number", Toast.LENGTH_SHORT).show();
                } else if (strGender.equals("")) {
                    Toast.makeText(AddDepartmentDoctorActivity.this, "Please select gender", Toast.LENGTH_SHORT).show();
                } else {

                    DepartmentDoctor departmentDoctor = new DepartmentDoctor();
                    departmentDoctor.setName(strName);
                    departmentDoctor.setEmailId(strEmail);
                    departmentDoctor.setMobile_number(strMobile);
                    departmentDoctor.setGender(strGender);
                    departmentDoctor.setTimeAt(new Date().getTime());
                    departmentDoctor.setCategory(strDepartmentName);
                    departmentDoctor.setProfileImage("");

                    database.getReference().child("department_doctor").child(auth.getUid()).child(strDepartmentName).push()
                            .setValue(departmentDoctor).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public
                                void onSuccess(Void unused) {
                                    progressDialog.dismiss();
                                    Toast.makeText(AddDepartmentDoctorActivity.this, "Doctor added!", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

    }

}