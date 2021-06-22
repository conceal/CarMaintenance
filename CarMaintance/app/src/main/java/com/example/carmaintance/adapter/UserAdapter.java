package com.example.carmaintance.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
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
import com.example.carmaintance.bean.User;
import com.example.carmaintance.util.HttpUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    private Activity mActivity;
    private String url;
    private String imgUrl;
    private List<User> mData = new ArrayList<>();
    private String TAG = "UserAdapter";

    public UserAdapter(Activity mActivity, String baseUrl, String imgUrl) {
        this.mActivity = mActivity;
        this.url = baseUrl + "/user/deleteUser";
        this.imgUrl = imgUrl;
    }

    public void updateData(List<User> data) {
        this.mData.clear();
        this.mData.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_user_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = mData.get(position);
        Glide.with(mActivity).load(imgUrl + user.getHead_url()).into(holder.headImg);
        holder.account.setText(user.getAccount());
        holder.nickname.setText(user.getNickname());
        holder.phone_number.setText(user.getPhone_number());
        holder.brand.setText(user.getCar_brand());
        holder.registerTime.setText(user.getRegister_time().substring(0, 10));
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(mActivity);
                dialog.setMessage("是否删除车主账号");
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteUser(user.getId(), position);
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

    @Override
    public int getItemCount() {
        return mData.size();
    }

    private void deleteUser(int id, int position) {
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

        private ImageView headImg;
        private TextView account;
        private TextView nickname;
        private TextView phone_number;
        private TextView brand;
        private TextView registerTime;

        public ViewHolder(View itemView) {
            super(itemView);
            headImg = (ImageView) itemView.findViewById(R.id.user_recycler_img);
            account = (TextView) itemView.findViewById(R.id.user_recycler_account);
            nickname = (TextView) itemView.findViewById(R.id.user_recycler_nickname);
            phone_number = (TextView) itemView.findViewById(R.id.user_recycler_phone_number);
            brand = (TextView) itemView.findViewById(R.id.user_recycler_brand);
            registerTime = (TextView) itemView.findViewById(R.id.user_recycler_register_time);
        }
    }
}
