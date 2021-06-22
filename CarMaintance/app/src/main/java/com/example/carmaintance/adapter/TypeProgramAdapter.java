package com.example.carmaintance.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.carmaintance.MyApplication;
import com.example.carmaintance.R;
import com.example.carmaintance.bean.TypeProgram;
import com.example.carmaintance.userPage.maintenanceInfoPage.MaintenanceDetailsActivity;
import com.example.carmaintance.userPage.typePage.SearchProgramActivity;

import java.util.ArrayList;
import java.util.List;

public class TypeProgramAdapter extends RecyclerView.Adapter<TypeProgramAdapter.ViewHolder> {


    private List<TypeProgram> mDates = new ArrayList<>();
    private Activity mActivity;
    private View view;

    public TypeProgramAdapter(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public void updateDates(List<TypeProgram> dates) {
        mDates.clear();
        mDates.addAll(dates);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.type_recycler_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TypeProgram program = mDates.get(position);
        Glide.with(view).load(program.getProgramImg()).into(holder.programImg);
        holder.programName.setText(program.getProgramName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, SearchProgramActivity.class);
                intent.putExtra("program", holder.programName.getText().toString());
                mActivity.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mDates.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private View itemView;
        private ImageView programImg;
        private TextView programName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = (View) itemView.findViewById(R.id.type_item);
            programImg = (ImageView) itemView.findViewById(R.id.type_program_img);
            programName = (TextView) itemView.findViewById(R.id.type_program_text);
        }
    }
}
