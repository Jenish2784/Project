package com.example.dotechuser;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.dotechuser.Model.Appointment;
import com.example.dotechuser.Model.Consultation;
import com.example.dotechuser.adapter.PaymentAppoinrmentHistroyAdapter;
import com.example.dotechuser.adapter.PaymentHistoryAdapter;
import com.example.dotechuser.databinding.ActivityPaymentHistoryBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PaymentHistoryActivity extends AppCompatActivity {

    ActivityPaymentHistoryBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    List<String> spinnerList = new ArrayList<>();
    ArrayAdapter spinnerAdapter = null;
    List<Consultation> list = new ArrayList<>();
    List<Appointment> appointmentList = new ArrayList<>();

    PaymentAppoinrmentHistroyAdapter appoinrmentHistroyAdapter;
    PaymentHistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        binding.toolbar.setTitle(getResources().getString(R.string.payment_history));

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        spinnerList.add("Consultation");
        spinnerList.add("Appointment");

        spinnerAdapter = new ArrayAdapter(PaymentHistoryActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerList);
        binding.spinner.setAdapter(spinnerAdapter);

        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spinnerList.get(i).equals("Consultation")) {
                    adapter = new PaymentHistoryAdapter(PaymentHistoryActivity.this, list);
                    binding.paymentHistoryRecyclerView.setAdapter(adapter);
                    LinearLayoutManager manager = new LinearLayoutManager(PaymentHistoryActivity.this, LinearLayoutManager.VERTICAL, false);
                    binding.paymentHistoryRecyclerView.setLayoutManager(manager);
                    binding.paymentHistoryRecyclerView.setHasFixedSize(true);

                    Query query = database.getReference("Consultation").orderByChild("userId").equalTo(auth.getUid());

                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int counter = 0;
                            list.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Consultation consultation = dataSnapshot.getValue(Consultation.class);
                                counter++;
                                consultation.setConsultId(counter + "");
                                list.add(consultation);
                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else if (spinnerList.get(i).equals("Appointment")) {
                    appoinrmentHistroyAdapter = new PaymentAppoinrmentHistroyAdapter(PaymentHistoryActivity.this, appointmentList);
                    binding.paymentHistoryRecyclerView.setAdapter(appoinrmentHistroyAdapter);
                    LinearLayoutManager manager = new LinearLayoutManager(PaymentHistoryActivity.this, LinearLayoutManager.VERTICAL, false);
                    binding.paymentHistoryRecyclerView.setLayoutManager(manager);
                    binding.paymentHistoryRecyclerView.setHasFixedSize(true);

                    Query query = database.getReference("Appointment").orderByChild("userId").equalTo(auth.getUid());

                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int counter = 0;
                            list.clear();
                            appointmentList.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Appointment appointment = dataSnapshot.getValue(Appointment.class);
                                counter++;
                                appointment.setConsultId(counter + "");
                                appointmentList.add(appointment);
                            }
                            appoinrmentHistroyAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else {
                    Toast.makeText(PaymentHistoryActivity.this, "Server down!", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }
}