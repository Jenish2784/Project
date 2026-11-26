package com.example.doctorappui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.doctorappui.databinding.ActivityProfileBinding;
import com.example.doctorappui.doctor.MainActivity;
import com.example.doctorappui.hospital.HospitalMainActivity;
import com.example.doctorappui.modal.Users;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public
class ProfileActivity extends AppCompatActivity {

    ActivityProfileBinding binding;
    String strName = "", strEmail = "", strMobile = "", strCategory = "", strAdd = "", strType = "", uId = "";
    String edtStrName = "", edtStrEmail = "", edtStrMobile = "", edtStrCategory = "", edtStrAdd = "";
    String strProfileImage = "";
    Uri uri;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    ProgressDialog progressDialog = null;
    AlertDialog.Builder alert;

    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uId = user.getUid();

        progressDialog = new ProgressDialog(ProfileActivity.this);
        progressDialog.setTitle("Picture uploading");
        progressDialog.setMessage("Please wait.");

        SharedPreferences preferences = getSharedPreferences("userData", MODE_PRIVATE);
        strName = preferences.getString("name", "");
        strEmail = preferences.getString("email", "");
        strMobile = preferences.getString("mobile", "");
        strCategory = preferences.getString("category", "");
        strAdd = preferences.getString("address", "");
        strType = preferences.getString("type", "");
        strProfileImage = preferences.getString("profileImage","");

        if(strType.equals("hospital")){
            binding.edtSpeciality.setVisibility(View.GONE);
            binding.txtSpeciality.setVisibility(View.GONE);
            binding.txtNotChanged.setVisibility(View.GONE);
        }


        binding.edtName.setText(strName);
        binding.edtEmail.setText(strEmail);
        binding.edtMo.setText(strMobile);
        binding.edtSpeciality.setText(strCategory);
        binding.edtAdd.setText(strAdd);



        if (!strProfileImage.equals("")){
            Picasso.get()
                    .load(strProfileImage)
                    .placeholder(R.drawable.placeholder)
                    .into(binding.profileImage);
        }else {
            binding.profileImage.setImageResource(R.drawable.dotech_logo);
        }




        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                if (strType.equals("doctor")) {
                    Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                    intent.putExtra("tab", "4");
                    startActivity(intent);
                } else if (strType.equals("hospital")) {
                    Intent intent = new Intent(ProfileActivity.this, HospitalMainActivity.class);
                    startActivity(intent);
                }
            }
        });

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                edtStrName = binding.edtName.getText().toString();
                edtStrEmail = binding.edtEmail.getText().toString();
                edtStrMobile = binding.edtMo.getText().toString();
                edtStrCategory = binding.edtSpeciality.getText().toString();
                edtStrAdd = binding.edtAdd.getText().toString();

                if (edtStrName.equals("")) {
                    Toast.makeText(ProfileActivity.this, "Please enter name", Toast.LENGTH_SHORT).show();
                } else if (edtStrEmail.equals("")) {
                    Toast.makeText(ProfileActivity.this, "Please enter email id", Toast.LENGTH_SHORT).show();
                } else if (edtStrMobile.equals("")) {
                    Toast.makeText(ProfileActivity.this, "Please enter mobile number", Toast.LENGTH_SHORT).show();
                } else if (edtStrAdd.equals("")) {
                    Toast.makeText(ProfileActivity.this, "Please enter address", Toast.LENGTH_SHORT).show();
                } else {
                    if (strName.equals(edtStrName) && strEmail.equals(edtStrEmail) && strMobile.equals(edtStrMobile) &&  strAdd.equals(edtStrAdd)) {
                        Toast.makeText(ProfileActivity.this, "Details not changed!", Toast.LENGTH_SHORT).show();
                    } else {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("doctor and hospital").child(uId);
                        databaseReference.child("name").setValue(edtStrName);
                        databaseReference.child("mobileNumber").setValue(edtStrMobile);
                        databaseReference.child("address").setValue(edtStrAdd);
                        SharedPreferences.Editor pref = getSharedPreferences("userData", MODE_PRIVATE).edit();
                        pref.putString("name", edtStrName);
                        pref.putString("mobile", edtStrMobile);
                        pref.putString("address", edtStrAdd);
                        pref.apply();
                        Toast.makeText(ProfileActivity.this, "Changes Save!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        binding.addProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 20);
            }
        });

        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                alert = new AlertDialog.Builder(ProfileActivity.this);
                LayoutInflater inflater = LayoutInflater.from(ProfileActivity.this);
                View alert_view = inflater.inflate(R.layout.image_alert_dialog,null,false);
                alert.setView(alert_view);
                @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageView image = alert_view.findViewById(R.id.imageView);
                AlertDialog dialog = alert.create();
                dialog.show();

                if (!strProfileImage.equals("")){
                    Picasso.get()
                            .load(strProfileImage)
                            .placeholder(R.drawable.placeholder)
                            .into(image);
                }else {
                    image.setImageResource(R.drawable.dotech_logo);
                }
            }
        });

    }

    @Override
    protected
    void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 20) {
                if (data.getData() != null) {
                    uri = data.getData();
                    binding.profileImage.setImageURI(uri);

                    progressDialog.show();
                    final StorageReference reference = storage.getReference().child("profile_image")
                            .child(FirebaseAuth.getInstance().getUid());
                    reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public
                        void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(ProfileActivity.this, "Profile image uploaded", Toast.LENGTH_SHORT).show();
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public
                                void onSuccess(Uri uri) {
                                    database.getReference().child("doctor and hospital").child(uId).child("profileImage").setValue(uri.toString());
                                    SharedPreferences.Editor pref = getSharedPreferences("userData", MODE_PRIVATE).edit();
                                    pref.putString("profileImage",uri.toString());
                                    pref.apply();
                                }
                            });
                        }
                    });

                } else {
                    Toast.makeText(this, "image not selected", Toast.LENGTH_SHORT).show();
                }
            }
        }catch (Exception e){

        }
    }
}