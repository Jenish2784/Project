package com.example.doctorappui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doctorappui.R;
import com.example.doctorappui.admin.UserDataActivity;
import com.example.doctorappui.modal.SystemUsers;
import com.example.doctorappui.modal.Users;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public
class RegisteredUserAdapter extends RecyclerView.Adapter<RegisteredUserAdapter.ViewHolder> {

    ArrayList<SystemUsers> list;
    Context context;

    public
    RegisteredUserAdapter(ArrayList<SystemUsers> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setFilteredList(List<SystemUsers> filteredList){
        this.list = (ArrayList<SystemUsers>) filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public
    RegisteredUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.doctor_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public
    void onBindViewHolder(@NonNull RegisteredUserAdapter.ViewHolder holder, int position) {
        SystemUsers systemUsers = list.get(position);
        holder.tvName.setText(systemUsers.getName());
        holder.tvEmail.setText(systemUsers.getEmail());
        holder.tvCategory.setText(systemUsers.getMobile());

        if (!systemUsers.getProfileImage().equals("")){
            Picasso.get()
                    .load(systemUsers.getProfileImage())
                    .placeholder(R.drawable.placeholder)
                    .into(holder.imageView);
        }else {
            holder.imageView.setImageResource(R.drawable.dotech_logo);
        }

        if (systemUsers.getStatus().equals("0")){
            holder.blockLayout.setVisibility(View.VISIBLE);
            holder.tvMsg.setText("Patient Blocked");
        }else {
            holder.blockLayout.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                Intent intent = new Intent(context, UserDataActivity.class);
                intent.putExtra("email",systemUsers.getEmail());
                intent.putExtra("uId",systemUsers.getUserId());
                intent.putExtra("image",systemUsers.getProfileImage());
                intent.putExtra("status",systemUsers.getStatus());
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
