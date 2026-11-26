package com.example.doctorappui.hospital;

import androidx.annotation.NonNull;
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

import com.example.doctorappui.ProfileActivity;
import com.example.doctorappui.R;
import com.example.doctorappui.databinding.ActivityDepartmentDoctorDataBinding;
import com.example.doctorappui.modal.DepartmentDoctor;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public
class DepartmentDoctorDataActivity extends AppCompatActivity {

    ActivityDepartmentDoctorDataBinding binding;
    String strEmail = "", strUId = "", strName = "", strMobile = "", strGender = "", strDepartmentName = "", strProfileImage = "";
    String edtStrName, edtStrEmail, edtStrMobile, edtStrGender;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    ProgressDialog progressDialog = null;

    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDepartmentDoctorDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.home)));

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

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

        progressDialog = new ProgressDialog(DepartmentDoctorDataActivity.this);
        progressDialog.setTitle("Picture uploading");
        progressDialog.setMessage("Please wait.");

        Query query = FirebaseDatabase.getInstance().getReference("department_doctor").child(auth.getUid()).child(strDepartmentName).orderByChild("emailId").equalTo(strEmail);

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
                        edtStrGender = "male";
                    } else if (strGender.equals("female")) {
                        binding.btnFemale.setChecked(true);
                        edtStrGender = "female";
                    } else {
                        binding.btnOther.setChecked(true);
                        edtStrGender = "other";
                    }
                    getSupportActionBar().setTitle(strName);
                }
            }

            @Override
            public
            void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public
            void onCheckedChanged(RadioGroup radioGroup, int i) {
                binding.btnMale.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public
                    void onClick(View view) {
                        edtStrGender = "male";
                    }
                });
                binding.btnFemale.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public
                    void onClick(View view) {
                        edtStrGender = "female";
                    }
                });
                binding.btnOther.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public
                    void onClick(View view) {
                        edtStrGender = "other";
                    }
                });
            }
        });


        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                edtStrName = binding.edtName.getText().toString();
                edtStrEmail = binding.edtEmail.getText().toString();
                edtStrMobile = binding.edtMobile.getText().toString();


                if (edtStrName.equals("")) {
                    Toast.makeText(DepartmentDoctorDataActivity.this, "Please enter name", Toast.LENGTH_SHORT).show();
                } else if (edtStrEmail.equals("")) {
                    Toast.makeText(DepartmentDoctorDataActivity.this, "Please enter email id", Toast.LENGTH_SHORT).show();
                } else if (edtStrMobile.equals("")) {
                    Toast.makeText(DepartmentDoctorDataActivity.this, "Please enter mobile number", Toast.LENGTH_SHORT).show();
                } else {
                    if (strName.equals(edtStrName) && strEmail.equals(edtStrEmail) && strMobile.equals(edtStrMobile) && strGender.equals(edtStrGender)) {
                        Toast.makeText(DepartmentDoctorDataActivity.this, "Details not changed!", Toast.LENGTH_SHORT).show();
                    } else {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("department_doctor").child(auth.getUid()).child(strDepartmentName).child(strUId);
                        databaseReference.child("name").setValue(edtStrName);
                        databaseReference.child("emailId").setValue(edtStrEmail);
                        databaseReference.child("mobile_number").setValue(edtStrMobile);
                        databaseReference.child("gender").setValue(edtStrGender);
                        Toast.makeText(DepartmentDoctorDataActivity.this, "Changes save!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 10);
            }
        });

    }

    @Override
    protected
    void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 10) {
                if (data.getData() != null) {
                    Uri uri = data.getData();
                    strProfileImage = uri.toString();
                    binding.profileImage.setImageURI(uri);

                    progressDialog.show();
                    final StorageReference reference = storage.getReference().child("department_doctor")
                            .child(auth.getUid()).child(strUId);
                    reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public
                        void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(DepartmentDoctorDataActivity.this, "Profile image uploaded", Toast.LENGTH_SHORT).show();
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public
                                void onSuccess(Uri uri) {
                                    database.getReference().child("department_doctor").child(auth.getUid()).child(strDepartmentName).child(strUId).child("profileImage").setValue(uri.toString()
                                    );
                                }
                            });
                        }
                    });
                }
            }
        } catch (Exception e) {

        }

    }
}