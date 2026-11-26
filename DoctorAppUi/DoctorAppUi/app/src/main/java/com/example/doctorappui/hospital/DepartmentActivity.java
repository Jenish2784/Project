package com.example.doctorappui.hospital;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.doctorappui.R;
import com.example.doctorappui.adapter.DepartmentAdapter;
import com.example.doctorappui.databinding.ActivityDepartmentBinding;
import com.example.doctorappui.modal.Department;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.util.ArrayList;
import java.util.Date;

public
class DepartmentActivity extends AppCompatActivity {

    ActivityDepartmentBinding binding;
    AlertDialog.Builder alert;
    Uri uri = null;
    ImageView departmentIcon;
    EditText edtDepartment;
    Button btnAddDepartment;
    String strDepartment;
    String strUri = "";

    ProgressDialog progressDialog = null;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    DatabaseReference reference;
    ArrayList<Department> list = new ArrayList<>();
    DepartmentAdapter adapter;

    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDepartmentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        getSupportActionBar().setTitle("Departments");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.home)));

        progressDialog = new ProgressDialog(DepartmentActivity.this);
        progressDialog.setTitle("Icon uploading");
        progressDialog.setMessage("Please wait.");


        //fetch department data
        adapter = new DepartmentAdapter(list, DepartmentActivity.this);
        binding.departmentRecyclerview.setAdapter(adapter);

        LinearLayoutManager layoutManager = new GridLayoutManager(DepartmentActivity.this, 2, LinearLayoutManager.VERTICAL, false);
        binding.departmentRecyclerview.setLayoutManager(layoutManager);
        binding.departmentRecyclerview.setHasFixedSize(true);

        reference = FirebaseDatabase.getInstance().getReference("doctor and hospital");
        Query query = FirebaseDatabase.getInstance().getReference("doctor and hospital").child(auth.getUid()).child("department");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public
            void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Department department = dataSnapshot.getValue(Department.class);
                    department.setUserId(dataSnapshot.getKey());
                    list.add(department);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public
            void onCancelled(@NonNull DatabaseError error) {

            }
        });


        binding.addDepartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                alert = new AlertDialog.Builder(DepartmentActivity.this);
                LayoutInflater inflater = LayoutInflater.from(DepartmentActivity.this);
                View alert_view = inflater.inflate(R.layout.add_department_dialog, null, false);
                alert.setView(alert_view);
                departmentIcon = alert_view.findViewById(R.id.department_icon);
                edtDepartment = alert_view.findViewById(R.id.edtDepartment);
                btnAddDepartment = alert_view.findViewById(R.id.btnAddDepartment);
                AlertDialog dialog = alert.create();
                dialog.show();


                departmentIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public
                    void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent, 10);
                    }
                });

                btnAddDepartment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public
                    void onClick(View view) {
                        strDepartment = edtDepartment.getText().toString();
                        if (strUri.equals("")) {
                            Toast.makeText(DepartmentActivity.this, "Please select image", Toast.LENGTH_SHORT).show();
                        } else if (strDepartment.equals("")) {
                            Toast.makeText(DepartmentActivity.this, "please enter name", Toast.LENGTH_SHORT).show();
                        } else {
                            progressDialog.show();
                            final StorageReference reference = storage.getReference().child("department_icon").child(auth.getUid()).child(strDepartment);
                            reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public
                                void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    progressDialog.dismiss();
                                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public
                                        void onSuccess(Uri uri) {
                                            Department department = new Department();
                                            department.setDepartmentIcon(uri.toString());
                                            department.setDepartmentName(strDepartment);
                                            department.setTimeAt(new Date().getTime());
                                            department.setFees("200");

                                            database.getReference().child("doctor and hospital").child(auth.getUid())
                                                    .child("department").push()
                                                    .setValue(department).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public
                                                        void onSuccess(Void unused) {
                                                            dialog.dismiss();
                                                            Toast.makeText(DepartmentActivity.this, "Department added successfully", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    });
                                }
                            });

                        }
                    }
                });

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
                    departmentIcon.setImageURI(uri);
                    strUri = uri.toString();
                }
            }
        } catch (Exception e) {

        }
    }
}