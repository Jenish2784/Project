package com.example.doctorappui.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.doctorappui.R;
import com.example.doctorappui.databinding.ActivityUserDataBinding;
import com.example.doctorappui.modal.SystemUsers;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public
class UserDataActivity extends AppCompatActivity {

    ActivityUserDataBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;
    String strName="",strEmail="",strMobile="",strImage="",strDob="",strGender="",strHeight="",strWeight="",strBloodGroup="",uId="",strStatus="";
    AlertDialog.Builder alert;


    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.home)));

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

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        if (strStatus.equals("0")){
           binding.btnUnblock.setVisibility(View.VISIBLE);
           binding.btnBlock.setVisibility(View.INVISIBLE);
        }else {
            binding.btnUnblock.setVisibility(View.INVISIBLE);
            binding.btnBlock.setVisibility(View.VISIBLE);
        }

        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("email").equalTo(strEmail);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public
            void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    SystemUsers systemUsers = dataSnapshot.getValue(SystemUsers.class);
                    strMobile = systemUsers.getMobile();
                    strName = systemUsers.getName();
                    strDob = systemUsers.getBirthDate();
                    strGender = systemUsers.getGender();
                    strHeight = systemUsers.getHeight();
                    strWeight = systemUsers.getWeight();
                    strBloodGroup = systemUsers.getBloodGroup();
                    if (!strImage.equals("")){
                        Picasso.get()
                                .load(strImage)
                                .placeholder(R.drawable.placeholder)
                                .into(binding.profileImage);
                    }
                    getSupportActionBar().setTitle(strName);
                    binding.edtEmail.setText(strEmail);
                    binding.edtMobile.setText(strMobile);
                    binding.edtName.setText(strName);
                    binding.edtDob.setText(strDob);
                    binding.edtGender.setText(strGender);
                    binding.edtHeight.setText(strHeight);
                    binding.edtWeight.setText(strWeight);
                    binding.edtBloodGroup.setText(strBloodGroup);
                }
            }

            @Override
            public
            void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.btnBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                alert = new AlertDialog.Builder(UserDataActivity.this);
                LayoutInflater inflater = LayoutInflater.from(UserDataActivity.this);
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
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(uId);
                        databaseReference.child("status").setValue("0");
                        Toast.makeText(UserDataActivity.this, "User blocked", Toast.LENGTH_SHORT).show();
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

                alert = new AlertDialog.Builder(UserDataActivity.this);
                LayoutInflater inflater = LayoutInflater.from(UserDataActivity.this);
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
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(uId);
                        databaseReference.child("status").setValue("1");
                        Toast.makeText(UserDataActivity.this, "User unblocked", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        binding.btnBlock.setVisibility(View.VISIBLE);
                        binding.btnUnblock.setVisibility(View.INVISIBLE);

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
}