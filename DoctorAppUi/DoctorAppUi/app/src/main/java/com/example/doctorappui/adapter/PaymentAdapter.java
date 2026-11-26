package com.example.doctorappui.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doctorappui.ConsultDataActivity;
import com.example.doctorappui.R;
import com.example.doctorappui.modal.Schedule;
import com.example.doctorappui.modal.Users;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;

public
class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ViewHolder>{

    private
    Context context;
    private
    List<Users> list;
    AlertDialog.Builder alert;



    public
    PaymentAdapter(Context context, List<Users> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public
    PaymentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.payment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public
    void onBindViewHolder(@NonNull PaymentAdapter.ViewHolder holder, int position) {
        Users users = list.get(position);

        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(0);
        format.setCurrency(Currency.getInstance("INR"));

        holder.name.setText(users.getName());
        holder.email.setText(users.getEmailId());
        holder.mobile.setText(users.getMobileNumber());
        holder.pAmount.setText(format.format(Long.parseLong(users.getTotalAmount())));
        holder.dAmount.setText(format.format(Long.parseLong(users.getDepositAmount())));
        holder.tAmount.setText(format.format(Long.parseLong(users.getTotalAmount()) + Long.parseLong(users.getDepositAmount())));

        holder.payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                if (users.getAccountNo().equals("")){
                    Toast.makeText(context,"Account Details Not Found!", Toast.LENGTH_SHORT).show();
                }else {
                    alert = new AlertDialog.Builder(context);
                    LayoutInflater inflater = LayoutInflater.from(context);
                    View alert_view = inflater.inflate(R.layout.payment_alert_dialog, null, false);
                    @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EditText edtAmount = alert_view.findViewById(R.id.edtAmount);
                    @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button btnYes = alert_view.findViewById(R.id.btnYes);
                    @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button btnNo = alert_view.findViewById(R.id.btnNo);
                    alert.setView(alert_view);
                    alert.setCancelable(false);
                    AlertDialog dialog = alert.create();
                    dialog.show();

                    btnYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public
                        void onClick(View view) {
                            String strAmount = edtAmount.getText().toString();
                            String strNote = "DoTech Payment";
                            if (strAmount.equals("")){
                                Toast.makeText(context, "Enter Amount", Toast.LENGTH_SHORT).show();
                            }
                            else if (Integer.parseInt(strAmount) > Integer.parseInt(users.getTotalAmount())){
                                Toast.makeText(context, "Your amount is not valid", Toast.LENGTH_SHORT).show();
                            }else {
                                int pendingAmount = 0;
                                int depositAmount = 0;

                                pendingAmount = Integer.parseInt(users.getTotalAmount()) - Integer.parseInt(strAmount);
                                depositAmount = Integer.parseInt(users.getDepositAmount()) + Integer.parseInt(strAmount);

                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("doctor and hospital").child(users.getUserId());
                                databaseReference.child("totalAmount").setValue(String.valueOf(pendingAmount));
                                databaseReference.child("depositAmount").setValue(String.valueOf(depositAmount));
                                dialog.dismiss();
                            }
                        }
                    });
                    btnNo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public
                        void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                }
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
        TextView name,email,mobile,pAmount,dAmount,tAmount;
        Button payBtn;
        public
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvName);
            email = itemView.findViewById(R.id.tvEmail);
            mobile = itemView.findViewById(R.id.tvMobile);
            pAmount = itemView.findViewById(R.id.tvPendingAmount);
            dAmount = itemView.findViewById(R.id.tvDepositAmount);
            tAmount = itemView.findViewById(R.id.tvTotalAmount);
            payBtn = itemView.findViewById(R.id.btnPay);
        }
    }
}
