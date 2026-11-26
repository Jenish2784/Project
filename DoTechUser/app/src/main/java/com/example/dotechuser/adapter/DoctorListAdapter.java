package com.example.dotechuser.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dotechuser.DoctorDetailsActivity;
import com.example.dotechuser.Model.Category;
import com.example.dotechuser.Model.Doctor;
import com.example.dotechuser.Model.Rating;
import com.example.dotechuser.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DoctorListAdapter extends RecyclerView.Adapter<DoctorListAdapter.ViewHolder> {
    private Context context;
    private List<Doctor> doctorList;
    String strMsgAmount;
    int totalAmount, gstAmount, msgAmount;

    public DoctorListAdapter(Context context, List<Doctor> doctorList) {
        this.context = context;
        this.doctorList = doctorList;
    }

    public void setFilteredList(List<Doctor> filteredList) {
        this.doctorList = (ArrayList<Doctor>) filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DoctorListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.doctor_category_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorListAdapter.ViewHolder holder, int position) {
        Doctor doctor = doctorList.get(position);
        holder.doctorName.setText(doctor.getName());
        holder.doctorCategory.setText(doctor.getCategory());

        strMsgAmount = doctor.getMsgAmount();

        if (!strMsgAmount.equals("")) {
            msgAmount = Integer.parseInt(strMsgAmount);
            gstAmount = msgAmount * 18 / 100;
            totalAmount = msgAmount + gstAmount;
            holder.doctorFees.setText(totalAmount + " " + "₹");
        } else {
            holder.doctorFees.setText("NA");
        }

        String strImage = doctor.getProfileImage();

        if (!strImage.equals("")) {
            Picasso.get()
                    .load(strImage)
                    .placeholder(R.drawable.placeholder)
                    .into(holder.doctorProfileImage);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DoctorDetailsActivity.class);
                intent.putExtra("email", doctor.getEmailId());
                intent.putExtra("doctorUId", doctor.getuId());
                /*intent.putExtra("totalAmount", doctor.getMsgAmount());*/
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView doctorProfileImage;
        TextView doctorName, doctorCategory, doctorFees;

        RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            doctorProfileImage = itemView.findViewById(R.id.doctorProfileImage);
            doctorName = itemView.findViewById(R.id.doctorName);
            doctorCategory = itemView.findViewById(R.id.doctorCategory);
            doctorFees = itemView.findViewById(R.id.doctorFees);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }
    }
}
