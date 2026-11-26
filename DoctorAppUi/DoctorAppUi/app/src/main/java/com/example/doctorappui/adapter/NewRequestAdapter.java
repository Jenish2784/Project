package com.example.doctorappui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doctorappui.EncryptDecrypt;
import com.example.doctorappui.admin.DoctorDataActivity;
import com.example.doctorappui.R;
import com.example.doctorappui.modal.Users;
import com.squareup.picasso.Picasso;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public
class NewRequestAdapter extends RecyclerView.Adapter<NewRequestAdapter.ViewHolder> {

    ArrayList<Users> list;
    Context context;


    public
    NewRequestAdapter(ArrayList<Users> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setFilteredList(List<Users> filteredList){
        this.list = (ArrayList<Users>) filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public
    NewRequestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.doctor_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public
    void onBindViewHolder(@NonNull NewRequestAdapter.ViewHolder holder, int position) {
        Users users = list.get(position);
        /*try {
            holder.tvName.setText(EncryptDecrypt.decrypt(users.getName()));
            holder.tvEmail.setText(EncryptDecrypt.decrypt(users.getEmailId()));
            holder.tvCategory.setText(EncryptDecrypt.decrypt(users.getCategory()));
        } catch (NoSuchPaddingException | NoSuchAlgorithmException |
                 InvalidAlgorithmParameterException | InvalidKeyException |
                 IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }*/
        holder.tvName.setText(users.getName());
        holder.tvEmail.setText(users.getEmailId());
        holder.tvCategory.setText(users.getCategory());


        if (!users.getDegreeImage().equals("")){
            Picasso.get()
                    .load(users.getDegreeImage())
                    .placeholder(R.drawable.placeholder)
                    .into(holder.imageView);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                Intent intent = new Intent(context, DoctorDataActivity.class);
                intent.putExtra("email", users.getEmailId());
                intent.putExtra("uId", users.getUserId());
                intent.putExtra("image",users.getDegreeImage());
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
        TextView tvName, tvEmail, tvCategory;

        public
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.profile_image);
            tvName = itemView.findViewById(R.id.tvName);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvCategory = itemView.findViewById(R.id.tvCategory);
        }
    }
}
