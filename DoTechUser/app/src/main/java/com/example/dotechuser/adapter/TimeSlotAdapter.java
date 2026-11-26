//package com.example.dotechuser.adapter;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.example.dotechuser.Model.TimeSlote;
import com.example.dotechuser.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
//
//public class TimeSlotAdapter
//        extends SectionedRecyclerViewAdapter<TimeSlotAdapter.viewHolder>
//{
//    Activity activity;
//    ArrayList<String> sectionList;
//    HashMap<String, List<TimeSlote>> itemList = new HashMap<>();
//    int selectedSection = -1;
//    int selectedItem = -1;
//
//    public TimeSlotAdapter(Activity activity, ArrayList<String> sectionList, HashMap<String, List<TimeSlote>> itemList) {
//        this.activity = activity;
//        this.sectionList = sectionList;
//        this.itemList = itemList;
//       // notifyDataSetChanged();
//    }
//
//   // @Override
//    public int getSectionCount() {
//        return sectionList.size();
//    }
//
//    @Override
//    public int getItemCount(int section) {
//        return itemList.get(sectionList.get(section)).size();
//    }
//
//    @Override
//    public void onBindHeaderViewHolder(viewHolder holder, int section, boolean expanded) {
//
//    }
//
//    @Override
//    public void onBindFooterViewHolder(viewHolder holder, int section) {
//
//    }
//
//   /* @Override*/
//    public void onBindHeaderViewHolder(viewHolder viewHolder, int i) {
//        viewHolder.textView.setText(sectionList.get(i));
//    }
//
//    @Override
//    public void onBindViewHolder(viewHolder viewHolder, int i, int i1, int i2) {
//        String sItem = String.valueOf(itemList.get(sectionList.get(i)).get(i1));
//
//        viewHolder.textView.setText(sItem);
//
//        viewHolder.textView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                //Toast.makeText(view.getContext(), sItem, Toast.LENGTH_SHORT).show();
//
//
//                SharedPreferences.Editor pref = activity.getSharedPreferences("timeSlot", MODE_PRIVATE).edit();
//                pref.putString("time",sItem);
//                pref.apply();
//
//                selectedSection = i;
//                selectedItem = i1;
//                notifyDataSetChanged();
//            }
//        });
//
//        if (selectedSection == i && selectedItem == i1) {
//            viewHolder.textView.setBackground(ContextCompat.getDrawable(activity,R.drawable.rectangle_fill));
//
//            viewHolder.textView.setTextColor(Color.WHITE);
//
//        }
//        else{
//            viewHolder.textView.setBackground(ContextCompat.getDrawable(activity,R.drawable.rectangle_outline));
//
//            viewHolder.textView.setTextColor(Color.BLACK);
//        }
//    }
//
//    @Override
//    public int getItemViewType(int section, int relativePosition, int absolutePosition) {
//        if (section == 1) {
//            return 0;
//        }
//        return super.getItemViewType(section, relativePosition, absolutePosition);
//    }
//
//    @NonNull
//    @Override
//    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        int layout;
//        if (viewType == VIEW_TYPE_HEADER) {
//            layout = R.layout.item_header;
//        } else {
//            layout = R.layout.item_slot;
//        }
//
//        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
//        return new viewHolder(view);
//    }
//
//    public class viewHolder extends RecyclerView.ViewHolder {
//        TextView textView;
//        public viewHolder(@NonNull View itemView) {
//            super(itemView);
//            textView = itemView.findViewById(R.id.text_view);
//        }
//    }
//}
