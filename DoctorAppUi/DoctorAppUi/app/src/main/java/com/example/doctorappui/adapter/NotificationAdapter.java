package com.example.doctorappui.adapter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doctorappui.R;
import com.example.doctorappui.modal.Notification;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public
class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    ArrayList<Notification> list;
    Context context;

    public
    NotificationAdapter(ArrayList<Notification> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public
    NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public
    void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position) {
        Notification notification = list.get(position);
        SharedPreferences preferences = context.getSharedPreferences("userData", MODE_PRIVATE);

        holder.tvNotification.setText(Html.fromHtml("<b>" + preferences.getString("name", "") + "</b>" + " new " + notification.getConsultType() +
                " request by " + "<b>" + notification.getUserName() + "</b>" + " from " + notification.getDate() + " and " + notification.getTime()));
        holder.time.setText(notification.getCurrentDate() + "      " + notification.getCurrentTime());

        if (!notification.getUserProfile().equals("")) {
            Picasso.get()
                    .load(notification.getUserProfile())
                    .placeholder(R.drawable.placeholder)
                    .into(holder.imageView);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                Toast.makeText(context, notification.getCurrentTime(), Toast.LENGTH_SHORT).show();
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
        TextView tvNotification, time;

        public
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.notificationProfile);
            tvNotification = itemView.findViewById(R.id.notification);
            time = itemView.findViewById(R.id.time);
        }
    }
}
