package com.example.doctorappui.doctor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doctorappui.ConnectionReceiver;
import com.example.doctorappui.R;
import com.example.doctorappui.SignInActivity;
import com.example.doctorappui.admin.AdminMainActivity;
import com.example.doctorappui.admin.RegisteredDoctorActivity;
import com.example.doctorappui.databinding.ActivityMainBinding;
import com.example.doctorappui.doctor.ChatFragment;
import com.example.doctorappui.doctor.HomeFragment;
import com.example.doctorappui.doctor.ProfileFragment;
import com.example.doctorappui.doctor.ScheduleFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public
class MainActivity extends AppCompatActivity{

    ActivityMainBinding binding;
    String strStatus="";
    AlertDialog.Builder alert;
    FirebaseDatabase database;
    FirebaseAuth auth;

    private int selectedTab = 1;


    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();




        SharedPreferences preferences = this.getSharedPreferences("userData",MODE_PRIVATE);
        strStatus = preferences.getString("status","");
        /*if (strStatus.equals("0")){
            alert = new AlertDialog.Builder(MainActivity.this);
            LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
            View alert_view = inflater.inflate(R.layout.warning_alert_dialog,null,false);
            alert.setView(alert_view);
            alert.setCancelable(false);
            AlertDialog dialog = alert.create();
            dialog.show();
        }*/


        getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                                .replace(R.id.fragmentContainer, HomeFragment.class,null)
                                        .commit();

        Intent intent = getIntent();
        if (intent.hasExtra("tab")){
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragmentContainer, ProfileFragment.class,null)
                    .commit();

            //unselected other tabs expect home tab
            binding.homeTxt.setVisibility(View.GONE);
            binding.scheduleTxt.setVisibility(View.GONE);
            binding.chatTxt.setVisibility(View.GONE);

            binding.homeImage.setImageResource(R.drawable.home);
            binding.scheduleImage.setImageResource(R.drawable.schedule);
            binding.chatImage.setImageResource(R.drawable.chat);

            binding.homeLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            binding.scheduleLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            binding.chatLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

            binding.profileTxt.setVisibility(View.VISIBLE);
            binding.profileImage.setImageResource(R.drawable.profile_selected);
            binding.profileLayout.setBackgroundResource(R.drawable.round_back_home_100);

