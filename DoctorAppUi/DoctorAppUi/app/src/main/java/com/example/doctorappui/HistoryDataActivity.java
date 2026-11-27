package com.example.doctorappui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.doctorappui.adapter.HistoryFragmentsAdapter;
import com.example.doctorappui.databinding.ActivityHistoryDataBinding;
import com.example.doctorappui.doctor.MainActivity;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.auth.FirebaseAuth;

public
class HistoryDataActivity extends AppCompatActivity {

    ActivityHistoryDataBinding binding;
    FirebaseAuth auth;

    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHistoryDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Consult History");
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.home)));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        binding.viewpager.setAdapter(new HistoryFragmentsAdapter(getSupportFragmentManager()));
        binding.tabLayout.setupWithViewPager(binding.viewpager);


        //calender range

        MaterialDatePicker materialDatePicker = MaterialDatePicker.Builder.dateRangePicker()
                .setSelection(Pair.create(MaterialDatePicker.thisMonthInUtcMilliseconds(), MaterialDatePicker.todayInUtcMilliseconds())).build();

        /*binding.btnCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                materialDatePicker.show(getSupportFragmentManager(), "tag_picker");
                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public
                    void onPositiveButtonClick(Object selection) {
                        Toast.makeText(HistoryDataActivity.this, materialDatePicker.getHeaderText() + "", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });*/

    }

    @Override
    public boolean onSupportNavigateUp() {
        exitFormApp();
        finish();
        return true;
    }

    @Override
    public
    void onBackPressed() {
        super.onBackPressed();
        exitFormApp();
    }

    private
    void exitFormApp() {
        Intent intent = new Intent(HistoryDataActivity.this, MainActivity.class);
        startActivity(intent);
    }


}