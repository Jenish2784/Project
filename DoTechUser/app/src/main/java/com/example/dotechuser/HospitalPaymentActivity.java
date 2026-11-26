package com.example.dotechuser;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dotechuser.Model.Appointment;
import com.example.dotechuser.Model.Follow;
import com.example.dotechuser.Model.Following;
import com.example.dotechuser.Model.Hospital;
import com.example.dotechuser.databinding.ActivityHospitalPaymentBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HospitalPaymentActivity extends AppCompatActivity implements PaymentResultListener {

    ActivityHospitalPaymentBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;

    String strUserId = "";
    String strHospitalUID = "";
    String strDoctorName = "";
    String strDoctorId = "";
    String strDoctorEmail = "";
    String strHospitalName = "";
    String strHospitalEmail = "";
    String strUserName = "";
    String strUserEmail = "";
    String strMobileNumber = "";

    String strDoctorProfileImage = "";
    String strGender = "";
    String strbirthDate = "";
    String strHeight = "";
    String strWeight = "";
    String strbloodGroup = "";
    String strSelectDate = "";
    String strSelectTime = "";
    String strProblem = "";
    String problemImage = "";
    String profileImage = "";
    String paymentStatus = "1";
    String strCategory = "";
    String strType = "";
    String status = "0";
    String strTimeId = "";
    String strMsgAmount = "";
    String strVcAmount = "";
    String strhospitalAddress = "";
    String strHospitalMobileNumber = "";
    String currentDate = "";
    String currentTime = "";
    String fees="";
    int msgAmount, gstAmount, totalAmount, vcAmount, totalDoctorAmount, intdoctorTotalAmount;
    String doctorTotalAmount = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHospitalPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();


        SharedPreferences pref = getSharedPreferences("hospitalDetails", MODE_PRIVATE);
        strHospitalUID = pref.getString("hospitalId", "");

        Intent intent = getIntent();
        if (intent.hasExtra("doctorEmail")) {
            strDoctorEmail = intent.getStringExtra("doctorEmail");
        }
        if (intent.hasExtra("doctorId")) {
            strDoctorId = intent.getStringExtra("doctorId");
        }
        if (intent.hasExtra("doctorName")) {
            strDoctorName = intent.getStringExtra("doctorName");
            binding.doctorName.setText(strDoctorName);
        }
        if (intent.hasExtra("hospitalEmail")) {
            strHospitalEmail = intent.getStringExtra("hospitalEmail");
        }
        if (intent.hasExtra("hospitalMobileNumber")) {
            strHospitalMobileNumber = intent.getStringExtra("hospitalMobileNumber");
        }
        if (intent.hasExtra("hospitalName")) {
            strHospitalName = intent.getStringExtra("hospitalName");
            binding.hospitalName.setText(strHospitalName);
        }
        if (intent.hasExtra("timeId")) {
            strTimeId = intent.getStringExtra("timeId");
        }

        if (intent.hasExtra("doctorProfileImage")) {
            strDoctorProfileImage = intent.getStringExtra("doctorProfileImage");
        }
        if (intent.hasExtra("category")) {
            strCategory = intent.getStringExtra("category");
        }
        if (intent.hasExtra("selectDate")) {
            strSelectDate = intent.getStringExtra("selectDate");
        }
        if (intent.hasExtra("time")) {
            strSelectTime = intent.getStringExtra("time");
        }
        if (intent.hasExtra("type")) {
            strType = intent.getStringExtra("type");
        }
        if (intent.hasExtra("problem")) {
            strProblem = intent.getStringExtra("problem");
        }
        if (intent.hasExtra("strhospitalAddress")) {
            strhospitalAddress = intent.getStringExtra("strhospitalAddress");
        }
        if (intent.hasExtra("fees")) {
            fees= intent.getStringExtra("fees");
            binding.tvAmount.setText(fees + " " + "₹");
        }

        totalAmount = Integer.parseInt(fees);

        binding.tvTime.setText(strSelectDate + "\n" + strSelectTime);

        SharedPreferences preferences = this.getSharedPreferences("userDetails", MODE_PRIVATE);
        strUserName = preferences.getString("name", "");
        strUserEmail = preferences.getString("email", "");
        strMobileNumber = preferences.getString("mobile", "");
        strGender = preferences.getString("gender", "");
        strbirthDate = preferences.getString("birthDate", "");
        strHeight = preferences.getString("height", "");
        strWeight = preferences.getString("weight", "");
        strbloodGroup = preferences.getString("bloodGroup", "");
        profileImage = preferences.getString("profileImage", "");

        binding.tvType.setText(strType);

        strUserId = auth.getUid();

        currentDate = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

        currentTime = new SimpleDateFormat("KK : mm aaa").format(Calendar.getInstance().getTime());


        Query query = FirebaseDatabase.getInstance().getReference("doctor and hospital")
                .orderByChild("emailId").equalTo(strHospitalEmail);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Hospital doctor = dataSnapshot.getValue(Hospital.class);
                    strhospitalAddress = doctor.getAddress();
                    strMsgAmount = doctor.getMsgAmount();
                    strVcAmount = doctor.getCallAmount();
                    strHospitalMobileNumber = doctor.getMobileNumber();
                   /* doctorTotalAmount = doctor.getTotalAmount();

                    *//*if (strType.equals("Online Appointment")) {
                        msgAmount = Integer.parseInt(strMsgAmount);
                        gstAmount = msgAmount * 18 / 100;

                        totalAmount = msgAmount + gstAmount;
                        binding.tvAmount.setText(totalAmount + " " + "₹");

                        intdoctorTotalAmount = Integer.parseInt(doctorTotalAmount);
                        totalDoctorAmount = intdoctorTotalAmount + msgAmount;
                    } else {

                    }*//*
                    vcAmount = Integer.parseInt(strVcAmount);
                    gstAmount = vcAmount * 18 / 100;

                    totalAmount = vcAmount + gstAmount;
                    binding.tvAmount.setText(totalAmount + " " + "₹");

                    intdoctorTotalAmount = Integer.parseInt(doctorTotalAmount);
                    totalDoctorAmount = intdoctorTotalAmount + vcAmount;*/

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                starPayment();
            }
        });
    }

    private void starPayment() {
        Checkout checkout = new Checkout();

        checkout.setImage(R.mipmap.ic_launcher);

        final Activity activity = this;

        try {
            JSONObject options = new JSONObject();
            options.put("name", R.string.app_name);
            options.put("description", "Payment for Anything");
            options.put("send_sms_hash", true);
            options.put("allow_rotation", false);

            //you can omit the image option to fetch the image from dashboard
            options.put("currency", "INR");
            options.put("amount", totalAmount * 100);  ///*totalAmount*/

            JSONObject preFill = new JSONObject();
            preFill.put("email", strHospitalEmail);
            preFill.put("contact", strHospitalMobileNumber);

            options.put("prefill", preFill);

            checkout.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment" + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String paymentId) {
        Appointment appointment = new Appointment(strDoctorProfileImage, strUserId, strHospitalUID, strType, strCategory, profileImage, strUserName, strUserEmail, strHospitalName, strHospitalEmail, strMobileNumber, strDoctorName, strGender, strbirthDate, strHeight, strWeight, strbloodGroup, strSelectDate, strSelectTime, strProblem, status,fees /*String.valueOf(totalAmount)*/, paymentId, paymentStatus, strhospitalAddress, currentDate, currentTime,problemImage);
        database.getReference().child("Appointment").push().setValue(appointment);

        Intent intent = new Intent(HospitalPaymentActivity.this, EmailSendActivity.class);
        intent.putExtra("userName", strUserName);
        intent.putExtra("userProfile", profileImage);
        intent.putExtra("doctorName", strDoctorName);
        intent.putExtra("doctorUId", strDoctorId);
        intent.putExtra("doctorEmail", strHospitalEmail);
        intent.putExtra("date", strSelectDate);
        intent.putExtra("time", strSelectTime);
        intent.putExtra("currentDate", currentDate);
        intent.putExtra("currentTime", currentTime);
        intent.putExtra("doctorMobileNo", strHospitalMobileNumber);
        intent.putExtra("amount", String.valueOf(totalAmount));
        intent.putExtra("status", "Success");
        intent.putExtra("msg", "Appointment");
        intent.putExtra("consultType", strType);
        startActivity(intent);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("doctor and hospital").child(strHospitalUID);
        databaseReference.child("totalAmount").setValue(String.valueOf(totalDoctorAmount));

        DatabaseReference databaseReferenceStatus = FirebaseDatabase.getInstance().getReference().child("timeSlot").child(strDoctorId).child(strSelectDate).child(strTimeId);
        databaseReferenceStatus.child("status").setValue("1");

        SharedPreferences.Editor prefH = getSharedPreferences("appointmentMsg", MODE_PRIVATE).edit();
        prefH.putString("msg", "Appointment");
        prefH.apply();


        //follow doctor
        Follow follow = new Follow();
        follow.setFollowedBy(FirebaseAuth.getInstance().getUid());
        follow.setFollowedAt(new Date().getTime());

        FirebaseDatabase.getInstance().getReference()
                .child("doctor and hospital")
                .child(strHospitalUID)
                .child("followers")
                .child(FirebaseAuth.getInstance().getUid()).setValue(follow)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        /*Toast.makeText(PaymentActivity.this, "you followed " + strDoctorName, Toast.LENGTH_SHORT).show();*/
                        //binding.tvAmount.setText(Html.fromHtml("<b>"+ "hello" +"</b>"));
                    }
                });

        //  following
        Following following = new Following();
        following.setFollowingBy(strHospitalUID);
        following.setFollowingAt(new Date().getTime());
        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(auth.getUid())
                .child("following")
                .child("Hospital")
                .child(strHospitalUID)
                .setValue(following).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        /*Following dFollowing = new Following();
                        dFollowing.setFollowingBy(strDoctorId);
                        dFollowing.setFollowingAt(new Date().getTime());

                        FirebaseDatabase.getInstance().getReference()
                                .child("Users")
                                .child(auth.getUid())
                                .child("following")
                                .child("Hospital")
                                .child(strHospitalUID)
                                .child(strDoctorId).setValue(dFollowing)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        //Toast.makeText(HospitalPaymentActivity.this, "you following", Toast.LENGTH_SHORT).show();
                                    }
                                });*/
                    }
                });

        SharedPreferences.Editor pref = getSharedPreferences("hospitalDetails", MODE_PRIVATE).edit();
        pref.putString("hospitalId", "");
        pref.apply();
    }

    @Override
    public void onPaymentError(int i, String paymentId) {
        Appointment appointment = new Appointment(strDoctorProfileImage, strUserId, strHospitalUID, strType, strCategory, profileImage, strUserName, strUserEmail, strHospitalName, strHospitalEmail, strMobileNumber, strDoctorName, strGender, strbirthDate, strHeight, strWeight, strbloodGroup, strSelectDate, strSelectTime, strProblem, status, fees/*String.valueOf(totalAmount)*/, paymentId, "0", strhospitalAddress, currentDate, currentTime,problemImage);
        database.getReference().child("Appointment").push().setValue(appointment);
        Toast.makeText(HospitalPaymentActivity.this, "Payment Error", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(HospitalPaymentActivity.this, MainActivity.class);
        startActivity(intent);
        SharedPreferences.Editor pref = getSharedPreferences("hospitalDetails", MODE_PRIVATE).edit();
        pref.putString("hospitalId", "");
        pref.apply();
    }
}