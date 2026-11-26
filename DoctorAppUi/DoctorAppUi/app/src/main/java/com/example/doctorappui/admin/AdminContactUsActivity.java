package com.example.doctorappui.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.example.doctorappui.R;
import com.example.doctorappui.adapter.AppointmentDetailsAdapter;
import com.example.doctorappui.adapter.ContactUsAdapter;
import com.example.doctorappui.databinding.ActivityAdminContactUsBinding;
import com.example.doctorappui.modal.ContactUs;
import com.example.doctorappui.modal.Schedule;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public
class AdminContactUsActivity extends AppCompatActivity {

    ActivityAdminContactUsBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    List<ContactUs> list = new ArrayList<>();
    ContactUsAdapter adapter;

    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminContactUsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setTitle("ContactUs history");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.home)));

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        adapter = new ContactUsAdapter(AdminContactUsActivity.this,list);
        binding.contactUsRecyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AdminContactUsActivity.this,LinearLayoutManager.VERTICAL,false);
        binding.contactUsRecyclerView.setLayoutManager(linearLayoutManager);
        binding.contactUsRecyclerView.setHasFixedSize(true);


        Query query = database.getReference("contactUs");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public
            void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ContactUs contactUs = dataSnapshot.getValue(ContactUs.class);

                    list.add(contactUs);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public
            void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}