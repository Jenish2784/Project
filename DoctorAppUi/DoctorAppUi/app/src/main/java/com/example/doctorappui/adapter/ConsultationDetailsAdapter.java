package com.example.doctorappui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doctorappui.R;
import com.example.doctorappui.modal.Schedule;
import com.example.doctorappui.modal.Users;

import org.jetbrains.annotations.TestOnly;

import java.util.ArrayList;
import java.util.List;

public
class ConsultationDetailsAdapter extends RecyclerView.Adapter<ConsultationDetailsAdapter.ViewHolder> {
    private
    Context context;
    private
    List<Schedule> list;

    public
    ConsultationDetailsAdapter(Context context, List<Schedule> list) {
        this.context = context;
        this.list = list;
    }

    public void setFilteredList(List<Schedule> filteredList){
        this.list = (ArrayList<Schedule>) filteredList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public
    ConsultationDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.consultation_details, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public
    void onBindViewHolder(@NonNull ConsultationDetailsAdapter.ViewHolder holder, int position) {
        Schedule schedule = list.get(position);
        holder.dName.setText(schedule.getDoctorName());
        holder.pName.setText(schedule.getUserName());
        holder.type.setText(schedule.getType());
        holder.date.setText(schedule.getDate());
        holder.time.setText(schedule.getTime());
        holder.fees.setText(schedule.getTotalAmount() + " ₹");
        holder.index.setText(schedule.getConsultId());

        if (schedule.getPaymentStatus().equals("0")) {
            holder.paymentId.setText("Payment failed");
        } else {
            holder.paymentId.setText(schedule.getPaymentId());
        }

        if (schedule.getStatus().equals("0")) {
            holder.status.setText("Pending");
            holder.status.setTextColor(ContextCompat.getColor(context, R.color.orange));
        } else if (schedule.getStatus().equals("Accept")) {
            holder.status.setText("In Progress");
            holder.status.setTextColor(ContextCompat.getColor(context, R.color.green));
        } else if (schedule.getStatus().equals("Reject")) {
            holder.status.setText("Rejected");
            holder.status.setTextColor(ContextCompat.getColor(context, R.color.red));
        } else if (schedule.getStatus().equals("1")) {
            holder.status.setText("Completed");
            holder.status.setTextColor(ContextCompat.getColor(context, R.color.green));
        } else if (schedule.getPaymentStatus().equals("0")) {
            holder.status.setText("Payment failed");
            holder.status.setTextColor(ContextCompat.getColor(context, R.color.red));
        } else {
            holder.status.setText("Server down");
            holder.status.setTextColor(ContextCompat.getColor(context, R.color.red));
        }


    }

    @Override
    public
    int getItemCount() {
        return list.size();
    }

    public
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView dName, pName, type, date, time, paymentId, fees, status, index;

        public
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            dName = itemView.findViewById(R.id.doctorName);
            pName = itemView.findViewById(R.id.patientName);
            type = itemView.findViewById(R.id.consultType);
            date = itemView.findViewById(R.id.tvDate);
            time = itemView.findViewById(R.id.tvTime);
            paymentId = itemView.findViewById(R.id.userPaymentId);
            fees = itemView.findViewById(R.id.doctorFees);
            status = itemView.findViewById(R.id.status);
            index = itemView.findViewById(R.id.index);
        }
    }
}
