package com.example.doctorappui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doctorappui.R;
import com.example.doctorappui.modal.ContactUs;
import com.example.doctorappui.modal.Schedule;
import com.squareup.picasso.Picasso;

import java.util.List;

public
class ContactUsAdapter extends RecyclerView.Adapter<ContactUsAdapter.ViewHolder>{

    private
    Context context;
    private
    List<ContactUs> list;

    public
    ContactUsAdapter(Context context, List<ContactUs> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public
    ContactUsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.contact_us_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public
    void onBindViewHolder(@NonNull ContactUsAdapter.ViewHolder holder, int position) {
        ContactUs contactUs = list.get(position);

        if (!contactUs.getProfileImage().equals("")){
            Picasso.get()
                    .load(contactUs.getProfileImage())
                    .placeholder(R.drawable.placeholder)
                    .into(holder.profileImage);
        }else {
            holder.profileImage.setImageResource(R.drawable.dotech_logo);
        }

        if (!contactUs.getName().equals("")){
            holder.name.setText(contactUs.getName());
        }else {
            holder.name.setText("NULL");
        }
        if (!contactUs.getSubject().equals("")){
            holder.subject.setText(contactUs.getSubject());
        }else {
            holder.subject.setText("NULL");
        }
        if (!contactUs.getMessage().equals("")){
            holder.message.setText(contactUs.getMessage());
        }else {
            holder.message.setText("NULL");
        }
        holder.date.setText(contactUs.getCurrentDate());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                Toast.makeText(context,contactUs.getMessage(), Toast.LENGTH_SHORT).show();
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
        TextView name,subject,message,date,time;
        public
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profile_image);
            name = itemView.findViewById(R.id.tvName);
            subject = itemView.findViewById(R.id.tvSubject);
            message = itemView.findViewById(R.id.tvMessage);
            date = itemView.findViewById(R.id.tvDate);
            time = itemView.findViewById(R.id.tvTime);
        }
    }
}
