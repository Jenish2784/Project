package com.example.dotechuser;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.dotechuser.Model.Notification;
import com.example.dotechuser.databinding.ActivityEmailSendBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
    String boolStatus  = "0";
    String consultId  = "";
    String strType  = "";
    String uri = "";
    String paymentId = "";

    ProgressDialog progressDialog = null;
    final static String TAG_EMAIL = "email";
    final static String TAG_SUB = "subject";
    final static String TAG_NAME = "name";
    final static String TAG_USERNAME = "username";
    final static String TAG_MOBILENO = "mobile";
    final static String TAG_DATE = "date";
    final static String TAG_TIME = "time";
    final static String INSERT_URL = "http://dotech2023.000webhostapp.com/send_email.php";
    final static String SEND_SMS_URL = "https://dotech2023.000webhostapp.com/sms.php";

    SendData send;
    JSONObject jsonObject = null;

    FirebaseStorage storage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmailSendBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        userId = auth.getUid();

        Intent intent = getIntent();
        if (intent.hasExtra("userName")) {
            strUserName = intent.getStringExtra("userName");
        }
        if (intent.hasExtra("paymentId")) {
            paymentId = intent.getStringExtra("paymentId");
        }
        if (intent.hasExtra("doctorUId")) {
            doctorId = intent.getStringExtra("doctorUId");
        }
        if(intent.hasExtra("userProfile")){
            userProfile = intent.getStringExtra("userProfile");
        }
        if (intent.hasExtra("status")) {
            status = intent.getStringExtra("status");
        }
        if (intent.hasExtra("doctorName")) {
            strDoctorName = intent.getStringExtra("doctorName");
        }
        if (intent.hasExtra("doctorEmail")) {
            strDoctorEmail = intent.getStringExtra("doctorEmail");
        }
        if (intent.hasExtra("doctorMobileNo")) {
            strDoctorMobileNo = intent.getStringExtra("doctorMobileNo");
        }
        if (intent.hasExtra("date")) {
            date = intent.getStringExtra("date");
        }
        if (intent.hasExtra("time")) {
            time = intent.getStringExtra("time");
        }
        if (intent.hasExtra("currentTime")) {
            currentTime = intent.getStringExtra("currentTime");
        }
        if (intent.hasExtra("currentDate")) {
            currentDate = intent.getStringExtra("currentDate");
        }
        if (intent.hasExtra("consultType")) {
            consultType = intent.getStringExtra("consultType");
        }
        if (intent.hasExtra("amount")) {
            amount = intent.getStringExtra("amount");
        }
        if (intent.hasExtra("problemImgUri")) {
            uri = intent.getStringExtra("problemImgUri");
        }
        if (status.equals("Success")) {
            new Insert().execute(strDoctorEmail, "Consultation", strDoctorName, strUserName, date, time);
        }

    }

    public class Insert extends AsyncTask {

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(EmailSendActivity.this);
            progressDialog.setTitle("Transaction");
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            send = new SendData();
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair(TAG_EMAIL, objects[0].toString()));
            params.add(new BasicNameValuePair(TAG_SUB, objects[1].toString()));
            params.add(new BasicNameValuePair(TAG_NAME, objects[2].toString()));
            params.add(new BasicNameValuePair(TAG_USERNAME, objects[3].toString()));
            params.add(new BasicNameValuePair(TAG_DATE, objects[4].toString()));
            params.add(new BasicNameValuePair(TAG_TIME, objects[5].toString()));
            jsonObject = send.makeHttpRequest(INSERT_URL, "POST", params);

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            progressDialog.dismiss();
            new SmsSend().execute(strDoctorName,strUserName,date,time,strDoctorMobileNo);

            /*Toast.makeText(EmailSendActivity.this, uri, Toast.LENGTH_SHORT).show();

            final StorageReference reference = storage.getReference().child("problem_images").child(auth.getUid()).child(paymentId);
            reference.putFile(Uri.parse(uri)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            if (strType.equals("Consultation")){
                                database.getReference("Consultation").orderByChild("paymentId").equalTo(paymentId);
                            }
                            else if (strType.equals("Appointment")){
                                database.getReference("Appointment").child(auth.getUid()).child("problemImage").setValue(uri);
                            }
                            else{
                                Toast.makeText(EmailSendActivity.this, "Server down!", Toast.LENGTH_SHORT).show();
                            }
                            *//*database.getReference().child("Users").child(auth.getUid()).child("profileImage").setValue(uri.toString());*//*
                            //database ma uri set karavanmio

                        }
                    });
                }
            });*/

            /*binding.payAnimation.setVisibility(View.VISIBLE);
            binding.tvMsg.setVisibility(View.VISIBLE);
            binding.tvMsg.setText("Payment of ₹" + amount + " to " + strDoctorName + " successful");

            Notification notification = new Notification(userId,date,currentDate,time,currentTime,strUserName,userProfile,consultType,boolStatus);
            database.getReference("notification").child(doctorId).push().setValue(notification);

            SharedPreferences preferences = getSharedPreferences("appointmentMsg", MODE_PRIVATE);
            strType = preferences.getString("msg", "");

            if (strType.equals("Consultation")){
                binding.myAppoinment.setVisibility(View.INVISIBLE);
                binding.myConsultation.setVisibility(View.VISIBLE);

                binding.myConsultation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(EmailSendActivity.this,ConsultationListActivity.class);
                        startActivity(intent);
                    }
                });

            } else if (strType.equals("Appointment")) {
                binding.myAppoinment.setVisibility(View.VISIBLE);
                binding.myConsultation.setVisibility(View.INVISIBLE);

                binding.myConsultation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(EmailSendActivity.this,ConsultationListActivity.class);
                        startActivity(intent);
                    }
                });

                binding.myAppoinment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(EmailSendActivity.this,AppointmentActivity.class);
                        startActivity(intent);
                    }
                });
            }*/

            super.onPostExecute(o);
        }
    }

    public class SmsSend extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            send = new SendData();
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair(TAG_NAME, objects[0].toString()));
            params.add(new BasicNameValuePair(TAG_USERNAME, objects[1].toString()));
            params.add(new BasicNameValuePair(TAG_DATE, objects[2].toString()));
            params.add(new BasicNameValuePair(TAG_TIME, objects[3].toString()));
            params.add(new BasicNameValuePair(TAG_MOBILENO, objects[4].toString()));
            jsonObject = send.makeHttpRequest(SEND_SMS_URL, "POST", params);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            progressDialog.dismiss();
            binding.payAnimation.setVisibility(View.VISIBLE);
            binding.tvMsg.setVisibility(View.VISIBLE);
            binding.tvMsg.setText("Payment of ₹" + amount + " to " + strDoctorName + " successful");

            Notification notification = new Notification(userId,date,currentDate,time,currentTime,strUserName,userProfile,consultType,boolStatus);
            database.getReference("notification").child(doctorId).push().setValue(notification);

            SharedPreferences preferences = getSharedPreferences("appointmentMsg", MODE_PRIVATE);
            strType = preferences.getString("msg", "");

            if (strType.equals("Consultation")){
                binding.myAppoinment.setVisibility(View.INVISIBLE);
                binding.myConsultation.setVisibility(View.VISIBLE);

                binding.myConsultation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(EmailSendActivity.this,ConsultationListActivity.class);
                        startActivity(intent);
                    }
                });

            } else if (strType.equals("Appointment")) {
                binding.myAppoinment.setVisibility(View.VISIBLE);
                binding.myConsultation.setVisibility(View.INVISIBLE);

                binding.myAppoinment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(EmailSendActivity.this,ConsultationListActivity.class);
                        startActivity(intent);
                    }
                });
            }

            super.onPostExecute(o);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(EmailSendActivity.this,MainActivity.class);
        startActivity(intent);
    }
}