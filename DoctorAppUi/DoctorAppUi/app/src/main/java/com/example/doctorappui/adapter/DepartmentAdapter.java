package com.example.doctorappui.adapter;

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

import com.example.doctorappui.R;
import com.example.doctorappui.hospital.DepartmentDoctorActivity;
import com.example.doctorappui.modal.Department;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public
class DepartmentAdapter extends RecyclerView.Adapter<DepartmentAdapter.ViewHolder> {

    ArrayList<Department> list;
    Context context;

    public
    DepartmentAdapter(ArrayList<Department> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public
    DepartmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.department_data,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public
    void onBindViewHolder(@NonNull DepartmentAdapter.ViewHolder holder, int position) {
        Department department = list.get(position);

        holder.departmentName.setText(department.getDepartmentName());
        Picasso.get()
                .load(department.getDepartmentIcon())
                .placeholder(R.drawable.placeholder)
                .into(holder.departmentIcon);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                Intent intent = new Intent(context, DepartmentDoctorActivity.class);
                intent.putExtra("department",department.getDepartmentName());
                intent.putExtra("uId",department.getUserId());
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
        ImageView departmentIcon;
        TextView departmentName;
        public
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            departmentIcon = itemView.findViewById(R.id.departmentIcon);
            departmentName = itemView.findViewById(R.id.tvDepartmentName);
        }
    }
}
