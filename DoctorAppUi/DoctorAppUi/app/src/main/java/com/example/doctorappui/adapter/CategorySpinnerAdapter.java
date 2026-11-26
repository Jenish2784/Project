package com.example.doctorappui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.doctorappui.R;

import java.util.List;

public
class CategorySpinnerAdapter extends BaseAdapter {

    private
    Context context;
    private
    List<String> title;


    public
    CategorySpinnerAdapter(Context context, List<String> title) {
        this.context = context;
        this.title = title;
    }

    @Override
    public
    int getCount() {
        return title.size();
    }

    @Override
    public
    Object getItem(int i) {
        return null;
    }

    @Override
    public
    long getItemId(int i) {
        return 0;
    }

    @Override
    public
    View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View data = inflater.inflate(R.layout.category_spinner,parent,false);
        TextView title = data.findViewById(R.id.tvCategory);
        title.setText(this.title.get(position));
        return data;
    }
}
