package com.example.dotechuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.dotechuser.Model.Appointment;
import com.example.dotechuser.Model.Consultation;
import com.example.dotechuser.databinding.ActivityConsultationDetailsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ConsultationDetailsActivity extends AppCompatActivity {

    ActivityConsultationDetailsBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;
    String strDoctorName = "";
    String strDoctorProfileImage = "";
    String strDoctorCategory = "";
    String strDate = "";
    String strTime = "";
    String strProblem = "";
    String strHospitalAddress = "";
    String status = "";
    String type = "";
    String paymentId = "";
    String strConsultId = "";
    String doctorId = "";
    String meetId = "";
    String currentDate = "";
    String currentTime = "";
    String strType = "";
    private String sampleToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhcGlrZXkiOiIzOTNmZGU3MC05ZWMxLTRlOTctODcxOC1lOTM3YjVjMDAzZWQiLCJwZXJtaXNzaW9ucyI6WyJhbGxvd19qb2luIl0sImlhdCI6MTY3MTU5NzM1MCwiZXhwIjoxODI5Mzg1MzUwfQ.kgOQ82EerxjN5YNm97v2PKQ_DkCdiCvepGbDPQNLNSs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConsultationDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.status)));

        checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID);
        checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID);

        Intent intent = getIntent();
        if (intent.hasExtra("paymentId")) {
            paymentId = intent.getStringExtra("paymentId");
        }
        if (intent.hasExtra("consultId")) {
            strConsultId = intent.getStringExtra("consultId");
        }

        SharedPreferences preferences = getSharedPreferences("appointmentMsg", MODE_PRIVATE);
        strType = preferences.getString("msg", "");

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        if (strType.equals("Consultation")) {

            getSupportActionBar().setTitle("Consultation detail");

            Query query = FirebaseDatabase.getInstance().getReference("Consultation").orderByChild("paymentId").equalTo(paymentId);

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Consultation consultation = dataSnapshot.getValue(Consultation.class);
                        strDoctorProfileImage = consultation.getDoctorProfileImage();
                        strDoctorName = consultation.getDoctorName();
                        doctorId = consultation.getDoctorId();
                        strDoctorCategory = consultation.getCategory();
                        strDate = consultation.getDate();
                        strTime = consultation.getTime();
                        strProblem = consultation.getProblem();
                        status = consultation.getStatus();
                        type = consultation.getType();
                        meetId = consultation.getMeetLink();
                        strHospitalAddress = consultation.getAddress();


                        currentDate = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

                        currentTime = new SimpleDateFormat("KK : mm aaa").format(Calendar.getInstance().getTime());


                        if (type.equals("Chat Consultation")) {
                            binding.btnChat.setVisibility(View.VISIBLE);
                            binding.btnCall.setVisibility(View.INVISIBLE);
                        } else if (type.equals("Video Consultation")) {
                            if (status.equals("1")) {
                                binding.tvconsultCompleted.setVisibility(View.VISIBLE);
                                binding.btnCall.setVisibility(View.INVISIBLE);
                            } else if (currentDate.equals(strDate)) {
                                binding.btnCall.setVisibility(View.VISIBLE);
                                binding.btnChat.setVisibility(View.INVISIBLE);
                                binding.tvconsultCompleted.setVisibility(View.INVISIBLE);
                            } else {
                                binding.btnChat.setVisibility(View.INVISIBLE);
                                binding.btnCall.setVisibility(View.INVISIBLE);
                                binding.tvconsultCompleted.setVisibility(View.INVISIBLE);
                            }
                        } else {
                            binding.btnChat.setVisibility(View.INVISIBLE);
                            binding.btnCall.setVisibility(View.INVISIBLE);
                        }

                        if (!strDoctorProfileImage.equals("")) {
                            Picasso.get()
                                    .load(strDoctorProfileImage)
                                    .placeholder(R.drawable.placeholder)
                                    .into(binding.doctorProfile);
                        }

                        binding.doctorName.setText(strDoctorName);
                        binding.doctorCategory.setText(strDoctorCategory);
                        binding.dateAndTime.setText(strDate + " | " + strTime);
                        binding.tvAddress.setText(strHospitalAddress);
                        binding.tvProblem.setText(strProblem);


                        binding.tvRateDoctor.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(ConsultationDetailsActivity.this, DoctorRatingActivity.class);
                                intent.putExtra("doctorProfile", strDoctorProfileImage);
                                intent.putExtra("doctorName", strDoctorName);
                                intent.putExtra("doctorId", doctorId);
                                intent.putExtra("doctorCategory", strDoctorCategory);
                                startActivity(intent);
                            }
                        });


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else if (strType.equals("Appointment")) {
            getSupportActionBar().setTitle("Appointment detail");

            Query query = FirebaseDatabase.getInstance().getReference("Appointment").orderByChild("paymentId").equalTo(paymentId);

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()){
                        Appointment appointment = snapshot1.getValue(Appointment.class);
                        strDoctorProfileImage = appointment.getDoctorProfileImage();
                        strDoctorName = appointment.getDoctorName();
                        doctorId = appointment.getDoctorId();
                        strDoctorCategory = appointment.getCategory();
                        strDate = appointment.getDate();
                        strTime = appointment.getTime();
                        strProblem = appointment.getProblem();
                        status = appointment.getStatus();
                        type = appointment.getType();
                        meetId = appointment.getMeetLink();
                        strHospitalAddress = appointment.getAddress();

                        if (type.equals("Online Appointment") && status.equals("Accept")) {
                            binding.btnChat.setVisibility(View.INVISIBLE);
                            binding.btnCall.setVisibility(View.VISIBLE);
                        }

                        if (!strDoctorProfileImage.equals("")) {
                            Picasso.get()
                                    .load(strDoctorProfileImage)
                                    .placeholder(R.drawable.placeholder)
                                    .into(binding.doctorProfile);
                        }

                        binding.doctorName.setText(strDoctorName);
                        binding.doctorCategory.setText(strDoctorCategory);
                        binding.dateAndTime.setText(strDate + " | " + strTime);
                        binding.tvAddress.setText(strHospitalAddress);
                        binding.tvProblem.setText(strProblem);


                        binding.tvRateDoctor.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(ConsultationDetailsActivity.this, DoctorRatingActivity.class);
                                intent.putExtra("doctorProfile", strDoctorProfileImage);
                                intent.putExtra("doctorName", strDoctorName);
                                intent.putExtra("doctorId", doctorId);
                                intent.putExtra("doctorCategory", strDoctorCategory);
                                startActivity(intent);
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        } else {
            Toast.makeText(this, "Server Down..", Toast.LENGTH_SHORT).show();
        }

        binding.btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (status.equals("0")) {
                    Toast.makeText(ConsultationDetailsActivity.this, "Your consultation is pending", Toast.LENGTH_SHORT).show();
                } else if (status.equals("Reject")) {
                    Toast.makeText(ConsultationDetailsActivity.this, "Your consultation is rejected", Toast.LENGTH_SHORT).show();
                } else if (status.equals("Accept") || status.equals("1")) {
                    Intent intent = new Intent(ConsultationDetailsActivity.this, ChatActivity.class);
                    intent.putExtra("doctorProfile", strDoctorProfileImage);
                    intent.putExtra("doctorName", strDoctorName);
                    intent.putExtra("doctorId", doctorId);
                    intent.putExtra("consultId", strConsultId);
                    intent.putExtra("status", status);
                    startActivity(intent);
                } else {
                    Toast.makeText(ConsultationDetailsActivity.this, "Server error!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        binding.btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (status.equals("0")) {
                    Toast.makeText(ConsultationDetailsActivity.this, "Your consultation is pending", Toast.LENGTH_SHORT).show();
                } else if (status.equals("Reject")) {
                    Toast.makeText(ConsultationDetailsActivity.this, "Your consultation is rejected", Toast.LENGTH_SHORT).show();
                } else if (status.equals("Accept") || status.equals("1")) {
                    Toast.makeText(ConsultationDetailsActivity.this, meetId + "", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ConsultationDetailsActivity.this, MeetingActivity.class);
                    intent.putExtra("token", sampleToken);
                    intent.putExtra("meetingId", meetId);
                    startActivity(intent);
                } else {
                    Toast.makeText(ConsultationDetailsActivity.this, "Server error!", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private static final int PERMISSION_REQ_ID = 22;

    private static final String[] REQUESTED_PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
    };

    private boolean checkSelfPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, requestCode);
            return false;
        }
        return true;
    }

}