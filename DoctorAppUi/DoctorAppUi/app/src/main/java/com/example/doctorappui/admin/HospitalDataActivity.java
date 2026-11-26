package com.example.doctorappui.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.doctorappui.R;
import com.example.doctorappui.adapter.DepartmentAdapter;
import com.example.doctorappui.databinding.ActivityHospitalDataBinding;
import com.example.doctorappui.hospital.DepartmentActivity;
import com.example.doctorappui.modal.Department;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public
class HospitalDataActivity extends AppCompatActivity {

    ActivityHospitalDataBinding binding;
    String strEmail="",strName="",strMobile="",strImage="",strAddress="",strUId="",strStatus="";
    FirebaseAuth auth;
    FirebaseDatabase database;
    AlertDialog.Builder alert;


    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHospitalDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.home)));

        Intent intent = getIntent();
        if (intent.hasExtra("email")){
            strEmail = intent.getStringExtra("email");
        }
        if (intent.hasExtra("name")){
            strName = intent.getStringExtra("name");
            getSupportActionBar().setTitle(strName);
        }
        if (intent.hasExtra("mobile")){
            strMobile = intent.getStringExtra("mobile");
        }
        if (intent.hasExtra("image")){
            strImage = intent.getStringExtra("image");
        }
        if (intent.hasExtra("address")){
            strAddress = intent.getStringExtra("address");
        }
        if (intent.hasExtra("uId")){
            strUId = intent.getStringExtra("uId");
        }
        if (intent.hasExtra("status")){
            strStatus = intent.getStringExtra("status");
        }


        if (strStatus.equals("block")){
            binding.btnUnblock.setVisibility(View.VISIBLE);
            binding.btnBlock.setVisibility(View.INVISIBLE);
        }else {
            binding.btnUnblock.setVisibility(View.INVISIBLE);
            binding.btnBlock.setVisibility(View.VISIBLE);
        }

        binding.edtName.setText(strName);
        binding.edtEmail.setText(strEmail);
        binding.edtMobile.setText(strMobile);
        binding.edtAddress.setText(strAddress);

        if (!strImage.equals("")){
            Picasso.get()
                    .load(strImage)
                    .placeholder(R.drawable.placeholder)
                    .into(binding.profileImage);
        }


        binding.btnBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                alert = new AlertDialog.Builder(HospitalDataActivity.this);
                LayoutInflater inflater = LayoutInflater.from(HospitalDataActivity.this);
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
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("doctor and hospital").child(strUId);
                        databaseReference.child("status").setValue("block");
                        Toast.makeText(HospitalDataActivity.this, "Hospital blocked", Toast.LENGTH_SHORT).show();
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
                alert = new AlertDialog.Builder(HospitalDataActivity.this);
                LayoutInflater inflater = LayoutInflater.from(HospitalDataActivity.this);
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
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("doctor and hospital").child(strUId);
                        databaseReference.child("status").setValue("1");
                        Toast.makeText(HospitalDataActivity.this, "Hospital unblocked", Toast.LENGTH_SHORT).show();
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
}