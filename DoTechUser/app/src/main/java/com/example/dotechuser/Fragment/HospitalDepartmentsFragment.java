package com.example.dotechuser.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dotechuser.Model.Hospital;
import com.example.dotechuser.adapter.HospitalDepartmentAdapter;
import com.example.dotechuser.databinding.FragmentHospitalDepartmentsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HospitalDepartmentsFragment extends Fragment {

    FragmentHospitalDepartmentsBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;
    List<Hospital> list = new ArrayList<>();
    HospitalDepartmentAdapter adapter;
    String strHospitalId = "";

    public HospitalDepartmentsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHospitalDepartmentsBinding.inflate(inflater, container, false);

        SharedPreferences preferences = getContext().getSharedPreferences("hospitalDetails", MODE_PRIVATE);
        strHospitalId = preferences.getString("hospitalId", "");

        //  set adapter
        adapter = new HospitalDepartmentAdapter(getContext(), list);
        binding.hospitalDepartmentsRecycleView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2, LinearLayoutManager.VERTICAL, false);
        binding.hospitalDepartmentsRecycleView.setLayoutManager(layoutManager);
        binding.hospitalDepartmentsRecycleView.setHasFixedSize(true);

        Query query = FirebaseDatabase.getInstance().getReference("doctor and hospital").child(strHospitalId).child("department");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Hospital hospital = dataSnapshot.getValue(Hospital.class);
                    hospital.setHospitalId(dataSnapshot.getKey());
                    list.add(hospital);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return binding.getRoot();
    }
}