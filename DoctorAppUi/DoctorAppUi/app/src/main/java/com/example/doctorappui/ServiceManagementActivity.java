package com.example.doctorappui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.doctorappui.databinding.ActivityServiceManagementBinding;
import com.example.doctorappui.modal.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.Currency;

public
class ServiceManagementActivity extends AppCompatActivity {

    ActivityServiceManagementBinding binding;
    String strMsgAmount = "", strMsgDesc = "", strCallAmount = "", strCallDesc = "", uId, strType = "";
    String edtStrMsgAmount = "", edtStrMsgDesc = "", edtStrCallAmount = "", edtStrCallDesc = "";
    String pendingAmount = "", depositAmount = "" , totalAmount = "";
    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityServiceManagementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uId = user.getUid();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        SharedPreferences preferences = getSharedPreferences("userData", MODE_PRIVATE);
        strCallAmount = preferences.getString("callAmount", "");
        strMsgAmount = preferences.getString("msgAmount", "");
        strCallDesc = preferences.getString("callDesc", "");
        strMsgDesc = preferences.getString("msgDesc", "");
        strType = preferences.getString("type", "");

        binding.edtCallPrice.setText(strCallAmount);
        binding.edtCallDesc.setText(strCallDesc);
        binding.edtMsgPrice.setText(strMsgAmount);
        binding.edtMsgDesc.setText(strMsgDesc);

        if (strType.equals("hospital")) {
            binding.btnTimeSlot.setVisibility(View.GONE);
            binding.msgCardView.setVisibility(View.GONE);
        }

        binding.btnTimeSlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                Intent intent = new Intent(ServiceManagementActivity.this, ManageTimeSlotActivity.class);
                intent.putExtra("uId", auth.getUid());
                startActivity(intent);
            }
        });

        database.getReference().child("doctor and hospital").child(auth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public
                    void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Users users = snapshot.getValue(Users.class);
                            if (users.getStatus().equals("shutdown")) {
                                binding.switchStatus.setChecked(true);
                            }
                            NumberFormat format = NumberFormat.getCurrencyInstance();
                            format.setMaximumFractionDigits(0);
                            format.setCurrency(Currency.getInstance("INR"));
                            pendingAmount = users.getTotalAmount();
                            depositAmount = users.getDepositAmount();
                            binding.pendingAmount.setText(format.format(Long.parseLong(pendingAmount)));
                            binding.depositAmount.setText(format.format(Long.parseLong(depositAmount)));
                            binding.totalAmount.setText(format.format(Long.parseLong(pendingAmount) + Long.parseLong(depositAmount)));
                        }
                    }

                    @Override
                    public
                    void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        binding.switchStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public
            void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("doctor and hospital").child(uId);
                    databaseReference.child("status").setValue("shutdown");
                    Toast.makeText(ServiceManagementActivity.this, "Shutdown mode on", Toast.LENGTH_SHORT).show();
                } else {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("doctor and hospital").child(uId);
                    databaseReference.child("status").setValue("1");
                    Toast.makeText(ServiceManagementActivity.this, "Shutdown mode off", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                edtStrCallAmount = binding.edtCallPrice.getText().toString();
                edtStrCallDesc = binding.edtCallDesc.getText().toString();
                edtStrMsgAmount = binding.edtMsgPrice.getText().toString();
                edtStrMsgDesc = binding.edtMsgDesc.getText().toString();

                if (edtStrCallAmount.equals("")) {
                    Toast.makeText(ServiceManagementActivity.this, "Please enter call amount", Toast.LENGTH_SHORT).show();
                } else if (edtStrCallDesc.equals("")) {
                    Toast.makeText(ServiceManagementActivity.this, "Please enter call description", Toast.LENGTH_SHORT).show();
                } else if (edtStrMsgAmount.equals("") && strType.equals("doctor")) {
                    Toast.makeText(ServiceManagementActivity.this, "Please enter message amount", Toast.LENGTH_SHORT).show();
                } else if (edtStrMsgDesc.equals("") && strType.equals("doctor")) {
                    Toast.makeText(ServiceManagementActivity.this, "Please enter message description", Toast.LENGTH_SHORT).show();
                } else {
                    if ((strCallAmount.equals(edtStrCallAmount) && strCallDesc.equals(edtStrCallDesc) && strMsgAmount.equals(edtStrMsgAmount) && strMsgDesc.equals(edtStrMsgDesc)) || (strType.equals("hospital") && strCallAmount.equals(edtStrCallAmount) && strCallDesc.equals(edtStrCallDesc))) {
                        Toast.makeText(ServiceManagementActivity.this, "Details not changed !", Toast.LENGTH_SHORT).show();
                    } else {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("doctor and hospital").child(uId);
                        databaseReference.child("callAmount").setValue(edtStrCallAmount);
                        databaseReference.child("callDescription").setValue(edtStrCallDesc);
                        databaseReference.child("msgAmount").setValue(edtStrMsgAmount);
                        databaseReference.child("msgDescription").setValue(edtStrMsgDesc);
                        SharedPreferences.Editor pref = getSharedPreferences("userData", MODE_PRIVATE).edit();
                        pref.putString("callAmount", edtStrCallAmount);
                        pref.putString("callDesc", edtStrCallDesc);
                        pref.putString("msgAmount", edtStrMsgAmount);
                        pref.putString("msgDesc", edtStrMsgDesc);
                        pref.apply();
                        Toast.makeText(ServiceManagementActivity.this, "Changes Save!", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }
}