package com.example.dotechuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import com.example.dotechuser.Model.Category;
import com.example.dotechuser.Model.Doctor;
import com.example.dotechuser.Model.Rating;
import com.example.dotechuser.adapter.DoctorListAdapter;
import com.example.dotechuser.databinding.ActivityCategoryBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    ActivityCategoryBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;
    List<Doctor> doctorList = new ArrayList<>();
    DoctorListAdapter adapter;
    String strCategoryName = "";
    String doctorId =  "";

    String avg ="";
    float ans = 0;
    float avgRating = 0;
    long count = 0;
    float ratingData;

    float demo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.status)));

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        Intent intent = getIntent();
        strCategoryName = intent.getStringExtra("categoryName");
        getSupportActionBar().setTitle(strCategoryName);

        binding.searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filterText(String.valueOf(editable));
            }
        });


        //SET ADAPTER
        adapter = new DoctorListAdapter(CategoryActivity.this, doctorList);
        binding.doctorCategoryRecyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(CategoryActivity.this);
        binding.doctorCategoryRecyclerView.setLayoutManager(layoutManager);
        binding.doctorCategoryRecyclerView.setHasFixedSize(true);

        Query query = FirebaseDatabase.getInstance().getReference("doctor and hospital").orderByChild("status").equalTo("1");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                doctorList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Doctor doctor = dataSnapshot.getValue(Doctor.class);
                    if (doctor.getCategory().equals(strCategoryName) && doctor.getType().equals("doctor")) {
                        doctor.setuId(dataSnapshot.getKey());

                        doctorId =  doctor.getuId();

                        Query queryRating = FirebaseDatabase.getInstance().getReference("Rating").child("hospital_doctor").orderByChild("doctorId").equalTo(doctorId);

                        queryRating.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                count = snapshot.getChildrenCount();
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    Rating rating = dataSnapshot.getValue(Rating.class);

                                    ratingData = rating.getRating();
                                    ans+=ratingData;
                                    avgRating = ans/count;
                                }
                                demo = avgRating;
                                //Toast.makeText(CategoryActivity.this,demo+"", Toast.LENGTH_SHORT).show();

                                /*avgRating = ans/count;
                                avg = String.valueOf(avgRating);*/
                                ans  = 0;
                                //Toast.makeText(CategoryActivity.this,avgRating+"", Toast.LENGTH_SHORT).show();
                                //Toast.makeText(CategoryActivity.this,doctor.getRating() + "", Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        doctorList.add(doctor);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void filterText(String text) {
        List<Doctor> filteredList = new ArrayList<>();
        for (Doctor doctor : doctorList) {
            if (doctor.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(doctor);
            }
        }

        if (!filteredList.isEmpty()) {
            adapter.setFilteredList(filteredList);
        }
    }

}