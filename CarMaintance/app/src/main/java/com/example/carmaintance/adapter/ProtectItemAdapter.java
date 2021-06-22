package com.example.carmaintance.adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.carmaintance.R;
import com.example.carmaintance.bean.Appointment;
import com.example.carmaintance.bean.Program;
import com.example.carmaintance.userPage.typePage.ProgramActivity;

import java.util.ArrayList;
import java.util.List;

public class ProtectItemAdapter extends RecyclerView.Adapter<ProtectItemAdapter.ViewHolder> {

    private List<Program> mDates = new ArrayList<>();
    private View view;
    private Activity mActivity;
    private String imgUrl;

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView program_img;
        private TextView program_name;
        private TextView program_number;
        private TextView program_price;
        private RatingBar program_score;

        ViewHolder(View view) {
            super(view);
            program_img = (ImageView) view.findViewById(R.id.im_pic);
            program_name = (TextView) view.findViewById(R.id.tv_name);
            program_number = (TextView) view.findViewById(R.id.tv_number);
            program_price = (TextView) view.findViewById(R.id.tv_price);
            program_score = (RatingBar) view.findViewById(R.id.ratingBar);
        }
    }

    public ProtectItemAdapter(Activity mActivity, String imgUrl) {
        this.mActivity = mActivity;
        this.imgUrl = imgUrl;
    }

    public void updateData(List<Program> dates) {
        mDates.clear();
        mDates.addAll(dates);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.protect_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Program program = mDates.get(position);
        Glide.with(view).load(imgUrl + program.getProgram_url()).into(holder.program_img);
        holder.program_name.setText(program.getProgram_name());
        holder.program_number.setText("销量："+program.getTrading_volume());
        holder.program_price.setText(String.valueOf("¥ "+program.getPrice()));
        holder.program_score.setRating(program.getScore());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, ProgramActivity.class);
                intent.putExtra("id", program.getId());
                intent.putExtra("type", 1);
                mActivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDates.size();
    }
}
