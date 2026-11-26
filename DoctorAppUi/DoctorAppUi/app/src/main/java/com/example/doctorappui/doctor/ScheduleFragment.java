package com.example.doctorappui.doctor;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.doctorappui.NotificationActivity;
import com.example.doctorappui.R;
import com.example.doctorappui.adapter.ScheduleAdapter;
import com.example.doctorappui.databinding.FragmentScheduleBinding;
import com.example.doctorappui.modal.Schedule;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public
class ScheduleFragment extends Fragment {

    FragmentScheduleBinding binding;
    ArrayList<Schedule> list = new ArrayList<>();
    ScheduleAdapter adapter;
    FirebaseAuth auth;
    FirebaseDatabase database;
    String strName = "";
    String strProfileImage = "";
    String strType = "";
    String strDate = "", strMonth = "", strYear = "";
    String strSelectDate = "";
    int count = 0;
    int time = 0;
    Handler handler;
    int todayDate;
    int consultTomorrowDate;


    public
    ScheduleFragment() {
        // Required empty public construc
        // tor
    }

    @Override
    public
    void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    @Override
    public
    View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentScheduleBinding.inflate(inflater, container, false);


        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDateFormat = new SimpleDateFormat("dd");
        String strCurrent = currentDateFormat.format(calendar.getTime()).toString();
        //Toast.makeText(this,strCurrent, Toast.LENGTH_SHORT).show();

        todayDate = Integer.parseInt(strCurrent);


        SharedPreferences preferences = this.getContext().getSharedPreferences("userData", MODE_PRIVATE);
        strName = preferences.getString("name", "");
        strProfileImage = preferences.getString("profileImage", "");
        strType = preferences.getString("type", "");

        if (strType.equals("doctor")) {
            binding.doctorName.setText("Hello, Dr. " + strName);
        } else if (strType.equals("hospital")) {
            binding.doctorName.setText("Hello, " + strName);
        }

        SimpleDateFormat date = new SimpleDateFormat("dd");
        strDate = date.format(new Date());
        SimpleDateFormat month = new SimpleDateFormat("MM");
        strMonth = month.format(new Date());
        SimpleDateFormat year = new SimpleDateFormat("yyyy");
        strYear = year.format(new Date());


        if (!strProfileImage.equals("")) {
            Picasso.get()
                    .load(strProfileImage)
                    .placeholder(R.drawable.placeholder)
                    .into(binding.profileImage);
        } else {
            binding.profileImage.setImageResource(R.drawable.dotech_logo);
        }

        // binding.datePicker.setStartDate(Integer.parseInt(strDate), Integer.parseInt(strMonth), Integer.parseInt(strYear));

        adapter = new ScheduleAdapter(list, getContext());
        binding.scheduleRecyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.scheduleRecyclerView.setLayoutManager(layoutManager);
        binding.scheduleRecyclerView.setHasFixedSize(true);


