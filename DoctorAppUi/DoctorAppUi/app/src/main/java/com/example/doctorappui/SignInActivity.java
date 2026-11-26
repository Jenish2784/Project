package com.example.doctorappui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.doctorappui.admin.AdminMainActivity;
import com.example.doctorappui.databinding.ActivitySignInBinding;
import com.example.doctorappui.doctor.MainActivity;
import com.example.doctorappui.hospital.HospitalMainActivity;
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

public
class SignInActivity extends AppCompatActivity {

    ActivitySignInBinding binding;

    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    String strEmail = "", strMobile = "", strPass = "", strName = "", strDegreeName = "", strCategory = "", degreeImage = "", profileImage = "";
    String strAdd = "", strAccNo = "", strIfscNo = "", strCallDesc = "", strMsgDesc = "";
    String type, status, msgAmount, callAmount;
    ProgressDialog progressDialog = null;
    AlertDialog.Builder alert;

    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://dotech-7a869-default-rtdb.firebaseio.com");
        databaseReference = database.getReference();


        SharedPreferences preferences = getSharedPreferences("userData", MODE_PRIVATE);
        String strMsg = preferences.getString("message", "");

        if (strMsg.equals("admin")) {
            Intent intent = new Intent(SignInActivity.this, AdminMainActivity.class);
            startActivity(intent);
        } else if (strMsg.equals("doctor")) {
            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
            startActivity(intent);
        } else if (strMsg.equals("hospital")) {
            Intent intent = new Intent(SignInActivity.this, HospitalMainActivity.class);
            startActivity(intent);
        }

        progressDialog = new ProgressDialog(SignInActivity.this);
        progressDialog.setTitle("Login");
        progressDialog.setMessage("Please Wait\nValidation in Progress.");

