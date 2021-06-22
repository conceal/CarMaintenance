package com.example.carmaintance.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.carmaintance.R;
import com.example.carmaintance.bean.Evaluation;
import com.example.carmaintance.bean.User;
import com.example.carmaintance.bean.UserEvaluation;

import java.util.ArrayList;
import java.util.List;

public class StoreDetailsEvaluationAdapter extends RecyclerView.Adapter<StoreDetailsEvaluationAdapter.ViewHolder>{

    private Activity mActivity;
    private String imgUrl;
    private List<UserEvaluation> mDates = new ArrayList<>();
    private String TAG = "StoreDetailsEvaluationAdapter";

    public StoreDetailsEvaluationAdapter(Activity mActivity, String imgUrl) {
        this.mActivity = mActivity;
        this.imgUrl = imgUrl;
    }

    public void updateDates (List<UserEvaluation> dates) {
        mDates.clear();
        mDates.addAll(dates);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_details_evaluation_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserEvaluation evaluation = mDates.get(position);
        Log.e(TAG, imgUrl + evaluation.getHead_url());
        Glide.with(holder.itemView).load(imgUrl + evaluation.getHead_url()).into(holder.storeDetailsEvaluationImg);
        holder.storeDetailsEvaluationName.setText(evaluation.getNickname());
        holder.storeDetailsEvaluationTime.setText(evaluation.getEvaluate_time().substring(0, 10));
        holder.storeDetailsEvaluationProgram.setText(evaluation.getProgram_name());
        holder.storeDetailsEvaluationDetails.setText(evaluation.getEvaluation());
        holder.storeDetailsEvaluationScore.setText(String.valueOf(evaluation.getScore()));
        holder.store_name.setText(evaluation.getStore_name());
    }

    @Override
    public int getItemCount() {
        return mDates.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView storeDetailsEvaluationImg;
        private TextView storeDetailsEvaluationName;
        private TextView storeDetailsEvaluationTime;
        private TextView storeDetailsEvaluationProgram;
        private TextView storeDetailsEvaluationDetails;
        private TextView storeDetailsEvaluationScore;
        private TextView store_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            storeDetailsEvaluationImg = (ImageView) itemView.findViewById(R.id.store_details_evaluation_img);
            storeDetailsEvaluationName = (TextView) itemView.findViewById(R.id.store_details_evaluation_nickname);
            storeDetailsEvaluationTime = (TextView) itemView.findViewById(R.id.store_details_evaluation_time);
            storeDetailsEvaluationProgram = (TextView) itemView.findViewById(R.id.store_details_evaluation_program);
            storeDetailsEvaluationDetails = (TextView) itemView.findViewById(R.id.store_details_evaluation_details);
            storeDetailsEvaluationScore = (TextView) itemView.findViewById(R.id.store_details_evaluation_score);
            store_name = (TextView) itemView.findViewById(R.id.evaluate_store_name);
        }
    }
}
