package com.example.dotechuser.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.dotechuser.ConsultationListActivity;
import com.example.dotechuser.Model.User;
import com.example.dotechuser.PaymentHistoryActivity;
import com.example.dotechuser.R;
import com.example.dotechuser.RecentDoctorListActivity;
import com.example.dotechuser.RecentHospitalListActivity;
import com.example.dotechuser.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        SharedPreferences preferences = getContext().getSharedPreferences("userDetails", MODE_PRIVATE);
        String strName = preferences.getString("name", "");

        binding.userName.setText(strName);

        database.getReference().child("Users").child(auth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            User user = snapshot.getValue(User.class);

                            if (!user.getProfileImage().equals("")) {
                                Picasso.get()
                                        .load(user.getProfileImage())
                                        .placeholder(R.drawable.placeholder)
                                        .into(binding.imgProfile);
                            }
                            /*if (user.getStatus().equals("0")){
                                Toast.makeText(getContext(), "sudbckjsbcskjcb", Toast.LENGTH_SHORT).show();
                            }*/
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.myConsultation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ConsultationListActivity.class);
                SharedPreferences.Editor pref = getContext().getSharedPreferences("appointmentMsg", MODE_PRIVATE).edit();
                pref.putString("msg", "Consultation");
                pref.apply();
                startActivity(intent);
            }
        });

        binding.myAppoinment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ConsultationListActivity.class);
                SharedPreferences.Editor pref = getContext().getSharedPreferences("appointmentMsg", MODE_PRIVATE).edit();
                pref.putString("msg", "Appointment");
                pref.apply();
                startActivity(intent);
            }
        });

        binding.doctorList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), RecentDoctorListActivity.class);
                startActivity(intent);
            }
        });

        binding.hospitalList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), RecentHospitalListActivity.class);
                startActivity(intent);
            }
        });

        binding.paymentHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PaymentHistoryActivity.class);
                startActivity(intent);
            }
        });

        return binding.getRoot();
    }


}