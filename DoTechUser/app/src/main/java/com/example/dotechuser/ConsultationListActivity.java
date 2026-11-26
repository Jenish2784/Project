package com.example.dotechuser;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import com.example.dotechuser.adapter.ConsultationsFragmentAdapter;
import com.example.dotechuser.databinding.ActivityConsultationListBinding;

public class ConsultationListActivity extends AppCompatActivity {

    ActivityConsultationListBinding binding;

    String strType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConsultationListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.home)));

        SharedPreferences preferences = getSharedPreferences("appointmentMsg", MODE_PRIVATE);
        strType = preferences.getString("msg", "");

        if (strType.equals("Consultation")){
            getSupportActionBar().setTitle("My Consultations");
        }
        else if(strType.equals("Appointment")){
            getSupportActionBar().setTitle("My Appointments");
        }
        else{
            getSupportActionBar().setTitle("Server Down!!");
        }

        binding.viewpager.setAdapter(new ConsultationsFragmentAdapter(getSupportFragmentManager()));
        binding.tabLayout.setupWithViewPager(binding.viewpager);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ConsultationListActivity.this,MainActivity.class);
        startActivity(intent);
    }
}