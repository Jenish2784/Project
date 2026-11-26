package com.example.doctorappui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doctorappui.ConsultDataActivity;
import com.example.doctorappui.R;
import com.example.doctorappui.modal.Schedule;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public
class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {

    ArrayList<Schedule> list;
    private
    Context context;

    public
    ScheduleAdapter(ArrayList<Schedule> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public
    ScheduleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View item = inflater.inflate(R.layout.schedule_item, parent, false);
        return new ViewHolder(item);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public
    void onBindViewHolder(@NonNull ScheduleAdapter.ViewHolder holder, int position) {
        Schedule schedule = list.get(position);

        holder.txtName.setText(schedule.getUserName());
        holder.txtTime.setText(schedule.getTime());

        if (schedule.getType().equals("Chat Consultation")){
            holder.txtCall.setText("Chat consultation");
        }else if (schedule.getType().equals("Video Consultation")){
            holder.txtCall.setText("Video consultation");
        }else if (schedule.getType().equals("Online Appointment")){
            holder.txtCall.setText("Virtual");
        }

        if (schedule.getPaymentStatus().equals("0")) {
            holder.txtStatus.setText("Payment failed");
            holder.txtStatus.setTextColor(ContextCompat.getColor(context, R.color.red));
        } else if (schedule.getStatus().equals("0")) {
            holder.txtStatus.setText("Pending");
            holder.txtStatus.setTextColor(ContextCompat.getColor(context, R.color.orange));
        } else if (schedule.getStatus().equals("1")) {
            holder.txtStatus.setText("Complete");
            holder.txtStatus.setTextColor(ContextCompat.getColor(context, R.color.green));
        } else if (schedule.getStatus().equals("Accept")){
            holder.txtStatus.setText("In progress");
            holder.txtStatus.setTextColor(ContextCompat.getColor(context, R.color.green));
        }else if (schedule.getStatus().equals("Reject")){
            holder.txtStatus.setText("Rejected");
            holder.txtStatus.setTextColor(ContextCompat.getColor(context, R.color.red));
        } else if (schedule.getStatus().equals("AcceptH")){
            holder.txtStatus.setText("In Progress");
            holder.txtStatus.setTextColor(ContextCompat.getColor(context, R.color.green));
        } else {
            holder.txtStatus.setText("Payment success");
            holder.txtStatus.setTextColor(ContextCompat.getColor(context, R.color.green));
        }

        if (!schedule.getProfileImage().equals("")) {
            Picasso.get()
                    .load(schedule.getProfileImage())
                    .placeholder(R.drawable.placeholder)
                    .into(holder.imageView);
        } else {
            holder.imageView.setImageResource(R.drawable.dotech_logo);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                Intent intent = new Intent(context,ConsultDataActivity.class);
                intent.putExtra("paymentId",schedule.getPaymentId());
                intent.putExtra("consultId",schedule.getConsultId());
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
        TextView txtCall, txtName, txtTime, txtStatus;

        public
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.profileImage);
            txtCall = itemView.findViewById(R.id.txtCall);
            txtName = itemView.findViewById(R.id.txtName);
            txtTime = itemView.findViewById(R.id.txtTime);
            txtStatus = itemView.findViewById(R.id.txtStatus);
        }
    }
}
