package com.example.doctorappui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.doctorappui.adapter.MessageAdapter;
import com.example.doctorappui.databinding.ActivityChatBinding;
import com.example.doctorappui.doctor.MainActivity;
import com.example.doctorappui.modal.Message;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public
class ChatActivity extends AppCompatActivity {

    ActivityChatBinding binding;
    String userName = "";
    String userId = "";
    String consultId = "";
    String consultDate = "";
    String paymentId = "";
    String status = "";
    String userProfileImage = "";
    FirebaseAuth auth;
    FirebaseDatabase database;
    Calendar calendar;
    String currentTime = "";
    String currentDate = "";
    AlertDialog.Builder alert;
    String senderRoom = "";
    String receiverRoom = "";
    String str;
    String strType = "";

    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        calendar = Calendar.getInstance();

        SimpleDateFormat simpleTime = new SimpleDateFormat("HH:mm");
        currentTime = simpleTime.format(calendar.getTime()).toString();
        //Toast.makeText(this,currentTime, Toast.LENGTH_SHORT).show();

        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = simpleDate.format(calendar.getTime()).toString();
        //Toast.makeText(this,currentDate, Toast.LENGTH_SHORT).show();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        Intent intent = getIntent();
        if (intent.hasExtra("name")) {
            userName = intent.getStringExtra("name");
            binding.userName.setText(userName);
        }
        if (intent.hasExtra("paymentId")) {
            paymentId = intent.getStringExtra("paymentId");
        }
        if (intent.hasExtra("profileImage")) {
            userProfileImage = intent.getStringExtra("profileImage");
            if (!userProfileImage.equals("")) {
                Picasso.get()
                        .load(userProfileImage)
                        .placeholder(R.drawable.placeholder)
                        .into(binding.profileImage);
            } else {
                binding.profileImage.setImageResource(R.drawable.dotech_logo);
            }
        }
        if (intent.hasExtra("userId")) {
            userId = intent.getStringExtra("userId");
        }
        if (intent.hasExtra("consultId")) {
            consultId = intent.getStringExtra("consultId");
            //Toast.makeText(this,consultId, Toast.LENGTH_SHORT).show();
        }
        if (intent.hasExtra("consultDate")) {
            consultDate = intent.getStringExtra("consultDate");
        }
        if (intent.hasExtra("status")) {
            status = intent.getStringExtra("status");
            if (status.equals("1")) {
                binding.relativeLayout.setVisibility(View.GONE);
                binding.edtMessage.setVisibility(View.INVISIBLE);
                binding.send.setVisibility(View.INVISIBLE);
                binding.tvConfirm.setVisibility(View.VISIBLE);
            } else {
                binding.relativeLayout.setVisibility(View.VISIBLE);
            }
        }
        if (intent.hasExtra("a")) {
            str = intent.getStringExtra("a");
        }

        SharedPreferences preferences = getSharedPreferences("userData", MODE_PRIVATE);
        strType = preferences.getString("type", "");

        if (strType.equals("hospital")) {
            binding.edtMessage.setVisibility(View.VISIBLE);
            binding.send.setVisibility(View.VISIBLE);
        }

        senderRoom = auth.getUid() + userId;
        receiverRoom = userId + auth.getUid();


        /*binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                Intent intent = new Intent(ChatActivity.this, ConsultDataActivity.class);
                intent.putExtra("paymentId", paymentId);
                intent.putExtra("consultId", consultId);
                startActivity(intent);
            }
        });*/

