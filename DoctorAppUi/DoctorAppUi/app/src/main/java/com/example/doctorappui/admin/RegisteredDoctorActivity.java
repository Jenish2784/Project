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

import com.example.doctorappui.EncryptDecrypt;
import com.example.doctorappui.R;
import com.example.doctorappui.adapter.RegisteredDoctorAdapter;
import com.example.doctorappui.databinding.ActivityRegisteredDoctorBinding;
import com.example.doctorappui.modal.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public
class RegisteredDoctorActivity extends AppCompatActivity {

    ActivityRegisteredDoctorBinding binding;
    ArrayList<Users> list = new ArrayList<>();
    RegisteredDoctorAdapter adapter;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    String strCategory = "";

    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisteredDoctorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /*Intent intent = getIntent();
        if (intent.hasExtra("category")){
            strCategory=intent.getStringExtra("category");
        }*/


        getSupportActionBar().setTitle("Registered Doctor");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.home)));

        mAuth = FirebaseAuth.getInstance();
        adapter = new RegisteredDoctorAdapter(list, RegisteredDoctorActivity.this);
        binding.registeredDoctorRecyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(RegisteredDoctorActivity.this);
        binding.registeredDoctorRecyclerView.setLayoutManager(layoutManager);


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

        databaseReference = FirebaseDatabase.getInstance().getReference("doctor and hospital");
        Query query = FirebaseDatabase.getInstance().getReference("doctor and hospital");

        // fetch Available Doctor request data
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public
            void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Users users = dataSnapshot.getValue(Users.class);
                    users.setUserId(dataSnapshot.getKey());

                    String decryptType="";
                    /*try {
                        decryptType = EncryptDecrypt.decrypt(users.getType());

                    } catch (NoSuchPaddingException | NoSuchAlgorithmException |
                             InvalidAlgorithmParameterException | InvalidKeyException |
                             IllegalBlockSizeException | BadPaddingException e) {
                        throw new RuntimeException(e);
                    }*/
                    if (users.getType().equals("doctor") && (users.getStatus().equals("1") || users.getStatus().equals("block"))) {
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
    public
    void onBackPressed() {
        super.onBackPressed();
        exitFormApp();
    }
    private void exitFormApp(){
        Intent intent=new Intent(RegisteredDoctorActivity.this,AdminMainActivity.class);
        startActivity(intent);
    }

}