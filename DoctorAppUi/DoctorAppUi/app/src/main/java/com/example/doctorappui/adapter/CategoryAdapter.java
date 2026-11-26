package com.example.doctorappui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doctorappui.R;
import com.example.doctorappui.modal.Category;

import java.util.List;

public
class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private
    List<Category> list;
    private
    Context context;

    public
    CategoryAdapter(List<Category> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public
    CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.category_data,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public
    void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        Category category = list.get(position);
        holder.tvCategory.setText(category.getStrCategory());
    }

    @Override
    public
    int getItemCount() {
        return list.size();
    }

    public
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategory;
        public
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategory = itemView.findViewById(R.id.tvCategoryName);
        }
    }
}
