package com.example.doctorappui.doctor;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doctorappui.AppointReportActivity;
import com.example.doctorappui.HistoryDataActivity;
import com.example.doctorappui.NotificationActivity;
import com.example.doctorappui.PatientListActivity;
import com.example.doctorappui.ProfileActivity;
import com.example.doctorappui.R;
import com.example.doctorappui.admin.DoctorDataActivity;
import com.example.doctorappui.databinding.FragmentHomeBinding;
import com.example.doctorappui.hospital.DepartmentActivity;
import com.example.doctorappui.modal.Follow;
import com.example.doctorappui.modal.Schedule;
import com.example.doctorappui.modal.SystemUsers;
import com.example.doctorappui.modal.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public
class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    String strName = "";
    String strEmail = "";
    String strProfileImage = "";
    String strStatus = "";

    String strCallDesc = "",strMsgDesc = "",strAdd = "",strCallAmount = "",strAccNo = "",
            strIfscNo = "",strMsgAmount = "";

    String strType = "";
    long count = 0;
    long completeCount = 0;
    long totalConsult = 0;
    long newConsultRequest = 0;
    long doctorPatient = 0;
    long departmentCount = 0;
    Calendar calendar;
    String currentDate = "";
    String uId = "";
    AlertDialog.Builder alert;

    public
    HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public
    void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        calendar = Calendar.getInstance();
    }

    @Override
    public
    View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uId = user.getUid();

        SharedPreferences preferences = this.getContext().getSharedPreferences("userData", MODE_PRIVATE);
        //strStatus = preferences.getString("status", "");

        strEmail = preferences.getString("email", "");
        strName = preferences.getString("name", "");
        strProfileImage = preferences.getString("profileImage", "");
        strName = preferences.getString("name", "");
        strType = preferences.getString("type", "");

        strCallDesc = preferences.getString("callDesc", "");
        strMsgDesc = preferences.getString("msgDesc", "");
        strCallAmount = preferences.getString("callAmount", "");
        strMsgAmount = preferences.getString("msgAmount", "");
        strAdd = preferences.getString("address", "");
        strAccNo = preferences.getString("accNo", "");
        strIfscNo = preferences.getString("ifscNo", "");



        if (strType.equals("doctor")) {
            binding.doctorName.setText("Hello, Dr. " + strName);
        } else if (strType.equals("hospital")) {
            binding.doctorName.setText("Hello, " + strName);
        }


        if (!strProfileImage.equals("")) {
            Picasso.get()
                    .load(strProfileImage)
                    .placeholder(R.drawable.placeholder)
                    .into(binding.profileImage);
        } else {
            binding.profileImage.setImageResource(R.drawable.dotech_logo);
        }

        if (strType.equals("doctor")) {
            binding.departmentCardView.setVisibility(View.GONE);
        }


        //check all field are not null
        if (strCallAmount.equals("") || strMsgAmount.equals("") || strCallDesc.equals("") || strMsgDesc.equals("") ||
                strAdd.equals("") || strIfscNo.equals("") || strAccNo.equals("")){

            alert = new AlertDialog.Builder(getContext());
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            View alert_view = layoutInflater.inflate(R.layout.datafilling_alert_dialog,null,false);
            alert.setView(alert_view);
            TextView tvAccNo = alert_view.findViewById(R.id.tvAccNo);
            TextView tvIfscNo = alert_view.findViewById(R.id.tvIfscNo);
            TextView tvAdd = alert_view.findViewById(R.id.tvAdd);
            TextView tvCallAmt = alert_view.findViewById(R.id.tvCallAmount);
            TextView tvMsgAmt = alert_view.findViewById(R.id.tvMsgAmount);
            TextView tvCallDesc = alert_view.findViewById(R.id.tvCallDesc);
            TextView tvMsgDesc = alert_view.findViewById(R.id.tvMsgDesc);

            EditText edtAccNo = alert_view.findViewById(R.id.edtAccNo);
            EditText edtIfscNo = alert_view.findViewById(R.id.edtIfscNo);
            EditText edtAdd = alert_view.findViewById(R.id.edtAdd);
            EditText edtCallAmt = alert_view.findViewById(R.id.edtCallAmount);
            EditText edtMsgAmt = alert_view.findViewById(R.id.edtMsgAmount);
            EditText edtCallDesc = alert_view.findViewById(R.id.edtCallDesc);
            EditText edtMsgDesc = alert_view.findViewById(R.id.edtMsgDesc);

            Button btnSave = alert_view.findViewById(R.id.btnSave);


            AlertDialog dialog = alert.create();
            dialog.setCancelable(false);
            dialog.show();

            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public
                void onClick(View view) {

                    if (edtAccNo.getText().toString().equals("") || edtAdd.getText().toString().equals("") ||
                            edtCallAmt.getText().toString().equals("") || edtCallDesc.getText().toString().equals("") ||
                            edtIfscNo.getText().toString().equals("") || edtMsgAmt.getText().toString().equals("") || edtMsgDesc.getText().toString().equals("")){
                        Toast.makeText(getContext(), "Enter details", Toast.LENGTH_SHORT).show();
                    }else {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("doctor and hospital").child(uId);
                        databaseReference.child("accountNo").setValue(edtAccNo.getText().toString());
                        databaseReference.child("address").setValue(edtAdd.getText().toString());
                        databaseReference.child("callAmount").setValue(edtCallAmt.getText().toString());
                        databaseReference.child("callDescription").setValue(edtCallDesc.getText().toString());
                        databaseReference.child("ifscNo").setValue(edtIfscNo.getText().toString());
                        databaseReference.child("msgAmount").setValue(edtMsgAmt.getText().toString());
                        databaseReference.child("msgDescription").setValue(edtMsgDesc.getText().toString());
                        SharedPreferences.Editor pref = getContext().getSharedPreferences("userData", MODE_PRIVATE).edit();
                        pref.putString("accNo", edtAccNo.getText().toString());
                        pref.putString("address", edtAdd.getText().toString());
                        pref.putString("callAmount", edtCallAmt.getText().toString());
                        pref.putString("callDesc", edtCallDesc.getText().toString());
                        pref.putString("ifscNo", edtIfscNo.getText().toString());
                        pref.putString("msgAmount", edtMsgAmt.getText().toString());
                        pref.putString("msgDesc", edtMsgDesc.getText().toString());
                        pref.apply();
                        dialog.dismiss();
                        Toast.makeText(getContext(), "Changes Save!", Toast.LENGTH_SHORT).show();
                    }


                }
            });



        }

        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = simpleDate.format(calendar.getTime()).toString();

        //fetch today consultation and consult data
        if (strType.equals("doctor")) {
            Query query = FirebaseDatabase.getInstance().getReference("Consultation").orderByChild("doctorId").equalTo(mAuth.getUid());
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public
                void onDataChange(@NonNull DataSnapshot snapshot) {
                    totalConsult = snapshot.getChildrenCount();
                    binding.tvTotalConsult.setText(totalConsult + " Total");
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Schedule schedule = dataSnapshot.getValue(Schedule.class);
                        if (schedule.getDate().equals(currentDate)) {
                            count++;
                        }
                        if (schedule.getDate().equals(currentDate) && schedule.getStatus().equals("1")) {
                            completeCount++;
                        }
                        if (schedule.getPaymentStatus().equals("1") && (schedule.getStatus().equals("0") || schedule.getStatus().equals("Accept"))) {
                            newConsultRequest++;
                        }
                    }

                    //new consult
                    if (newConsultRequest > 10) {
                        binding.tvNewConsultRequest.setText("10+ New");
                    } else {
                        binding.tvNewConsultRequest.setText(newConsultRequest + "");
                    }

                    if (count > 0) {
                        binding.tvTodayConsult.setText(Html.fromHtml("<b>" + completeCount + "</b>" + " of " + "<b>" + count + "</b>" + " Completed"));
                    } else {
                        binding.tvTodayConsult.setText("Today Consult Not Available");
                    }
                }

                @Override
                public
                void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else if (strType.equals("hospital")){
            Query query = FirebaseDatabase.getInstance().getReference("Appointment").orderByChild("hospitalId").equalTo(mAuth.getUid());
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public
                void onDataChange(@NonNull DataSnapshot snapshot) {
                    totalConsult = snapshot.getChildrenCount();
                    binding.tvTotalConsult.setText(totalConsult + " Total");
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Schedule schedule = dataSnapshot.getValue(Schedule.class);
                        if (schedule.getDate().equals(currentDate)) {
                            count++;
                        }
                        if (schedule.getDate().equals(currentDate) && schedule.getStatus().equals("1")) {
                            completeCount++;
                        }
                        if (schedule.getPaymentStatus().equals("1") && (schedule.getStatus().equals("0") || schedule.getStatus().equals("Accept") || schedule.getStatus().equals("AcceptH"))) {
                            newConsultRequest++;
                        }
                    }

                    //new consult
                    if (newConsultRequest > 10) {
                        binding.tvNewConsultRequest.setText("10+ New");
                    } else {
                        binding.tvNewConsultRequest.setText(newConsultRequest + "");
                    }

                    if (count > 0) {
                        binding.tvTodayConsult.setText(Html.fromHtml("<b>" + completeCount + "</b>" + " of " + "<b>" + count + "</b>" + " Completed"));
                    } else {
                        binding.tvTodayConsult.setText("Today Consult Not Available");
                    }
                }

                @Override
                public
                void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            binding.tvNewConsultRequest.setText("Server down");
            binding.tvTotalConsult.setText("Server down");
            binding.tvTodayConsult.setText("Server down");
        }


        //fetch doctor patient
        if (strType.equals("doctor")) {
            Query queryPatient = FirebaseDatabase.getInstance().getReference("doctor and hospital").child(mAuth.getUid()).child("followers");
            queryPatient.addValueEventListener(new ValueEventListener() {
                @Override
                public
                void onDataChange(@NonNull DataSnapshot snapshot) {
                    doctorPatient = snapshot.getChildrenCount();
                    binding.doctorPatientCount.setText(doctorPatient + " total");
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    }
                }

                @Override
                public
                void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else if (strType.equals("hospital")) {
            Query queryPatient = FirebaseDatabase.getInstance().getReference("doctor and hospital").child(mAuth.getUid()).child("followers");
            queryPatient.addValueEventListener(new ValueEventListener() {
                @Override
                public
                void onDataChange(@NonNull DataSnapshot snapshot) {
                    doctorPatient = snapshot.getChildrenCount();
                    binding.doctorPatientCount.setText(doctorPatient + " total");
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    }
                }

                @Override
                public
                void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            binding.doctorPatientCount.setText("Server down");
        }

        //count department
        if (strType.equals("hospital")) {
            Query departmentQuery = FirebaseDatabase.getInstance().getReference("doctor and hospital").child(mAuth.getUid()).child("department");
            departmentQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public
                void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        departmentCount = snapshot.getChildrenCount();
                        binding.departmentCount.setText(departmentCount + "");
                    }
                }

                @Override
                public
                void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else {
            binding.departmentCount.setText("Server down");
        }

        //data fetch to server
        database.getReference().child("doctor and hospital").child(mAuth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public
                    void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Users users = snapshot.getValue(Users.class);
                            /*if (!users.getProfileImage().equals("")) {
                                Picasso.get()
                                        .load(users.getProfileImage())
                                        .placeholder(R.drawable.placeholder)
                                        .into(binding.profileImage);
                            }*/

                            // future work
                            strStatus = users.getStatus();
                            if (strStatus.equals("block")) {
                                Toast.makeText(getContext(), "You are blocked by admin", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public
                    void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        binding.scheduleCardView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public
            void onClick(View view) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra("tabSchedule", "");
                startActivity(intent);
            }
        });

        binding.iconNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                Intent intent = new Intent(getContext(), NotificationActivity.class);
                startActivity(intent);
            }
        });

        binding.historyCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                Intent intent = new Intent(getContext(), HistoryDataActivity.class);
                startActivity(intent);
            }
        });

        binding.patientCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                Intent intent = new Intent(getContext(), PatientListActivity.class);
                startActivity(intent);
            }
        });

        binding.appointCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                Intent intent = new Intent(getContext(), AppointReportActivity.class);
                startActivity(intent);
            }
        });

        binding.departmentCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                Intent intent = new Intent(getContext(), DepartmentActivity.class);
                startActivity(intent);
            }
        });

        return binding.getRoot();
    }
}