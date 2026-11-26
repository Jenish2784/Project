package com.example.dotechuser;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dotechuser.Model.User;
import com.example.dotechuser.databinding.ActivityEditProfileBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class EditProfileActivity extends AppCompatActivity {

    ActivityEditProfileBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    String strGender = "";
    String strName = "";
    String strEmail = "";
    String strMobileNo = "";
    String strBirthDate = "";
    String strHeight = "";
    String strWeight = "";
    String strBloodGroup = "";
    String edtStringGender = "";
    String edtStringBloodGroup = "";
    String edtStrName, edtStrEmail, edtStrMobileNo, edtStrBirthDate, edtStrHeight, edtStrWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        SharedPreferences preferences = getSharedPreferences("userDetails", MODE_PRIVATE);
        strName = preferences.getString("name", "");
        strEmail = preferences.getString("email", "");
        strMobileNo = preferences.getString("mobile", "");
        strGender = preferences.getString("gender", "");
        strBloodGroup = preferences.getString("bloodGroup", "");
        strBirthDate = preferences.getString("birthDate", "");
        strHeight = preferences.getString("height", "");
        strWeight = preferences.getString("weight", "");

        binding.edtName.setText(strName);
        binding.edtEmail.setText(strEmail);
        binding.edtMobileNo.setText(strMobileNo);
        binding.edtBirthdate.setText(strBirthDate);
        binding.edtHeight.setText(strHeight);
        binding.edtWeight.setText(strWeight);

        //  get profile image to firebase database
        database.getReference().child("Users").child(auth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            User user = snapshot.getValue(User.class);

                            if (!user.getProfileImage().equals("")) {
                                Picasso.get()
                                        .load(user.getProfileImage())
                                        .placeholder(R.drawable.placeholder)
                                        .into(binding.profileImage);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(EditProfileActivity.this, "Server down!", Toast.LENGTH_SHORT).show();
                    }
                });

        if (strGender.equals("male")) {
            binding.male.setChecked(true);
            edtStringGender = "male";
        } else if (strGender.equals("female")) {
            binding.female.setChecked(true);
            edtStringGender = "female";
        } else {
            binding.other.setChecked(true);
            edtStringGender = "other";
        }

        binding.radioGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                binding.male.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        edtStringGender = "male";
                    }
                });
                binding.female.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        edtStringGender = "female";
                    }
                });
                binding.other.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        edtStringGender = "other";
                    }
                });
            }
        });

        if (strBloodGroup.equals("A+")) {
            binding.aPositive.setChecked(true);
            edtStringBloodGroup = "A+";
        } else if (strBloodGroup.equals("A-")) {
            binding.aNegative.setChecked(true);
            edtStringBloodGroup = "A-";
        } else if (strBloodGroup.equals("B+")) {
            binding.bPositive.setChecked(true);
            edtStringBloodGroup = "B+";
        } else if (strBloodGroup.equals("B-")) {
            binding.bNegative.setChecked(true);
            edtStringBloodGroup = "B-";
        } else if (strBloodGroup.equals("AB+")) {
            binding.abPositive.setChecked(true);
            edtStringBloodGroup = "AB+";
        } else if (strBloodGroup.equals("AB-")) {
            binding.abNegative.setChecked(true);
            edtStringBloodGroup = "AB-";
        } else if (strBloodGroup.equals("O+")) {
            binding.oPositive.setChecked(true);
            edtStringBloodGroup = "O+";
        } else if (strBloodGroup.equals("O-")) {
            binding.oNegative.setChecked(true);
            edtStringBloodGroup = "O-";
        } else {
            binding.aNegative.setChecked(true);
            edtStringBloodGroup = "A+";
        }

        binding.radioBlood.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                binding.aPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        edtStringBloodGroup = "A+";
                    }
                });
                binding.aNegative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        edtStringBloodGroup = "A-";
                    }
                });
                binding.bPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        edtStringBloodGroup = "B+";
                    }
                });
                binding.bNegative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        edtStringBloodGroup = "B-";
                    }
                });
                binding.abPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        edtStringBloodGroup = "AB+";
                    }
                });
                binding.abNegative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        edtStringBloodGroup = "AB-";
                    }
                });
                binding.oPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        edtStringBloodGroup = "O+";
                    }
                });
                binding.oNegative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        edtStringBloodGroup = "O-";
                    }
                });
            }
        });


        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtStrName = binding.edtName.getText().toString();
                edtStrEmail = binding.edtEmail.getText().toString();
                edtStrMobileNo = binding.edtMobileNo.getText().toString();
                edtStrBirthDate = binding.edtBirthdate.getText().toString();
                edtStrHeight = binding.edtHeight.getText().toString();
                edtStrWeight = binding.edtWeight.getText().toString();

                if (edtStrName.equals("")) {
                    Toast.makeText(EditProfileActivity.this, "Please enter name", Toast.LENGTH_SHORT).show();
                } else if (edtStrEmail.equals("")) {
                    Toast.makeText(EditProfileActivity.this, "Please enter email", Toast.LENGTH_SHORT).show();
                } else if (edtStrMobileNo.equals("")) {
                    Toast.makeText(EditProfileActivity.this, "Please enter mobile number", Toast.LENGTH_SHORT).show();
                } else if (edtStringGender.equals("")) {
                    Toast.makeText(EditProfileActivity.this, "Please select gender", Toast.LENGTH_SHORT).show();
                } else if (edtStrBirthDate.equals("")) {
                    Toast.makeText(EditProfileActivity.this, "Please enter birthdate", Toast.LENGTH_SHORT).show();
                } else if (edtStrHeight.equals("")) {
                    Toast.makeText(EditProfileActivity.this, "Please enter height", Toast.LENGTH_SHORT).show();
                } else if (edtStrWeight.equals("")) {
                    Toast.makeText(EditProfileActivity.this, "Please enter weight", Toast.LENGTH_SHORT).show();
                } else if (edtStringBloodGroup.equals("")) {
                    Toast.makeText(EditProfileActivity.this, "Please enter blood group", Toast.LENGTH_SHORT).show();
                } else {
                    if (strName.equals(edtStrName) && strEmail.equals(edtStrEmail) && strMobileNo.equals(edtStrMobileNo) &&
                            strBirthDate.equals(edtStrBirthDate) && strHeight.equals(edtStrHeight) &&
                            strWeight.equals(edtStrWeight) && strBloodGroup.equals(edtStringBloodGroup) &&
                            strGender.equals(edtStringGender)) {
                        Toast.makeText(EditProfileActivity.this, "Details not changed!", Toast.LENGTH_SHORT).show();
                    } else {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getUid());
                        databaseReference.child("name").setValue(edtStrName);
                        databaseReference.child("mobile").setValue(edtStrMobileNo);
                        databaseReference.child("gender").setValue(edtStringGender);
                        databaseReference.child("birthDate").setValue(edtStrBirthDate);
                        databaseReference.child("weight").setValue(edtStrWeight);
                        databaseReference.child("height").setValue(edtStrHeight);
                        databaseReference.child("bloodGroup").setValue(edtStringBloodGroup);

                        SharedPreferences.Editor pref = getSharedPreferences("userDetails", MODE_PRIVATE).edit();
                        pref.putString("name", edtStrName);
                        pref.putString("mobile", edtStrMobileNo);
                        pref.putString("bloodGroup", edtStringBloodGroup);
                        pref.putString("gender", edtStringGender);
                        pref.putString("birthDate", edtStrBirthDate);
                        pref.putString("height", edtStrHeight);
                        pref.putString("weight", edtStrWeight);
                        pref.apply();

                        Toast.makeText(EditProfileActivity.this, "Changes save!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 10);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*if (requestCode == 10 && requestCode == RESULT_OK && data != null && data.getData() != null)*/
        if (requestCode == 10) {
            if (data.getData() != null) {
                Uri uri = data.getData();
                binding.profileImage.setImageURI(uri);

                final StorageReference reference = storage.getReference().child("user_profile_image")
                        .child(auth.getUid());
                reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                database.getReference().child("Users").child(auth.getUid()).child("profileImage").setValue(uri.toString());
                                SharedPreferences.Editor pref = getSharedPreferences("userDetails", MODE_PRIVATE).edit();
                                pref.putString("profileImage", uri.toString());
                                pref.apply();
                            }
                        });
                    }
                });
            }
        }
    }

    /*public void spinnerData(){
        list.add("A-positive");
        list.add("A-negative");
        list.add("B-positive");
        list.add("B-negative");
        list.add("AB-positive");
        list.add("AB-negative");
        list.add("O-positive");
        list.add("O-negative");

        adapter = new ArrayAdapter(EditProfileActivity.this,androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,list);
        binding.edtBloodGroup.setAdapter(adapter);

        binding.edtBloodGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                strBloodGroup = list.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }*/
}