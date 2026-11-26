package com.example.doctorappui.hospital;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.doctorappui.R;
import com.example.doctorappui.adapter.ChatAdapter;
import com.example.doctorappui.databinding.FragmentChatBinding;
import com.example.doctorappui.databinding.FragmentHospitalChatBinding;
import com.example.doctorappui.modal.ChatData;
import com.example.doctorappui.modal.SystemUsers;

import java.util.ArrayList;
import java.util.List;

public
class HospitalChatFragment extends Fragment {

    FragmentHospitalChatBinding binding;
    ArrayList<SystemUsers> chatData = new ArrayList<>();
    ChatAdapter adapter;

    public
    HospitalChatFragment() {
        // Required empty public constructor
    }

    @Override
    public
    View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHospitalChatBinding.inflate(inflater,container,false);

        adapter = new ChatAdapter(chatData,getContext());
        binding.chatRecyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.chatRecyclerView.setLayoutManager(layoutManager);

        /*chatData.add(new ChatData("Consultation","Hello", R.drawable.doctor));
        chatData.add(new ChatData("Consultation","Hello",R.drawable.doctor));
        chatData.add(new ChatData("Consultation","Hello",R.drawable.doctor));
        chatData.add(new ChatData("Consultation","Hello",R.drawable.doctor));*/

        return binding.getRoot();
    }
}