package com.example.carmaintance.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carmaintance.R;

import java.util.ArrayList;
import java.util.List;

public class SelectTimeAdapter extends RecyclerView.Adapter<SelectTimeAdapter.ViewHolder>{

    private List<String> mDates = new ArrayList<>();
    private int selectPosition = -1;
    private Context context;
    private String TAG = "SelectTime";

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView selectTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            selectTime = (TextView) itemView.findViewById(R.id.select_time);
        }
    }

    public SelectTimeAdapter(Context mActivity) {
        this.context = mActivity;
    }

    public void updateDates(List<String> dates) {
        mDates.clear();
        mDates.addAll(dates);
        notifyDataSetChanged();
    }

    public String getSelectTime() {
        if (selectPosition == -1) return null;
        else return mDates.get(selectPosition);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.select_time_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                holder.selectTime.setTextColor(Color.RED);
                selectPosition = position;
                notifyDataSetChanged();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String time = mDates.get(position);
        holder.selectTime.setText(time);
        if (position == selectPosition) {
            holder.selectTime.setTextColor(Color.RED);
        }else {
            holder.selectTime.setTextColor(Color.BLACK);
        }
    }

    @Override
    public int getItemCount() {
        return mDates.size();
    }
}
