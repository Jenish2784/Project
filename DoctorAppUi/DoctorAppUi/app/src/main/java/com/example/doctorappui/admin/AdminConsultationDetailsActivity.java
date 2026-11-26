package com.example.doctorappui.admin;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.example.doctorappui.R;
import com.example.doctorappui.adapter.ConsultationDetailsAdapter;
import com.example.doctorappui.databinding.ActivityAdminConsultationDetailsBinding;
import com.example.doctorappui.doctor.MainActivity;
import com.example.doctorappui.modal.Schedule;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public
class AdminConsultationDetailsActivity extends AppCompatActivity {

    ActivityAdminConsultationDetailsBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    List<Schedule> list = new ArrayList<>();
    ConsultationDetailsAdapter adapter;


    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminConsultationDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setTitle("Consultation history");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.home)));





        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        adapter = new ConsultationDetailsAdapter(AdminConsultationDetailsActivity.this,list);
        binding.consultDetailsRecyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AdminConsultationDetailsActivity.this,LinearLayoutManager.VERTICAL,false);
        binding.consultDetailsRecyclerView.setLayoutManager(linearLayoutManager);
        binding.consultDetailsRecyclerView.setHasFixedSize(true);

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

        //fetch consultation details
        Query query = database.getReference("Consultation");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public
            void onDataChange(@NonNull DataSnapshot snapshot) {
                int id = 0;
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Schedule schedule = dataSnapshot.getValue(Schedule.class);
                    id++;
                    schedule.setConsultId(id+"");
                    list.add(schedule);
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
        List<Schedule> filteredList = new ArrayList<>();
        for (Schedule schedule : list){
            if (schedule.getDoctorName().toLowerCase().contains(text.toLowerCase()) ||
                    schedule.getUserName().toLowerCase().contains(text.toLowerCase()) ||
                    schedule.getDate().toLowerCase().contains(text.toLowerCase()) ||
                    schedule.getTime().toLowerCase().contains(text.toLowerCase()) ||
                    schedule.getPaymentId().toLowerCase().contains(text.toLowerCase()) ||
                    schedule.getTotalAmount().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(schedule);
            }
        }

        if (!filteredList.isEmpty()){
            adapter.setFilteredList(filteredList);
        }
    }

}