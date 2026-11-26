package com.example.doctorappui.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import com.example.doctorappui.R;
import com.example.doctorappui.adapter.PaymentAdapter;
import com.example.doctorappui.databinding.ActivityAdminPaymentBinding;
import com.example.doctorappui.modal.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public
class AdminPaymentActivity extends AppCompatActivity {

    ActivityAdminPaymentBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ArrayList<Users> list = new ArrayList<>();
    PaymentAdapter adapter;
    String strType = "doctor";

    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setTitle("Payment");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.home)));

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();


        adapter = new PaymentAdapter(AdminPaymentActivity.this,list);
        binding.paymentRecyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AdminPaymentActivity.this,LinearLayoutManager.VERTICAL,false);
        binding.paymentRecyclerView.setLayoutManager(linearLayoutManager);
        binding.paymentRecyclerView.setHasFixedSize(true);

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


        binding.typeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public
            void onCheckedChanged(RadioGroup radioGroup, int i) {
                binding.btnDoctor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public
                    void onClick(View view) {
                        adapter = new PaymentAdapter(AdminPaymentActivity.this,list);
                        binding.paymentRecyclerView.setAdapter(adapter);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AdminPaymentActivity.this,LinearLayoutManager.VERTICAL,false);
                        binding.paymentRecyclerView.setLayoutManager(linearLayoutManager);
                        binding.paymentRecyclerView.setHasFixedSize(true);

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
                });
                binding.btnHospital.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public
                    void onClick(View view) {
                        adapter = new PaymentAdapter(AdminPaymentActivity.this,list);
                        binding.paymentRecyclerView.setAdapter(adapter);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AdminPaymentActivity.this,LinearLayoutManager.VERTICAL,false);
                        binding.paymentRecyclerView.setLayoutManager(linearLayoutManager);
                        binding.paymentRecyclerView.setHasFixedSize(true);

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
                                    if (users.getType().equals("hospital") && (users.getStatus().equals("1") || users.getStatus().equals("block"))) {
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
                });
            }
        });


    }
}