package com.example.dotechuser;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.Toast;

import com.example.dotechuser.Fragment.DoctorFragment;
import com.example.dotechuser.Fragment.HomeFragment;
import com.example.dotechuser.Fragment.HospitalFragment;
import com.example.dotechuser.Fragment.ProfileFragment;
import com.example.dotechuser.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    AlertDialog.Builder alert;
    private long pressed;
    private int selectedTab = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragmentContainer, HomeFragment.class, null)
                .commit();

        binding.homeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedTab != 1) {

                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainer, HomeFragment.class, null)
                            .commit();

                    // unselect other tabs
                    binding.doctorTxt.setVisibility(View.GONE);
                    binding.hospitalTxt.setVisibility(View.GONE);
                    binding.profileTxt.setVisibility(View.GONE);

                    binding.doctorImage.setImageResource(R.drawable.doctor);
                    binding.hospitalImage.setImageResource(R.drawable.hospital);
                    binding.profileImage.setImageResource(R.drawable.profile);

                    binding.doctorLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    binding.hospitalLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    binding.profileLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                    //  selected tab
                    binding.homeTxt.setVisibility(View.VISIBLE);
                    binding.homeImage.setImageResource(R.drawable.home_selected);
                    binding.homeLayout.setBackgroundResource(R.drawable.round_back_home_100);

                    //  animation
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 1f, 1f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    binding.homeLayout.startAnimation(scaleAnimation);

                    selectedTab = 1;
                }
            }
        });

        binding.doctorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedTab != 2) {

                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainer, DoctorFragment.class, null)
                            .commit();

                    // unselect other tabs
                    binding.homeTxt.setVisibility(View.GONE);
                    binding.hospitalTxt.setVisibility(View.GONE);
                    binding.profileTxt.setVisibility(View.GONE);

                    binding.homeImage.setImageResource(R.drawable.home);
                    binding.hospitalImage.setImageResource(R.drawable.hospital);
                    binding.profileImage.setImageResource(R.drawable.profile);

                    binding.homeLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    binding.hospitalLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    binding.profileLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                    //  selected tab
                    binding.doctorTxt.setVisibility(View.VISIBLE);
                    binding.doctorImage.setImageResource(R.drawable.doctor_selected);
                    binding.doctorLayout.setBackgroundResource(R.drawable.round_back_home_100);

                    //  animation
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 1f, 1f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    binding.doctorLayout.startAnimation(scaleAnimation);

                    selectedTab = 2;
                }
            }
        });

        binding.hospitalLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedTab != 3) {

                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainer, HospitalFragment.class, null)
                            .commit();

                    // unselect other tabs
                    binding.doctorTxt.setVisibility(View.GONE);
                    binding.homeTxt.setVisibility(View.GONE);
                    binding.profileTxt.setVisibility(View.GONE);

                    binding.doctorImage.setImageResource(R.drawable.doctor);
                    binding.homeImage.setImageResource(R.drawable.home);
                    binding.profileImage.setImageResource(R.drawable.profile);

                    binding.doctorLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    binding.homeLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    binding.profileLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                    //  selected tab
                    binding.hospitalTxt.setVisibility(View.VISIBLE);
                    binding.hospitalImage.setImageResource(R.drawable.hospital_selected);
                    binding.hospitalLayout.setBackgroundResource(R.drawable.round_back_home_100);

                    //  animation
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 1f, 1f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    binding.hospitalLayout.startAnimation(scaleAnimation);

                    selectedTab = 3;
                }
            }
        });

        binding.profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedTab != 4) {

                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainer, ProfileFragment.class, null)
                            .commit();

                    // unselect other tabs
                    binding.doctorTxt.setVisibility(View.GONE);
                    binding.hospitalTxt.setVisibility(View.GONE);
                    binding.homeTxt.setVisibility(View.GONE);

                    binding.doctorImage.setImageResource(R.drawable.doctor);
                    binding.hospitalImage.setImageResource(R.drawable.hospital);
                    binding.homeImage.setImageResource(R.drawable.home);

                    binding.doctorLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    binding.hospitalLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    binding.homeLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                    //  selected tab
                    binding.profileTxt.setVisibility(View.VISIBLE);
                    binding.profileImage.setImageResource(R.drawable.profile_selected);
                    binding.profileLayout.setBackgroundResource(R.drawable.round_back_home_100);

                    //  animation
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 1f, 1f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    binding.profileLayout.startAnimation(scaleAnimation);

                    selectedTab = 4;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /*if (pressed + 2000 > System.currentTimeMillis()){
            *//*Intent intent = new Intent(MainActivity.this,SignInActivity.class);
            startActivity(intent);*//*
            Intent intent = new Intent(Intent.ACTION_MAIN);
            *//*intent.addCategory(Intent.CATEGORY_HOME);*//*
            startActivity(intent);
            finish();
        }else {
            Toast.makeText(getApplicationContext() , "Press Back again to Exit", Toast.LENGTH_SHORT).show();
        }
        pressed = System.currentTimeMillis();*/

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);

    }


    /*@Override
    public void onBackPressed() {
        alert = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        View alert_view = inflater.inflate(R.layout.logout_alert_dialog,null,false);
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
                SharedPreferences.Editor pref = getSharedPreferences("userDetails", MODE_PRIVATE).edit();
                pref.putString("message", "logout");
                pref.putString("name", "");
                pref.putString("email", "");
                pref.putString("mobile", "");
                pref.putString("gender", "");
                pref.putString("height", "");
                pref.putString("birthDate", "");
                pref.putString("bloodGroup", "");
                pref.putString("weight", "");
                pref.putString("profileImage", "");
                pref.apply();

                finish();
                *//*
                Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                startActivity(intent);*//*
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                dialog.dismiss();
            }
        });

    }*/
}