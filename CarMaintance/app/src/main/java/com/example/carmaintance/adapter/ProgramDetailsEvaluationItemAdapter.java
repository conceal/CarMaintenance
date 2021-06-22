package com.example.carmaintance.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.carmaintance.R;
import com.example.carmaintance.bean.EvaluationForProgram;
import com.example.carmaintance.bean.ProgramDetailsEvaluation;

import java.util.ArrayList;
import java.util.List;

public class ProgramDetailsEvaluationItemAdapter extends RecyclerView.Adapter<ProgramDetailsEvaluationItemAdapter.ViewHolder> {

    private List<EvaluationForProgram> mDates = new ArrayList<>();
    private String imgUrl;

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView nickname;
        private TextView brand;
        private TextView evaluationTime;
        private TextView evaluationDetails;
        private TextView program;
        private RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.program_details_user_img);
            nickname = (TextView) itemView.findViewById(R.id.program_details_evaluation_nickname);
            brand = (TextView) itemView.findViewById(R.id.program_details_evaluation_brand);
            evaluationTime = (TextView) itemView.findViewById(R.id.program_details_evaluation_time);
            evaluationDetails = (TextView) itemView.findViewById(R.id.program_details_evaluation_details);
            program = (TextView) itemView.findViewById(R.id.program_details_evaluation_program);
            ratingBar = (RatingBar) itemView.findViewById(R.id.program_details_evaluation_score);
        }
    }

    public ProgramDetailsEvaluationItemAdapter(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void updateDate(List<EvaluationForProgram> dates) {
        mDates.clear();
        mDates.addAll(dates);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.program_details_evaluation_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EvaluationForProgram evaluation = mDates.get(position);
        Glide.with(holder.imageView).load(imgUrl + evaluation.getHead_url()).into(holder.imageView);
        holder.nickname.setText(evaluation.getNickname());
        holder.brand.setText(evaluation.getCar_brand());
        String time = evaluation.getEvaluate_time();
        holder.evaluationTime.setText(time.substring(0, 4) + "." + time.substring(5, 7) + "." + time.substring(8, 10));
        holder.ratingBar.setRating((float) evaluation.getScore());
        holder.evaluationDetails.setText(evaluation.getEvaluation());
        holder.program.setText(evaluation.getProgram());
    }

    @Override
    public int getItemCount() {
        return mDates.size();
    }
}