        binding.txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                strEmail = binding.edtEmail.getText().toString().trim();
                strPass = binding.edtPass.getText().toString().trim();
                if (strEmail.equals("")) {
                    Toast.makeText(SignInActivity.this, "Please enter email id", Toast.LENGTH_SHORT).show();
                } else if (strPass.equals("")) {
                    Toast.makeText(SignInActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.show();
                    mAuth.signInWithEmailAndPassword(strEmail, strPass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public
                                void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        progressDialog.dismiss();
                                        String err = task.getException() != null ? task.getException().getMessage() : "Authentication failed";
                                        Toast.makeText(SignInActivity.this, err, Toast.LENGTH_LONG).show();
                                        return;
                                    }

                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    if (user == null) {
                                        progressDialog.dismiss();
                                        Toast.makeText(SignInActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    // 1. Direct lookup by Auth UID first (instant and doesn't require index)
                                    databaseReference.child("doctor and hospital").child(user.getUid())
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot uidSnapshot) {
                                                    if (uidSnapshot.exists() && uidSnapshot.hasChildren()) {
                                                        processLoginSnapshot(uidSnapshot);
                                                    } else {
                                                        // 2. Fetch all records from "doctor and hospital" and match email case-insensitively or UID
                                                        databaseReference.child("doctor and hospital")
                                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot allDocs) {
                                                                        String targetEmail = (user.getEmail() != null ? user.getEmail() : strEmail).trim().toLowerCase();
                                                                        for (DataSnapshot docChild : allDocs.getChildren()) {
                                                                            String docEmail = String.valueOf(docChild.child("emailId").getValue()).trim().toLowerCase();
                                                                            if (docEmail.equals(targetEmail) || docChild.getKey().equals(user.getUid())) {
                                                                                processLoginSnapshot(docChild);
                                                                                return;
                                                                            }
                                                                        }
                                                                        // 3. Fallback: Check if it's a Patient
                                                                        checkIfPatientAndAlert(user);
                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                                        checkIfPatientAndAlert(user);
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
                                }
                            });
                }
            }
        });

        binding.forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    private void processLoginSnapshot(DataSnapshot data) {
        strMobile = String.valueOf(data.child("mobileNumber").getValue());
        strName = String.valueOf(data.child("name").getValue());
        strDegreeName = String.valueOf(data.child("degreeName").getValue());
        strCategory = String.valueOf(data.child("category").getValue());
        degreeImage = String.valueOf(data.child("degreeImage").getValue());
        profileImage = String.valueOf(data.child("profileImage").getValue());
        strAdd = String.valueOf(data.child("address").getValue());
        strAccNo = String.valueOf(data.child("accountNo").getValue());
        strIfscNo = String.valueOf(data.child("ifscNo").getValue());
        strCallDesc = String.valueOf(data.child("callDescription").getValue());
        strMsgDesc = String.valueOf(data.child("msgDescription").getValue());
        type = String.valueOf(data.child("type").getValue());
        status = String.valueOf(data.child("status").getValue());
        msgAmount = String.valueOf(data.child("msgAmount").getValue());
        callAmount = String.valueOf(data.child("callAmount").getValue());

        SharedPreferences.Editor pref = getSharedPreferences("userData", MODE_PRIVATE).edit();
        pref.putString("email", strEmail);
        pref.putString("pass", strPass);
        pref.putString("mobile", strMobile);
        pref.putString("name", strName);
        pref.putString("degreeName", strDegreeName);
        pref.putString("category", strCategory);
        pref.putString("degreeImage", degreeImage);
        pref.putString("profileImage", profileImage);
        pref.putString("address", strAdd);
        pref.putString("accNo", strAccNo);
        pref.putString("ifscNo", strIfscNo);
        pref.putString("callDesc", strCallDesc);
        pref.putString("msgDesc", strMsgDesc);
        pref.putString("type", type);
        pref.putString("status", status);
        pref.putString("msgAmount", msgAmount);
        pref.putString("callAmount", callAmount);
        pref.apply();

        progressDialog.dismiss();

        if ("doctor".equals(type)) {
            if ("1".equals(status) || "shutdown".equals(status)) {
                pref.putString("message", "doctor");
                pref.apply();
                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                startActivity(intent);
            } else if ("0".equals(status)) {
                showWarningDialog();
            } else if ("reject".equals(status)) {
                showRejectDialog();
            } else if ("block".equals(status)) {
                showBlockDialog();
            } else {
                Toast.makeText(SignInActivity.this, "Server down!", Toast.LENGTH_SHORT).show();
            }
        } else if ("admin".equals(type)) {
            pref.putString("message", "admin");
            pref.apply();
            Intent intent = new Intent(SignInActivity.this, AdminMainActivity.class);
            startActivity(intent);
        } else if ("hospital".equals(type)) {
            if ("1".equals(status)) {
                pref.putString("message", "hospital");
                pref.apply();
                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                startActivity(intent);
            } else if ("0".equals(status)) {
                showWarningDialog();
            } else if ("block".equals(status)) {
                showBlockDialog();
            } else if ("reject".equals(status)) {
                showRejectDialog();
            } else {
                Toast.makeText(SignInActivity.this, "Server down!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(SignInActivity.this, "Server down!", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkIfPatientAndAlert(FirebaseUser user) {
        databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot allUsers) {
                progressDialog.dismiss();
                String targetEmail = (user.getEmail() != null ? user.getEmail() : strEmail).trim().toLowerCase();
                boolean isPatient = false;
                for (DataSnapshot uChild : allUsers.getChildren()) {
                    String uEmail = String.valueOf(uChild.child("email").getValue()).trim().toLowerCase();
                    if (uEmail.equals(targetEmail) || uChild.getKey().equals(user.getUid())) {
                        isPatient = true;
                        break;
                    }
                }

                if (isPatient) {
                    Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.example.dotechuser");
                    if (launchIntent != null) {
                        Toast.makeText(SignInActivity.this, "Patient account detected! Opening DoTech (Patient) app...", Toast.LENGTH_LONG).show();
                        startActivity(launchIntent);
                    } else {
                        Toast.makeText(SignInActivity.this, "This is a Patient account! Please open the DoTech (Patient) app.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(SignInActivity.this, "Account not found in Doctor/Hospital records. If you are a Patient, use the DoTech (Patient) app.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
                Toast.makeText(SignInActivity.this, "Account not found in Doctor/Hospital records.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showWarningDialog() {
        alert = new AlertDialog.Builder(SignInActivity.this);
        LayoutInflater inflater = LayoutInflater.from(SignInActivity.this);
        View alert_view = inflater.inflate(R.layout.warning_alert_dialog, null, false);
        alert.setView(alert_view);
        AlertDialog dialog = alert.create();
        Button btnOk = alert_view.findViewById(R.id.btnOk);
        if (btnOk != null) {
            btnOk.setOnClickListener(v -> dialog.dismiss());
        }
        dialog.show();
    }

    private void showRejectDialog() {
        alert = new AlertDialog.Builder(SignInActivity.this);
        LayoutInflater inflater = LayoutInflater.from(SignInActivity.this);
        View alert_view = inflater.inflate(R.layout.reject_alert_dialog, null, false);
        alert.setView(alert_view);
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    private void showBlockDialog() {
        alert = new AlertDialog.Builder(SignInActivity.this);
        LayoutInflater inflater = LayoutInflater.from(SignInActivity.this);
        View alert_view = inflater.inflate(R.layout.block_msg_alert_dialog, null, false);
        alert.setView(alert_view);
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    @Override
    public
    void onBackPressed() {
        super.onBackPressed();
        exitFormApp();
    }
    private void exitFormApp(){
        Intent intent=new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

}