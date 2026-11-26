package com.example.dotechuser.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dotechuser.Model.Appointment;
import com.example.dotechuser.Model.Consultation;
import com.example.dotechuser.R;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PaymentHistoryAdapter extends RecyclerView.Adapter<PaymentHistoryAdapter.ViewHolder> {

    private Context context;
    private List<Consultation> list;


    public PaymentHistoryAdapter(Context context, List<Consultation> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public PaymentHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.table_payment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentHistoryAdapter.ViewHolder holder, int position) {
        Consultation consultation = list.get(position);
        holder.name.setText(consultation.getDoctorName());
        holder.paymentId.setText(consultation.getPaymentId());
        holder.date.setText(consultation.getCurrentDate());
        holder.time.setText(consultation.getCurrentTime());
        holder.consultType.setText(consultation.getType());
        holder.fees.setText(consultation.getTotalAmount() + " ₹");
        holder.index.setText(consultation.getConsultId());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
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
