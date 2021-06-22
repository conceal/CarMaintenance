package com.example.carmaintance.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carmaintance.R;
import com.example.carmaintance.bean.Type;
import com.example.carmaintance.userPage.maintenanceInfoPage.MaintenanceDetailsActivity;

import java.util.ArrayList;
import java.util.List;

public class MaintainInfoAdapter extends RecyclerView.Adapter<MaintainInfoAdapter.ViewHolder> {


    private static final int TYPE_HEADER = 0;
    private static final int TYPE_NORMAL = 1;
    private List<Type> mData = new ArrayList<>();
    private View mHeaderView;
    private Activity mActivity;

    public MaintainInfoAdapter(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public void setHeaderView(View headerView) {
        this.mHeaderView = headerView;
        notifyItemInserted(0);
    }

    private View getHeaderView() {
        return mHeaderView;
    }

    public void updateDates(List<Type> dates) {
        mData.clear();
        mData.addAll(dates);
        notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = ((GridLayoutManager) manager);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup(){
                @Override
                public int getSpanSize(int position) {
                    return getItemViewType(position) == TYPE_HEADER ? gridLayoutManager.getSpanCount() : 1;
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mHeaderView == null) return TYPE_NORMAL;
        if (position == 0) return TYPE_HEADER;
        return TYPE_NORMAL;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER) return new ViewHolder(mHeaderView);
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.maintain_program_item, parent, false);
        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADER) return;
        final int index = getRealPosition(holder);
        Type type = mData.get(index);
        holder.program_text.setText(type.getType());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, MaintenanceDetailsActivity.class);
                intent.putExtra("program", holder.program_text.getText().toString());
                mActivity.startActivity(intent);
            }
        });
    }

    public int getRealPosition (MaintainInfoAdapter.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return mHeaderView == null ? position : position - 1;
    }

    @Override
    public int getItemCount() {
        return mHeaderView == null ? mData.size() : mData.size() + 1;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView program_text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            program_text = (TextView) itemView.findViewById(R.id.maintain_info_program_text);
        }
    }
}
