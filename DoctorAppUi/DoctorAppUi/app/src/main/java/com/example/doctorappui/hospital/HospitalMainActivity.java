package com.example.doctorappui.hospital;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;

import com.example.doctorappui.R;
import com.example.doctorappui.SignInActivity;
import com.example.doctorappui.admin.AdminMainActivity;
import com.example.doctorappui.databinding.ActivityHospitalMainBinding;
import com.example.doctorappui.doctor.ChatFragment;
import com.example.doctorappui.doctor.HomeFragment;
import com.example.doctorappui.doctor.ProfileFragment;
import com.example.doctorappui.doctor.ScheduleFragment;

public
class HospitalMainActivity extends AppCompatActivity {

    ActivityHospitalMainBinding binding;
    AlertDialog.Builder alert;

    private int selectedTab = 1;

    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHospitalMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragmentContainer, HomeFragment.class,null)
                .commit();


        binding.homeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                if (selectedTab != 1){

                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainer, HomeFragment.class,null)
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
            }
        });



    }

    @Override
    public
    void onBackPressed() {
        alert = new AlertDialog.Builder(HospitalMainActivity.this);
        LayoutInflater inflater = LayoutInflater.from(HospitalMainActivity.this);
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
                Intent intent = new Intent(HospitalMainActivity.this, SignInActivity.class);
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