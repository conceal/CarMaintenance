package com.example.carmaintance.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.carmaintance.R;
import com.example.carmaintance.bean.Program;
import com.example.carmaintance.userPage.typePage.ProgramActivity;

import java.util.ArrayList;
import java.util.List;

public class SearchProgramAdapter extends RecyclerView.Adapter<SearchProgramAdapter.ViewHolder> {

    private Activity mActivity;
    private List<Program> mDates = new ArrayList<>();
    private View view;
    private String imgUrl;

    public SearchProgramAdapter(Activity mActivity, String imgUrl) {
        this.mActivity = mActivity;
        this.imgUrl = imgUrl;
    }

    public void updateDates(List<Program> dates) {
        mDates.clear();
        mDates.addAll(dates);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_program_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Program program = mDates.get(position);
        Glide.with(view).load(imgUrl + program.getProgram_url()).into(holder.programImg);
        holder.programName.setText(program.getProgram_name());
        holder.programStore.setText(program.getStore_name());
        holder.tradingVolume.setText("销量：" + String.valueOf(program.getTrading_volume()));
        holder.programPrice.setText("¥ " + String.valueOf(program.getPrice()));
        holder.programScore.setText("评分：" + String.valueOf(program.getScore()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, ProgramActivity.class);
                intent.putExtra("id", program.getId());
                intent.putExtra("type", 0);
                mActivity.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDates.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView programImg;
        private TextView programName;
        private TextView programStore;
        private TextView tradingVolume;
        private TextView programPrice;
        private TextView programScore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            programImg = (ImageView) itemView.findViewById(R.id.search_program_img);
            programName = (TextView) itemView.findViewById(R.id.search_program_name);
            programStore = (TextView) itemView.findViewById(R.id.search_program_store);
            tradingVolume = (TextView) itemView.findViewById(R.id.program_trading_volume);
            programPrice = (TextView) itemView.findViewById(R.id.search_program_price);
            programScore = (TextView) itemView.findViewById(R.id.search_program_score);
        }
    }
}