        if (strType.equals("doctor")) {
            Query query = FirebaseDatabase.getInstance().getReference("Consultation").orderByChild("doctorId").equalTo(auth.getUid());
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public
                void onDataChange(@NonNull DataSnapshot snapshot) {
                    list.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Schedule schedule = dataSnapshot.getValue(Schedule.class);
                        schedule.setConsultId(dataSnapshot.getKey());
                        if (schedule.getPaymentStatus().equals("1") && (schedule.getStatus().equals("0") || schedule.getStatus().equals("Accept"))) {
                            list.add(schedule);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public
                void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else if (strType.equals("hospital")){
            Query query = FirebaseDatabase.getInstance().getReference("Appointment").orderByChild("hospitalId").equalTo(auth.getUid());
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public
                void onDataChange(@NonNull DataSnapshot snapshot) {
                    list.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Schedule schedule = dataSnapshot.getValue(Schedule.class);
                        schedule.setConsultId(dataSnapshot.getKey());
                        if (schedule.getPaymentStatus().equals("1") && (schedule.getStatus().equals("0") || schedule.getStatus().equals("Accept") || schedule.getStatus().equals("AcceptH"))) {
                            list.add(schedule);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public
                void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else {
            Toast.makeText(getContext(), "Server down!", Toast.LENGTH_SHORT).show();
        }

        binding.iconNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                Intent intent = new Intent(getContext(), NotificationActivity.class);
                startActivity(intent);
            }
        });


        return binding.getRoot();
    }


}



/*binding.datePicker.getSelectedDate(new OnDateSelectedListener() {
            @Override
            public
            void onDateSelected(@androidx.annotation.Nullable Date date) {
                strSelectDate = date.toString();
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public
                    void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Schedule schedule = dataSnapshot.getValue(Schedule.class);
                            schedule.setConsultId(dataSnapshot.getKey());
                            if (strSelectDate.equals(schedule.getDate())){
                                list.add(schedule);
                                count++;
                            }
                        }
                        if (count==0){
                            binding.tvDnf.setVisibility(View.VISIBLE);
                        }
                        else {
                            binding.tvDnf.setVisibility(View.INVISIBLE);
                            count = 0;
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public
                    void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });*/


//data fetch thase tyare thase
                        /*String[] seperated = schedule.getDate().split("-");
                        String consultDate = seperated[2];
                        int date = Integer.parseInt(consultDate);
                        consultTomorrowDate = date + 1;



                        if (todayDate == consultTomorrowDate){
                            //Toast.makeText(getContext(), "true", Toast.LENGTH_SHORT).show();
                            Toast.makeText(getContext(),schedule.getConsultId(), Toast.LENGTH_SHORT).show();
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Consultation").child(schedule.getConsultId());
                            databaseReference.child("status").setValue("1");
                        }else {
                            //Toast.makeText(getContext(), "false", Toast.LENGTH_SHORT).show();
                        }*/


 /*Query query = FirebaseDatabase.getInstance().getReference("Users");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public
            void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Users users = dataSnapshot.getValue(Users.class);
                    users.setUserId(dataSnapshot.getKey());
                    ScheduleData scheduleData = dataSnapshot.getValue(ScheduleData.class);
                    list.add(scheduleData);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public
            void onCancelled(@NonNull DatabaseError error) {

            }
        });
*/
        /*scheduleData.add(new ScheduleData(R.drawable.ic_launcher_background,"Voice Call","Dr.Abc","8:20","Uncompleted"));
        scheduleData.add(new ScheduleData(R.drawable.doctor,"Video Call","Dr.Xyz","8:20","Uncompleted"));
        scheduleData.add(new ScheduleData(R.drawable.doctor,"Voice Call","Dr.Pqr","8:20","Completed"));
        scheduleData.add(new ScheduleData(R.drawable.doctor,"Voice Call","Dr.Demo","8:20","Uncompleted"));
        scheduleData.add(new ScheduleData(R.drawable.doctor,"Voice Call","Dr.Abc","8:20","Completed"));
        scheduleData.add(new ScheduleData(R.drawable.doctor,"Voice Call","Dr.Abc","8:20","Completed"));
        scheduleData.add(new ScheduleData(R.drawable.doctor,"Voice Call","Dr.Abc","8:20","Completed"));
        scheduleData.add(new ScheduleData(R.drawable.doctor,"Voice Call","Dr.Abc","8:20","Completed"));
        scheduleData.add(new ScheduleData(R.drawable.doctor,"Voice Call","Dr.Abc","8:20","Completed"));
        scheduleData.add(new ScheduleData(R.drawable.doctor,"Voice Call","Dr.Abc","8:20","Completed"));
        scheduleData.add(new ScheduleData(R.drawable.doctor,"Voice Call","Dr.Abc","8:20","Completed"));
        scheduleData.add(new ScheduleData(R.drawable.doctor,"Voice Call","Dr.Abc","8:20","Completed"));
        scheduleData.add(new ScheduleData(R.drawable.ic_launcher_background,"Voice Call","Dr.Abc","8:20","Completed"));*/