package com.example.doctorappui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doctorappui.R;
import com.example.doctorappui.modal.Message;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public
class MessageAdapter extends RecyclerView.Adapter {

    ArrayList<Message> list;
    Context context;

    int SENDER_VIEW_TYPE = 1;
    int RECEIVER_VIEW_TYPE = 2;
    String recId;

    public
    MessageAdapter(ArrayList<Message> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public
    MessageAdapter(ArrayList<Message> list, Context context, String recId) {
        this.list = list;
        this.context = context;
        this.recId = recId;
    }



    @NonNull
    @Override
    public
    RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == SENDER_VIEW_TYPE) {
            View view = LayoutInflater.from(context).inflate(R.layout.sample_sender, parent, false);
            return new SenderViewHolder(view);
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.sample_receiver, parent, false);
            return new ReceiverViewHolder(view);
        }

    }

    @Override
    public
    int getItemViewType(int position) {
        if (list.get(position).getUserId().equals(FirebaseAuth.getInstance().getUid())){
            return SENDER_VIEW_TYPE;
        }else {
            return RECEIVER_VIEW_TYPE;
        }
    }

    @Override
    public
    void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = list.get(position);

        if (holder.getClass() == SenderViewHolder.class){
            ((SenderViewHolder)holder).senderMessage.setText(message.getMessage());
            Date date = new Date(message.getTimeStamp());
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("h:mm a");
            String strTime= simpleDateFormat.format(date);
            ((SenderViewHolder)holder).senderTime.setText(strTime);
        }else {
            ((ReceiverViewHolder)holder).receiverMessage.setText(message.getMessage());
            Date date = new Date(message.getTimeStamp());
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("h:mm a");
            String strTime= simpleDateFormat.format(date);
            ((ReceiverViewHolder)holder).receiverTime.setText(strTime);
        }
    }

    @Override
    public
    int getItemCount() {
        return list.size();
    }

    public
    class ReceiverViewHolder extends RecyclerView.ViewHolder {
        TextView receiverMessage, receiverTime;

        public
        ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            receiverMessage = itemView.findViewById(R.id.receiverText);
            receiverTime = itemView.findViewById(R.id.receiverTime);
        }
    }

    public
    class SenderViewHolder extends RecyclerView.ViewHolder {
        TextView senderMessage, senderTime;

        public
        SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMessage = itemView.findViewById(R.id.senderText);
            senderTime = itemView.findViewById(R.id.senderTime);
        }
    }

}
