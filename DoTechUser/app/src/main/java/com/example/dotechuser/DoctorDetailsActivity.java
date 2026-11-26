package com.example.dotechuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.dotechuser.Model.Doctor;
import com.example.dotechuser.Model.Rating;
import com.example.dotechuser.databinding.ActivityDoctorDetailsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class DoctorDetailsActivity extends AppCompatActivity {

    ActivityDoctorDetailsBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    String strName = "";
    String strCategory = "";
    String strEmail = "";
    String strDoctorUId = "";
    String strTotalAmount = "";
    String strExperience = "";
    String strCallAmount = "";
    String strMsgAmount = "";
    String avg ="";
    float ans = 0;
    float avgRating = 0;
    long count = 0;
    float ratingData;
    int gstAmount, smsAmount, totalSmsAmount;
    int callAmount, gstCallAmount, totalCallAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDoctorDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.status)));

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        Intent intent = getIntent();
        if (intent.hasExtra("email")) {
            strEmail = intent.getStringExtra("email");
        }
        if (intent.hasExtra("doctorUId")) {
            strDoctorUId = intent.getStringExtra("doctorUId");
        }
        /*if (intent.hasExtra("totalAmount")) {
            strTotalAmount = intent.getStringExtra("totalAmount");

            if (strTotalAmount.equals("")) {
                binding.doctorConsultancFees.setText("NA");
                binding.btnBookAppoinment.setVisibility(View.INVISIBLE);
            } else {
                smsAmount = Integer.parseInt(strTotalAmount);
                gstAmount = smsAmount * 18 / 100;
                totalSmsAmount = smsAmount + gstAmount;
                binding.doctorConsultancFees.setText(totalSmsAmount + " " + "₹");
            }
        }*/

        Query queryRating = FirebaseDatabase.getInstance().getReference("Rating").child("hospital_doctor").orderByChild("doctorId").equalTo(strDoctorUId);

        queryRating.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                count = snapshot.getChildrenCount();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Rating rating = dataSnapshot.getValue(Rating.class);
                    ratingData = rating.getRating();
                    ans+=ratingData;
                }
                avgRating = ans/count;
                avg = String.valueOf(avgRating);
                binding.doctorFeedback.setText(avg);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        Query query = FirebaseDatabase.getInstance().getReference("doctor and hospital")
                .orderByChild("emailId").equalTo(strEmail);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Doctor doctor = dataSnapshot.getValue(Doctor.class);
                    strName = doctor.getName();
                    strCategory = doctor.getCategory();
                    strExperience = doctor.getExperience();
                    strCallAmount = doctor.getCallAmount();
                    strMsgAmount = doctor.getMsgAmount();

                    if (strMsgAmount.equals("")) {
                        binding.doctorConsultancFees.setText("NA");
                        binding.btnBookAppoinment.setVisibility(View.INVISIBLE);
                    } else {
                        smsAmount = Integer.parseInt(strMsgAmount);
                        gstAmount = smsAmount * 18 / 100;
                        totalSmsAmount = smsAmount + gstAmount;
                        binding.doctorConsultancFees.setText(totalSmsAmount + " " + "₹");
                    }

                    if (strCallAmount.equals("")) {
                        binding.doctorAppointmentFees.setText("NA");
                        binding.btnBookAppoinment.setVisibility(View.INVISIBLE);
                    } else {
                        callAmount = Integer.parseInt(strCallAmount);
                        gstCallAmount = callAmount * 18 / 100;
                        totalCallAmount = callAmount + gstCallAmount;
                        binding.doctorAppointmentFees.setText(totalCallAmount + " " + "₹");
                    }

                    binding.doctorName.setText(strName);
                    binding.doctorCategory.setText(strCategory);
                    //binding.doctorExperience.setText("NA");
                    if (strExperience.equals("")){
                        binding.doctorExperience.setText("NA");
                    }else {
                        binding.doctorExperience.setText(strExperience + " Year");
                    }

                    if (!doctor.getProfileImage().equals("")) {
                        Picasso.get()
                                .load(doctor.getProfileImage())
                                .placeholder(R.drawable.placeholder)
                                .into(binding.doctorProfile);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.btnBookAppoinment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DoctorDetailsActivity.this, AppointmentTimeandDateActivity.class);
                intent.putExtra("email", strEmail);
                intent.putExtra("doctorUId", strDoctorUId);
                startActivity(intent);
            }
        });

    }
}

