package com.example.dotechuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.dotechuser.Model.Rating;
import com.example.dotechuser.databinding.ActivityDoctorRatingBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DoctorRatingActivity extends AppCompatActivity {

    ActivityDoctorRatingBinding binding;
    String strDoctorProfile = "";
    String strDoctorName = "";
    String strDoctorCategory = "";
    String strTextArea = "";
    String doctorId = "";
    String id = "";
    String userId = "";
    String currentDate = "";
    Float ratingData = 0.0f;
    Float ratingNumber;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDoctorRatingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setTitle("Give Feedback");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.status)));

        auth = FirebaseAuth.getInstance();

        userId = auth.getUid();

        Intent intent = getIntent();
        if (intent.hasExtra("doctorCategory")) {
            strDoctorCategory = intent.getStringExtra("doctorCategory");
            binding.doctorCategory.setText(strDoctorCategory);
        }
        if (intent.hasExtra("doctorName")) {
            strDoctorName = intent.getStringExtra("doctorName");
            binding.doctorName.setText(strDoctorName);
        }
        if (intent.hasExtra("doctorId")) {
            doctorId = intent.getStringExtra("doctorId");
        }
        if (intent.hasExtra("doctorProfile")) {
            strDoctorProfile = intent.getStringExtra("doctorProfile");
            if (!strDoctorProfile.equals("")) {
                Picasso.get()
                        .load(strDoctorProfile)
                        .placeholder(R.drawable.placeholder)
                        .into(binding.doctorProfile);
            } else {
                binding.doctorProfile.setImageResource(R.drawable.dotech_logo);
            }
        }

        id = userId + doctorId;

        currentDate = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

        binding.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean isChanged) {
                if (isChanged) {
                    ratingNumber = rating;
                }
            }
        });

        Query query = FirebaseDatabase.getInstance().getReference("Rating").child("hospital_doctor");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Rating rating = dataSnapshot.getValue(Rating.class);

                    if (dataSnapshot.getKey().equals(auth.getUid()+doctorId)){
                        ratingData = rating.getRating();

                        if (!ratingData.toString().equals("")){
                            binding.ratingBar.setRating(ratingData);
                        }else {
                            Toast.makeText(DoctorRatingActivity.this, "not rating", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        /*binding.textAreaInformation.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK){
                    case MotionEvent.ACTION_UP:
                        view.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });*/

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strTextArea = binding.textAreaInformation.getText().toString();

                if (ratingNumber == null) {
                    Toast.makeText(DoctorRatingActivity.this, "fill rating", Toast.LENGTH_SHORT).show();
                } else {
                    Rating ratingBar = new Rating(userId, doctorId, currentDate, strTextArea, new Date().getTime(), ratingNumber);

                    FirebaseDatabase.getInstance().getReference("Rating").child("hospital_doctor").child(id).setValue(ratingBar);

                    Toast.makeText(DoctorRatingActivity.this, "Submited your review", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}