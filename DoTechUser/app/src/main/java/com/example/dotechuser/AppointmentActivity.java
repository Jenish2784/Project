package com.example.dotechuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.example.dotechuser.Model.Consultation;
import com.example.dotechuser.adapter.ConsultationListAdapter;
import com.example.dotechuser.databinding.ActivityAppointmentBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AppointmentActivity extends AppCompatActivity {

    ActivityAppointmentBinding binding;
    FirebaseAuth auth;
    List<Consultation> consultationList = new ArrayList<>();
    ConsultationListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAppointmentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.status)));
        getSupportActionBar().setTitle("My Appointments");

        auth = FirebaseAuth.getInstance();

        //SET ADAPTER
        adapter = new ConsultationListAdapter(AppointmentActivity.this, consultationList);
        binding.consultationListRecyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(AppointmentActivity.this);
        binding.consultationListRecyclerView.setLayoutManager(layoutManager);
        binding.consultationListRecyclerView.setHasFixedSize(true);

        Query query = FirebaseDatabase.getInstance().getReference("Consultation").orderByChild("userId").equalTo(auth.getUid());

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                consultationList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Consultation consultation = dataSnapshot.getValue(Consultation.class);
                    consultation.setConsultId(dataSnapshot.getKey());
                    consultationList.add(consultation);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}