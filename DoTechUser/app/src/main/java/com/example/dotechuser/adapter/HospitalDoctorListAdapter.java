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

import com.example.dotechuser.HospitalDoctorDetailsActivity;
import com.example.dotechuser.Model.Department_Doctor;
import com.example.dotechuser.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HospitalDoctorListAdapter extends RecyclerView.Adapter<HospitalDoctorListAdapter.ViewHolder> {

    private Context context;
    private List<Department_Doctor> list;
    String strMsgAmount;
    int totalAmount, gstAmount, msgAmount;

    public HospitalDoctorListAdapter(Context context, List<Department_Doctor> list) {
        this.context = context;
        this.list = list;
    }

    public void setFilteredList(List<Department_Doctor> filteredList){
        this.list = (ArrayList<Department_Doctor>) filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HospitalDoctorListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.doctor_category_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HospitalDoctorListAdapter.ViewHolder holder, int position) {
        Department_Doctor departmentDoctor = list.get(position);
        holder.doctorName.setText(departmentDoctor.getName());
        holder.doctorCategory.setText(departmentDoctor.getCategory());

        strMsgAmount = departmentDoctor.getFees();

        if (!strMsgAmount.equals("")) {
            msgAmount = Integer.parseInt(strMsgAmount);
            gstAmount = msgAmount * 18 / 100;
            totalAmount = msgAmount + gstAmount;
            holder.doctorFees.setText(totalAmount + " " + "₹");
        } else {
            holder.doctorFees.setText("NA");
        }

        String strImage = departmentDoctor.getProfileImage();

        if (!strImage.equals("")){
            Picasso.get()
                    .load(strImage)
                    .placeholder(R.drawable.placeholder)
                    .into(holder.doctorProfileImage);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, HospitalDoctorDetailsActivity.class);
                intent.putExtra("doctorName",departmentDoctor.getName());
                intent.putExtra("doctorId",departmentDoctor.getDoctorId());
                intent.putExtra("doctorProfileImage",departmentDoctor.getProfileImage());
                intent.putExtra("doctorCategory",departmentDoctor.getCategory());
                intent.putExtra("emailId",departmentDoctor.getEmailId());
                intent.putExtra("fees",totalAmount + "");
                context.startActivity(intent);
               /* SharedPreferences.Editor pref = context.getSharedPreferences("hospitalDetails", MODE_PRIVATE).edit();
                pref.putString("hospitalId", "");
                pref.apply();*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView doctorProfileImage;
        TextView doctorName, doctorCategory, doctorFees;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            doctorProfileImage = itemView.findViewById(R.id.doctorProfileImage);
            doctorName = itemView.findViewById(R.id.doctorName);
            doctorCategory = itemView.findViewById(R.id.doctorCategory);
            doctorFees = itemView.findViewById(R.id.doctorFees);
        }
    }
}
