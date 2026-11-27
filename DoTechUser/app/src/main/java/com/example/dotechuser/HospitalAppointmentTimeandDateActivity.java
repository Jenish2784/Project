package com.example.dotechuser;

import static com.example.dotechuser.calender.CalendarUtils.daysInWeekArray;
import static com.example.dotechuser.calender.CalendarUtils.monthYearFromDate;
import static com.example.dotechuser.calender.CalendarUtils.selectedDate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dotechuser.Model.Doctor;
import com.example.dotechuser.Model.Hospital;
import com.example.dotechuser.Model.TimeSlote;
import com.example.dotechuser.adapter.SloteAdapter;
//import com.example.dotechuser.adapter.TimeSlotAdapter;
import com.example.dotechuser.calender.CalendarAdapter;
import com.example.dotechuser.calender.CalendarUtils;
import com.example.dotechuser.databinding.ActivityHospitalAppointmentTimeandDateBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HospitalAppointmentTimeandDateActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener {

    ActivityHospitalAppointmentTimeandDateBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    String strProfileImage = "";
    String doctorId = "";
    String strDoctorName = "";
    String strCategory = "";
    String strEmail = "";
    String strHospitalEmail = "";
    String strDate = "";
    String strType = "";
    String fees = "";
    String strhospitalAddress = "";
    String strhospitalMobileNumber = "";
    String hospitalName = "";

    ArrayList<String> sectionList = new ArrayList<>();
    HashMap<String, ArrayList<String>> itemList = new HashMap<>();
    List<TimeSlote> list=new ArrayList<>();
    SloteAdapter adapter;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHospitalAppointmentTimeandDateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Select date & time");
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.status)));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        SharedPreferences preferences = getSharedPreferences("hospitalDetails", MODE_PRIVATE);
        hospitalName = preferences.getString("hospitalName", "");
        strHospitalEmail = preferences.getString("strEmailId", "");

        binding.tvHospitalName.setText(hospitalName);

        Intent intent = getIntent();
        if (intent.hasExtra("doctorProfileImage")) {
            strProfileImage = intent.getStringExtra("doctorProfileImage");
            if (!strProfileImage.equals("")){
                Picasso.get()
                        .load(strProfileImage)
                        .placeholder(R.drawable.placeholder)
                        .into(binding.doctorProfile);
            }
            else{
                binding.doctorProfile.setImageResource(R.drawable.placeholder);
            }
        }
        if (intent.hasExtra("doctorName")) {
            strDoctorName = intent.getStringExtra("doctorName");
            binding.doctorName.setText(strDoctorName);
        }
        if (intent.hasExtra("fees")) {
            fees = intent.getStringExtra("fees");
        }
        if (intent.hasExtra("doctorId")) {
            doctorId = intent.getStringExtra("doctorId");
        }
        if (intent.hasExtra("doctorCategory")) {
            strCategory = intent.getStringExtra("doctorCategory");
            binding.doctorCategory.setText(strCategory);
        }

        //  calender
        selectedDate = LocalDate.now();

        setWeekView();

        strDate = selectedDate.toString();
        binding.tvDate.setText(strDate);

        binding.previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedDate = selectedDate.minusWeeks(1);
                setWeekView();
            }
        });

        binding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedDate = selectedDate.plusWeeks(1);
                setWeekView();
            }
        });

        adapter = new SloteAdapter(HospitalAppointmentTimeandDateActivity.this,list);
        binding.recyclerViewTimeSlot.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,3, LinearLayoutManager.VERTICAL,false);
        binding.recyclerViewTimeSlot.setLayoutManager(layoutManager);
        binding.recyclerViewTimeSlot.setHasFixedSize(true);

        loadHospitalTimeSlots();

        //  select consultation type
        binding.radioType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                binding.online.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        strType = "Online Appointment";
                    }
                });
                binding.physical.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        strType = "Physical Appointment";
                    }
                });
            }
        });


        //  fetch doctor data
        Query query = FirebaseDatabase.getInstance().getReference("doctor and hospital")
                .orderByChild("emailId").equalTo(strHospitalEmail);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Hospital doctor = dataSnapshot.getValue(Hospital.class);
                    hospitalName = doctor.getName();
                    strhospitalAddress= doctor.getAddress();
                    strhospitalMobileNumber = doctor.getMobileNumber();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.btnConfirmAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = getSharedPreferences("timeSlot", MODE_PRIVATE);
                String strCurrentTime = preferences.getString("time", "");
                String strTimeId = preferences.getString("timeId", "");
                if (strCurrentTime.isEmpty()) {
                    Toast.makeText(HospitalAppointmentTimeandDateActivity.this, "Please select time", Toast.LENGTH_SHORT).show();
                } else if (strType.equals("")) {
                    Toast.makeText(HospitalAppointmentTimeandDateActivity.this, "Please select consultation type", Toast.LENGTH_SHORT).show();
                } else if (binding.edtProblem.getText().toString().equals("")) {
                    Toast.makeText(HospitalAppointmentTimeandDateActivity.this, "Please enter your problem", Toast.LENGTH_SHORT).show();
                } else {
                    strDate = CalendarUtils.selectedDate.toString();
                    Intent intent = new Intent(HospitalAppointmentTimeandDateActivity.this, HospitalPaymentActivity.class);
                    intent.putExtra("doctorEmail", strEmail);
                    intent.putExtra("doctorName", strDoctorName);
                    intent.putExtra("doctorId", doctorId);
                    intent.putExtra("hospitalEmail", strHospitalEmail);
                    intent.putExtra("hospitalName", hospitalName);
                    intent.putExtra("hospitalMobileNumber", strhospitalMobileNumber);
                    intent.putExtra("doctorProfileImage", strProfileImage);
                    intent.putExtra("category", strCategory);
                    intent.putExtra("selectDate", strDate);
                    intent.putExtra("time", strCurrentTime);
                    intent.putExtra("timeId", strTimeId);
                    intent.putExtra("type", strType);
                    intent.putExtra("strhospitalAddress", strhospitalAddress);
                    intent.putExtra("problem", binding.edtProblem.getText().toString());
                    intent.putExtra("fees",fees);
                    startActivity(intent);

                    SharedPreferences.Editor pref = getSharedPreferences("timeSlot", MODE_PRIVATE).edit();
                    pref.putString("time", "");
                    pref.putString("timeId", "");
                    pref.apply();
                }
            }
        });


    }

    private void setWeekView() {
        binding.monthYearTV.setText(monthYearFromDate(selectedDate));
        ArrayList<LocalDate> days = daysInWeekArray(selectedDate);
        CalendarAdapter calendarAdapter = new CalendarAdapter(days, this::onItemClick);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        binding.calendarRecyclerView.setLayoutManager(layoutManager);
        binding.calendarRecyclerView.setAdapter(calendarAdapter);
    }

    @Override
    public void onItemClick(int position, LocalDate date) {
        CalendarUtils.selectedDate = date;
        setWeekView();
        binding.tvDate.setText(selectedDate + "");

        loadHospitalTimeSlots();
    }

    private void loadHospitalTimeSlots() {
        if (doctorId == null || doctorId.isEmpty() || selectedDate == null) {
            return;
        }

        Query query1 = FirebaseDatabase.getInstance().getReference("timeSlot").child(doctorId).child(selectedDate.toString());
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                if (snapshot.getChildrenCount() > 0) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        TimeSlote timeSlote = dataSnapshot.getValue(TimeSlote.class);
                        if (timeSlote != null) {
                            timeSlote.setTimeId(dataSnapshot.getKey());
                            if ("0".equals(timeSlote.getStatus())) {
                                list.add(timeSlote);
                            }
                        }
                    }
                } else {
                    // Auto-generate default available slots for this doctor and date
                    String[] defaultTimes = new String[]{
                        "09:00 AM", "10:00 AM", "11:00 AM", "12:00 PM",
                        "02:00 PM", "03:00 PM", "04:00 PM", "05:00 PM", "06:00 PM"
                    };
                    DatabaseReference dateRef = FirebaseDatabase.getInstance().getReference("timeSlot").child(doctorId).child(selectedDate.toString());
                    for (String t : defaultTimes) {
                        String slotKey = dateRef.push().getKey();
                        if (slotKey != null) {
                            TimeSlote s = new TimeSlote(t, "0", slotKey);
                            dateRef.child(slotKey).setValue(s);
                            list.add(s);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (list.isEmpty()) {
                    String[] defaultTimes = new String[]{
                        "09:00 AM", "10:00 AM", "11:00 AM", "12:00 PM",
                        "02:00 PM", "03:00 PM", "04:00 PM", "05:00 PM", "06:00 PM"
                    };
                    for (int i = 0; i < defaultTimes.length; i++) {
                        list.add(new TimeSlote(defaultTimes[i], "0", "slot_" + i));
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}