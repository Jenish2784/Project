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

import com.example.doctorappui.ChatActivity;
import com.example.doctorappui.R;
import com.example.doctorappui.modal.ChatData;
import com.example.doctorappui.modal.SystemUsers;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public
class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private
    ArrayList<SystemUsers> list;
    private
    Context context;

    public
    ChatAdapter(ArrayList<SystemUsers> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public
    ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View item = inflater.inflate(R.layout.department_doctor_item,parent,false);
        return new ViewHolder(item);
    }

    @Override
    public
    void onBindViewHolder(@NonNull ChatAdapter.ViewHolder holder, int position) {
        SystemUsers systemUsers = list.get(position);
        holder.tvName.setText(systemUsers.getName());
        holder.tvEmail.setText(systemUsers.getEmail());

        if (!systemUsers.getProfileImage().equals("")){
            Picasso.get()
                    .load(systemUsers.getProfileImage())
                    .placeholder(R.drawable.placeholder)
                    .into(holder.imageView);
        }else {
            holder.imageView.setImageResource(R.drawable.dotech_logo);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                Intent intent = new Intent(context,ChatActivity.class);
                intent.putExtra("name",systemUsers.getName());
                intent.putExtra("profileImage",systemUsers.getProfileImage());
                intent.putExtra("userId",systemUsers.getUserId());
                //intent.putExtra("status",systemUsers.getStatus());
                intent.putExtra("a","user");
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
        TextView tvName,tvEmail;

        public
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.profile_image);
            tvName = itemView.findViewById(R.id.tvName);
            tvEmail = itemView.findViewById(R.id.tvEmail);
        }
    }
}
