package com.example.doctorappui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.doctorappui.adapter.CategorySpinnerAdapter;
import com.example.doctorappui.databinding.ActivitySignUpVerifyBinding;
import com.example.doctorappui.modal.Category;
import com.example.doctorappui.modal.Notification;
import com.example.doctorappui.modal.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public
class SignUpVerifyActivity extends AppCompatActivity {

    ActivitySignUpVerifyBinding binding;
    CategorySpinnerAdapter adapter;
    List<String> categories = new ArrayList<>();
    String strEmail = "", strMobile = "", strPass = "", strName = "", strDegreeName = "", strCategory = "", degreeImage = "", profileImage = "";
    String strAdd = "", strAccNo = "", strIfscNo = "", strCallDesc = "", strMsgDesc = "",strTotalAmount = "0",strDepositAmount = "0";
    String strImage="";
    String type = "doctor", status = "0", msgAmount = "", callAmount = "";


    /*String encryptedEmail,encryptedMobile,encryptedPass,encryptedName,encryptedDegreeName,encryptedCategory,
            encryptedAdd,encryptedAccNo,encryptedIfscNo,encryptedCallDesc,encryptedMsgDesc,encryptedType,
            encryptedMsgAmount,encryptedCallAmount,encryptedTotalAmount,encryptedDepositAmount,encryptProfileImage;*/
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    FirebaseStorage storage;
    ProgressDialog progressDialog = null;
    ProgressDialog imageProgressDialog = null;
    AlertDialog.Builder alert;
    Uri uri;

    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpVerifyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        databaseReference = database.getReference();

        Intent intent = getIntent();
        if (intent.hasExtra("email")) {
            strEmail = intent.getStringExtra("email");
        }
        if (intent.hasExtra("mobile")) {
            strMobile = intent.getStringExtra("mobile");
        }
        if (intent.hasExtra("pass")) {
            strPass = intent.getStringExtra("pass");
        }

        categories.add("Cardiologist");
        categories.add("Dentist");
        categories.add("Dermatologist");
        categories.add("Neurologist");
        categories.add("Orthopedic");
        categories.add("General Physician");
        categories.add("Pediatrician");
        categories.add("Gynecologist");
        categories.add("ENT Specialist");
        strCategory = "Cardiologist";

        adapter = new CategorySpinnerAdapter(SignUpVerifyActivity.this, categories);
        binding.spinner.setAdapter(adapter);

