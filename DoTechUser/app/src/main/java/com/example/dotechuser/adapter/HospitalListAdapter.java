package com.example.dotechuser.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dotechuser.HospitalActivity;
import com.example.dotechuser.Model.Hospital;
import com.example.dotechuser.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HospitalListAdapter extends RecyclerView.Adapter<HospitalListAdapter.ViewHolder> {
    private Context context;
    private List<Hospital> hospitalList;

    public HospitalListAdapter(Context context, List<Hospital> hospitalList) {
        this.context = context;
        this.hospitalList = hospitalList;
    }

    public void setFilteredList(List<Hospital> filteredList) {
        this.hospitalList = (ArrayList<Hospital>) filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HospitalListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.hospital_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HospitalListAdapter.ViewHolder holder, int position) {
        Hospital hospital = hospitalList.get(position);
        holder.tvHospital.setText(hospital.getName());
        holder.hospitalAddress.setText(hospital.getAddress());

        if (!hospital.getProfileImage().equals("")){
            Picasso.get()
                    .load(hospital.getProfileImage())
                    .placeholder(R.drawable.placeholder)
                    .into(holder.hopitalProfile);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, HospitalActivity.class);
                intent.putExtra("hospitalName",hospital.getName());
                intent.putExtra("emailId",hospital.getEmailId());
                intent.putExtra("hospitalId",hospital.getHospitalId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return hospitalList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvHospital,hospitalAddress;
        ImageView hopitalProfile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHospital = itemView.findViewById(R.id.hospitalName);
            hospitalAddress = itemView.findViewById(R.id.hospitalAddress);
            hopitalProfile = itemView.findViewById(R.id.hospitalProfileImage);
        }
    }
}
