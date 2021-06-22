package com.example.carmaintance.userPage.maintenanceInfoPage;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.carmaintance.MyApplication;
import com.example.carmaintance.R;
import com.example.carmaintance.activity.BaseActivity;
import com.example.carmaintance.bean.MaintenanceInfo;
import com.example.carmaintance.util.HttpUtil;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MaintenanceDetailsActivity extends BaseActivity implements View.OnClickListener {

    private MyApplication application;
    private String url;
    private ImageView backImg;
    private TextView programTitle;
    private TextView maintainTime;
    private TextView maintainInterval;
    private TextView lastMaintainTime;
    private TextView nextMaintainTime;
    private String TAG = "MaintenanceDetailsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance_details);
        application = (MyApplication) getApplication();
        url = application.getBaseUrl() + "/maintenanceInfo/selectMaintenanceInfo";
        Bundle bundle = this.getIntent().getExtras();
        String maintenanceName = bundle.getString("program");
        backImg = (ImageView) findViewById(R.id.maintenance_back);
        programTitle = (TextView) findViewById(R.id.program_title);
        maintainTime = (TextView) findViewById(R.id.maintain_time);
        maintainInterval = (TextView) findViewById(R.id.maintain_time_interval);
        lastMaintainTime = (TextView) findViewById(R.id.last_maintain_time);
        nextMaintainTime = (TextView) findViewById(R.id.next_maintain_time);
        backImg.setOnClickListener(this);
        programTitle.setText(maintenanceName);
        requestDate(maintenanceName);
    }

    private void requestDate(String maintenanceName) {
        SharedPreferences preferences = getSharedPreferences("login_info", MODE_PRIVATE);
        int userId = preferences.getInt("id", 0);
        RequestBody requestBody = new FormBody.Builder()
                .add("userId", String.valueOf(userId))
                .add("maintenanceName", maintenanceName)
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        MaintenanceInfo maintenanceInfo = gson.fromJson(result, MaintenanceInfo.class);
                        if (maintenanceInfo != null) {
                            maintainTime.setText(String.valueOf(maintenanceInfo.getMaintenance_time()) + "次");
                            maintainInterval.setText(String.valueOf(maintenanceInfo.getTime_interval()) + "天");
                            lastMaintainTime.setText(maintenanceInfo.getLast_time().substring(0, 10));
                            nextMaintainTime.setText(maintenanceInfo.getNext_time().substring(0, 10));
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.maintenance_back:
                finish();
                break;
        }
    }
}