            //animation
            ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f,1.0f,1f,1f, Animation.RELATIVE_TO_SELF,1.0f,Animation.RELATIVE_TO_SELF,0.0f);
            scaleAnimation.setDuration(200);
            scaleAnimation.setFillAfter(true);
            binding.profileLayout.startAnimation(scaleAnimation);

            selectedTab = 4;

        }

        if (intent.hasExtra("tabSchedule")){
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragmentContainer, ScheduleFragment.class,null)
                    .commit();

            //unselected other tabs expect home tab
            binding.homeTxt.setVisibility(View.GONE);
            binding.chatTxt.setVisibility(View.GONE);
            binding.profileTxt.setVisibility(View.GONE);

            binding.homeImage.setImageResource(R.drawable.home);
            binding.chatImage.setImageResource(R.drawable.chat);
            binding.profileImage.setImageResource(R.drawable.profile);

            binding.homeLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            binding.chatLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            binding.profileLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

            binding.scheduleTxt.setVisibility(View.VISIBLE);
            binding.scheduleImage.setImageResource(R.drawable.schedule_selected);
            binding.scheduleLayout.setBackgroundResource(R.drawable.round_back_home_100);

            //animation
            ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f,1.0f,1f,1f, Animation.RELATIVE_TO_SELF,1.0f,Animation.RELATIVE_TO_SELF,0.0f);
            scaleAnimation.setDuration(200);
            scaleAnimation.setFillAfter(true);
            binding.scheduleLayout.startAnimation(scaleAnimation);

            selectedTab = 2;
        }

        binding.homeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                if (selectedTab != 1){

                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainer,HomeFragment.class,null)
                            .commit();

                    //unselected other tabs expect home tab
                    binding.scheduleTxt.setVisibility(View.GONE);
                    binding.chatTxt.setVisibility(View.GONE);
                    binding.profileTxt.setVisibility(View.GONE);

                    binding.scheduleImage.setImageResource(R.drawable.schedule);
                    binding.chatImage.setImageResource(R.drawable.chat);
                    binding.profileImage.setImageResource(R.drawable.profile);

                    binding.scheduleLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    binding.chatLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    binding.profileLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                    binding.homeTxt.setVisibility(View.VISIBLE);
                    binding.homeImage.setImageResource(R.drawable.home_selected);
                    binding.homeLayout.setBackgroundResource(R.drawable.round_back_home_100);

                    //animation
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f,1.0f,1f,1f, Animation.RELATIVE_TO_SELF,1.0f,Animation.RELATIVE_TO_SELF,0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    binding.homeLayout.startAnimation(scaleAnimation);

                    selectedTab = 1;
                }
            }
        });

        binding.scheduleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                if (selectedTab != 2){

                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainer,ScheduleFragment.class,null)
                            .commit();

                    //unselected other tabs expect home tab
                    binding.homeTxt.setVisibility(View.GONE);
                    binding.chatTxt.setVisibility(View.GONE);
                    binding.profileTxt.setVisibility(View.GONE);

                    binding.homeImage.setImageResource(R.drawable.home);
                    binding.chatImage.setImageResource(R.drawable.chat);
                    binding.profileImage.setImageResource(R.drawable.profile);

                    binding.homeLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    binding.chatLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    binding.profileLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                    binding.scheduleTxt.setVisibility(View.VISIBLE);
                    binding.scheduleImage.setImageResource(R.drawable.schedule_selected);
                    binding.scheduleLayout.setBackgroundResource(R.drawable.round_back_home_100);

                    //animation
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f,1.0f,1f,1f, Animation.RELATIVE_TO_SELF,1.0f,Animation.RELATIVE_TO_SELF,0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    binding.scheduleLayout.startAnimation(scaleAnimation);

                    selectedTab = 2;
                }
            }
        });

        binding.chatLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                if (selectedTab != 3){

                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainer, ChatFragment.class,null)
                            .commit();

                    //unselected other tabs expect home tab
                    binding.homeTxt.setVisibility(View.GONE);
                    binding.scheduleTxt.setVisibility(View.GONE);
                    binding.profileTxt.setVisibility(View.GONE);

                    binding.homeImage.setImageResource(R.drawable.home);
                    binding.scheduleImage.setImageResource(R.drawable.schedule);
                    binding.profileImage.setImageResource(R.drawable.profile);

                    binding.homeLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    binding.scheduleLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    binding.profileLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                    binding.chatTxt.setVisibility(View.VISIBLE);
                    binding.chatImage.setImageResource(R.drawable.chat_selected);
                    binding.chatLayout.setBackgroundResource(R.drawable.round_back_home_100);

                    //animation
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f,1.0f,1f,1f, Animation.RELATIVE_TO_SELF,1.0f,Animation.RELATIVE_TO_SELF,0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    binding.chatLayout.startAnimation(scaleAnimation);

                    selectedTab = 3;
                }
            }
        });

        binding.profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                if (selectedTab != 4){

                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainer,ProfileFragment.class,null)
                            .commit();

                    //unselected other tabs expect home tab
                    binding.homeTxt.setVisibility(View.GONE);
                    binding.scheduleTxt.setVisibility(View.GONE);
                    binding.chatTxt.setVisibility(View.GONE);

                    binding.homeImage.setImageResource(R.drawable.home);
                    binding.scheduleImage.setImageResource(R.drawable.schedule);
                    binding.chatImage.setImageResource(R.drawable.chat);

                    binding.homeLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    binding.scheduleLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    binding.chatLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                    binding.profileTxt.setVisibility(View.VISIBLE);
                    binding.profileImage.setImageResource(R.drawable.profile_selected);
                    binding.profileLayout.setBackgroundResource(R.drawable.round_back_home_100);

                    //animation
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f,1.0f,1f,1f, Animation.RELATIVE_TO_SELF,1.0f,Animation.RELATIVE_TO_SELF,0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    binding.profileLayout.startAnimation(scaleAnimation);

                    selectedTab = 4;
                }
            }
        });

    }





    @Override
    public
    void onBackPressed() {
        alert = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
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
                Intent intent = new Intent(MainActivity.this, SignInActivity.class);
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