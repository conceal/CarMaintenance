package com.example.carmaintance.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.carmaintance.R;
import com.example.carmaintance.bean.AppointmentForUser;
import com.example.carmaintance.userPage.minePage.EvaluateAppointmentActivity;
import com.example.carmaintance.userPage.typePage.ProgramActivity;

import java.util.ArrayList;
import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder> {

    private List<AppointmentForUser> mDates = new ArrayList<>();
    private View view;
    private Activity mActivity;
    private int type;
    private String imgUrl;

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView program_img;
        private TextView program_name;
        private TextView store_name;
        private TextView program_price;
        private TextView appointment_time;

        public ViewHolder(View view) {
            super(view);
            program_img = (ImageView) view.findViewById(R.id.recycler_program_img);
            program_name = (TextView) view.findViewById(R.id.recycler_program_name);
            store_name = (TextView) view.findViewById(R.id.recycler_store_name);
            program_price = (TextView) view.findViewById(R.id.recycler_appointment_price);
            appointment_time = (TextView) view.findViewById(R.id.recycler_appointment_time);
        }
    }

    public AppointmentAdapter(Activity mActivity, int type, String imgUrl) {
        this.mActivity = mActivity;
        this.type = type;
        this.imgUrl = imgUrl;
    }

    public void addDate(List<AppointmentForUser> dates) {
        mDates.clear();
        mDates.addAll(dates);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AppointmentForUser appointmentForUser = mDates.get(position);
        Glide.with(view).load(imgUrl + appointmentForUser.getProgram_url()).into(holder.program_img);
        holder.program_name.setText(appointmentForUser.getProgram_name());
        holder.store_name.setText(appointmentForUser.getStore_name());
        holder.program_price.setText("¥ " + String.valueOf(appointmentForUser.getPrice()));
        holder.appointment_time.setText(appointmentForUser.getAppointment_time().substring(0, 16));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == 2) {
                    Intent evaluateIntent = new Intent(mActivity, EvaluateAppointmentActivity.class);
                    evaluateIntent.putExtra("id", appointmentForUser.getId());
                    evaluateIntent.putExtra("store_id", appointmentForUser.getStore_id());
                    evaluateIntent.putExtra("program_id", appointmentForUser.getProgram_id());
                    evaluateIntent.putExtra("program_url", appointmentForUser.getProgram_url());
                    mActivity.startActivity(evaluateIntent);
                } else {
                    Intent programIntent = new Intent(mActivity, ProgramActivity.class);
                    programIntent.putExtra("id", appointmentForUser.getProgram_id());
                    programIntent.putExtra("type", 0);
                    mActivity.startActivity(programIntent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDates.size();
    }
}
