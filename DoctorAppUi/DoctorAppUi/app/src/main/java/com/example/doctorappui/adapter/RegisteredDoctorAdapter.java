package com.example.doctorappui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doctorappui.R;
import com.example.doctorappui.admin.DoctorDataActivity;
import com.example.doctorappui.modal.Users;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public
class RegisteredDoctorAdapter extends RecyclerView.Adapter<RegisteredDoctorAdapter.ViewHolder> {

    ArrayList<Users> list;
    Context context;

    public
    RegisteredDoctorAdapter(ArrayList<Users> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setFilteredList(List<Users> filteredList){
        this.list = (ArrayList<Users>) filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public
    RegisteredDoctorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.doctor_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public
    void onBindViewHolder(@NonNull RegisteredDoctorAdapter.ViewHolder holder, int position) {
        Users users = list.get(position);
        holder.tvName.setText(users.getName());
        holder.tvEmail.setText(users.getEmailId());
        holder.tvCategory.setText(users.getCategory());

        if (users.getStatus().equals("block")){
            holder.blockLayout.setVisibility(View.VISIBLE);
            holder.tvMsg.setText("Doctor Blocked");
        }else {
            holder.blockLayout.setVisibility(View.GONE);
        }

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {

            }
        });

        if (!users.getProfileImage().equals("")){
            Picasso.get()
                    .load(users.getProfileImage())
                    .placeholder(R.drawable.placeholder)
                    .into(holder.imageView);
        }else {
            holder.imageView.setImageResource(R.drawable.dotech_logo);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                Intent intent = new Intent(context, DoctorDataActivity.class);
                intent.putExtra("email",users.getEmailId());
                intent.putExtra("uId",users.getUserId());
                intent.putExtra("image",users.getProfileImage());
                intent.putExtra("status",users.getStatus());
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
        ImageView imageView;
        TextView tvName,tvEmail,tvCategory,tvMsg;
        RelativeLayout blockLayout;
        public
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.profile_image);
            tvName = itemView.findViewById(R.id.tvName);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            blockLayout = itemView.findViewById(R.id.blockLayout);
            tvMsg = itemView.findViewById(R.id.tvMsg);
        }
    }
}
