package com.example.dotechuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.dotechuser.Model.Rating;
import com.example.dotechuser.databinding.ActivityHospitalDoctorDetailsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class HospitalDoctorDetailsActivity extends AppCompatActivity {

    ActivityHospitalDoctorDetailsBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    String strProfileImage = "";
    String strName = "";
    String doctorId = "";
    String strCategory = "";
    String avg ="";
    float ans = 0;
    float avgRating = 0;
    long count = 0;
    String feees = "";
    float ratingData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHospitalDoctorDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        Intent intent = getIntent();
        if (intent.hasExtra("doctorProfileImage")) {
            strProfileImage = intent.getStringExtra("doctorProfileImage");
            if (!strProfileImage.equals("")){
                Picasso.get()
                        .load(strProfileImage)
                        .placeholder(R.drawable.placeholder)
                        .into(binding.doctorProfile);
            }
            else{
                binding.doctorProfile.setImageResource(R.drawable.placeholder);
            }
        }
        if (intent.hasExtra("doctorName")) {
            strName = intent.getStringExtra("doctorName");
            binding.doctorName.setText(strName);
        }
        if (intent.hasExtra("doctorId")) {
            doctorId = intent.getStringExtra("doctorId");
        }
        if (intent.hasExtra("fees")) {
            feees = intent.getStringExtra("fees");
            if (feees.equals("")){
                binding.btnBookAppoinment.setVisibility(View.INVISIBLE);
            }
            binding.doctorAppointmentFees.setText(feees + " ₹");
        }
        if (intent.hasExtra("doctorCategory")) {
            strCategory = intent.getStringExtra("doctorCategory");
            binding.doctorCategory.setText(strCategory);
        }

        Query queryRating = FirebaseDatabase.getInstance().getReference("Rating").child("hospital_doctor").orderByChild("doctorId").equalTo(doctorId);

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


        binding.btnBookAppoinment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HospitalDoctorDetailsActivity.this,HospitalAppointmentTimeandDateActivity.class);
                intent.putExtra("doctorProfileImage",strProfileImage);
                intent.putExtra("doctorName",strName);
                intent.putExtra("doctorId",doctorId);
                intent.putExtra("fees",feees);
                intent.putExtra("doctorCategory",strCategory);
                startActivity(intent);
            }
        });
    }
}