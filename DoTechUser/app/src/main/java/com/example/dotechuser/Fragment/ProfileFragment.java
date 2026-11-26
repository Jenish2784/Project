package com.example.dotechuser.Fragment;

import static android.content.Context.MODE_PRIVATE;
import static com.example.dotechuser.R.drawable;
import static com.example.dotechuser.R.id;
import static com.example.dotechuser.R.layout.logout_alert_dialog;
import static com.example.dotechuser.R.layout.rating_alert_dialog;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.dotechuser.ConditionsActivity;
import com.example.dotechuser.ContactUsActivity;
import com.example.dotechuser.DoctorRatingActivity;
import com.example.dotechuser.EditProfileActivity;
import com.example.dotechuser.Model.Rating;
import com.example.dotechuser.Model.User;
import com.example.dotechuser.R;
import com.example.dotechuser.SignInActivity;
import com.example.dotechuser.databinding.FragmentProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Date;

public class ProfileFragment extends Fragment {

    FragmentProfileBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    String strName = "";
    String strEmail = "";
    String strMobileNo = "";
    Float ratingNumber ;
    String strRatingMsg = "";
    String profileImage = "";

    AlertDialog.Builder alert;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        SharedPreferences preferences = getContext().getSharedPreferences("userDetails", MODE_PRIVATE);
        strName = preferences.getString("name", "");
        strEmail = preferences.getString("email", "");
        strMobileNo = preferences.getString("mobile", "");

        binding.userName.setText(strName);
        binding.userEmail.setText(strEmail);
        binding.userMobileNo.setText(strMobileNo);

        //  get profile image to firebase database
        database.getReference().child("Users").child(auth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            User user = snapshot.getValue(User.class);
                            profileImage = user.getProfileImage();
                            ;
                            if (!user.getProfileImage().equals("")) {
                                Picasso.get()
                                        .load(profileImage)
                                        .placeholder(drawable.placeholder)
                                        .into(binding.acProfileImage);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.profileCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), EditProfileActivity.class);
                startActivity(intent);
            }
        });

        binding.rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert = new AlertDialog.Builder(getContext());
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                View alert_rating = layoutInflater.inflate(rating_alert_dialog, null, false);
                alert.setView(alert_rating);
                @SuppressLint({"MissingInflatedId", "LocalSuppress"}) RatingBar ratingBar = alert_rating.findViewById(id.rate_plateform);
                @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EditText edtMsg = alert_rating.findViewById(R.id.txtMsg);
                @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button btnSubmit = alert_rating.findViewById(id.btnSubmit);
                @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button btnCancel = alert_rating.findViewById(id.btnCancel);
                AlertDialog dialog = alert.create();
                dialog.show();
                dialog.setCancelable(false);

                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        strRatingMsg = edtMsg.getText().toString();

                        /*ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                            @Override
                            public void onRatingChanged(RatingBar ratingBar, float rating, boolean isChanged) {
                                if (isChanged) {
                                    ratingNumber = rating;
                                }
                            }
                        });*/

                        ratingNumber = ratingBar.getRating();

                        Rating rating_plateform = new Rating(strRatingMsg, ratingNumber, auth.getUid(), new Date().getTime());
                        FirebaseDatabase.getInstance().getReference("Rating").child("doTech").child(auth.getUid()).setValue(rating_plateform);
                        dialog.dismiss();
                        Toast.makeText(getContext(), "Submited your review", Toast.LENGTH_SHORT).show();

                        /*if (ratingNumber == null) {
                            Toast.makeText(getContext(), "fill rating", Toast.LENGTH_SHORT).show();
                        } else {
                            Rating rating_plateform = new Rating(strRatingMsg, ratingNumber, auth.getUid(), new Date().getTime());
                            FirebaseDatabase.getInstance().getReference("Rating").child("doTech").child(auth.getUid()).setValue(rating_plateform);
                            dialog.dismiss();
                            Toast.makeText(getContext(), "Submited your review", Toast.LENGTH_SHORT).show();
                        }*/

                    }
                });
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });

        binding.contactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ContactUsActivity.class);
                intent.putExtra("name", strName);
                intent.putExtra("email", strEmail);
                intent.putExtra("mobile", strMobileNo);
                intent.putExtra("profileImage", profileImage);
                startActivity(intent);
            }
        });

        binding.condition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(getContext(), ConditionsActivity.class);
                startActivity(intent);*/
            }
        });

        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = LayoutInflater.from(getContext());
                View alert_view = inflater.inflate(logout_alert_dialog, null, false);
                alert.setView(alert_view);
                @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button yes = alert_view.findViewById(id.btnYes);
                @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button no = alert_view.findViewById(id.btnNo);
                AlertDialog dialog = alert.create();
                dialog.show();
                dialog.setCancelable(false);

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences.Editor pref = getContext().getSharedPreferences("userDetails", MODE_PRIVATE).edit();
                        pref.putString("message", "logout");
                        pref.putString("name", "");
                        pref.putString("email", "");
                        pref.putString("mobile", "");
                        pref.putString("gender", "");
                        pref.putString("height", "");
                        pref.putString("birthDate", "");
                        pref.putString("bloodGroup", "");
                        pref.putString("weight", "");
                        pref.putString("profileImage", "");
                        pref.apply();
                        Intent intent = new Intent(getContext(), SignInActivity.class);
                        startActivity(intent);
                    }
                });

                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

            }
        });

        return binding.getRoot();
    }
}