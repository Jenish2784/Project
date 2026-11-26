package com.example.doctorappui;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.doctorappui.adapter.ScheduleAdapter;
import com.example.doctorappui.databinding.FragmentCompleteBinding;
import com.example.doctorappui.modal.Schedule;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public
class CompleteFragment extends Fragment {

    FragmentCompleteBinding binding;
    ArrayList<Schedule> list = new ArrayList<>();
    ScheduleAdapter adapter;
    FirebaseAuth auth;
    FirebaseDatabase database;
    String strType = "";
    int count = 0;


    public
    CompleteFragment() {
        // Required empty public constructor
    }


    @Override
    public
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    @Override
    public
    View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCompleteBinding.inflate(inflater, container, false);

        SharedPreferences preferences = this.getContext().getSharedPreferences("userData", MODE_PRIVATE);
        strType = preferences.getString("type", "");

        if (strType.equals("doctor")) {


            Query query = FirebaseDatabase.getInstance().getReference("Consultation").orderByChild("doctorId").equalTo(auth.getUid());

            adapter = new ScheduleAdapter(list, getContext());
            binding.completeRecyclerView.setAdapter(adapter);

            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            binding.completeRecyclerView.setLayoutManager(layoutManager);
            binding.completeRecyclerView.setHasFixedSize(true);

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public
                void onDataChange(@NonNull DataSnapshot snapshot) {
                    list.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Schedule schedule = dataSnapshot.getValue(Schedule.class);
                        schedule.setConsultId(dataSnapshot.getKey());
                        if (schedule.getPaymentStatus().equals("1") && schedule.getStatus().equals("1")) {
                            list.add(schedule);
                            count++;
                        }
                    }
                    if (count == 0){
                        binding.tvDnf.setVisibility(View.VISIBLE);
                    }else {
                        count = 0;
                        binding.tvDnf.setVisibility(View.INVISIBLE);
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public
                void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else if (strType.equals("hospital")) {
            Query query = FirebaseDatabase.getInstance().getReference("Appointment").orderByChild("hospitalId").equalTo(auth.getUid());

            adapter = new ScheduleAdapter(list, getContext());
            binding.completeRecyclerView.setAdapter(adapter);

            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            binding.completeRecyclerView.setLayoutManager(layoutManager);
            binding.completeRecyclerView.setHasFixedSize(true);

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public
                void onDataChange(@NonNull DataSnapshot snapshot) {
                    list.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Schedule schedule = dataSnapshot.getValue(Schedule.class);
                        schedule.setConsultId(dataSnapshot.getKey());
                        if (schedule.getPaymentStatus().equals("1") && schedule.getStatus().equals("1")) {
                            list.add(schedule);
                            count++;
                        }
                    }
                    if (count == 0){
                        binding.tvDnf.setVisibility(View.VISIBLE);
                    }else {
                        count = 0;
                        binding.tvDnf.setVisibility(View.INVISIBLE);
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public
                void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else {
            Toast.makeText(getContext(), "Server down!", Toast.LENGTH_SHORT).show();
        }

        return binding.getRoot();
    }
}