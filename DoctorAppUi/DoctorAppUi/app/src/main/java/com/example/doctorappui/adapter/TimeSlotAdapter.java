package com.example.doctorappui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doctorappui.R;
import com.example.doctorappui.modal.Category;
import com.example.doctorappui.modal.TimeSlot;

import java.util.List;

public
class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.ViewHolder> {

    private
    List<TimeSlot> list;
    private
    Context context;


    public
    TimeSlotAdapter(List<TimeSlot> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public
    TimeSlotAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.time_data,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public
    void onBindViewHolder(@NonNull TimeSlotAdapter.ViewHolder holder, int position) {
        TimeSlot timeSlot = list.get(position);
        holder.tvTime.setText(timeSlot.getTime());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                Toast.makeText(context,timeSlot.getTimeId(), Toast.LENGTH_SHORT).show();

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
        TextView tvTime;
        public
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }
}
