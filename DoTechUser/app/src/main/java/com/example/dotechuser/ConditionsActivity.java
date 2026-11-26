package com.example.dotechuser;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.dotechuser.databinding.ActivityConditionsBinding;

public class ConditionsActivity extends AppCompatActivity {

    ActivityConditionsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConditionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}