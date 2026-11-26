package com.example.doctorappui.hospital;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.doctorappui.R;
import com.example.doctorappui.adapter.ScheduleAdapter;
import com.example.doctorappui.databinding.FragmentHospitalScheduleBinding;
import com.example.doctorappui.modal.Schedule;

import java.util.ArrayList;
import java.util.List;

public
class HospitalScheduleFragment extends Fragment {


    FragmentHospitalScheduleBinding binding;
    ArrayList<Schedule> scheduleData = new ArrayList<>();
    ScheduleAdapter adapter;

    public
    HospitalScheduleFragment() {
        // Required empty public constructor
    }


    @Override
    public
    View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHospitalScheduleBinding.inflate(inflater,container,false);

        adapter = new ScheduleAdapter(scheduleData,getContext());
        binding.scheduleRecyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        binding.scheduleRecyclerView.setLayoutManager(layoutManager);

        /*scheduleData.add(new Schedule(R.drawable.doctor,"Appointment","Dr.Abc","8:20","Uncompleted"));
        scheduleData.add(new Schedule(R.drawable.doctor,"Consultation","Dr.Xyz","8:20","Uncompleted"));
        scheduleData.add(new Schedule(R.drawable.doctor,"Appointment","Dr.Pqr","8:20","Completed"));
        scheduleData.add(new Schedule(R.drawable.doctor,"Appointment ","Dr.Demo","8:20","Uncompleted"));
        scheduleData.add(new Schedule(R.drawable.doctor,"Consultation","Dr.Abc","8:20","Completed"));*/

        return binding.getRoot();
    }
}