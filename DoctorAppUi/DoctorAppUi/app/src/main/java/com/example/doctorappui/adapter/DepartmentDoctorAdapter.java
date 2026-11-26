package com.example.doctorappui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doctorappui.hospital.DepartmentDoctorDataActivity;
import com.example.doctorappui.R;
import com.example.doctorappui.hospital.ManageDepartmentDoctorActivity;
import com.example.doctorappui.modal.DepartmentDoctor;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public
class DepartmentDoctorAdapter extends RecyclerView.Adapter<DepartmentDoctorAdapter.ViewHolder> {

    ArrayList<DepartmentDoctor> list;
    Context context;

    public
    DepartmentDoctorAdapter(ArrayList<DepartmentDoctor> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public
    DepartmentDoctorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.department_doctor_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public
    void onBindViewHolder(@NonNull DepartmentDoctorAdapter.ViewHolder holder, int position) {
        DepartmentDoctor departmentDoctor = list.get(position);

        holder.name.setText(departmentDoctor.getName());
        holder.email.setText(departmentDoctor.getEmailId());

        if (!departmentDoctor.getProfileImage().equals("")){
            Picasso.get()
                    .load(departmentDoctor.getProfileImage())
                    .placeholder(R.drawable.placeholder)
                    .into(holder.profileImage);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                Intent intent = new Intent(context, ManageDepartmentDoctorActivity.class);
                intent.putExtra("email",departmentDoctor.getEmailId());
                intent.putExtra("uId",departmentDoctor.getUserId());
                intent.putExtra("departmentName",departmentDoctor.getCategory());
                intent.putExtra("name",departmentDoctor.getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public
    int getItemCount() {
        return list.size();
    }

    public
    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage;
        TextView email,name;
        public
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profile_image);
            email = itemView.findViewById(R.id.tvEmail);
            name = itemView.findViewById(R.id.tvName);
        }
    }
}
