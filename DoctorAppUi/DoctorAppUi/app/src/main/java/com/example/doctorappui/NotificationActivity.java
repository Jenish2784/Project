package com.example.doctorappui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.example.doctorappui.adapter.NotificationAdapter;
import com.example.doctorappui.databinding.ActivityNotificationBinding;
import com.example.doctorappui.modal.Notification;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public
class NotificationActivity extends AppCompatActivity {

    ActivityNotificationBinding binding;
    FirebaseAuth auth;
    ArrayList<Notification> list = new ArrayList<>();
    NotificationAdapter adapter;

    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Notifications");
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.home)));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        auth = FirebaseAuth.getInstance();

        adapter = new NotificationAdapter(list,NotificationActivity.this);
        binding.notificationRecyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(NotificationActivity.this, RecyclerView.VERTICAL,true);
        binding.notificationRecyclerView.setLayoutManager(layoutManager);
        layoutManager.setReverseLayout(false);
        binding.notificationRecyclerView.setHasFixedSize(true);

        Query query = FirebaseDatabase.getInstance().getReference("notification").child(auth.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public
            void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Notification notification = dataSnapshot.getValue(Notification.class);
                    list.add(notification);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public
            void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}