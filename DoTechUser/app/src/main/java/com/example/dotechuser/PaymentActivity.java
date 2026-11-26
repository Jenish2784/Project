package com.example.dotechuser;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dotechuser.Model.Consultation;
import com.example.dotechuser.Model.Doctor;
import com.example.dotechuser.Model.Follow;
import com.example.dotechuser.Model.Following;
import com.example.dotechuser.databinding.ActivityPaymentBinding;
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

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {

    ActivityPaymentBinding binding;

    FirebaseAuth auth;
    FirebaseDatabase database;

    String strUserId = "";
    String strDoctorUId = "";
    String strDoctorName = "";
    String strDoctorMobileNo = "";
    String strEmail = "";
    String strUserName = "";
    String strUserEmail = "";
    String strMobileNumber = "";
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
    String doctorProfileImage = "";
    String strCategory = "";
    String strType = "";
    String strTimeId = "";
    String strMsgAmount = "";
    String strVcAmount = "";
    String strMeetLink = "";
    String strhospitalAddress = "";
    String currentDate = "";
    String currentTime = "";
    int msgAmount, gstAmount, totalAmount, vcAmount, totalDoctorAmount, intdoctorTotalAmount;
    String doctorTotalAmount = "";

    String uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://dotech-7a869-default-rtdb.firebaseio.com");
        Checkout.preload(getApplicationContext());

        Intent intent = getIntent();
        if (intent.hasExtra("email")) {
            strEmail = intent.getStringExtra("email");
        }
        if (intent.hasExtra("timeId")) {
            strTimeId = intent.getStringExtra("timeId");
        }
        if (intent.hasExtra("doctorUId")) {
            strDoctorUId = intent.getStringExtra("doctorUId");
        }
        if (intent.hasExtra("doctorProfileImage")) {
            doctorProfileImage = intent.getStringExtra("doctorProfileImage");
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
        if (intent.hasExtra("problemImgUri")) {
            uri = intent.getStringExtra("problemImgUri");
        }


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

        binding.tvTime.setText(strSelectDate + "\n" + strSelectTime);
        binding.tvType.setText(strType);

        strUserId = auth.getUid();

        currentDate = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

        currentTime = new SimpleDateFormat("KK : mm aaa").format(Calendar.getInstance().getTime());


        Query query = FirebaseDatabase.getInstance().getReference("doctor and hospital")
                .orderByChild("emailId").equalTo(strEmail);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Doctor doctor = dataSnapshot.getValue(Doctor.class);
                    strDoctorName = doctor.getName();
                    strhospitalAddress = doctor.getAddress();
                    strDoctorMobileNo = doctor.getMobileNumber();
                    binding.doctorName.setText(strDoctorName);
                    strMsgAmount = doctor.getMsgAmount();
                    strVcAmount = doctor.getCallAmount();
                    doctorTotalAmount = doctor.getTotalAmount();

                    if (strType.equals("Chat Consultation")) {
                        msgAmount = Integer.parseInt(strMsgAmount);
                        gstAmount = msgAmount * 18 / 100;

                        totalAmount = msgAmount + gstAmount;
                        binding.tvAmount.setText(totalAmount + " " + "₹");

                        intdoctorTotalAmount = Integer.parseInt(doctorTotalAmount);
                        totalDoctorAmount = intdoctorTotalAmount + msgAmount;
                    } else {
                        vcAmount = Integer.parseInt(strVcAmount);
                        gstAmount = vcAmount * 18 / 100;

                        totalAmount = vcAmount + gstAmount;
                        binding.tvAmount.setText(totalAmount + " " + "₹");

                        intdoctorTotalAmount = Integer.parseInt(doctorTotalAmount);
                        totalDoctorAmount = intdoctorTotalAmount + vcAmount;
                    }
                    /*Toast.makeText(PaymentActivity.this,doctorTotalAmount, Toast.LENGTH_SHORT).show();*/
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

        binding.btnSubmit.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(PaymentActivity.this, "Demo Mode: Simulating Successful Payment...", Toast.LENGTH_SHORT).show();
                onPaymentSuccess("pay_demo_" + System.currentTimeMillis());
                return true;
            }
        });

    }

    private void starPayment() {
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_U4pVXE6ygH2GS1");
        checkout.setImage(R.mipmap.ic_launcher);

        final Activity activity = this;

        try {
            int payAmount = (totalAmount > 0 ? totalAmount : 500) * 100;

            JSONObject options = new JSONObject();
            options.put("name", "DoTech Healthcare");
            options.put("description", "Doctor Consultation Payment");
            options.put("send_sms_hash", true);
            options.put("allow_rotation", false);
            options.put("currency", "INR");
            options.put("amount", payAmount);

            JSONObject preFill = new JSONObject();
            preFill.put("email", strUserEmail != null && !strUserEmail.isEmpty() ? strUserEmail : strEmail);
            preFill.put("contact", strMobileNumber);

            options.put("prefill", preFill);

            checkout.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    @Override
    public void onPaymentSuccess(String paymentId) {

        Consultation consultation = new Consultation(strUserId, strDoctorUId, doctorProfileImage, strType, strCategory, profileImage, strUserName, strUserEmail, strMobileNumber, strDoctorName, strGender, strbirthDate, strHeight, strWeight, strbloodGroup, strSelectDate, strSelectTime, strProblem, "0", String.valueOf(totalAmount), paymentId, "1", strhospitalAddress, strMeetLink, currentDate, currentTime,problemImage);
        database.getReference().child("Consultation").push().setValue(consultation);

        Intent intent = new Intent(PaymentActivity.this, EmailSendActivity.class);
        intent.putExtra("userName", strUserName);
        intent.putExtra("userProfile", profileImage);
        intent.putExtra("doctorUId", strDoctorUId);
        intent.putExtra("doctorName", strDoctorName);
        intent.putExtra("doctorEmail", strEmail);
        intent.putExtra("time", strSelectTime);
        intent.putExtra("date", strSelectDate);
        intent.putExtra("currentDate", currentDate);
        intent.putExtra("currentTime", currentTime);
        intent.putExtra("doctorMobileNo", strDoctorMobileNo);
        intent.putExtra("status", "Success");
        intent.putExtra("msg", "Consultation");
        intent.putExtra("consultType", strType);
        intent.putExtra("paymentId",paymentId);
        intent.putExtra("amount", String.valueOf(totalAmount));
        intent.putExtra("problemImgUri",uri);
        startActivity(intent);

        SharedPreferences.Editor prefH = getSharedPreferences("appointmentMsg", MODE_PRIVATE).edit();
        prefH.putString("msg", "Consultation");
        prefH.apply();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("doctor and hospital").child(strDoctorUId);
        databaseReference.child("totalAmount").setValue(String.valueOf(totalDoctorAmount));

        DatabaseReference databaseReferenceStatus = FirebaseDatabase.getInstance().getReference().child("timeSlot").child(strDoctorUId).child(strSelectDate).child(strTimeId);
        databaseReferenceStatus.child("status").setValue("1");


        //follow doctor
        Follow follow = new Follow();
        follow.setFollowedBy(FirebaseAuth.getInstance().getUid());
        follow.setFollowedAt(new Date().getTime());

        FirebaseDatabase.getInstance().getReference()
                .child("doctor and hospital")
                .child(strDoctorUId)
                .child("followers")
                .child(FirebaseAuth.getInstance().getUid()).setValue(follow)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //Toast.makeText(PaymentActivity.this, "you followed " + strDoctorName, Toast.LENGTH_SHORT).show();
                        //binding.tvAmount.setText(Html.fromHtml("<b>"+ "hello" +"</b>"));
                    }
                });

        //  folloeing
        Following following = new Following();
        following.setFollowingBy(strDoctorUId);
        following.setFollowingAt(new Date().getTime());
        
        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(auth.getUid())
                .child("following")
                .child("Doctor")
                .child(strDoctorUId).setValue(following)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //Toast.makeText(PaymentActivity.this, "you following", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    public void onPaymentError(int i, String paymentId) {
        Consultation consultation = new Consultation(strUserId, strDoctorUId, doctorProfileImage, strType, strCategory, profileImage, strUserName, strUserEmail, strMobileNumber, strDoctorName, strGender, strbirthDate, strHeight, strWeight, strbloodGroup, strSelectDate, strSelectTime, strProblem, "0", String.valueOf(totalAmount), paymentId, "0", strhospitalAddress, strMeetLink, currentDate, currentTime,problemImage);
        database.getReference().child("Consultation").push().setValue(consultation);
        Toast.makeText(PaymentActivity.this, "Payment Error", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
        startActivity(intent);
    }


}