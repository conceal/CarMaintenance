package com.example.carmaintance.adminPage;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.carmaintance.MyApplication;
import com.example.carmaintance.R;
import com.example.carmaintance.activity.BaseActivity;
import com.example.carmaintance.activity.LoginActivity;
import com.example.carmaintance.adapter.StoreAdapter;
import com.example.carmaintance.adapter.UserAdapter;
import com.example.carmaintance.bean.Store;
import com.example.carmaintance.bean.User;
import com.example.carmaintance.util.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AdminMainActivity extends BaseActivity implements View.OnClickListener{

    private MyApplication myApplication;
    private String userUrl;
    private String storeUrl;
    private ImageView back;
    private TextView TypeTitle;
    private TextView carUserText;
    private TextView storeUserText;
    private ImageView carUserImg;
    private ImageView storeUserImg;
    private View carUserTab;
    private View storeUserTab;
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private StoreAdapter storeAdapter;
    private List<User> userDate = new ArrayList<>();
    private List<Store> storeData = new ArrayList<>();
    private String TAG = "AdminMainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        myApplication = (MyApplication) getApplication();
        userUrl = myApplication.getBaseUrl() + "/user/selectAllUser";
        storeUrl = myApplication.getBaseUrl() + "/store/selectAllStore";

        back = (ImageView) findViewById(R.id.admin_back);
        TypeTitle = (TextView) findViewById(R.id.user_type);
        carUserText = (TextView) findViewById(R.id.car_user_text);
        storeUserText = (TextView) findViewById(R.id.store_user_text);
        carUserImg = (ImageView) findViewById(R.id.car_user_img);
        storeUserImg = (ImageView) findViewById(R.id.store_user_img);
        carUserTab = (View) findViewById(R.id.car_user_tab);
        storeUserTab = (View) findViewById(R.id.store_user_tab);

        recyclerView = (RecyclerView) findViewById(R.id.admin_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        userAdapter = new UserAdapter(this, myApplication.getBaseUrl(), myApplication.getImgUrl());
        storeAdapter = new StoreAdapter(this, 1, myApplication.getBaseUrl(), myApplication.getImgUrl());
        recyclerView.setAdapter(userAdapter);

        carUserText.setTextColor(Color.RED);
        storeUserText.setTextColor(Color.GRAY);
        carUserImg.setColorFilter(Color.RED);
        storeUserImg.setColorFilter(Color.GRAY);

        back.setOnClickListener(this);
        carUserTab.setOnClickListener(this);
        storeUserTab.setOnClickListener(this);

        initUserData();
    }

    private void initUserData() {
        HttpUtil.sendOkHttpRequest(userUrl, new Callback() {
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
                        userDate = gson.fromJson(result, new TypeToken<List<User>>(){}.getType());
                        recyclerView.setAdapter(userAdapter);
                        userAdapter.updateData(userDate);
                    }
                });
            }
        });
    }

    private void initStoreData() {
        HttpUtil.sendOkHttpRequest(storeUrl, new Callback() {
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
                        storeData = gson.fromJson(result, new TypeToken<List<Store>>(){}.getType());
                        recyclerView.setAdapter(storeAdapter);
                        storeAdapter.updateDates(storeData);
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.admin_back:
                SharedPreferences preferences = getSharedPreferences("admin_info", MODE_PRIVATE);
                preferences.edit().clear().commit();
                Intent loginIntent = new Intent(this, LoginActivity.class);
                finish();
                startActivity(loginIntent);
                break;
            case R.id.car_user_tab:
                initUserData();
                TypeTitle.setText("车主");
                carUserText.setTextColor(Color.RED);
                storeUserText.setTextColor(Color.GRAY);
                carUserImg.setColorFilter(Color.RED);
                storeUserImg.setColorFilter(Color.GRAY);
                break;
            case R.id.store_user_tab:
                initStoreData();
                TypeTitle.setText("保养店");
                carUserText.setTextColor(Color.GRAY);
                storeUserText.setTextColor(Color.RED);
                carUserImg.setColorFilter(Color.GRAY);
                storeUserImg.setColorFilter(Color.RED);
                break;
        }
    }
}