        binding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                alert = new AlertDialog.Builder(ChatActivity.this);
                LayoutInflater inflater = LayoutInflater.from(ChatActivity.this);
                View alert_view = inflater.inflate(R.layout.consult_confirm_alert_dialog, null, false);
                alert.setView(alert_view);
                Button yes = alert_view.findViewById(R.id.btnYes);
                Button no = alert_view.findViewById(R.id.btnNo);
                AlertDialog dialog = alert.create();
                dialog.show();

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public
                    void onClick(View view) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Consultation").child(consultId);
                        databaseReference.child("status").setValue("1");
                        dialog.dismiss();
                        binding.relativeLayout.setVisibility(View.GONE);
                        binding.edtMessage.setVisibility(View.INVISIBLE);
                        binding.send.setVisibility(View.INVISIBLE);
                        binding.tvConfirm.setVisibility(View.VISIBLE);
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
        });

        final ArrayList<Message> list = new ArrayList<>();
        final MessageAdapter adapter = new MessageAdapter(list, this, userId);
        binding.chatView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
       /* layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(false);
        binding.chatView.setHasFixedSize(true);*/
        binding.chatView.setLayoutManager(layoutManager);

        if (str.equals("user")) {
            if (strType.equals("doctor")){
                binding.tvConfirm.setVisibility(View.VISIBLE);
                binding.tvConfirm.setText("All chat available");
                binding.send.setVisibility(View.GONE);
                binding.edtMessage.setVisibility(View.GONE);
            }
            database.getReference().child("Chats")
                    .child(senderRoom)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public
                        void onDataChange(@NonNull DataSnapshot snapshot) {
                            list.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Message message = dataSnapshot.getValue(Message.class);
                                message.setMessageId(dataSnapshot.getKey());
                                list.add(message);
                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public
                        void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }

        if (str.equals("consult")) {

            database.getReference().child("Chats")
                    .child(consultId)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public
                        void onDataChange(@NonNull DataSnapshot snapshot) {
                            list.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Message message = dataSnapshot.getValue(Message.class);
                                message.setMessageId(dataSnapshot.getKey());
                                list.add(message);
                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public
                        void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

        }

        /*if (currentDate.equals(consultDate)){
            binding.edtMessage.setVisibility(View.VISIBLE);
            binding.send.setVisibility(View.VISIBLE);
        }else {
            binding.edtMessage.setVisibility(View.INVISIBLE);
            binding.send.setVisibility(View.INVISIBLE);
            binding.tvConfirm.setVisibility(View.VISIBLE);
        }*/


        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                String strMessage = binding.edtMessage.getText().toString();
                if (strMessage.equals("")) {

                } else {
                    if (strType.equals("doctor")) {
                        final Message message = new Message(auth.getUid(), strMessage);
                        message.setTimeStamp(new Date().getTime());
                        binding.edtMessage.setText("");

                        database.getReference().child("Chats")
                                .child(consultId).push()
                                .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public
                                    void onSuccess(Void unused) {
                                        final MessageAdapter adapter = new MessageAdapter(list, ChatActivity.this, userId);
                                        binding.chatView.setAdapter(adapter);

                                    /*LinearLayoutManager layoutManager = new LinearLayoutManager(ChatActivity.this);
                                    layoutManager.setStackFromEnd(true);
                                    layoutManager.setReverseLayout(false);
                                    binding.chatView.setHasFixedSize(true);
                                    binding.chatView.setLayoutManager(layoutManager);*/

                                        database.getReference().child("Chats")
                                                .child(senderRoom).push()
                                                .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public
                                                    void onSuccess(Void unused) {
                                                        database.getReference().child("Chats")
                                                                .child(receiverRoom).push()
                                                                .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public
                                                                    void onSuccess(Void unused) {

                                                                    }
                                                                });
                                                    }
                                                });

                                    }
                                });
                    } else if (strType.equals("hospital")) {
                        final Message message = new Message(auth.getUid(), strMessage);
                        message.setTimeStamp(new Date().getTime());
                        binding.edtMessage.setText("");

                        database.getReference().child("Chats")
                                .child(senderRoom).push()
                                .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public
                                    void onSuccess(Void unused) {
                                        database.getReference().child("Chats")
                                                .child(receiverRoom).push()
                                                .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public
                                                    void onSuccess(Void unused) {
                                                        final MessageAdapter adapter = new MessageAdapter(list, ChatActivity.this, userId);
                                                        binding.chatView.setAdapter(adapter);
                                                    }
                                                });
                                    }
                                });
                    } else {
                        Toast.makeText(ChatActivity.this, "Server down!", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

    }
}