package com.example.dotechuser.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dotechuser.ConsultationDetailsActivity;
import com.example.dotechuser.Model.Consultation;
import com.example.dotechuser.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ConsultationListAdapter extends RecyclerView.Adapter<ConsultationListAdapter.ViewHolder> {

    private Context context;
    private List<Consultation> consultationList;

    public ConsultationListAdapter(Context context, List<Consultation> consultationList) {
        this.context = context;
        this.consultationList = consultationList;
    }

    @NonNull
    @Override
    public ConsultationListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.doctor_consultation_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConsultationListAdapter.ViewHolder holder, int position) {
        Consultation consultation = consultationList.get(position);
        holder.doctorName.setText(consultation.getDoctorName());
        holder.doctorCategory.setText(consultation.getCategory());
        holder.date.setText(consultation.getDate());
        holder.time.setText(consultation.getTime());

        if (consultation.getPaymentStatus().equals("0")) {
            holder.tvStatus.setText("Payment failed");
            holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.red));
        } else if (consultation.getStatus().equals("0")) {
            holder.tvStatus.setText("Pending");
            holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.orange));
        } else if (consultation.getStatus().equals("1")) {
            holder.tvStatus.setText("Completed");
            holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.green));
        } else if (consultation.getStatus().equals("Accept")) {
            holder.tvStatus.setText("In process");
            holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.green));
        } else if (consultation.getStatus().equals("Reject")) {
            holder.tvStatus.setText("Rejected");
            holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.red));
        } else {
            holder.tvStatus.setText("Payment success");
            holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.green));
        }

        String strImage = consultation.getDoctorProfileImage();
        if (!strImage.equals("")) {
            Picasso.get()
                    .load(strImage)
                    .placeholder(R.drawable.placeholder)
                    .into(holder.doctorProfileImage);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ConsultationDetailsActivity.class);
                intent.putExtra("paymentId", consultation.getPaymentId());
                intent.putExtra("consultId", consultation.getConsultId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return consultationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView doctorProfileImage;
        TextView doctorName, doctorCategory, date, time, tvStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            doctorProfileImage = itemView.findViewById(R.id.docProfileImage);
            doctorName = itemView.findViewById(R.id.docName);
            doctorCategory = itemView.findViewById(R.id.docCategory);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            tvStatus = itemView.findViewById(R.id.status);
        }
    }
}
