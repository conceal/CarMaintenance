package com.example.carmaintance.userPage.minePage;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.carmaintance.MyApplication;
import com.example.carmaintance.R;
import com.example.carmaintance.activity.BaseActivity;
import com.example.carmaintance.adapter.StoreDetailsEvaluationAdapter;
import com.example.carmaintance.bean.Evaluation;
import com.example.carmaintance.bean.UserEvaluation;
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

public class EvaluationActivity extends BaseActivity {

    private MyApplication myApplication;
    private String url;
    private int id;
    private ImageView back;
    private RecyclerView recyclerView;
    private StoreDetailsEvaluationAdapter adapter;
    private List<UserEvaluation> mDates = new ArrayList<>();
    private String TAG = "EvaluationActivity";
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation);

        Bundle bundle = getIntent().getExtras();
        id = bundle.getInt("id");
        myApplication = (MyApplication) getApplication();
        url = myApplication.getBaseUrl() + "/evaluation/selectUserEvaluation";
        back = (ImageView) findViewById(R.id.evaluation_back);
        recyclerView = (RecyclerView) findViewById(R.id.evaluation_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new StoreDetailsEvaluationAdapter(this, myApplication.getImgUrl());
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout = findViewById(R.id.evaluation_swipe);
        swipeRefreshLayout.setColorSchemeResources(R.color.design_default_color_primary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initDates();
            }
        });
        initDates();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initDates() {
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
                Log.e(TAG, result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        mDates = gson.fromJson(result, new TypeToken<List<UserEvaluation>>(){}.getType());
                        adapter.updateDates(mDates);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });
    }
}