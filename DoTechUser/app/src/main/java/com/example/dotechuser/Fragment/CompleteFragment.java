package com.example.dotechuser.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.dotechuser.Model.Consultation;
import com.example.dotechuser.R;
import com.example.dotechuser.adapter.ConsultationListAdapter;
import com.example.dotechuser.databinding.FragmentCompleteBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class CompleteFragment extends Fragment {

    FragmentCompleteBinding binding;
    FirebaseAuth auth;
    String strType = "";
    List<Consultation> list = new ArrayList<>();
    ConsultationListAdapter adapter;
    String strStatus = "";

    public CompleteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCompleteBinding.inflate(inflater, container, false);

        SharedPreferences preferences = getContext().getSharedPreferences("appointmentMsg", MODE_PRIVATE);
        strType = preferences.getString("msg", "");


        if(strType.equals("Consultation")) {
            adapter = new ConsultationListAdapter(getContext(), list);
            binding.completeListRecyclerView.setAdapter(adapter);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
            layoutManager.setStackFromEnd(true);
            binding.completeListRecyclerView.setLayoutManager(layoutManager);
            binding.completeListRecyclerView.setHasFixedSize(true);

            Query query = FirebaseDatabase.getInstance().getReference("Consultation").orderByChild("userId").equalTo(auth.getUid());

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    list.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Consultation consultation = dataSnapshot.getValue(Consultation.class);
                        strStatus = consultation.getStatus();
                        if (strStatus.equals("1") && consultation.getPaymentStatus().equals("1")) {
                            consultation.setConsultId(dataSnapshot.getKey());
                            list.add(consultation);
                        }
                    }
                    if (binding != null) {
                        if (list.isEmpty()) {
                            binding.emptyView.setVisibility(View.VISIBLE);
                            binding.completeListRecyclerView.setVisibility(View.GONE);
                        } else {
                            binding.emptyView.setVisibility(View.GONE);
                            binding.completeListRecyclerView.setVisibility(View.VISIBLE);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else if(strType.equals("Appointment")){
            adapter = new ConsultationListAdapter(getContext(), list);
            binding.completeListRecyclerView.setAdapter(adapter);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
            layoutManager.setStackFromEnd(true);
            binding.completeListRecyclerView.setLayoutManager(layoutManager);
            binding.completeListRecyclerView.setHasFixedSize(true);

            Query query = FirebaseDatabase.getInstance().getReference("Appointment").orderByChild("userId").equalTo(auth.getUid());

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    list.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Consultation consultation = dataSnapshot.getValue(Consultation.class);
                        strStatus = consultation.getStatus();
                        if (strStatus.equals("1") && consultation.getPaymentStatus().equals("1")) {
                            consultation.setConsultId(dataSnapshot.getKey());
                            list.add(consultation);
                        }
                    }
                    if (binding != null) {
                        if (list.isEmpty()) {
                            binding.emptyView.setVisibility(View.VISIBLE);
                            binding.completeListRecyclerView.setVisibility(View.GONE);
                        } else {
                            binding.emptyView.setVisibility(View.GONE);
                            binding.completeListRecyclerView.setVisibility(View.VISIBLE);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else{
            Toast.makeText(getContext(), "Server Down...", Toast.LENGTH_SHORT).show();
        }

        return binding.getRoot();
    }
}