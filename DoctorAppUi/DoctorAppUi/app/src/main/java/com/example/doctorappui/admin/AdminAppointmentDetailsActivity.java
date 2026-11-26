package com.example.doctorappui.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.example.doctorappui.R;
import com.example.doctorappui.adapter.AppointmentDetailsAdapter;
import com.example.doctorappui.databinding.ActivityAdminAppointmentDetailsBinding;
import com.example.doctorappui.databinding.ActivityAdminConsultationDetailsBinding;
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
class AdminAppointmentDetailsActivity extends AppCompatActivity {

    ActivityAdminAppointmentDetailsBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    List<Schedule> list = new ArrayList<>();
    AppointmentDetailsAdapter adapter;

    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminAppointmentDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setTitle("Appointment history");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.home)));

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        adapter = new AppointmentDetailsAdapter(AdminAppointmentDetailsActivity.this,list);
        binding.appointmentDetailsRecyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AdminAppointmentDetailsActivity.this,LinearLayoutManager.VERTICAL,false);
        binding.appointmentDetailsRecyclerView.setLayoutManager(linearLayoutManager);
        binding.appointmentDetailsRecyclerView.setHasFixedSize(true);

        Query query = database.getReference("Appointment");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public
            void onDataChange(@NonNull DataSnapshot snapshot) {
                int id = 0;
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Schedule schedule = dataSnapshot.getValue(Schedule.class);
                    id++;
                    schedule.setConsultId(id+"");
                    list.add(schedule);
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