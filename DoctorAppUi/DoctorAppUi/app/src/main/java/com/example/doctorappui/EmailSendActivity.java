package com.example.doctorappui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class EmailSendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_send);
        Toast.makeText(this, "Notification processed successfully", Toast.LENGTH_SHORT).show();
        finish();
    }
}
