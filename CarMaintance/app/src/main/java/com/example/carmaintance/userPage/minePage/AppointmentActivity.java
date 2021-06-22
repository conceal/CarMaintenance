package com.example.carmaintance.userPage.minePage;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.carmaintance.MyApplication;
import com.example.carmaintance.R;
import com.example.carmaintance.activity.BaseActivity;
import com.example.carmaintance.adapter.AppointmentAdapter;
import com.example.carmaintance.bean.AppointmentForUser;
import com.example.carmaintance.util.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AppointmentActivity extends BaseActivity {

    private List<AppointmentForUser> appointmentList = new ArrayList<>();
    private MyApplication myApplication;
    private String url;
    private String imgUrl;
    private String evaluateUrl;
    private String TAG = "AppointmentActivity";
    private RecyclerView recyclerView;
    private AppointmentAdapter adapter;
    private TextView appointmentTitle;
    private int complete;
    private int evaluate;
    private int type;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
        Bundle bundle = this.getIntent().getExtras();
        complete = bundle.getInt("complete");
        evaluate = bundle.getInt("evaluation");
        type = bundle.getInt("type");
        myApplication = (MyApplication) getApplication();
        url = myApplication.getBaseUrl() + "/appointment/userCompleteAppointment";
        evaluateUrl = myApplication.getBaseUrl() + "/appointment/selectEvaluateAppointment";
        appointmentTitle = (TextView) findViewById(R.id.appointment_title);
        if (type == 0) {
            appointmentTitle.setText("预约中");
        } else if (type == 1){
            appointmentTitle.setText("已完成");
        } else {
            appointmentTitle.setText("待评价");
        }
        recyclerView  = (RecyclerView) findViewById(R.id.appointment_recycleView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new AppointmentAdapter(this, type, myApplication.getImgUrl());
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout = findViewById(R.id.appointment_swipe);
        swipeRefreshLayout.setColorSchemeResources(R.color.design_default_color_primary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });
        initData();
    }

    private void initData () {
        if (type != 2) {
            SharedPreferences preferences = getSharedPreferences("login_info", MODE_PRIVATE);
            int user_id = preferences.getInt("id", 0);
            RequestBody requestBody = new FormBody.Builder()
                    .add("user_id", String.valueOf(user_id))
                    .add("complete", String.valueOf(complete))
                    .build();
            HttpUtil.postOkHttpRequest(url, requestBody, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, Log.getStackTraceString(e));
                    Log.e(TAG, "请求失败");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    Log.e(TAG, result);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Gson gson = new Gson();
                            appointmentList = gson.fromJson(result, new TypeToken<List<AppointmentForUser>>(){}.getType());
                            adapter.addDate(appointmentList);
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });
                }
            });
        } else {
            SharedPreferences preferences = getSharedPreferences("login_info", MODE_PRIVATE);
            int user_id = preferences.getInt("id", 0);
            RequestBody requestBody = new FormBody.Builder()
                    .add("user_id", String.valueOf(user_id))
                    .add("complete", String.valueOf(complete))
                    .add("evaluate", String.valueOf(evaluate))
                    .build();
            HttpUtil.postOkHttpRequest(evaluateUrl, requestBody, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, Log.getStackTraceString(e));
                    Log.e(TAG, "请求失败");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    Log.e(TAG, result);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Gson gson = new Gson();
                            appointmentList = gson.fromJson(result, new TypeToken<List<AppointmentForUser>>(){}.getType());
                            adapter.addDate(appointmentList);
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });
                }
            });
        }

    }
}