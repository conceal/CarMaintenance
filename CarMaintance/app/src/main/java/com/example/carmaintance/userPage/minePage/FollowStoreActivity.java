package com.example.carmaintance.userPage.minePage;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.carmaintance.MyApplication;
import com.example.carmaintance.R;
import com.example.carmaintance.activity.BaseActivity;
import com.example.carmaintance.adapter.StoreAdapter;
import com.example.carmaintance.bean.Store;
import com.example.carmaintance.util.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FollowStoreActivity extends BaseActivity {

    private MyApplication myApplication;
    private String url;
    private ImageView back;
    private RecyclerView recyclerView;
    private StoreAdapter adapter;
    private List<Store> mDates;
    private String TAG = "FollowStoreActivity";
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_store);

        myApplication = (MyApplication) getApplication();
        url = myApplication.getBaseUrl() + "/follow/selectFollowStores";

        back = (ImageView) findViewById(R.id.follow_back);
        recyclerView = (RecyclerView) findViewById(R.id.follow_store_recycleView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new StoreAdapter(this, 0, myApplication.getBaseUrl(), myApplication.getImgUrl());
        recyclerView.setAdapter(adapter);
        initDates();

        swipeRefreshLayout = findViewById(R.id.follow_store_swipe);
        swipeRefreshLayout.setColorSchemeResources(R.color.design_default_color_primary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initDates();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initDates(){
        SharedPreferences preferences = getSharedPreferences("login_info", MODE_PRIVATE);
        int id = preferences.getInt("id", 0);
        RequestBody requestBody = new FormBody.Builder()
                .add("user_id", String.valueOf(id))
                .build();
        HttpUtil.postOkHttpRequest(url, requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        mDates = gson.fromJson(result, new TypeToken<List<Store>>(){}.getType());
                        adapter.updateDates(mDates);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });
    }
}