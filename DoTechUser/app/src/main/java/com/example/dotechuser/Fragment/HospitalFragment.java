package com.example.dotechuser.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dotechuser.Model.Hospital;
import com.example.dotechuser.Model.User;
import com.example.dotechuser.R;
import com.example.dotechuser.adapter.HospitalListAdapter;
import com.example.dotechuser.databinding.FragmentHospitalBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HospitalFragment extends Fragment {

    FragmentHospitalBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;
    List<Hospital> hospitalList = new ArrayList<>();
    HospitalListAdapter adapter;

    public HospitalFragment() {
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
        binding = FragmentHospitalBinding.inflate(inflater, container, false);

        SharedPreferences preferences = getContext().getSharedPreferences("userDetails", MODE_PRIVATE);
        String strName = preferences.getString("name", "");

        binding.userName.setText(strName);

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

        database.getReference().child("Users").child(auth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            User user = snapshot.getValue(User.class);
                            if (!user.getProfileImage().equals("")) {
                                Picasso.get()
                                        .load(user.getProfileImage())
                                        .placeholder(R.drawable.placeholder)
                                        .into(binding.imgProfile);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        adapter = new HospitalListAdapter(getContext(),hospitalList);
        binding.hospitalRecyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1, LinearLayoutManager.VERTICAL, false);
        binding.hospitalRecyclerView.setLayoutManager(layoutManager);
        binding.hospitalRecyclerView.setHasFixedSize(true);

        Query query = FirebaseDatabase.getInstance().getReference("doctor and hospital").orderByChild("status").equalTo("1");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                hospitalList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Hospital hospital = dataSnapshot.getValue(Hospital.class);
                    hospital.setHospitalId(dataSnapshot.getKey());
                    if (hospital.getType().equals("hospital")) {
                        hospitalList.add(hospital);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return binding.getRoot();
    }

    private void filterText(String text) {
        List<Hospital> filteredList = new ArrayList<>();
        for (Hospital hospital : hospitalList) {
            if (hospital.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(hospital);
            }
        }

        if (!filteredList.isEmpty()) {
            adapter.setFilteredList(filteredList);
        }
    }

}