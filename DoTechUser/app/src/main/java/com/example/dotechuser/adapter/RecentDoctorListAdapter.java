package com.example.dotechuser.adapter;

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

import com.example.dotechuser.DoctorDetailsActivity;
import com.example.dotechuser.Model.Consultation;
import com.example.dotechuser.Model.Doctor;
import com.example.dotechuser.Model.Following;
import com.example.dotechuser.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecentDoctorListAdapter extends RecyclerView.Adapter<RecentDoctorListAdapter.ViewHolder> {

    private Context context;
    private List<Doctor> list;
    String strMsgAmount;
    int totalAmount, gstAmount, msgAmount;

    public RecentDoctorListAdapter(Context context, List<Doctor> list) {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public RecentDoctorListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.doctor_category_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentDoctorListAdapter.ViewHolder holder, int position) {
        Doctor doctor = list.get(position);
        holder.doctorName.setText(doctor.getName());
        holder.doctorCategory.setText(doctor.getCategory());
        //holder.doctorFees.setText("NA");

        strMsgAmount = doctor.getMsgAmount();

        if (!strMsgAmount.equals("")) {
            msgAmount = Integer.parseInt(strMsgAmount);
            gstAmount = msgAmount * 18 / 100;
            totalAmount = msgAmount + gstAmount;
            holder.doctorFees.setText(totalAmount + " " + "₹");
        } else {
            holder.doctorFees.setText("NA");
        }

        if (!doctor.getProfileImage().equals("")){
            Picasso.get()
                    .load(doctor.getProfileImage())
                    .placeholder(R.drawable.placeholder)
                    .into(holder.profileImage);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DoctorDetailsActivity.class);
                intent.putExtra("email", doctor.getEmailId());
                intent.putExtra("doctorUId", doctor.getuId());
                intent.putExtra("totalAmount", doctor.getMsgAmount());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage;
        TextView doctorName,doctorCategory,doctorFees;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.doctorProfileImage);
            doctorName = itemView.findViewById(R.id.doctorName);
            doctorFees = itemView.findViewById(R.id.doctorFees);
            doctorCategory = itemView.findViewById(R.id.doctorCategory);
        }
    }
}
