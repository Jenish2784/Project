package com.example.doctorappui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.doctorappui.databinding.ActivityBankAccountBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public
class BankAccountActivity extends AppCompatActivity {

    ActivityBankAccountBinding binding;
    String uId = "",strName = "",strAccNo = "",strIfscNo = "";
    String edtStrName = "",edtStrAccNo = "",edtStrIfscNo = "";

    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBankAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uId = user.getUid();

        SharedPreferences preferences = getSharedPreferences("userData",MODE_PRIVATE);
        strName = preferences.getString("name","");
        strAccNo = preferences.getString("accNo","");
        strIfscNo = preferences.getString("ifscNo","");

        binding.edtName.setText(strName);
        binding.edtAccNo.setText(strAccNo);
        binding.edtIfsc.setText(strIfscNo);

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                edtStrName = binding.edtName.getText().toString();
                edtStrAccNo = binding.edtAccNo.getText().toString();
                edtStrIfscNo = binding.edtIfsc.getText().toString();

                if (edtStrName.equals("")){
                    Toast.makeText(BankAccountActivity.this, "Please enter name", Toast.LENGTH_SHORT).show();
                }else if (edtStrAccNo.equals("")){
                    Toast.makeText(BankAccountActivity.this, "Please enter account number", Toast.LENGTH_SHORT).show();
                }else if (edtStrIfscNo.equals("")){
                    Toast.makeText(BankAccountActivity.this, "Please enter ifsc number", Toast.LENGTH_SHORT).show();
                }else {
                    if (strName.equals(edtStrName) && strAccNo.equals(edtStrAccNo) && strIfscNo.equals(edtStrIfscNo)){
                        Toast.makeText(BankAccountActivity.this, "Details not changed", Toast.LENGTH_SHORT).show();
                    }else {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("doctor and hospital").child(uId);
                        databaseReference.child("accountNo").setValue(edtStrAccNo);
                        databaseReference.child("ifscNo").setValue(edtStrIfscNo);
                        SharedPreferences.Editor pref=getSharedPreferences("userData",MODE_PRIVATE).edit();
                        pref.putString("accNo",edtStrAccNo);
                        pref.putString("ifscNo",edtStrIfscNo);
                        pref.apply();
                        Toast.makeText(BankAccountActivity.this, "Changes save!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }
}