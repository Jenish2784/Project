package com.example.dotechuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.dotechuser.Model.User;
import com.example.dotechuser.databinding.ActivitySignInBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SignInActivity extends AppCompatActivity {

    ActivitySignInBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;
    String strEmail = "", strPass = "";
    String strName = "", strMobile = "", status = "";
    String strGender = "", strBirthDate = "", strHeight = "", strWeight = "", strBloodGroup = "", strProfileImage = "";
    AlertDialog.Builder alert;
    ProgressDialog progressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://dotech-7a869-default-rtdb.firebaseio.com");
        reference = database.getReference();

        SharedPreferences preferences = getSharedPreferences("userDetails", MODE_PRIVATE);
        String str = preferences.getString("message", "");
        if (str.equals("login")) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Sign In");
        progressDialog.setMessage("Please wait \n Validation in progress. ");

        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strEmail = binding.edtEmail.getText().toString().trim();
                strPass = binding.edtPassword.getText().toString().trim();

                if (strEmail.equals("")) {
                    binding.edtEmail.setError("Please Enter Your Email");
                } else if (strPass.equals("")) {
                    binding.edtPassword.setError("Please Enter Your Correct Password");
                } else {
                    progressDialog.show();
                    auth.signInWithEmailAndPassword(strEmail, strPass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        if (user == null) {
                                            progressDialog.dismiss();
                                            Toast.makeText(SignInActivity.this, "Authentication failed. User is null.", Toast.LENGTH_SHORT).show();
                                            return;
                                        }

                                        // Step 1: Direct lookup by Auth UID in Users
                                        reference.child("Users").child(user.getUid())
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot uidSnapshot) {
                                                        if (uidSnapshot.exists() && uidSnapshot.hasChildren()) {
                                                            processPatientLogin(uidSnapshot);
                                                        } else {
                                                            // Step 2: Fetch all Users and compare email case-insensitively
                                                            reference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot allUsers) {
                                                                    String targetEmail = (user.getEmail() != null ? user.getEmail() : strEmail).trim().toLowerCase();
                                                                    for (DataSnapshot uChild : allUsers.getChildren()) {
                                                                        String uEmail = String.valueOf(uChild.child("email").getValue()).trim().toLowerCase();
                                                                        if (uEmail.equals(targetEmail) || uChild.getKey().equals(user.getUid())) {
                                                                            processPatientLogin(uChild);
                                                                            return;
                                                                        }
                                                                    }
                                                                    // Step 3: Fallback check if Doctor or Admin
                                                                    checkIfDoctorOrAdminAndAlert(user);
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError error) {
                                                                    checkIfDoctorOrAdminAndAlert(user);
                                                                }
                                                            });
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(SignInActivity.this, "Database connection failed: " + error.getMessage(), Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                    } else {
                                        progressDialog.dismiss();
                                        String err = task.getException() != null ? task.getException().getMessage() : "Authentication failed";
                                        Toast.makeText(SignInActivity.this, err, Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                }
            }
        });

        binding.forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, ForgeotPasswordActivity.class);
                startActivity(intent);
            }
        });

        binding.txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void processPatientLogin(DataSnapshot data) {
        strName = String.valueOf(data.child("name").getValue());
        strMobile = String.valueOf(data.child("mobile").getValue());
        status = String.valueOf(data.child("status").getValue());
        strGender = String.valueOf(data.child("gender").getValue());
        strBirthDate = String.valueOf(data.child("birthDate").getValue());
        strHeight = String.valueOf(data.child("height").getValue());
        strWeight = String.valueOf(data.child("weight").getValue());
        strBloodGroup = String.valueOf(data.child("bloodGroup").getValue());
        strProfileImage = String.valueOf(data.child("profileImage").getValue());

        SharedPreferences.Editor pref = getSharedPreferences("userDetails", MODE_PRIVATE).edit();
        pref.putString("message", "login");
        pref.putString("name", strName);
        pref.putString("email", strEmail);
        pref.putString("mobile", strMobile);
        pref.putString("status", status);
        pref.putString("gender", strGender);
        pref.putString("birthDate", strBirthDate);
        pref.putString("height", strHeight);
        pref.putString("weight", strWeight);
        pref.putString("bloodGroup", strBloodGroup);
        pref.putString("profileImage", strProfileImage);
        pref.apply();

        progressDialog.dismiss();
        if ("1".equals(status)) {
            binding.edtEmail.setText("");
            binding.edtPassword.setText("");

            Toast.makeText(SignInActivity.this, "SignIn Successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            alert = new AlertDialog.Builder(SignInActivity.this);
            LayoutInflater inflater = LayoutInflater.from(SignInActivity.this);
            View alertUser = inflater.inflate(R.layout.user_disable_by_admin_alert, null, false);
            alert.setView(alertUser);
            alert.setCancelable(false);

            Button ok = alertUser.findViewById(R.id.btnOk);
            Button cancel = alertUser.findViewById(R.id.btnCancel);

            AlertDialog dialog = alert.create();
            dialog.show();

            ok.setOnClickListener(view -> {
                Intent intent = new Intent(SignInActivity.this, ContactUsActivity.class);
                startActivity(intent);
            });

            cancel.setOnClickListener(view -> dialog.dismiss());
        }
    }

    private void checkIfDoctorOrAdminAndAlert(FirebaseUser user) {
        reference.child("doctor and hospital").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot allDocs) {
                progressDialog.dismiss();
                String targetEmail = (user.getEmail() != null ? user.getEmail() : strEmail).trim().toLowerCase();
                DataSnapshot foundDoc = null;
                for (DataSnapshot docChild : allDocs.getChildren()) {
                    String docEmail = String.valueOf(docChild.child("emailId").getValue()).trim().toLowerCase();
                    if (docEmail.equals(targetEmail) || docChild.getKey().equals(user.getUid())) {
                        foundDoc = docChild;
                        break;
                    }
                }

                if (foundDoc != null) {
                    Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.example.doctorappui");
                    if (launchIntent != null) {
                        Toast.makeText(SignInActivity.this, "Admin/Doctor account! Launching DoTech - Doctor & Admin app...", Toast.LENGTH_LONG).show();
                        startActivity(launchIntent);
                    } else {
                        // Allow login into patient app as well so they can browse
                        String docName = String.valueOf(foundDoc.child("name").getValue());
                        SharedPreferences.Editor pref = getSharedPreferences("userDetails", MODE_PRIVATE).edit();
                        pref.putString("message", "login");
                        pref.putString("name", docName);
                        pref.putString("email", strEmail);
                        pref.putString("status", "1");
                        pref.apply();
                        Toast.makeText(SignInActivity.this, "Welcome " + docName + "! (Viewing as Administrator)", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(SignInActivity.this, "User profile not found in database. Please Sign Up first.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
                Toast.makeText(SignInActivity.this, "User profile not found in database.", Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }
}