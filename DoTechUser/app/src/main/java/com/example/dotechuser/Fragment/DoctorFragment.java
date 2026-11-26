package com.example.dotechuser.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.dotechuser.Model.Category;
import com.example.dotechuser.Model.User;
import com.example.dotechuser.R;
import com.example.dotechuser.adapter.CategoryAdapter;
import com.example.dotechuser.databinding.FragmentDoctorBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DoctorFragment extends Fragment {

    FragmentDoctorBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;
    List<Category> list = new ArrayList<>();
    CategoryAdapter adapter;

    public DoctorFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDoctorBinding.inflate(inflater, container, false);

        SharedPreferences preferences = getContext().getSharedPreferences("userDetails", MODE_PRIVATE);
        String strName = preferences.getString("name", "");

        binding.userName.setText(strName);


        binding.searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filterText(String.valueOf(editable));
            }
        });

        database.getReference().child("Users").child(auth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            User user = snapshot.getValue(User.class);

                            if (!user.getProfileImage().equals("")) {
                                Picasso.get()
                                        .load(user.getProfileImage())
                                        .placeholder(R.drawable.placeholder)
                                        .into(binding.imgProfile);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        //  set adapter
        adapter = new CategoryAdapter(list, getContext());
        binding.findRecyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2, LinearLayoutManager.VERTICAL, false);
        binding.findRecyclerView.setLayoutManager(layoutManager);
        binding.findRecyclerView.setHasFixedSize(true);

        reference = FirebaseDatabase.getInstance().getReference("category");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Category category = dataSnapshot.getValue(Category.class);
                    category.setCategoryId(dataSnapshot.getKey());
                    list.add(category);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return binding.getRoot();
    }

    private void filterText(String text) {
        List<Category> filteredList = new ArrayList<>();
        for (Category category : list) {
            if (category.getStrCategory().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(category);
            }
        }

        if (!filteredList.isEmpty()) {
            adapter.setFilteredList(filteredList);
        }
    }

}