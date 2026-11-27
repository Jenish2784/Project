package com.example.doctorappui.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import com.example.doctorappui.R;
import com.example.doctorappui.adapter.RegisteredUserAdapter;
import com.example.doctorappui.databinding.ActivityRegisteredUserBinding;
import com.example.doctorappui.modal.SystemUsers;
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
class RegisteredUserActivity extends AppCompatActivity {

    ActivityRegisteredUserBinding binding;
    ArrayList<SystemUsers> list = new ArrayList<>();
    RegisteredUserAdapter adapter;
    FirebaseAuth auth;
    DatabaseReference databaseReference;

    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisteredUserBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Registered Patients");
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.home)));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

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

        auth = FirebaseAuth.getInstance();

        adapter = new RegisteredUserAdapter(list,RegisteredUserActivity.this);
        binding.registeredUserRecyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(RegisteredUserActivity.this);
        binding.registeredUserRecyclerView.setLayoutManager(layoutManager);
        binding.registeredUserRecyclerView.setHasFixedSize(true);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        Query query = FirebaseDatabase.getInstance().getReference("Users");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public
            void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    SystemUsers systemUsers = dataSnapshot.getValue(SystemUsers.class);
                    systemUsers.setUserId(dataSnapshot.getKey());
                    list.add(systemUsers);
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
        List<SystemUsers> filteredList = new ArrayList<>();
        for (SystemUsers users : list){
            if (users.getEmail().toLowerCase().contains(text.toLowerCase())){
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
        Intent intent=new Intent(RegisteredUserActivity.this,AdminMainActivity.class);
        startActivity(intent);
    }

}