package com.example.doctorappui.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.doctorappui.EmailSendActivity;
import com.example.doctorappui.EncryptDecrypt;
import com.example.doctorappui.R;
import com.example.doctorappui.databinding.ActivityDoctorDataBinding;
import com.example.doctorappui.modal.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public
class DoctorDataActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    ActivityDoctorDataBinding binding;
    String strCategory="",strDegreeName="",strImage="",strStatus="",strEmail="",strMobile="",strType="",strUsername="",uId="";
    AlertDialog.Builder alert;
    AlertDialog dialog;

    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDoctorDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Doctor Details");
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.home)));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        Intent intent = getIntent();
        if (intent.hasExtra("email")){
            strEmail=intent.getStringExtra("email");
        }
        if (intent.hasExtra("uId")){
            uId=intent.getStringExtra("uId");
        }
        if (intent.hasExtra("image")){
            strImage = intent.getStringExtra("image");
        }
        if (intent.hasExtra("status")){
            strStatus = intent.getStringExtra("status");
        }

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.home)));

        if (strStatus.equals("1")){
            binding.btnApprove.setVisibility(View.GONE);
            binding.btnReject.setVisibility(View.GONE);
            binding.btnBlock.setVisibility(View.VISIBLE);
        }
        if (strStatus.equals("block")){
            binding.btnApprove.setVisibility(View.GONE);
            binding.btnReject.setVisibility(View.GONE);
            binding.btnUnblock.setVisibility(View.VISIBLE);
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("doctor and hospital");
        Query query = FirebaseDatabase.getInstance().getReference("doctor and hospital").orderByChild("emailId").equalTo(strEmail);


        query.addValueEventListener(new ValueEventListener() {
            @Override
            public
            void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Users users = dataSnapshot.getValue(Users.class);
                    strCategory = users.getCategory();
                    strDegreeName = users.getDegreeName();
                    strMobile = users.getMobileNumber();
                    strType = users.getType();
                    strUsername = users.getName();
                    if (!strImage.equals("")){
                        Picasso.get()
                                .load(strImage)
                                .placeholder(R.drawable.placeholder)
                                .into(binding.profileImage);
                    }

                }
                getSupportActionBar().setTitle(strUsername);
                binding.edtCategory.setText(strCategory);
                binding.edtName.setText(strUsername);
                binding.edtDegreeName.setText(strDegreeName);
                binding.edtMobile.setText(strMobile);
                binding.edtType.setText(strType);
                binding.edtEmail.setText(strEmail);

                binding.profileImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (strImage != null && !strImage.isEmpty()) {
                            showDegreeCertificateDialog(strImage);
                        } else {
                            Toast.makeText(DoctorDataActivity.this, "No certificate image available", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            private void showDegreeCertificateDialog(String imageUrl) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DoctorDataActivity.this);
                LinearLayout layout = new LinearLayout(DoctorDataActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setPadding(20, 20, 20, 20);

                ImageView iv = new ImageView(DoctorDataActivity.this);
                iv.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, 800));
                iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
                Picasso.get().load(imageUrl).placeholder(R.drawable.placeholder).into(iv);
                layout.addView(iv);

                builder.setView(layout);
                builder.setTitle("Medical Degree Certificate");
                builder.setPositiveButton("Close", (d, w) -> d.dismiss());
                builder.show();
            }

            @Override
            public
            void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {

                alert = new AlertDialog.Builder(DoctorDataActivity.this);
                LayoutInflater inflater = LayoutInflater.from(DoctorDataActivity.this);
                View alert_view = inflater.inflate(R.layout.request_approve_alert_dialog,null,false);
                alert.setView(alert_view);
                @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button yes = alert_view.findViewById(R.id.btnYes);
                @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button no = alert_view.findViewById(R.id.btnNo);
                dialog = alert.create();
                dialog.setCancelable(false);
                dialog.show();

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public
                    void onClick(View view) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("doctor and hospital").child(uId);
                        databaseReference.child("status").setValue("1");
                        Toast.makeText(DoctorDataActivity.this, "User Approved!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        Intent intent = new Intent(DoctorDataActivity.this, NewRequestActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public
                    void onClick(View view) {
                        dialog.dismiss();
                    }
                });


            }
        });

        binding.btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {

                alert = new AlertDialog.Builder(DoctorDataActivity.this);
                LayoutInflater inflater = LayoutInflater.from(DoctorDataActivity.this);
                View alert_view = inflater.inflate(R.layout.request_reject_alert_dialog,null,false);
                alert.setView(alert_view);
                @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button yes = alert_view.findViewById(R.id.btnYes);
                @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button no = alert_view.findViewById(R.id.btnNo);
                AlertDialog dialog = alert.create();
                dialog.setCancelable(false);
                dialog.show();

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public
                    void onClick(View view) {
                        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("doctor and hospital").child(uId);
                        databaseReference.child("status").setValue("reject");
                        Toast.makeText(DoctorDataActivity.this, "User Rejected!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(DoctorDataActivity.this,NewRequestActivity.class);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public
                    void onClick(View view) {
                        dialog.dismiss();
                    }
                });

            }
        });

        binding.btnBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                alert = new AlertDialog.Builder(DoctorDataActivity.this);
                LayoutInflater inflater = LayoutInflater.from(DoctorDataActivity.this);
                View alert_view = inflater.inflate(R.layout.block_alert_dialog,null,false);
                alert.setView(alert_view);
                Button yes = alert_view.findViewById(R.id.btnYes);
                Button no = alert_view.findViewById(R.id.btnNo);
                AlertDialog dialog = alert.create();
                dialog.setCancelable(false);
                dialog.show();

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public
                    void onClick(View view) {
                        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("doctor and hospital").child(uId);
                        databaseReference.child("status").setValue("block");
                        Toast.makeText(DoctorDataActivity.this, "Doctor blocked", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        binding.btnUnblock.setVisibility(View.VISIBLE);
                        binding.btnBlock.setVisibility(View.INVISIBLE);
                    }
                });
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public
                    void onClick(View view) {
                        dialog.dismiss();
                    }
                });

            }
        });

        binding.btnUnblock.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                alert = new AlertDialog.Builder(DoctorDataActivity.this);
                LayoutInflater inflater = LayoutInflater.from(DoctorDataActivity.this);
                View alert_view = inflater.inflate(R.layout.unblock_alert_dialog,null,false);
                alert.setView(alert_view);
                Button yes = alert_view.findViewById(R.id.btnYes);
                Button no = alert_view.findViewById(R.id.btnNo);
                AlertDialog dialog = alert.create();
                dialog.setCancelable(false);
                dialog.show();

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public
                    void onClick(View view) {
                        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("doctor and hospital").child(uId);
                        databaseReference.child("status").setValue("1");
                        Toast.makeText(DoctorDataActivity.this, "Doctor unblocked", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        binding.btnUnblock.setVisibility(View.INVISIBLE);
                        binding.btnBlock.setVisibility(View.VISIBLE);
                    }
                });
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public
                    void onClick(View view) {
                        dialog.dismiss();
                    }
                });

            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}