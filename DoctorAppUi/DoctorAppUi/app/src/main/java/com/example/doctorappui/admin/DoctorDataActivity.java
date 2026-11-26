package com.example.doctorappui.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.doctorappui.EmailSendActivity;
import com.example.doctorappui.EncryptDecrypt;
import com.example.doctorappui.R;

import com.example.doctorappui.SendData;
import com.example.doctorappui.databinding.ActivityDoctorDataBinding;
import com.example.doctorappui.modal.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public
class DoctorDataActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    ActivityDoctorDataBinding binding;
    String strCategory="",strDegreeName="",strImage="",strStatus="",strEmail="",strMobile="",strType="",strUsername="",uId="";
    AlertDialog.Builder alert;
    AlertDialog dialog;

    ProgressDialog progressDialog = null;
    final static String TAG_EMAIL = "email";
    final static String TAG_SUB = "subject";
    final static String TAG_NAME = "name";

    final static String INSERT_URL="http://dotech2023.000webhostapp.com/send_approve_email.php";

    SendData send;
    JSONObject jsonObject = null;

    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDoctorDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Intent intent = getIntent();
        if (intent.hasExtra("email")){
            strEmail=intent.getStringExtra("email");
        }
        if (intent.hasExtra("uId")){
            uId=intent.getStringExtra("uId");
        }
        if (intent.hasExtra("image")){
            strImage = intent.getStringExtra("image");
        }
        if (intent.hasExtra("status")){
            strStatus = intent.getStringExtra("status");
        }

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.home)));

        if (strStatus.equals("1")){
            binding.btnApprove.setVisibility(View.GONE);
            binding.btnReject.setVisibility(View.GONE);
            binding.btnBlock.setVisibility(View.VISIBLE);
        }
        if (strStatus.equals("block")){
            binding.btnApprove.setVisibility(View.GONE);
            binding.btnReject.setVisibility(View.GONE);
            binding.btnUnblock.setVisibility(View.VISIBLE);
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("doctor and hospital");
        Query query = FirebaseDatabase.getInstance().getReference("doctor and hospital").orderByChild("emailId").equalTo(strEmail);


        query.addValueEventListener(new ValueEventListener() {
            @Override
            public
            void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Users users = dataSnapshot.getValue(Users.class);
                    strCategory = users.getCategory();
                    strDegreeName = users.getDegreeName();
                    strMobile = users.getMobileNumber();
                    strType = users.getType();
                    strUsername = users.getName();
                    if (!strImage.equals("")){
                        Picasso.get()
                                .load(strImage)
                                .placeholder(R.drawable.placeholder)
                                .into(binding.profileImage);
                    }

                }
                getSupportActionBar().setTitle(strUsername);
                binding.edtCategory.setText(strCategory);
                binding.edtName.setText(strUsername);
                binding.edtDegreeName.setText(strDegreeName);
                binding.edtMobile.setText(strMobile);
                binding.edtType.setText(strType);
                binding.edtEmail.setText(strEmail);
            }

            @Override
            public
            void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {

                alert = new AlertDialog.Builder(DoctorDataActivity.this);
                LayoutInflater inflater = LayoutInflater.from(DoctorDataActivity.this);
                View alert_view = inflater.inflate(R.layout.request_approve_alert_dialog,null,false);
                alert.setView(alert_view);
                @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button yes = alert_view.findViewById(R.id.btnYes);
                @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button no = alert_view.findViewById(R.id.btnNo);
                dialog = alert.create();
                dialog.setCancelable(false);
                dialog.show();

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public
                    void onClick(View view) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("doctor and hospital").child(uId);
                        databaseReference.child("status").setValue("1");
                        Toast.makeText(DoctorDataActivity.this, "User Approved!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        Intent intent = new Intent(DoctorDataActivity.this, NewRequestActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public
                    void onClick(View view) {
                        dialog.dismiss();
                    }
                });


            }
        });

        binding.btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {

                alert = new AlertDialog.Builder(DoctorDataActivity.this);
                LayoutInflater inflater = LayoutInflater.from(DoctorDataActivity.this);
                View alert_view = inflater.inflate(R.layout.request_reject_alert_dialog,null,false);
                alert.setView(alert_view);
                @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button yes = alert_view.findViewById(R.id.btnYes);
                @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button no = alert_view.findViewById(R.id.btnNo);
                AlertDialog dialog = alert.create();
                dialog.setCancelable(false);
                dialog.show();

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public
                    void onClick(View view) {
                        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("doctor and hospital").child(uId);
                        databaseReference.child("status").setValue("reject");
                        Toast.makeText(DoctorDataActivity.this, "User Rejected!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(DoctorDataActivity.this,NewRequestActivity.class);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public
                    void onClick(View view) {
                        dialog.dismiss();
                    }
                });

            }
        });

        binding.btnBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                alert = new AlertDialog.Builder(DoctorDataActivity.this);
                LayoutInflater inflater = LayoutInflater.from(DoctorDataActivity.this);
                View alert_view = inflater.inflate(R.layout.block_alert_dialog,null,false);
                alert.setView(alert_view);
                Button yes = alert_view.findViewById(R.id.btnYes);
                Button no = alert_view.findViewById(R.id.btnNo);
                AlertDialog dialog = alert.create();
                dialog.setCancelable(false);
                dialog.show();

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public
                    void onClick(View view) {
                        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("doctor and hospital").child(uId);
                        databaseReference.child("status").setValue("block");
                        Toast.makeText(DoctorDataActivity.this, "Doctor blocked", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        binding.btnUnblock.setVisibility(View.VISIBLE);
                        binding.btnBlock.setVisibility(View.INVISIBLE);
                    }
                });
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public
                    void onClick(View view) {
                        dialog.dismiss();
                    }
                });

            }
        });

        binding.btnUnblock.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                alert = new AlertDialog.Builder(DoctorDataActivity.this);
                LayoutInflater inflater = LayoutInflater.from(DoctorDataActivity.this);
                View alert_view = inflater.inflate(R.layout.unblock_alert_dialog,null,false);
                alert.setView(alert_view);
                Button yes = alert_view.findViewById(R.id.btnYes);
                Button no = alert_view.findViewById(R.id.btnNo);
                AlertDialog dialog = alert.create();
                dialog.setCancelable(false);
                dialog.show();

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public
                    void onClick(View view) {
                        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("doctor and hospital").child(uId);
                        databaseReference.child("status").setValue("1");
                        Toast.makeText(DoctorDataActivity.this, "Doctor unblocked", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        binding.btnUnblock.setVisibility(View.INVISIBLE);
                        binding.btnBlock.setVisibility(View.VISIBLE);
                    }
                });
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public
                    void onClick(View view) {
                        dialog.dismiss();
                    }
                });

            }
        });

    }

    //mail send to user
    public class Insert extends AsyncTask {

        @Override
        protected
        void onPreExecute() {
            progressDialog=new ProgressDialog(DoctorDataActivity.this);
            progressDialog.setTitle("User Approve");
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected
        Object doInBackground(Object[] objects) {
            send = new SendData();
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair(TAG_EMAIL,objects[0].toString()));
            params.add(new BasicNameValuePair(TAG_SUB,objects[1].toString()));
            params.add(new BasicNameValuePair(TAG_NAME,objects[2].toString()));
            jsonObject = send.makeHttpRequest(INSERT_URL,"POST",params);

            return null;
        }

        @Override
        protected
        void onPostExecute(Object o) {
            progressDialog.dismiss();

            DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("doctor and hospital").child(uId);
            databaseReference.child("status").setValue("1");
            Toast.makeText(DoctorDataActivity.this, "User Approved!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(DoctorDataActivity.this, NewRequestActivity.class);
            startActivity(intent);
            dialog.dismiss();
            //Toast.makeText(MainActivity.this, ""+message+">>>"+status, Toast.LENGTH_SHORT).show();
            super.onPostExecute(o);
        }
    }
}