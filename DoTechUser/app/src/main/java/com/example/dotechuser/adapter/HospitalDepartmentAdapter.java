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

import com.example.dotechuser.HospitalDepartmentDoctorActivity;
import com.example.dotechuser.Model.Hospital;
import com.example.dotechuser.R;
import com.squareup.picasso.Picasso;


import java.util.List;

public class HospitalDepartmentAdapter extends RecyclerView.Adapter<HospitalDepartmentAdapter.ViewHolder> {

    private Context context;
    private List<Hospital> list;

    public HospitalDepartmentAdapter(Context context, List<Hospital> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public HospitalDepartmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.doctor_category_find_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HospitalDepartmentAdapter.ViewHolder holder, int position) {
        Hospital hospital = list.get(position);
        holder.departmentName.setText(hospital.getDepartmentName());

        String strImage = hospital.getDepartmentIcon();
        if (!strImage.equals("")){
            Picasso.get()
                    .load(strImage)
                    .placeholder(R.drawable.placeholder)
                    .into(holder.departmentIcon);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, HospitalDepartmentDoctorActivity.class);
                intent.putExtra("departmentName",hospital.getDepartmentName());
                intent.putExtra("fees",hospital.getFees());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView departmentIcon;
        TextView departmentName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            departmentIcon = itemView.findViewById(R.id.categoryImage);
            departmentName = itemView.findViewById(R.id.categoryName);
        }
    }
}
