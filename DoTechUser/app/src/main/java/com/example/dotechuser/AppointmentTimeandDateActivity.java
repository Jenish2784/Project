package com.example.dotechuser;

import static com.example.dotechuser.calender.CalendarUtils.daysInWeekArray;
import static com.example.dotechuser.calender.CalendarUtils.monthYearFromDate;
import static com.example.dotechuser.calender.CalendarUtils.selectedDate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.example.dotechuser.Model.Doctor;
import com.example.dotechuser.Model.TimeSlote;
import com.example.dotechuser.Model.User;
import com.example.dotechuser.adapter.SloteAdapter;
//import com.example.dotechuser.adapter.TimeSlotAdapter;
import com.example.dotechuser.calender.CalendarAdapter;
import com.example.dotechuser.calender.CalendarUtils;
import com.example.dotechuser.databinding.ActivityAppointmentTimeandDateBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.harrywhewell.scrolldatepicker.OnDateSelectedListener;
import com.squareup.picasso.Picasso;

import org.joda.time.MonthDay;
import org.joda.time.YearMonth;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class AppointmentTimeandDateActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener {

    ActivityAppointmentTimeandDateBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    String strName = "";
    String strCategory = "";
    String strEmail = "";
    String strDate = "";
    String strUserId = "";
    String strDoctorUId = "";
    String strProfileImage = "";
    String strType = "";
    String strhospitalAddress = "";

    private TextView monthYearText;
    private TextView date;
    private RecyclerView calendarRecyclerView;
    private ImageView previous;
    private ImageView next;

    Uri uri;

    ArrayList<String> sectionList = new ArrayList<>();
    HashMap<String, ArrayList<String>> itemList = new HashMap<>();
    List<TimeSlote> list=new ArrayList<>();
    SloteAdapter adapter;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAppointmentTimeandDateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setTitle("Select date & time");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.status)));

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();


        Intent intent = getIntent();
        if (intent.hasExtra("email")) {
            strEmail = intent.getStringExtra("email");
        }
        if (intent.hasExtra("doctorUId")) {
            strDoctorUId = intent.getStringExtra("doctorUId");
        }

        strUserId = auth.getUid();

        //  calender

        initWidgets();
        selectedDate = LocalDate.now();

        setWeekView();

        strDate = selectedDate.toString();
        binding.tvDate.setText(strDate);

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedDate = selectedDate.minusWeeks(1);
                setWeekView();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedDate = selectedDate.plusWeeks(1);
                setWeekView();
            }
        });

        adapter = new SloteAdapter(AppointmentTimeandDateActivity.this,list);
        binding.recyclerViewTimeSlot.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,3,LinearLayoutManager.VERTICAL,false);
        binding.recyclerViewTimeSlot.setLayoutManager(layoutManager);
        binding.recyclerViewTimeSlot.setHasFixedSize(true);

        loadTimeSlotsForSelectedDate();

        //  select consultation type

        binding.radioType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                binding.chat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        strType = "Chat Consultation";
                    }
                });
                binding.videoCall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        strType = "Video Consultation";
                    }
                });
            }
        });


        //  fetch doctor data
        Query query = FirebaseDatabase.getInstance().getReference("doctor and hospital")
                .orderByChild("emailId").equalTo(strEmail);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Doctor doctor = dataSnapshot.getValue(Doctor.class);
                    strName = doctor.getName();
                    strCategory = doctor.getCategory();
                    strProfileImage = doctor.getProfileImage();
                    strhospitalAddress = doctor.getAddress();
                    binding.doctorName.setText(strName);
                    binding.doctorCategory.setText(strCategory);

                    if (!doctor.getProfileImage().equals("")) {
                        Picasso.get()
                                .load(doctor.getProfileImage())
                                .placeholder(R.drawable.placeholder)
                                .into(binding.doctorProfile);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.addProblemImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 10);
            }
        });

        binding.btnConfirmAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = getSharedPreferences("timeSlot", MODE_PRIVATE);
                String strCurrentTime = preferences.getString("time", "");
                String strTimeId = preferences.getString("timeId", "");
                if (strCurrentTime.isEmpty()) {
                    Toast.makeText(AppointmentTimeandDateActivity.this, "Please select time", Toast.LENGTH_SHORT).show();
                } else if (strType.equals("")) {
                    Toast.makeText(AppointmentTimeandDateActivity.this, "Please select consultation type", Toast.LENGTH_SHORT).show();
                } else if (binding.edtProblem.getText().toString().equals("")) {
                    Toast.makeText(AppointmentTimeandDateActivity.this, "Please enter your problem", Toast.LENGTH_SHORT).show();
                } else {
                    strDate = CalendarUtils.selectedDate.toString();
                    Intent intent = new Intent(AppointmentTimeandDateActivity.this, PaymentActivity.class);
                    intent.putExtra("email", strEmail);
                    intent.putExtra("doctorUId", strDoctorUId);
                    intent.putExtra("doctorProfileImage", strProfileImage);
                    intent.putExtra("category", strCategory);
                    intent.putExtra("selectDate", strDate);
                    intent.putExtra("time", strCurrentTime);
                    intent.putExtra("timeId", strTimeId);
                    intent.putExtra("type", strType);
                    intent.putExtra("strhospitalAddress", strhospitalAddress);
                    intent.putExtra("problem", binding.edtProblem.getText().toString());
                    /*intent.putExtra("problemImgUri",uri.toString());*/
                    startActivity(intent);

                    SharedPreferences.Editor pref = getSharedPreferences("timeSlot", MODE_PRIVATE).edit();
                    pref.putString("time", "");
                    pref.putString("timeId", "");
                    pref.apply();
                }

            }
        });
    }

    private void initWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
        previous = findViewById(R.id.previous);
        next = findViewById(R.id.next);
    }

    private void setWeekView() {
        monthYearText.setText(monthYearFromDate(selectedDate));
        ArrayList<LocalDate> days = daysInWeekArray(selectedDate);
        CalendarAdapter calendarAdapter = new CalendarAdapter(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    @Override
    public void onItemClick(int position, LocalDate date) {
        selectedDate = date;
        setWeekView();
        binding.tvDate.setText(selectedDate + "");

        loadTimeSlotsForSelectedDate();
    }

    private void loadTimeSlotsForSelectedDate() {
        if (strDoctorUId == null || strDoctorUId.isEmpty() || selectedDate == null) {
            return;
        }

        Query query1 = FirebaseDatabase.getInstance().getReference("timeSlot").child(strDoctorUId).child(selectedDate.toString());
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
                    DatabaseReference dateRef = FirebaseDatabase.getInstance().getReference("timeSlot").child(strDoctorUId).child(selectedDate.toString());
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (data.getData() != null) {
                uri = data.getData();
                binding.problemImage.setVisibility(View.VISIBLE);
                binding.addProblemImgBtn.setVisibility(View.INVISIBLE);
                binding.problemImage.setImageURI(uri);
            }
        }
    }
}