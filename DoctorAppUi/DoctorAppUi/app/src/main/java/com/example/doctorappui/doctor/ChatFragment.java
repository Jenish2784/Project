package com.example.doctorappui.doctor;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.doctorappui.NotificationActivity;
import com.example.doctorappui.R;
import com.example.doctorappui.adapter.ChatAdapter;
import com.example.doctorappui.adapter.PatientListAdapter;
import com.example.doctorappui.databinding.FragmentChatBinding;
import com.example.doctorappui.modal.ChatData;
import com.example.doctorappui.modal.Follow;
import com.example.doctorappui.modal.SystemUsers;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public
class ChatFragment extends Fragment {

    FragmentChatBinding binding;
    ArrayList<SystemUsers> chatData = new ArrayList<>();
    ChatAdapter adapter;
    String strType = "";
    FirebaseDatabase database;
    FirebaseAuth auth;

    public
    ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public
    View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentChatBinding.inflate(inflater, container, false);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        adapter = new ChatAdapter(chatData, getContext());
        binding.chatRecyclerView.setAdapter(adapter);

        SharedPreferences preferences = getContext().getSharedPreferences("userData", MODE_PRIVATE);
        strType = preferences.getString("type", "");


        if (strType.equals("doctor")) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            binding.chatRecyclerView.setLayoutManager(layoutManager);
            binding.chatRecyclerView.setHasFixedSize(true);

            Query queryPatient = FirebaseDatabase.getInstance().getReference("doctor and hospital").child(auth.getUid()).child("followers");
            queryPatient.addValueEventListener(new ValueEventListener() {
                @Override
                public
                void onDataChange(@NonNull DataSnapshot snapshot) {
                    chatData.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Follow follow = dataSnapshot.getValue(Follow.class);


                        //fetch user data
                        Query query = FirebaseDatabase.getInstance().getReference("Users");
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public
                            void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                                    SystemUsers systemUsers = dataSnapshot1.getValue(SystemUsers.class);
                                    systemUsers.setUserId(dataSnapshot1.getKey());
                                    if (follow.getFollowedBy().equals(dataSnapshot1.getKey())) {
                                        chatData.add(systemUsers);
                                    }
                                }
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public
                            void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }

                }

                @Override
                public
                void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else if (strType.equals("hospital")) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            binding.chatRecyclerView.setLayoutManager(layoutManager);
            binding.chatRecyclerView.setHasFixedSize(true);

            Query queryPatient = FirebaseDatabase.getInstance().getReference("doctor and hospital").child(auth.getUid()).child("followers");
            queryPatient.addValueEventListener(new ValueEventListener() {
                @Override
                public
                void onDataChange(@NonNull DataSnapshot snapshot) {
                    chatData.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Follow follow = dataSnapshot.getValue(Follow.class);


                        //fetch user data
                        Query query = FirebaseDatabase.getInstance().getReference("Users");
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public
                            void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                                    SystemUsers systemUsers = dataSnapshot1.getValue(SystemUsers.class);
                                    systemUsers.setUserId(dataSnapshot1.getKey());
                                    if (follow.getFollowedBy().equals(dataSnapshot1.getKey())) {
                                        chatData.add(systemUsers);
                                    }
                                }
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public
                            void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }

                }

                @Override
                public
                void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else {
            Toast.makeText(getContext(), "Server down!", Toast.LENGTH_SHORT).show();
        }


        binding.iconNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                Intent intent = new Intent(getContext(), NotificationActivity.class);
                startActivity(intent);
            }
        });


        return binding.getRoot();
    }
}