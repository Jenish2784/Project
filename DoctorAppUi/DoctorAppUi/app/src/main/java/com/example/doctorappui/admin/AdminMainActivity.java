package com.example.doctorappui.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.example.doctorappui.R;
import com.example.doctorappui.SignInActivity;
import com.example.doctorappui.databinding.ActivityAdminMainBinding;
import com.example.doctorappui.modal.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public
class AdminMainActivity extends AppCompatActivity {

    ActivityAdminMainBinding binding;
    String strName="";
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;
    AlertDialog.Builder alert;
    long newRequestCount = 0;
    long registeredDoctorCount = 0;
    long registeredHospitalCount = 0;
    long registeredUserCount = 0;

    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();


        SharedPreferences preferences = getSharedPreferences("userData",MODE_PRIVATE);
        strName = preferences.getString("name","");

        binding.adminName.setText(strName);


        //check platform is shutdown or not
        /*Query queryShutDown = FirebaseDatabase.getInstance().getReference("System").child("status");
        queryShutDown.addValueEventListener(new ValueEventListener() {
            @Override
            public
            void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if (dataSnapshot.getValue().equals("1")){
                        binding.switchStatus.setChecked(true);
                    }
                }
            }

            @Override
            public
            void onCancelled(@NonNull DatabaseError error) {

            }
        });*/


        /*binding.switchStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public
            void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("System").child("status");
                    databaseReference.child("status").setValue("1");
                    Toast.makeText(AdminMainActivity.this, "Shutdown mode on", Toast.LENGTH_SHORT).show();
                }else {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("System").child("status");
                    databaseReference.child("status").setValue("0");
                    Toast.makeText(AdminMainActivity.this, "Shutdown mode off", Toast.LENGTH_SHORT).show();
                }
            }
        });*/


        Query newRequestQuery = FirebaseDatabase.getInstance().getReference("doctor and hospital").orderByChild("status").equalTo("0");
        newRequestQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public
            void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        newRequestCount = snapshot.getChildrenCount();
                        binding.newRequestCount.setText(newRequestCount+"");
                }
            }

            @Override
            public
            void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Query registeredDoctorQuery = FirebaseDatabase.getInstance().getReference("doctor and hospital").orderByChild("status").equalTo("1");
        registeredDoctorQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public
            void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Users users = dataSnapshot.getValue(Users.class);
                    if (users.getType().equals("doctor")){
                        registeredDoctorCount++;
                    }
                    binding.registeredDoctorCount.setText(registeredDoctorCount+"");
                }
            }

            @Override
            public
            void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Query registeredHospitalQuery = FirebaseDatabase.getInstance().getReference("doctor and hospital").orderByChild("status").equalTo("1");
        registeredHospitalQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public
            void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Users users = dataSnapshot.getValue(Users.class);
                    if (users.getType().equals("hospital")){
                        registeredHospitalCount++;
                    }
                    binding.registeredHospitalCount.setText(registeredHospitalCount+"");
                }
            }

            @Override
            public
            void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Query registeredUserQuery  = FirebaseDatabase.getInstance().getReference("Users").orderByChild("status").equalTo("1");
        registeredUserQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public
            void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    registeredUserCount = snapshot.getChildrenCount();
                    binding.registeredUserCount.setText(registeredUserCount+"");
                }
            }

            @Override
            public
            void onCancelled(@NonNull DatabaseError error) {

            }
        });

        database.getReference().child("doctor and hospital").child(auth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public
                    void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            Users users = snapshot.getValue(Users.class);
                            if (!users.getProfileImage().equals("")){
                                Picasso.get()
                                        .load(users.getProfileImage())
                                        .placeholder(R.drawable.placeholder)
                                        .into(binding.profileImage);
                            }
                        }
                    }

                    @Override
                    public
                    void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.profileRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                Intent intent = new Intent(AdminMainActivity.this,AdminProfileActivity.class);
                startActivity(intent);
            }
        });

        binding.registeredDoctorCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                Intent intent = new Intent(AdminMainActivity.this, RegisteredDoctorActivity.class);
                startActivity(intent);
            }
        });

        binding.newRequestCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                Intent intent = new Intent(AdminMainActivity.this, NewRequestActivity.class);
                startActivity(intent);
            }
        });

        binding.registeredUserCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                Intent intent = new Intent(AdminMainActivity.this,RegisteredUserActivity.class);
                startActivity(intent);
            }
        });

        binding.registeredHospitalCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                Intent intent = new Intent(AdminMainActivity.this, RegisteredHospitalActivity.class);
                startActivity(intent);
            }
        });

        binding.profileCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                Intent intent = new Intent(AdminMainActivity.this, AdminProfileActivity.class);
                startActivity(intent);
            }
        });

        binding.consultationCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                Intent intent = new Intent(AdminMainActivity.this,AdminConsultationDetailsActivity.class);
                startActivity(intent);
            }
        });

        binding.appointmentCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                Intent intent = new Intent(AdminMainActivity.this, AdminAppointmentDetailsActivity.class);
                startActivity(intent);
            }
        });

        binding.ratingCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                Intent intent = new Intent(AdminMainActivity.this,RatingActivity.class);
                startActivity(intent);
            }
        });

        binding.contactUsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                Intent intent = new Intent(AdminMainActivity.this,AdminContactUsActivity.class);
                startActivity(intent);
            }
        });

        binding.paymentCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                Intent intent = new Intent(AdminMainActivity.this, AdminPaymentActivity.class);
                startActivity(intent);
            }
        });

        binding.logoutCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                alert = new AlertDialog.Builder(AdminMainActivity.this);
                LayoutInflater inflater = LayoutInflater.from(AdminMainActivity.this);
                View alert_view = inflater.inflate(R.layout.logout_alert_dialog,null,false);
                alert.setView(alert_view);
                Button yes = alert_view.findViewById(R.id.btnYes);
                Button no = alert_view.findViewById(R.id.btnNo);
                AlertDialog dialog = alert.create();
                dialog.show();

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public
                    void onClick(View view) {
                        SharedPreferences.Editor pref=getSharedPreferences("userData",MODE_PRIVATE).edit();
                        pref.putString("message","logout");
                        pref.apply();
                        Intent intent = new Intent(AdminMainActivity.this, SignInActivity.class);
                        startActivity(intent);
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

        binding.categoryCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                Intent intent = new Intent(AdminMainActivity.this, AddCategory.class);
                startActivity(intent);
            }
        });



    }

    @Override
    public
    void onBackPressed() {
        alert = new AlertDialog.Builder(AdminMainActivity.this);
        LayoutInflater inflater = LayoutInflater.from(AdminMainActivity.this);
        View alert_view = inflater.inflate(R.layout.logout_alert_dialog,null,false);
        alert.setView(alert_view);
        Button yes = alert_view.findViewById(R.id.btnYes);
        Button no = alert_view.findViewById(R.id.btnNo);
        AlertDialog dialog = alert.create();
        dialog.show();

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                SharedPreferences.Editor pref=getSharedPreferences("userData",MODE_PRIVATE).edit();
                pref.putString("message","logout");
                pref.apply();
                Intent intent = new Intent(AdminMainActivity.this, SignInActivity.class);
                startActivity(intent);
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
}