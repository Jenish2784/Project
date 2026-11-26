package com.example.doctorappui.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.doctorappui.adapter.CategoryAdapter;
import com.example.doctorappui.databinding.ActivityAddCategoryBinding;
import com.example.doctorappui.modal.Category;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public
class AddCategory extends AppCompatActivity {

    ActivityAddCategoryBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    String strCategory;
    List<Category> list = new ArrayList<>();
    CategoryAdapter adapter;

    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                strCategory = binding.edtCategory.getText().toString();
                if (strCategory.equals("")){
                    Toast.makeText(AddCategory.this, "Enter category", Toast.LENGTH_SHORT).show();
                }else {
                    binding.edtCategory.setText("");
                    Category category = new Category(strCategory);
                    database.getReference().child("category").push().setValue(category);
                    Toast.makeText(AddCategory.this, "category inserted!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        adapter = new CategoryAdapter(list,AddCategory.this);
        binding.categoryRecyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(AddCategory.this,2, LinearLayoutManager.VERTICAL,false);
        binding.categoryRecyclerView.setLayoutManager(layoutManager);
        binding.categoryRecyclerView.setHasFixedSize(true);

        Query query = FirebaseDatabase.getInstance().getReference("category");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public
            void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Category category = dataSnapshot.getValue(Category.class);
                    list.add(category);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public
            void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}