        Query query = FirebaseDatabase.getInstance().getReference("category");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public
            void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() > 0) {
                    categories.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Category category = dataSnapshot.getValue(Category.class);
                        if (category != null && category.getStrCategory() != null) {
                            categories.add(category.getStrCategory());
                        }
                    }
                    if (!categories.isEmpty()) {
                        strCategory = categories.get(0);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public
            void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public
            void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position < categories.size()) {
                    strCategory = categories.get(position);
                }
            }

            @Override
            public
            void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public
            void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == R.id.doctor) {
                    type = "doctor";
                    binding.txtCategory.setVisibility(View.VISIBLE);
                    binding.spinner.setVisibility(View.VISIBLE);
                } else if (checkedId == R.id.hospital) {
                    type = "hospital";
                    binding.txtCategory.setVisibility(View.GONE);
                    binding.spinner.setVisibility(View.GONE);
                }
            }
        });

        progressDialog = new ProgressDialog(SignUpVerifyActivity.this);
        progressDialog.setTitle("Account Creating");
        progressDialog.setMessage("Please wait.");

        imageProgressDialog = new ProgressDialog(SignUpVerifyActivity.this);
        imageProgressDialog.setTitle("Uploading");
        imageProgressDialog.setMessage("Please wait...");


        binding.txtSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                Intent intent = new Intent(SignUpVerifyActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });

        binding.btnDegreeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 10);
            }
        });

        binding.btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                strName = binding.edtName.getText().toString();
                strDegreeName = binding.edtDegreeName.getText().toString();

                /*//user data encryption
                try {
                    encryptedEmail = EncryptDecrypt.encrypt(strEmail);
                    encryptedMobile = EncryptDecrypt.encrypt(strMobile);
                    encryptedPass = EncryptDecrypt.encrypt(strPass);
                    encryptedName = EncryptDecrypt.encrypt(strName);
                    encryptedDegreeName = EncryptDecrypt.encrypt(strDegreeName);
                    encryptedCategory = EncryptDecrypt.encrypt(strCategory);
                    encryptedAdd = EncryptDecrypt.encrypt(strAdd);
                    encryptedAccNo = EncryptDecrypt.encrypt(strAccNo);
                    encryptedIfscNo = EncryptDecrypt.encrypt(strIfscNo);
                    encryptedCallDesc = EncryptDecrypt.encrypt(strCallDesc);
                    encryptedMsgDesc = EncryptDecrypt.encrypt(strMsgDesc);
                    encryptedType = EncryptDecrypt.encrypt(type);
                    encryptedMsgAmount = EncryptDecrypt.encrypt(msgAmount);
                    encryptedCallAmount = EncryptDecrypt.encrypt(callAmount);
                    encryptedTotalAmount = EncryptDecrypt.encrypt(strTotalAmount);
                    encryptedDepositAmount = EncryptDecrypt.encrypt(strDepositAmount);
                } catch (NoSuchPaddingException | NoSuchAlgorithmException |
                         InvalidAlgorithmParameterException | InvalidKeyException |
                         IllegalBlockSizeException | BadPaddingException e) {
                    throw new RuntimeException(e);
                }*/

                if (strName.equals("")) {
                    Toast.makeText(SignUpVerifyActivity.this, "Please enter name", Toast.LENGTH_SHORT).show();
                } else if (strDegreeName.equals("")) {
                    Toast.makeText(SignUpVerifyActivity.this, "Please enter degree name", Toast.LENGTH_SHORT).show();
                } else if (strImage.equals("")) {
                    Toast.makeText(SignUpVerifyActivity.this, "Please select degree image", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.show();
                    mAuth.createUserWithEmailAndPassword(strEmail, strPass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public
                                void onComplete(@NonNull Task<AuthResult> task) {
                                    progressDialog.dismiss();
                                    if (task.isSuccessful()) {
                                        binding.edtName.setText("");
                                        binding.edtDegreeName.setText("");
                                        Users users = new Users(strEmail, strMobile, strPass, strName, strDegreeName, strCategory,
                                                degreeImage, strAdd, strAccNo, strIfscNo, strCallDesc, strMsgDesc, profileImage,
                                                type, status, msgAmount, callAmount,strTotalAmount,strDepositAmount);
                                        String uId = task.getResult().getUser().getUid();
                                        database.getReference().child("doctor and hospital").child(uId).setValue(users);

                                        //degree image set on storage
                                        imageProgressDialog.show();
                                        final StorageReference reference = storage.getReference().child("degree_image")
                                                .child(FirebaseAuth.getInstance().getUid());
                                        reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public
                                            void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                imageProgressDialog.dismiss();
                                                Toast.makeText(SignUpVerifyActivity.this, "Degree image uploaded", Toast.LENGTH_SHORT).show();
                                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public
                                                    void onSuccess(Uri uri) {
                                                        database.getReference().child("doctor and hospital").child(uId).child("degreeImage").setValue(uri.toString());
                                                    }
                                                });
                                            }
                                        });

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

                                        Toast.makeText(SignUpVerifyActivity.this, "Account Created", Toast.LENGTH_SHORT).show();

                                        alert = new AlertDialog.Builder(SignUpVerifyActivity.this);
                                        LayoutInflater inflater = LayoutInflater.from(SignUpVerifyActivity.this);
                                        View alert_view = inflater.inflate(R.layout.warning_alert_dialog, null, false);
                                        alert.setView(alert_view);
                                        alert.setCancelable(false);
                                        AlertDialog dialog = alert.create();
                                        Button btnOk = alert_view.findViewById(R.id.btnOk);
                                        if (btnOk != null) {
                                            btnOk.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialog.dismiss();
                                                    Intent intent = new Intent(SignUpVerifyActivity.this, SignInActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            });
                                        }
                                        dialog.show();

                                        /*if (type.equals("doctor")){
                                            pref.putString("message","doctor");
                                            pref.apply();
                                            Intent intent = new Intent(SignUpVerifyActivity.this,MainActivity.class);
                                            startActivity(intent);
                                        }else if (type.equals("hospital")){
                                            pref.putString("message","hospital");
                                            pref.apply();
                                            Intent intent = new Intent(SignUpVerifyActivity.this,HospitalMainActivity.class);
                                            startActivity(intent);
                                        }else {
                                            Toast.makeText(SignUpVerifyActivity.this, "Server down!", Toast.LENGTH_SHORT).show();
                                        }*/

                                        Notification notification = new Notification();
                                        notification.setNotificationBy(uId);
                                        notification.setNotificationAt(new Date().getTime());
                                        notification.setUserEmail(strEmail);
                                        notification.setUserProfile(profileImage);
                                        notification.setUserType(type);
                                        notification.setType("SignUp");

                                        FirebaseDatabase.getInstance().getReference()
                                                .child("notification")
                                                .child("admin")
                                                .push()
                                                .setValue(notification);

                                    } else {
                                        String errMsg = task.getException() != null ? task.getException().getMessage() : "Registration failed";
                                        Toast.makeText(SignUpVerifyActivity.this, errMsg, Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });
    }

    @Override
    protected
    void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 10) {
                if (data.getData() != null) {
                    uri = data.getData();
                    binding.imgLogo.setImageURI(uri);
                    strImage = uri.toString();
                }
            }
        } catch (Exception e) {

        }
    }
}