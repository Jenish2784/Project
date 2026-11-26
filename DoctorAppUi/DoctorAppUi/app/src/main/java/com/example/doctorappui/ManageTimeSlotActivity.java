package com.example.doctorappui;

import static com.example.doctorappui.adapter.CalendarUtils.daysInWeekArray;
import static com.example.doctorappui.adapter.CalendarUtils.monthYearFromDate;
import static com.example.doctorappui.adapter.CalendarUtils.selectedDate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.doctorappui.adapter.CalendarAdapter;
import com.example.doctorappui.adapter.CalendarUtils;
import com.example.doctorappui.adapter.CategoryAdapter;
import com.example.doctorappui.adapter.TimeSlotAdapter;
import com.example.doctorappui.admin.AddCategory;
import com.example.doctorappui.databinding.ActivityManageTimeSlotBinding;
import com.example.doctorappui.modal.TimeSlot;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public
class ManageTimeSlotActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener {

    ActivityManageTimeSlotBinding binding;
    AlertDialog.Builder alert;
    List<String> time = new ArrayList<>();
    FirebaseAuth auth;
    FirebaseDatabase database;
    String strTime;
    String strUId = "";
    TimeSlotAdapter adapter;
    List<TimeSlot> list = new ArrayList<>();

    @SuppressLint("NewApi")
    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageTimeSlotBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        selectedDate = LocalDate.now();

        Intent intent = getIntent();
        if (intent.hasExtra("uId")){
            strUId = intent.getStringExtra("uId");
        }

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        getSupportActionBar().setTitle("Manage time slot");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.home)));


        setWeekView();

        String str = selectedDate.toString();
        //Toast.makeText(this, str, Toast.LENGTH_SHORT).show();


        adapter = new TimeSlotAdapter(list, ManageTimeSlotActivity.this);
        binding.timeRecyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(ManageTimeSlotActivity.this,4, LinearLayoutManager.VERTICAL,false);
        binding.timeRecyclerView.setLayoutManager(layoutManager);
        binding.timeRecyclerView.setHasFixedSize(true);

        Query query = FirebaseDatabase.getInstance().getReference("timeSlot").child(strUId).child(selectedDate.toString());

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public
            void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    TimeSlot timeSlot = dataSnapshot.getValue(TimeSlot.class);
                    timeSlot.setTimeId(dataSnapshot.getKey());
                        list.add(timeSlot);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public
            void onCancelled(@NonNull DatabaseError error) {

            }
        });


        binding.previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
                setWeekView();
            }
        });

        binding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
                setWeekView();
            }
        });

        binding.btnAddSlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                alert = new AlertDialog.Builder(ManageTimeSlotActivity.this);
                LayoutInflater inflater = LayoutInflater.from(ManageTimeSlotActivity.this);
                View alert_view = inflater.inflate(R.layout.time_alert_dialog, null, false);
                alert.setView(alert_view);
                AlertDialog dialog = alert.create();
                CheckBox ch1 = alert_view.findViewById(R.id.ch1);
                CheckBox ch2 = alert_view.findViewById(R.id.ch2);
                CheckBox ch3 = alert_view.findViewById(R.id.ch3);
                CheckBox ch4 = alert_view.findViewById(R.id.ch4);
                CheckBox ch5 = alert_view.findViewById(R.id.ch5);
                CheckBox ch6 = alert_view.findViewById(R.id.ch6);
                CheckBox ch7 = alert_view.findViewById(R.id.ch7);
                CheckBox ch8 = alert_view.findViewById(R.id.ch8);
                CheckBox ch9 = alert_view.findViewById(R.id.ch9);
                Button add = alert_view.findViewById(R.id.btnAdd);
                dialog.show();


                ch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public
                    void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        if (isChecked){
                            time.add(ch1.getText().toString());
                        }else {
                            time.remove(ch1.getText().toString());
                        }
                    }
                });
                ch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public
                    void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        if (isChecked){
                            time.add(ch2.getText().toString());
                        }else {
                            time.remove(ch2.getText().toString());
                        }
                    }
                });
                ch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public
                    void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        if (isChecked){
                            time.add(ch3.getText().toString());
                        }else {
                            time.remove(ch3.getText().toString());
                        }
                    }
                });
                ch4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public
                    void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        if (isChecked){
                            time.add(ch4.getText().toString());
                        }else {
                            time.remove(ch4.getText().toString());
                        }
                    }
                });
                ch5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public
                    void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        if (isChecked){
                            time.add(ch5.getText().toString());
                        }else {
                            time.remove(ch5.getText().toString());
                        }
                    }
                });
                ch6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public
                    void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        if (isChecked){
                            time.add(ch6.getText().toString());
                        }else {
                            time.remove(ch6.getText().toString());
                        }
                    }
                });
                ch7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public
                    void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        if (isChecked){
                            time.add(ch7.getText().toString());
                        }else {
                            time.remove(ch7.getText().toString());
                        }
                    }
                });
                ch8.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public
                    void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        if (isChecked){
                            time.add(ch8.getText().toString());
                        }else {
                            time.remove(ch8.getText().toString());
                        }
                    }
                });
                ch9.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public
                    void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        if (isChecked){
                            time.add(ch9.getText().toString());
                        }else {
                            time.remove(ch9.getText().toString());
                        }
                    }
                });

                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public
                    void onClick(View view) {
                        /*Toast.makeText(ManageTimeSlotActivity.this,time.toString(), Toast.LENGTH_SHORT).show();

                        Toast.makeText(ManageTimeSlotActivity.this,time.size() + "", Toast.LENGTH_SHORT).show();*/

                        for (int i =0 ; i<time.size();i++){
                            strTime = time.get(i);
                            //Toast.makeText(ManageTimeSlotActivity.this,strTime, Toast.LENGTH_SHORT).show();
                            TimeSlot timeSlot = new TimeSlot(strTime,"0");
                            database.getReference().child("timeSlot").child(strUId).child(selectedDate.toString()).push().setValue(timeSlot);


                        }
                        Toast.makeText(ManageTimeSlotActivity.this, "Time slot added!", Toast.LENGTH_SHORT).show();

                        time.clear();
                        dialog.dismiss();


                    }
                });

            }
        });
    }

    private
    void setWeekView() {
        binding.monthYearTV.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        binding.calendarRecyclerView.setLayoutManager(layoutManager);
        binding.calendarRecyclerView.setAdapter(calendarAdapter);
    }

    @Override
    public
    void onItemClick(int position, LocalDate date) {
        selectedDate = date;
        setWeekView();
        //Toast.makeText(this, selectedDate + "", Toast.LENGTH_SHORT).show();
        Query query = FirebaseDatabase.getInstance().getReference("timeSlot").child(strUId).child(selectedDate.toString());

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public
            void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    TimeSlot timeSlot = dataSnapshot.getValue(TimeSlot.class);
                    timeSlot.setTimeId(dataSnapshot.getKey());
                        list.add(timeSlot);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public
            void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected
    void onResume() {
        super.onResume();
    }

}