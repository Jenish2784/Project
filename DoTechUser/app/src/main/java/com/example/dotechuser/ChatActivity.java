package com.example.dotechuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.dotechuser.Model.Consultation;
import com.example.dotechuser.Model.Message;
import com.example.dotechuser.adapter.MessageAdapter;
import com.example.dotechuser.databinding.ActivityChatBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    ActivityChatBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;
    String strDoctorProfile = "";
    String strDoctorName = "";
    String strDoctorId = "";
    String strConsultId = "";
    String status = "";
    String senderRoom = "";
    String receiverRoom = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        Intent intent = getIntent();
        if (intent.hasExtra("doctorId")) {
            strDoctorId = intent.getStringExtra("doctorId");
        }
        if (intent.hasExtra("doctorName")) {
            strDoctorName = intent.getStringExtra("doctorName");
            binding.docName.setText(strDoctorName);
        }
        if (intent.hasExtra("status")) {
            status = intent.getStringExtra("status");
        }
        if (intent.hasExtra("consultId")) {
            strConsultId = intent.getStringExtra("consultId");
        }
        if (intent.hasExtra("doctorProfile")) {
            strDoctorProfile = intent.getStringExtra("doctorProfile");
            if (!strDoctorProfile.equals("")) {
                Picasso.get()
                        .load(strDoctorProfile)
                        .placeholder(R.drawable.placeholder)
                        .into(binding.docProfileImage);
            } else {
                binding.docProfileImage.setImageResource(R.drawable.dotech_logo);
            }
        }


        if(status.equals("Accept")){
            binding.edtMessage.setVisibility(View.VISIBLE);
            binding.btnSend.setVisibility(View.VISIBLE);
            binding.tvconsultCompleted.setVisibility(View.INVISIBLE);
        }
        else if(status.equals("1")){
            binding.edtMessage.setVisibility(View.INVISIBLE);
            binding.btnSend.setVisibility(View.INVISIBLE);
            binding.tvconsultCompleted.setVisibility(View.VISIBLE);
        }


        //  set adapter
        final ArrayList<Message> list = new ArrayList<>();
        final MessageAdapter messageAdapter = new MessageAdapter(list, this, strDoctorId);
        binding.chatView.setAdapter(messageAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.chatView.setLayoutManager(linearLayoutManager);






        database.getReference().child("Chats").child(strConsultId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Message message = dataSnapshot.getValue(Message.class);
                            message.setMessageId(dataSnapshot.getKey());
                            list.add(message);
                        }
                        messageAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.edtMessage.getText().toString().equals("")) {

                } else {
                    String strMessage = binding.edtMessage.getText().toString();
                    final Message message = new Message(FirebaseAuth.getInstance().getUid(), strMessage);
                    message.setTimeStamp(new Date().getTime());
                    binding.edtMessage.setText("");
                    senderRoom = auth.getUid() + strDoctorId;
                    receiverRoom = strDoctorId + auth.getUid();

                    database.getReference().child("Chats").child(strConsultId).push().setValue(message)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    database.getReference().child("Chats").child(senderRoom).push().setValue(message)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    database.getReference().child("Chats").child(receiverRoom).push().setValue(message)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {

                                                                }
                                                            });
                                                }
                                            });
                                }
                            });
                }
            }
        });


    }
}