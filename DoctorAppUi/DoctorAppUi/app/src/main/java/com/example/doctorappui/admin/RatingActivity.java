package com.example.doctorappui.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Toast;

import com.example.doctorappui.R;
import com.example.doctorappui.databinding.ActivityRatingBinding;
import com.example.doctorappui.modal.Rating;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public
class RatingActivity extends AppCompatActivity {

    ActivityRatingBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    double ans = 0;
    long totalRating = 0;

    double systemRating = 0;

    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRatingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.home)));
        getSupportActionBar().setTitle("DoTech rating");

        database.getReference().child("Rating").child("doTech")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public
                    void onDataChange(@NonNull DataSnapshot snapshot) {
                        totalRating = snapshot.getChildrenCount();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Rating rating = dataSnapshot.getValue(Rating.class);
                            ans+=rating.getRating();
                        }
                        systemRating = ans / totalRating;
                        binding.doTechRatingBar.setRating((float) systemRating);
                    }

                    @Override
                    public
                    void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
}