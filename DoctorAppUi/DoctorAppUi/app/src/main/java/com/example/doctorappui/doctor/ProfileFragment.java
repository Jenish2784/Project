package com.example.doctorappui.doctor;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.doctorappui.BankAccountActivity;
import com.example.doctorappui.ContactUsActivity;
import com.example.doctorappui.NotificationActivity;
import com.example.doctorappui.ProfileActivity;
import com.example.doctorappui.R;
import com.example.doctorappui.ServiceManagementActivity;
import com.example.doctorappui.SignInActivity;
import com.example.doctorappui.databinding.FragmentChatBinding;
import com.example.doctorappui.databinding.FragmentProfileBinding;
import com.example.doctorappui.modal.Rating;
import com.example.doctorappui.modal.Users;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.Date;

public
class ProfileFragment extends Fragment {

   FragmentProfileBinding binding;

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    String strName="",strEmail="",strMobile="";
    String strProfileImage = "";
    AlertDialog.Builder alert;
    String strReview = "";
    String rating = "";
    String review = "";
    RatingBar ratingBar;
    EditText edtRating;
    Button submit;

    public
    ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public
    void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    @Override
    public
    View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater,container,false);

        SharedPreferences preferences = getContext().getSharedPreferences("userData",MODE_PRIVATE);
        strName = preferences.getString("name","");
        strEmail = preferences.getString("email","");
        strMobile = preferences.getString("mobile","");

        strProfileImage = preferences.getString("profileImage","");


        if (!strProfileImage.equals("")){
            Picasso.get()
                    .load(strProfileImage)
                    .placeholder(R.drawable.placeholder)
                    .into(binding.profileImage);
        }else {
            binding.profileImage.setImageResource(R.drawable.dotech_logo);
        }


        binding.tvName.setText(strName);
        binding.tvEmail.setText(strEmail);
        binding.tvCall.setText(strMobile);




        binding.profileCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                Intent intent = new Intent(getContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        binding.bankAccCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                Intent intent = new Intent(getContext(), BankAccountActivity.class);
                startActivity(intent);
            }
        });

        binding.serviceCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                Intent intent = new Intent(getContext(), ServiceManagementActivity.class);
                startActivity(intent);
            }
        });

        binding.ratingCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {


                //fetch user rating
                Query query = database.getReference("Rating").child("doTech").orderByChild("ratingBy").equalTo(mAuth.getUid());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public
                    void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            rating = String.valueOf(dataSnapshot.child("rating").getValue());
                            review = String.valueOf(dataSnapshot.child("review").getValue());
                            ratingBar.setRating(Float.parseFloat(rating));
                            edtRating.setText(review);
                        }
                    }

                    @Override
                    public
                    void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                alert = new AlertDialog.Builder(getContext());
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                View alert_view = layoutInflater.inflate(R.layout.rating_alert_dialog,null,false);
                alert.setView(alert_view);
                ratingBar = alert_view.findViewById(R.id.ratingBar);
                edtRating = alert_view.findViewById(R.id.edtReview);
                submit = alert_view.findViewById(R.id.btnSubmit);
                AlertDialog dialog = alert.create();
                dialog.show();



                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public
                    void onClick(View view) {
                        double strRating = ratingBar.getRating();

                        strReview = edtRating.getText().toString();

                        Rating rating = new Rating();
                        rating.setRating(strRating);
                        rating.setReview(strReview);
                        rating.setTimedAt(new Date().getTime());
                        rating.setRatingBy(mAuth.getUid());

                        database.getReference().child("Rating").child("doTech").child(mAuth.getUid()).setValue(rating)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public
                                    void onSuccess(Void unused) {
                                        Toast.makeText(getContext(), "Rating send successfully", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                });
                    }
                });

            }
        });

        binding.logoutCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {

                alert = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = LayoutInflater.from(getContext());
                View alert_view = inflater.inflate(R.layout.logout_alert_dialog,null,false);
                alert.setView(alert_view);
                Button yes = alert_view.findViewById(R.id.btnYes);
                Button no = alert_view.findViewById(R.id.btnNo);
                AlertDialog dialog = alert.create();
                dialog.show();

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public
                    void onClick(View view) {
                        SharedPreferences.Editor pref=getContext().getSharedPreferences("userData",MODE_PRIVATE).edit();
                        pref.putString("message","logout");
                        pref.apply();
                        Intent intent = new Intent(getContext(), SignInActivity.class);
                        startActivity(intent);
                    }
                });

                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public
                    void onClick(View view) {
                        dialog.dismiss();
                    }
                });


            }
        });

        binding.contactCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                Intent intent = new Intent(getContext(), ContactUsActivity.class);
                startActivity(intent);
            }
        });

        binding.iconNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                Intent intent = new Intent(getContext(), NotificationActivity.class);
                startActivity(intent);
            }
        });

        return binding.getRoot();
    }
}