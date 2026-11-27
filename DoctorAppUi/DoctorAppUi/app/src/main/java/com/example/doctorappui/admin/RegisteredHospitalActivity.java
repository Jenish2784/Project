package com.example.doctorappui.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import com.example.doctorappui.R;
import com.example.doctorappui.adapter.RegisteredHospitalAdapter;
import com.example.doctorappui.databinding.ActivityRegisteredHospitalBinding;
import com.example.doctorappui.modal.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public
class RegisteredHospitalActivity extends AppCompatActivity {

    ActivityRegisteredHospitalBinding binding;
    ArrayList<Users> list = new ArrayList<>();
    FirebaseAuth auth;
    DatabaseReference databaseReference;
    RegisteredHospitalAdapter adapter;

    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisteredHospitalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public
            void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public
            void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public
            void afterTextChanged(Editable editable) {
                filterText(String.valueOf(editable));
            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Registered Hospitals");
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.home)));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        auth = FirebaseAuth.getInstance();
        adapter = new RegisteredHospitalAdapter(list,RegisteredHospitalActivity.this);
        binding.registeredHospitalRecyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(RegisteredHospitalActivity.this);
        binding.registeredHospitalRecyclerView.setLayoutManager(layoutManager);

        Query query = FirebaseDatabase.getInstance().getReference("doctor and hospital");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public
            void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Users users = dataSnapshot.getValue(Users.class);
                    users.setUserId(dataSnapshot.getKey());
                    if (users.getType().equals("hospital") && (users.getStatus().equals("1") || users.getStatus().equals("block"))){
                        list.add(users);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public
            void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private
    void filterText(String text) {
        List<Users> filteredList = new ArrayList<>();
        for (Users users : list){
            if (users.getEmailId().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(users);
            }
        }
        if (!filteredList.isEmpty()){
            adapter.setFilteredList(filteredList);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        exitFormApp();
        finish();
        return true;
    }

    @Override
    public
    void onBackPressed() {
        super.onBackPressed();
        exitFormApp();
    }
    private void exitFormApp(){
        Intent intent=new Intent(RegisteredHospitalActivity.this,AdminMainActivity.class);
        startActivity(intent);
    }

}