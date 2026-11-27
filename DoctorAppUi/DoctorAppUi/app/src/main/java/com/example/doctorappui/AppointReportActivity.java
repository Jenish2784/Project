package com.example.doctorappui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.doctorappui.databinding.ActivityAppointReportBinding;
import com.example.doctorappui.modal.Schedule;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

public
class AppointReportActivity extends AppCompatActivity {

    ActivityAppointReportBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    long totalConsult = 0;
    long pendingConsult = 0;
    long inProgressConsult = 0;
    long completedConsult = 0;
    long rejectedConsult = 0;
    String strType = "";
    double pending = 0;
    double inProgress = 0;
    double completed = 0;
    double rejected = 0;

    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAppointReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Consult Report");
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.home)));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        SharedPreferences preferences = getSharedPreferences("userData", MODE_PRIVATE);
        strType = preferences.getString("type", "");

        // fetch doctor total consult
        if (strType.equals("doctor")) {
            Query query = FirebaseDatabase.getInstance().getReference("Consultation").orderByChild("doctorId").equalTo(auth.getUid());
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public
                void onDataChange(@NonNull DataSnapshot snapshot) {
                    totalConsult = snapshot.getChildrenCount();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Schedule schedule = dataSnapshot.getValue(Schedule.class);

                        if (schedule.getStatus().equals("0") && schedule.getPaymentStatus().equals("1")) {
                            pendingConsult++;
                        }
                        if (schedule.getStatus().equals("Accept")) {
                            inProgressConsult++;
                        }
                        if (schedule.getStatus().equals("1")) {
                            completedConsult++;
                        }
                        if (schedule.getStatus().equals("Reject")) {
                            rejectedConsult++;
                        }

                    }
                    setData();
                }

                @Override
                public
                void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else if (strType.equals("hospital")) {
            Query query = FirebaseDatabase.getInstance().getReference("Appointment").orderByChild("hospitalId").equalTo(auth.getUid());
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public
                void onDataChange(@NonNull DataSnapshot snapshot) {
                    totalConsult = snapshot.getChildrenCount();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Schedule schedule = dataSnapshot.getValue(Schedule.class);

                        if (schedule.getStatus().equals("0") && schedule.getPaymentStatus().equals("1")) {
                            pendingConsult++;
                        }
                        if (schedule.getStatus().equals("Accept")) {
                            inProgressConsult++;
                        }
                        if (schedule.getStatus().equals("1")) {
                            completedConsult++;
                        }
                        if (schedule.getStatus().equals("Reject")) {
                            rejectedConsult++;
                        }

                    }
                    setData();
                }
                @Override
                public
                void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else {
            Toast.makeText(this, "Server down!", Toast.LENGTH_SHORT).show();
        }
    }

    private
    void setData() {

        if (pendingConsult == 0 && inProgressConsult == 0 && completedConsult == 0 && rejectedConsult == 0) {
            binding.pieChart.addPieSlice(
                    new PieModel(
                            "Pending",
                            100.00f,
                            Color.parseColor("#66BB6A")
                    )
            );
        } else {
            binding.tvPending.setText(Long.toString(pendingConsult));
            binding.tvInProgress.setText(Long.toString(inProgressConsult));
            binding.tvCompleted.setText(Long.toString(completedConsult));
            binding.tvRejected.setText(Long.toString(rejectedConsult));

            pending = (pendingConsult * 100 / totalConsult);
            inProgress = (inProgressConsult * 100 / totalConsult);
            completed = (completedConsult * 100 / totalConsult);
            rejected = (rejectedConsult * 100 / totalConsult);

            binding.pieChart.addPieSlice(
                    new PieModel(
                            "Pending",
                            (float) pending,
                            Color.parseColor("#29B6F6")
                    )
            );
            binding.pieChart.addPieSlice(
                    new PieModel(
                            "InProgress",
                            (float) inProgress,
                            Color.parseColor("#FFA726")
                    )
            );
            binding.pieChart.addPieSlice(
                    new PieModel(
                            "Completed",
                            (float) completed,
                            Color.parseColor("#66BB6A")
                    )
            );
            binding.pieChart.addPieSlice(
                    new PieModel(
                            "Rejected",
                            (float) rejected,
                            Color.parseColor("#EF5350")
                    )
            );

            binding.pieChart.startAnimation();
        }


    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}