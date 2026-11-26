package com.example.doctorappui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.doctorappui.databinding.ActivityConsultDataBinding;
import com.example.doctorappui.doctor.MainActivity;
import com.example.doctorappui.modal.Notification;
import com.example.doctorappui.modal.Schedule;
import com.example.doctorappui.modal.SystemUsers;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public
class ConsultDataActivity extends AppCompatActivity {

    ActivityConsultDataBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    String paymentId = "";
    String consultId = "";
    String consultDate = "";
    String status = "";
    String userName = "";
    String currentDate = "";
    String currentTime = "";
    String userId = "";
    String type = "";
    String strDate = "";
    String strMonth = "";
    String strLink = "";
    String userProfileImage = "";
    String strUserType = "";
    String strDoctorName = "";

    AlertDialog.Builder alert;
    Calendar calendar;
    int todayDate;
    int todayMonth;
    int consultTomorrowDate;

    private String sampleToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhcGlrZXkiOiIzOTNmZGU3MC05ZWMxLTRlOTctODcxOC1lOTM3YjVjMDAzZWQiLCJwZXJtaXNzaW9ucyI6WyJhbGxvd19qb2luIl0sImlhdCI6MTY3MTU5NzM1MCwiZXhwIjoxODI5Mzg1MzUwfQ.kgOQ82EerxjN5YNm97v2PKQ_DkCdiCvepGbDPQNLNSs";


    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConsultDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        SharedPreferences preferences = getSharedPreferences("userData", MODE_PRIVATE);
        strUserType = preferences.getString("type", "");

        checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID);
        checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID);


        calendar = Calendar.getInstance();

        SimpleDateFormat simpleTime = new SimpleDateFormat("HH:mm");
        currentTime = simpleTime.format(calendar.getTime()).toString();
        //Toast.makeText(this,currentTime, Toast.LENGTH_SHORT).show();

        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = simpleDate.format(calendar.getTime()).toString();
        //Toast.makeText(this,currentDate, Toast.LENGTH_SHORT).show();

       /* calendar.add(Calendar.DAY_OF_YEAR,1);
        Date tomorrow = calendar.getTime();
        SimpleDateFormat tomorrowDate = new SimpleDateFormat("yyy-MM-dd");
        String str = tomorrowDate.format(tomorrow);



        Toast.makeText(this,str, Toast.LENGTH_SHORT).show();*/

        SimpleDateFormat currentDateFormat = new SimpleDateFormat("dd");
        strDate = currentDateFormat.format(calendar.getTime()).toString();
        SimpleDateFormat currentMonthFormat = new SimpleDateFormat("MM");
        strMonth = currentMonthFormat.format(calendar.getTime()).toString();
        //Toast.makeText(this,strCurrent, Toast.LENGTH_SHORT).show();


        todayDate = Integer.parseInt(strDate);
        todayMonth = Integer.parseInt(strMonth);
        //Toast.makeText(this,todayDate+"", Toast.LENGTH_SHORT).show();


        Intent intent = getIntent();
        if (intent.hasExtra("paymentId")) {
            paymentId = intent.getStringExtra("paymentId");
        }
        if (intent.hasExtra("consultId")) {
            consultId = intent.getStringExtra("consultId");
        }


        /*Calendar today = Calendar.getInstance();
        int dayOfMonth = today.get(Calendar.DAY_OF_MONTH);
        int month = today.get(Calendar.MONTH);
        int year = today.get(Calendar.YEAR);
        if (month == Calendar.JUNE && dayOfMonth == 29 && year == 2014) {
            // June 29, 2014
            myButton.setEnabled(true);
        }
        else
        {
            // NOT June 29, 2014
            myButton.setEnabled(false);
        }*/


        //Toast.makeText(this,consultId, Toast.LENGTH_SHORT).show();





        if (strUserType.equals("doctor")) {


            Query query = FirebaseDatabase.getInstance().getReference("Consultation").orderByChild("paymentId").equalTo(paymentId);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public
                void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Schedule schedule = dataSnapshot.getValue(Schedule.class);

                        if (!schedule.getProfileImage().equals("")) {
                            Picasso.get()
                                    .load(schedule.getProfileImage())
                                    .placeholder(R.drawable.placeholder)
                                    .into(binding.profileImage);
                        } else {
                            binding.profileImage.setImageResource(R.drawable.dotech_logo);
                        }

                        status = schedule.getStatus();
                        userName = schedule.getUserName();
                        userId = schedule.getUserId();
                        type = schedule.getType();
                        consultDate = schedule.getDate();
                        strLink = schedule.getMeetLink();
                        userProfileImage = schedule.getProfileImage();
                        binding.tvName.setText(schedule.getUserName());
                        binding.tvGender.setText(schedule.getGender());
                        binding.tvDob.setText(schedule.getBirthDate());
                        binding.tvHeight.setText(schedule.getHeight());
                        binding.tvBloodGroup.setText(schedule.getBloodGroup());
                        binding.tvWeight.setText(schedule.getWeight());
                        binding.tvDate.setText("Date : " + schedule.getDate() + "\n" + "time : " + schedule.getTime());
                        binding.tvProblem.setText(schedule.getProblem());


                        //date split
                        String[] separated = consultDate.split("-");
                        //Toast.makeText(ConsultDataActivity.this,separated[2], Toast.LENGTH_SHORT).show();

                        String consultMonth = separated[1];
                        String consultDate = separated[2];


                        int date = Integer.parseInt(consultDate);
                        int month = Integer.parseInt(consultMonth);


                        if (status.equals("Accept")) {
                            if ((todayDate >= date && todayMonth >= month) && !type.equals("Chat Consultation")) {
                                binding.btnComplete.setVisibility(View.VISIBLE);
                            }
                        }

                        if (schedule.getPaymentStatus().equals("0")) {
                            binding.btnAccept.setVisibility(View.INVISIBLE);
                            binding.btnReject.setVisibility(View.INVISIBLE);
                        }

                        if (status.equals("Accept") || status.equals("1") || status.equals("Reject")) {
                            binding.btnAccept.setVisibility(View.GONE);
                            binding.btnReject.setVisibility(View.GONE);
                        } else {
                            if (schedule.getPaymentStatus().equals("0")) {
                                binding.btnAccept.setVisibility(View.GONE);
                                binding.btnReject.setVisibility(View.GONE);
                            } else {
                                binding.btnAccept.setVisibility(View.VISIBLE);
                                binding.btnReject.setVisibility(View.VISIBLE);
                            }
                        }

                        if (schedule.getType().equals("Chat Consultation")) {
                            binding.msgBtn.setVisibility(View.VISIBLE);
                            binding.videoBtn.setVisibility(View.INVISIBLE);
                        } else {
                            //binding.videoBtn.setVisibility(View.VISIBLE);
                            binding.msgBtn.setVisibility(View.GONE);
                            if (currentDate.equals(schedule.getDate())) {
                                binding.videoBtn.setVisibility(View.VISIBLE);
                            } else {
                                binding.videoBtn.setVisibility(View.GONE);
                            }
                        }
                    }
                }

                @Override
                public
                void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else if (strUserType.equals("hospital")) {
            Query query = FirebaseDatabase.getInstance().getReference("Appointment").orderByChild("paymentId").equalTo(paymentId);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public
                void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Schedule schedule = dataSnapshot.getValue(Schedule.class);
                        binding.tvDoctorName.setVisibility(View.VISIBLE);
                        binding.tvDepartmentName.setVisibility(View.VISIBLE);
                        binding.tvDetails.setText("Appointment Details");
                        binding.tvTime.setText("Appointment Time");


                        if (!schedule.getProfileImage().equals("")) {
                            Picasso.get()
                                    .load(schedule.getProfileImage())
                                    .placeholder(R.drawable.placeholder)
                                    .into(binding.profileImage);
                        } else {
                            binding.profileImage.setImageResource(R.drawable.dotech_logo);
                        }

                        status = schedule.getStatus();
                        userName = schedule.getUserName();
                        userId = schedule.getUserId();
                        type = schedule.getType();
                        consultDate = schedule.getDate();
                        strLink = schedule.getMeetLink();
                        strDoctorName = schedule.getDoctorName();
                        userProfileImage = schedule.getProfileImage();
                        binding.tvName.setText(schedule.getUserName());
                        binding.tvGender.setText(schedule.getGender());
                        binding.tvDob.setText(schedule.getBirthDate());
                        binding.tvHeight.setText(schedule.getHeight());
                        binding.tvBloodGroup.setText(schedule.getBloodGroup());
                        binding.tvWeight.setText(schedule.getWeight());
                        binding.tvDate.setText("Date : " + schedule.getDate() + "\n" + "time : " + schedule.getTime());
                        binding.tvProblem.setText("Problem : " + schedule.getProblem());
                        binding.tvDoctorName.setText("Doctor name : " + strDoctorName);
                        binding.tvDepartmentName.setText("Department Name : " + schedule.getCategory());


                        //date split
                        String[] separated = consultDate.split("-");
                        //Toast.makeText(ConsultDataActivity.this,separated[2], Toast.LENGTH_SHORT).show();

                        String consultMonth = separated[1];
                        String consultDate = separated[2];


                        int date = Integer.parseInt(consultDate);
                        int month = Integer.parseInt(consultMonth);


                        // today date hoi to btnComplete visible thai
                        if (status.equals("Accept") || status.equals("AcceptH")) {
                            if ((todayDate >= date && todayMonth >= month) && !type.equals("Chat Consultation")) {
                                binding.btnComplete.setVisibility(View.VISIBLE);
                            }
                        }



                        //payment na thayu hoi tyare btn na dekhay
                        if (schedule.getPaymentStatus().equals("0")) {
                            binding.btnAccept.setVisibility(View.INVISIBLE);
                            binding.btnReject.setVisibility(View.INVISIBLE);
                        }

                        //accept and reject btn hidding
                        if (status.equals("Accept") || status.equals("1") || status.equals("Reject") || status.equals("AcceptH")) {
                            binding.btnAccept.setVisibility(View.GONE);
                            binding.btnReject.setVisibility(View.GONE);
                        } else {
                            if (schedule.getPaymentStatus().equals("0")) {
                                binding.btnAccept.setVisibility(View.GONE);
                                binding.btnReject.setVisibility(View.GONE);
                            } else {
                                binding.btnAccept.setVisibility(View.VISIBLE);
                                binding.btnReject.setVisibility(View.VISIBLE);
                            }
                        }

                        //hospital mate atare kam nu nathi btw futere ma kam lagse
                        if (schedule.getType().equals("Chat Consultation")) {
                            binding.msgBtn.setVisibility(View.VISIBLE);
                            binding.videoBtn.setVisibility(View.INVISIBLE);
                        } else if (schedule.getType().equals("Physical Appointment")) {
                            binding.videoBtn.setVisibility(View.GONE);
                            binding.msgBtn.setVisibility(View.GONE);
                        } else {
                            //binding.videoBtn.setVisibility(View.VISIBLE);
                            binding.msgBtn.setVisibility(View.GONE);
                            if (currentDate.equals(schedule.getDate()) && schedule.getStatus().equals("Accept") && schedule.getPaymentStatus().equals("1")) {
                                binding.videoBtn.setVisibility(View.VISIBLE);
                            } else {
                                binding.videoBtn.setVisibility(View.GONE);
                            }
                        }
                    }
                }

                @Override
                public
                void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            Toast.makeText(this, "Server down!", Toast.LENGTH_SHORT).show();
        }

        binding.videoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                if (status.equals("Accept")) {
                    if (!strLink.equals("")) {
                        Intent intent = new Intent(ConsultDataActivity.this, MeetingActivity.class);
                        intent.putExtra("token", sampleToken);
                        intent.putExtra("meetingId", strLink);
                        startActivity(intent);
                    } else {
                        Toast.makeText(ConsultDataActivity.this, "Server down", Toast.LENGTH_SHORT).show();
                    }
                } else if (status.equals("AcceptH")) {
                    Toast.makeText(ConsultDataActivity.this, "User not accept request", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ConsultDataActivity.this, "Please accept consult request", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.msgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                if (status.equals("1") || status.equals("Accept")) {
                    Intent intent = new Intent(ConsultDataActivity.this, ChatActivity.class);
                    intent.putExtra("name", userName);
                    intent.putExtra("profileImage", userProfileImage);
                    intent.putExtra("userId", userId);
                    intent.putExtra("consultId", consultId);
                    intent.putExtra("paymentId", paymentId);
                    intent.putExtra("consultDate", consultDate);
                    intent.putExtra("status", status);
                    intent.putExtra("a", "consult");
                    startActivity(intent);
                } else {
                    Toast.makeText(ConsultDataActivity.this, "Please accept consult request", Toast.LENGTH_SHORT).show();

                }
            }
        });

        binding.btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                alert = new AlertDialog.Builder(ConsultDataActivity.this);
                LayoutInflater inflater = LayoutInflater.from(ConsultDataActivity.this);
                View alert_view = inflater.inflate(R.layout.consult_confirm_alert_dialog, null, false);
                alert.setView(alert_view);
                Button yes = alert_view.findViewById(R.id.btnYes);
                Button no = alert_view.findViewById(R.id.btnNo);
                AlertDialog dialog = alert.create();
                dialog.show();

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public
                    void onClick(View view) {
                        if (strUserType.equals("doctor")) {
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Consultation").child(consultId);
                            databaseReference.child("status").setValue("1");
                            dialog.dismiss();
                            binding.btnComplete.setVisibility(View.GONE);
                        } else if (strUserType.equals("hospital")) {
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Appointment").child(consultId);
                            databaseReference.child("status").setValue("1");
                            dialog.dismiss();
                            binding.btnComplete.setVisibility(View.GONE);
                        } else {
                            Toast.makeText(ConsultDataActivity.this, "Server down!", Toast.LENGTH_SHORT).show();
                        }
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

        binding.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {

                if (type.equals("Video Consultation")) {
                    //Toast.makeText(ConsultDataActivity.this, "true", Toast.LENGTH_SHORT).show();

                    alert = new AlertDialog.Builder(ConsultDataActivity.this);
                    LayoutInflater inflater = LayoutInflater.from(ConsultDataActivity.this);
                    View alert_view = inflater.inflate(R.layout.consult_accept_dialog, null, false);
                    alert.setView(alert_view);
                    Button yes = alert_view.findViewById(R.id.btnYes);
                    Button no = alert_view.findViewById(R.id.btnNo);
                    AlertDialog dialog = alert.create();
                    dialog.show();

                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public
                        void onClick(View view) {
                            createMeeting(sampleToken);
                            dialog.dismiss();

                            Notification notification = new Notification();
                            notification.setNotificationBy(FirebaseAuth.getInstance().getUid());
                            notification.setNotificationAt(new Date().getTime());
                            notification.setConsultType(type);
                            notification.setNotificationId(consultId);
                            notification.setType("Accept");

                            FirebaseDatabase.getInstance().getReference()
                                    .child("notification")
                                    .child(userId)
                                    .push()
                                    .setValue(notification);
                        }
                    });
                    no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public
                        void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                } else if (type.equals("Chat Consultation")) {
                    alert = new AlertDialog.Builder(ConsultDataActivity.this);
                    LayoutInflater inflater = LayoutInflater.from(ConsultDataActivity.this);
                    View alert_view = inflater.inflate(R.layout.consult_accept_dialog, null, false);
                    alert.setView(alert_view);
                    Button yes = alert_view.findViewById(R.id.btnYes);
                    Button no = alert_view.findViewById(R.id.btnNo);
                    AlertDialog dialog = alert.create();
                    dialog.show();

                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public
                        void onClick(View view) {
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Consultation").child(consultId);
                            databaseReference.child("status").setValue("Accept");
                            dialog.dismiss();
                            Toast.makeText(ConsultDataActivity.this, "Consultation Accepted", Toast.LENGTH_SHORT).show();
                            binding.btnAccept.setVisibility(View.GONE);
                            binding.btnReject.setVisibility(View.GONE);

                            Notification notification = new Notification();
                            notification.setNotificationBy(FirebaseAuth.getInstance().getUid());
                            notification.setNotificationAt(new Date().getTime());
                            notification.setConsultType(type);
                            notification.setNotificationId(consultId);
                            notification.setType("Accept");

                            FirebaseDatabase.getInstance().getReference()
                                    .child("notification")
                                    .child(userId)
                                    .push()
                                    .setValue(notification);
                        }
                    });
                    no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public
                        void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                } else if (type.equals("Online Appointment")) {
                    alert = new AlertDialog.Builder(ConsultDataActivity.this);
                    LayoutInflater inflater = LayoutInflater.from(ConsultDataActivity.this);
                    View alert_view = inflater.inflate(R.layout.consult_accept_dialog, null, false);
                    alert.setView(alert_view);
                    @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button yes = alert_view.findViewById(R.id.btnYes);
                    @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button no = alert_view.findViewById(R.id.btnNo);
                    AlertDialog dialog = alert.create();
                    dialog.show();


                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public
                        void onClick(View view) {
                            createMeeting(sampleToken);
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
                } else if (type.equals("Physical Appointment")) {
                    alert = new AlertDialog.Builder(ConsultDataActivity.this);
                    LayoutInflater inflater = LayoutInflater.from(ConsultDataActivity.this);
                    View alert_view = inflater.inflate(R.layout.consult_accept_dialog, null, false);
                    alert.setView(alert_view);
                    @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button yes = alert_view.findViewById(R.id.btnYes);
                    @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button no = alert_view.findViewById(R.id.btnNo);
                    AlertDialog dialog = alert.create();
                    dialog.show();

                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public
                        void onClick(View view) {
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Appointment").child(consultId);
                            databaseReference.child("status").setValue("Accept");
                            dialog.dismiss();
                            Toast.makeText(ConsultDataActivity.this, "Appointment Accepted", Toast.LENGTH_SHORT).show();
                            binding.btnAccept.setVisibility(View.GONE);
                            binding.btnReject.setVisibility(View.GONE);
                        }
                    });

                    no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public
                        void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                } else {
                    Toast.makeText(ConsultDataActivity.this, "Server down!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                alert = new AlertDialog.Builder(ConsultDataActivity.this);
                LayoutInflater inflater = LayoutInflater.from(ConsultDataActivity.this);
                View alert_view = inflater.inflate(R.layout.consult_reject_dialog, null, false);
                alert.setView(alert_view);
                Button yes = alert_view.findViewById(R.id.btnYes);
                Button no = alert_view.findViewById(R.id.btnNo);
                AlertDialog dialog = alert.create();
                dialog.show();

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public
                    void onClick(View view) {
                        if (strUserType.equals("doctor")) {
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Consultation").child(consultId);
                            databaseReference.child("status").setValue("Reject");
                            dialog.dismiss();
                            Toast.makeText(ConsultDataActivity.this, "Consultation Rejected", Toast.LENGTH_SHORT).show();
                            binding.btnAccept.setVisibility(View.GONE);
                            binding.btnReject.setVisibility(View.GONE);
                        } else if (strUserType.equals("hospital")) {
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Appointment").child(consultId);
                            databaseReference.child("status").setValue("Reject");
                            dialog.dismiss();
                            Toast.makeText(ConsultDataActivity.this, "Appointment Rejected", Toast.LENGTH_SHORT).show();
                            binding.btnAccept.setVisibility(View.GONE);
                            binding.btnReject.setVisibility(View.GONE);
                        }
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

    //video call meeting create
    private
    void createMeeting(String token) {
        // we will make an API call to VideoSDK Server to get a roomId
        AndroidNetworking.post("https://api.videosdk.live/v2/rooms")
                .addHeaders("Authorization", token) //we will pass the token in the Headers
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public
                    void onResponse(JSONObject response) {
                        try {
                            // response will contain `roomId`
                            final String meetingId = response.getString("roomId");
                            //check user is doctor or hospital
                            if (strUserType.equals("doctor")) {
                                Toast.makeText(ConsultDataActivity.this, meetingId, Toast.LENGTH_SHORT).show();
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Consultation").child(consultId);
                                databaseReference.child("meetLink").setValue(meetingId);
                                databaseReference.child("status").setValue("Accept");
                                Toast.makeText(ConsultDataActivity.this, "Consultation Accepted", Toast.LENGTH_SHORT).show();
                                binding.btnAccept.setVisibility(View.GONE);
                                binding.btnReject.setVisibility(View.GONE);
                            } else if (strUserType.equals("hospital")) {
                                Toast.makeText(ConsultDataActivity.this, meetingId, Toast.LENGTH_SHORT).show();
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Appointment").child(consultId);
                                databaseReference.child("status").setValue("Accept");
                                databaseReference.child("meetLink").setValue(meetingId);
                                Toast.makeText(ConsultDataActivity.this, "Appointment Accepted", Toast.LENGTH_SHORT).show();
                                binding.btnAccept.setVisibility(View.GONE);
                                binding.btnReject.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(ConsultDataActivity.this, "Server down!", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public
                    void onError(ANError anError) {
                        anError.printStackTrace();
                        Toast.makeText(ConsultDataActivity.this, anError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /*@Override
    public
    void onBackPressed() {
        super.onBackPressed();
        exitFormApp();
    }

    private
    void exitFormApp() {
        Intent intent = new Intent(ConsultDataActivity.this, HistoryDataActivity.class);
        startActivity(intent);
    }*/


    private static final int PERMISSION_REQ_ID = 22;

    private static final String[] REQUESTED_PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
    };

    private
    boolean checkSelfPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, requestCode);
            return false;
        }
        return true;
    }


}