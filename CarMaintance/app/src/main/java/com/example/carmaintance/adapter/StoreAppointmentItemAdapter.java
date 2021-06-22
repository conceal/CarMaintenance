package com.example.carmaintance.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.carmaintance.R;
import com.example.carmaintance.bean.AppointmentForStore;
import com.example.carmaintance.util.HttpUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StoreAppointmentItemAdapter extends RecyclerView.Adapter<StoreAppointmentItemAdapter.ViewHolder> {

    private List<AppointmentForStore> mDates = new ArrayList<>();
    private String url = null;
    private View view;
    private Activity mActivity;
    private String imgUrl;

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView program_img;
        private TextView nickname;
        private TextView program_name;
        private TextView user_photo_number;
        private TextView program_price;
        private TextView appointment_time;
        private Button bt_submit;

        ViewHolder(View view) {
            super(view);
            program_img = (ImageView) view.findViewById(R.id.store_appointment_img);
            nickname = (TextView) view.findViewById(R.id.store_appointment_nickname);
            program_name = (TextView) view.findViewById(R.id.store_appointment_program_name);
            user_photo_number = (TextView) view.findViewById(R.id.store_appointment_photo_number);
            program_price = (TextView) view.findViewById(R.id.store_appointment_program_price);
            appointment_time = (TextView) view.findViewById(R.id.store_appointment_time);
            bt_submit = (Button) view.findViewById(R.id.store_appointment_submit);
        }
    }

    public StoreAppointmentItemAdapter(Activity mActivity, String baseUrl, String imgUrl) {
        this.mActivity = mActivity;
        this.url = baseUrl + "/appointment/completedAppointment";
        this.imgUrl = imgUrl;
    }

    public void updateData(List<AppointmentForStore> dates) {
        mDates.clear();
        mDates.addAll(dates);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_appointment_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AppointmentForStore appointment = mDates.get(position);
        Glide.with(view).load(imgUrl + appointment.getProgram_url()).into(holder.program_img);
        holder.nickname.setText(appointment.getNickname());
        holder.program_name.setText(appointment.getProgram_name());
        holder.user_photo_number.setText(String.valueOf(appointment.getPhone_number()));
        holder.program_price.setText("¥ " + String.valueOf(appointment.getPrice()));
        holder.appointment_time.setText(appointment.getAppointment_time().substring(0, 10));
        //按钮点击时间，弹出弹窗
        holder.bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(appointment);
            }
        });

    }

    //显示弹窗
    private void showDialog(AppointmentForStore appointment){
        AlertDialog.Builder dialog = new AlertDialog.Builder(mActivity);
        dialog.setTitle("预约：");
        dialog.setMessage("是否处理预约？");
        dialog.setCancelable(false);    //设置是否可以通过点击对话框外区域或者返回按键关闭对话框
        //点击确定，修改数据库状态/更新RecyclerView
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateStatus(appointment);
            }
        });
        //点击取消，什么也不做
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        dialog.show();

    }

    //修改状态complete，更新RecyclerView，这里写逻辑
    private void updateStatus(AppointmentForStore appointment){
        SharedPreferences preferences = mActivity.getSharedPreferences("store_info", Context.MODE_PRIVATE);
        int store_id = preferences.getInt("id", 0);
        RequestBody requestBody = new FormBody.Builder()
                .add("id", String.valueOf(appointment.getId()))
                .build();
        HttpUtil.postOkHttpRequest(url, requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("StoreAppointmentAdapter", Log.getStackTraceString(e));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (result.equals("1")) {
                            mDates.remove(appointment);
                            notifyDataSetChanged();
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDates.size();
    }
}
