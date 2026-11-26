package com.example.doctorappui.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import com.example.doctorappui.R;
import com.example.doctorappui.adapter.NewRequestAdapter;
import com.example.doctorappui.databinding.ActivityNewRequestBinding;
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
class NewRequestActivity extends AppCompatActivity {

    ActivityNewRequestBinding binding;

    ArrayList<Users> list = new ArrayList<>();
    NewRequestAdapter adapter;
    ProgressDialog progressDialog = null;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;


    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewRequestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        getSupportActionBar().setTitle("New Request");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.home)));


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

        adapter=new NewRequestAdapter(list,NewRequestActivity.this);
        binding.requestListview.setAdapter(adapter);

        LinearLayoutManager layoutManager=new LinearLayoutManager(NewRequestActivity.this);
        binding.requestListview.setLayoutManager(layoutManager);

        databaseReference = FirebaseDatabase.getInstance().getReference("doctor and hospital");
        Query query = FirebaseDatabase.getInstance().getReference("doctor and hospital").orderByChild("status").equalTo("0");

        // fetch new doctor request data
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public
            void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Users users = dataSnapshot.getValue(Users.class);
                    users.setUserId(dataSnapshot.getKey());
                        list.add(users);
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
    public
    void onBackPressed() {
        super.onBackPressed();
        exitFormApp();
    }
    private void exitFormApp(){
        Intent intent=new Intent(NewRequestActivity.this,AdminMainActivity.class);
        startActivity(intent);
    }

}