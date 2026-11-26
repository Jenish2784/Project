package com.example.dotechuser.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dotechuser.Model.Appointment;
import com.example.dotechuser.R;

import java.util.List;

public class PaymentAppoinrmentHistroyAdapter extends RecyclerView.Adapter<PaymentAppoinrmentHistroyAdapter.ViewHolder> {
    private Context context;
    private List<Appointment> appointmentList;

    public PaymentAppoinrmentHistroyAdapter(Context context, List<Appointment> appointmentList) {
        this.context = context;
        this.appointmentList = appointmentList;
    }


    @NonNull
    @Override
    public PaymentAppoinrmentHistroyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.table_payment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentAppoinrmentHistroyAdapter.ViewHolder holder, int position) {
        Appointment appointment = appointmentList.get(position);
        holder.name.setText(appointment.getDoctorName());
        holder.paymentId.setText(appointment.getPaymentId());
        holder.date.setText(appointment.getCurrentDate());
        holder.time.setText(appointment.getCurrentTime());
        holder.consultType.setText(appointment.getType());
        holder.fees.setText(appointment.getTotalAmount() + " ₹");
        holder.index.setText(appointment.getConsultId());
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView doctorProfile;
        TextView name,paymentId,date,time,fees,consultType,index;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            doctorProfile = itemView.findViewById(R.id.imgProfile);
            name = itemView.findViewById(R.id.doctorName);
            paymentId = itemView.findViewById(R.id.userPaymentId);
            date = itemView.findViewById(R.id.tvDate);
            index = itemView.findViewById(R.id.index);
            time = itemView.findViewById(R.id.tvTime);
            fees = itemView.findViewById(R.id.doctorFees);
            consultType = itemView.findViewById(R.id.consultType);
        }
    }
}
