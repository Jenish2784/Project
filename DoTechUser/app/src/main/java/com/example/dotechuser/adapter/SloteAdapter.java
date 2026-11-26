package com.example.dotechuser.adapter;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dotechuser.Model.TimeSlote;
import com.example.dotechuser.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class SloteAdapter extends RecyclerView.Adapter<SloteAdapter.ViewHolder> {

    private Context context;
    private List<TimeSlote> list;

    FirebaseAuth auth;
    FirebaseDatabase database;

    String strTime;
    int selectedItem = -1;


    public SloteAdapter(Context context, List<TimeSlote> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public SloteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

        View view = LayoutInflater.from(context).inflate(R.layout.item_slot,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SloteAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        TimeSlote timeSlote = list.get(position);

        holder.textView.setText(timeSlote.getTime());

        holder.textView.setBackground(ContextCompat.getDrawable(context,R.drawable.rectangle_fill));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor pref = context.getSharedPreferences("timeSlot", MODE_PRIVATE).edit();
                pref.putString("time",timeSlote.getTime());
                pref.putString("timeId",timeSlote.getTimeId());
                pref.apply();

                selectedItem = position;
                notifyDataSetChanged();
            }
        });

        if (selectedItem == position){
            holder.textView.setBackground(ContextCompat.getDrawable(context,R.drawable.rectangle_fill));

            holder.textView.setTextColor(Color.WHITE);
        }else{
            holder.textView.setBackground(ContextCompat.getDrawable(context,R.drawable.rectangle_outline));

            holder.textView.setTextColor(Color.BLACK);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.text_view);
        }
    }
}
