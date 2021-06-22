package com.example.carmaintance.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.carmaintance.R;
import com.example.carmaintance.bean.Store;
import com.example.carmaintance.userPage.storePage.StoreDetailsActivity;
import com.example.carmaintance.util.HttpUtil;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.ViewHolder> {

    private Activity mActivity;
    private String url;
    private List<Store> mData = new ArrayList<>();
    private View view;
    private int type;
    private String TAG = "StoreAdapter";
    private String imgUrl;

    public StoreAdapter(Activity mActivity, int type, String baseUrl, String imgUrl) {
        this.mActivity = mActivity;
        this.type = type;
        this.url = baseUrl + "/store/deleteStore";
        this.imgUrl = imgUrl;
    }

    public void updateDates(List<Store> dates) {
        mData.clear();
        mData.addAll(dates);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_recycler_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Store store = mData.get(position);
        Glide.with(view).load(imgUrl + store.getStore_url()).into(holder.storeImg);
        holder.storeName.setText(store.getStore_name());
        DecimalFormat df = new DecimalFormat("#.00");
        holder.storeScore.setText(df.format(store.getScore()));
        holder.tradingVolume.setText(String.valueOf(store.getTrading_volume()) + "单");
        holder.storeAddress.setText(store.getStore_address());
        holder.distance.setText(String.valueOf(store.getDistance()) + "公里");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, StoreDetailsActivity.class);
                intent.putExtra("id", store.getId());
                intent.putExtra("type", type);
                mActivity.startActivity(intent);
            }
        });
        if (type == 1) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(mActivity);
                    dialog.setMessage("是否删除车主账号");
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteStore(store.getId(), position);
                        }
                    });
                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dialog.show();
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    private void deleteStore(int id, int position) {
        RequestBody requestBody = new FormBody.Builder()
                .add("id", String.valueOf(id))
                .build();
        HttpUtil.postOkHttpRequest(url, requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.e(TAG, result);
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (result.equals("1")) {
                            mData.remove(position);
                            notifyDataSetChanged();
                        }
                    }
                });
            }
        });
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView storeImg;
        private TextView storeName;
        private TextView storeScore;
        private TextView tradingVolume;
        private TextView storeAddress;
        private TextView distance;

        public ViewHolder(View view) {
            super(view);
            storeImg = (ImageView) view.findViewById(R.id.store_img);
            storeName = (TextView) view.findViewById(R.id.store_name);
            storeScore = (TextView) view.findViewById(R.id.store_score);
            tradingVolume = (TextView) view.findViewById(R.id.store_trading_volume);
            storeAddress = (TextView) view.findViewById(R.id.store_address);
            distance = (TextView) view.findViewById(R.id.distance);
        }
    }
}
