package com.example.dotechuser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.dotechuser.Model.Notification;
import com.example.dotechuser.databinding.ActivityEmailSendBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class EmailSendActivity extends AppCompatActivity {

    ActivityEmailSendBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    String strUserName = "";
    String userId = "";
    String doctorId = "";
    String status = "";
    String strDoctorName = "";
    String strDoctorMobileNo = "";
    String strDoctorEmail = "";
    String date = "";
    String time = "";
    String amount = "";

    String currentDate = "";
    String currentTime = "";
    String userProfile = "";
    String consultType = "";
    String boolStatus = "0";
    String strType = "";
    String uri = "";
    String paymentId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmailSendBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://dotech-7a869-default-rtdb.firebaseio.com");

        userId = auth.getUid();

        Intent intent = getIntent();
        if (intent.hasExtra("userName")) strUserName = intent.getStringExtra("userName");
        if (intent.hasExtra("paymentId")) paymentId = intent.getStringExtra("paymentId");
        if (intent.hasExtra("doctorUId")) doctorId = intent.getStringExtra("doctorUId");
        if (intent.hasExtra("userProfile")) userProfile = intent.getStringExtra("userProfile");
        if (intent.hasExtra("status")) status = intent.getStringExtra("status");
        if (intent.hasExtra("doctorName")) strDoctorName = intent.getStringExtra("doctorName");
        if (intent.hasExtra("doctorEmail")) strDoctorEmail = intent.getStringExtra("doctorEmail");
        if (intent.hasExtra("doctorMobileNo")) strDoctorMobileNo = intent.getStringExtra("doctorMobileNo");
        if (intent.hasExtra("date")) date = intent.getStringExtra("date");
        if (intent.hasExtra("time")) time = intent.getStringExtra("time");
        if (intent.hasExtra("currentTime")) currentTime = intent.getStringExtra("currentTime");
        if (intent.hasExtra("currentDate")) currentDate = intent.getStringExtra("currentDate");
        if (intent.hasExtra("amount")) amount = intent.getStringExtra("amount");
        if (intent.hasExtra("consultType")) consultType = intent.getStringExtra("consultType");
        if (intent.hasExtra("problemImgUri")) uri = intent.getStringExtra("problemImgUri");

        // Display immediate booking confirmation with Lottie animation
        binding.payAnimation.setVisibility(View.VISIBLE);
        binding.tvMsg.setVisibility(View.VISIBLE);
        String displayMsg = "Payment of ₹" + (amount != null && !amount.isEmpty() ? amount : "500") +
                " to Dr. " + (strDoctorName != null ? strDoctorName : "") + " Successful!\nAppointment Confirmed.";
        binding.tvMsg.setText(displayMsg);

        // Push in-app notification to Doctor in Firebase Realtime Database
        if (doctorId != null && !doctorId.isEmpty()) {
            Notification notification = new Notification(userId, date, currentDate, time, currentTime, strUserName, userProfile, consultType, boolStatus);
            database.getReference("notification").child(doctorId).push().setValue(notification);
        }

        SharedPreferences preferences = getSharedPreferences("appointmentMsg", MODE_PRIVATE);
        strType = preferences.getString("msg", "Consultation");

        if ("Consultation".equalsIgnoreCase(strType)) {
            binding.myAppoinment.setVisibility(View.GONE);
            binding.myConsultation.setVisibility(View.VISIBLE);
            binding.myConsultation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent listIntent = new Intent(EmailSendActivity.this, ConsultationListActivity.class);
                    startActivity(listIntent);
                    finish();
                }
            });
        } else {
            binding.myConsultation.setVisibility(View.GONE);
            binding.myAppoinment.setVisibility(View.VISIBLE);
            binding.myAppoinment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent apptIntent = new Intent(EmailSendActivity.this, AppointmentActivity.class);
                    startActivity(apptIntent);
                    finish();
                }
            });
        }

        // Tap on confirmation text allows patient to share receipt via Gmail / Email
        binding.tvMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareReceiptViaEmail();
            }
        });
    }

    private void shareReceiptViaEmail() {
        try {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:" + (strDoctorEmail != null ? strDoctorEmail : "")));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "DoTech Appointment Booking Receipt - " + strUserName);
            String emailBody = "Dear " + (strDoctorName != null ? strDoctorName : "Doctor") + ",\n\n" +
                    "An appointment has been booked on the DoTech Healthcare Platform.\n\n" +
                    "• Patient Name: " + strUserName + "\n" +
                    "• Date: " + date + "\n" +
                    "• Time: " + time + "\n" +
                    "• Consultation Type: " + consultType + "\n" +
                    "• Payment Reference: " + paymentId + "\n" +
                    "• Amount Paid: ₹" + amount + "\n\n" +
                    "Thank you,\nDoTech Healthcare Services";
            emailIntent.putExtra(Intent.EXTRA_TEXT, emailBody);
            startActivity(Intent.createChooser(emailIntent, "Send Booking Receipt"));
        } catch (Exception e) {
            Toast.makeText(EmailSendActivity.this, "Email client not found", Toast.LENGTH_SHORT).show();
        }
    }
}
