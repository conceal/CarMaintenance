package com.example.carmaintance.userPage.storePage;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.carmaintance.MyApplication;
import com.example.carmaintance.R;
import com.example.carmaintance.activity.BaseActivity;
import com.example.carmaintance.adapter.StoreDetailsEvaluationAdapter;
import com.example.carmaintance.adapter.StoreDetailsProgramAdapter;
import com.example.carmaintance.bean.Evaluation;
import com.example.carmaintance.bean.Program;
import com.example.carmaintance.bean.Store;
import com.example.carmaintance.bean.UserEvaluation;
import com.example.carmaintance.util.HttpUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StoreDetailsActivity extends BaseActivity implements View.OnClickListener{

    private MyApplication myApplication;
    private String url;
    private String programUrl;
    private String evaluationUrl;
    private String followUrl;
    private int followId = 0;

    private int store_id;
    private int user_id;
    private int type;

    private ImageView storeImg;
    private TextView storeName;
    private TextView storeScore;
    private TextView storeVolume;
    private TextView storeAddress;
    private View storeNavigation;
    private TextView storeProgramTab;
    private TextView storeEvaluationTab;
    private FloatingActionButton floating;

    private RecyclerView recyclerView;
    private StoreDetailsProgramAdapter programAdapter;
    private StoreDetailsEvaluationAdapter evaluationAdapter;

    private List<Program> mProgramDates = new ArrayList<>();
    private List<UserEvaluation> mEvaluationDates = new ArrayList<>();
    private String TAG = "StoreDetailsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_details);

        myApplication = (MyApplication) getApplication();
        url = myApplication.getBaseUrl() + "/store/selectStore";
        programUrl = myApplication.getBaseUrl() + "/program/selectProgramByStoreId";
        evaluationUrl = myApplication.getBaseUrl() + "/evaluation/selectEvaluationByStoreId";
        followUrl = myApplication.getBaseUrl() + "/follow";


        Bundle bundle = this.getIntent().getExtras();
        store_id = bundle.getInt("id");
        type = bundle.getInt("type");
        Log.e(TAG, String.valueOf(store_id));
        SharedPreferences preferences = getSharedPreferences("login_info", MODE_PRIVATE);
        user_id = preferences.getInt("id", 0);

        storeImg = (ImageView) findViewById(R.id.store_details_img);
        storeName = (TextView) findViewById(R.id.store_details_name);
        storeScore = (TextView) findViewById(R.id.store_details_score);
        storeVolume = (TextView) findViewById(R.id.store_details_volume);
        storeAddress = (TextView) findViewById(R.id.store_details_address);
        storeNavigation = (View) findViewById(R.id.store_navigation);
        storeProgramTab = (TextView) findViewById(R.id.store_details_program_tab);
        storeEvaluationTab = (TextView) findViewById(R.id.store_details_evaluation_tab);
        floating = (FloatingActionButton) findViewById(R.id.store_details_float);

        recyclerView = (RecyclerView) findViewById(R.id.store_details_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        programAdapter = new StoreDetailsProgramAdapter(this, myApplication.getImgUrl());
        evaluationAdapter = new StoreDetailsEvaluationAdapter(this, myApplication.getImgUrl());

        floating.setOnClickListener(this);
        storeNavigation.setOnClickListener(this);
        storeProgramTab.setOnClickListener(this);
        storeEvaluationTab.setOnClickListener(this);
        if (type == 1) floating.hide();
        initDates(store_id);
        initStoreProgram();
    }

    private void initDates(int store_id) {
        RequestBody requestBody = new FormBody.Builder()
                .add("id", String.valueOf(store_id))
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
                        Store store = gson.fromJson(result, Store.class);
                        Glide.with(myApplication).load(myApplication.getImgUrl() + store.getStore_url()).into(storeImg);
                        storeName.setText(store.getStore_name());
                        DecimalFormat df = new DecimalFormat("#.00");
                        storeScore.setText(df.format(store.getScore()));
                        storeVolume.setText(String.valueOf(store.getTrading_volume()) + "单");
                        storeAddress.setText(store.getStore_address());
                    }
                });
            }
        });


        RequestBody requestBody1 = new FormBody.Builder()
                .add("user_id", String.valueOf(user_id))
                .add("store_id", String.valueOf(store_id))
                .build();
        HttpUtil.postOkHttpRequest(followUrl + "/selectFollow", requestBody1, new Callback() {
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
                        followId = Integer.parseInt(result);
                        floating.setImageResource(followId > 0 ? R.drawable.nofollow : R.drawable.follow);
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.store_navigation:
                Intent naviagionInten = new Intent();
                String uri = "baidumap://map/navi?query=" + storeAddress.getText().toString() + storeName.getText().toString() +"&src=andr.baidu.openAPIdemo";
                naviagionInten.setData(Uri.parse(uri));
                startActivity(naviagionInten);
                break;
            case R.id.store_details_program_tab:
                initStoreProgram();
                break;
            case R.id.store_details_evaluation_tab:
                initEvaluationDates();
                break;
            case R.id.store_details_float:
                if (followId > 0) deleteFollow(this);  //取消关注
                else followStore(this);   //关注保养店
                break;
        }
    }

    private void initStoreProgram() {
        storeProgramTab.setTextColor(Color.RED);
        storeEvaluationTab.setTextColor(Color.GRAY);
        if (mProgramDates.isEmpty()) {
            RequestBody requestBody = new FormBody.Builder()
                    .add("store_id", String.valueOf(store_id) )
                    .build();
            HttpUtil.postOkHttpRequest(programUrl, requestBody, new Callback() {
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
                            Log.e(TAG, result);
                            mProgramDates = gson.fromJson(result, new TypeToken<List<Program>>(){}.getType());
                            recyclerView.setAdapter(programAdapter);
                            programAdapter.updateDates(mProgramDates);
                        }
                    });
                }
            });
        } else {
            recyclerView.setAdapter(programAdapter);
            programAdapter.updateDates(mProgramDates);
        }
    }

    private void initEvaluationDates() {
        storeProgramTab.setTextColor(Color.GRAY);
        storeEvaluationTab.setTextColor(Color.RED);
        if (mEvaluationDates.isEmpty()) {
            RequestBody requestBody = new FormBody.Builder()
                    .add("store_id", String.valueOf(store_id))
                    .build();
            HttpUtil.postOkHttpRequest(evaluationUrl, requestBody, new Callback() {
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
                            mEvaluationDates = gson.fromJson(result, new TypeToken<List<UserEvaluation>>(){}.getType());
                            recyclerView.setAdapter(evaluationAdapter);
                            evaluationAdapter.updateDates(mEvaluationDates);
                        }
                    });
                }
            });
        } else {
            recyclerView.setAdapter(evaluationAdapter);
            evaluationAdapter.updateDates(mEvaluationDates);
        }
    }

    private void followStore(Activity mActivity) {
        SharedPreferences preferences = getSharedPreferences("login_info", MODE_PRIVATE);
        int user_id = preferences.getInt("id", 0);
        RequestBody requestBody = new FormBody.Builder()
                .add("id", "0")
                .add("user_id", String.valueOf(user_id))
                .add("store_id", String.valueOf(store_id))
                .build();
        HttpUtil.postOkHttpRequest(followUrl + "/insertFollow", requestBody, new Callback() {
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
                        AlertDialog.Builder dialog = new AlertDialog.Builder(mActivity);
                        String message = null;

                        if (result.equals(0)) {
                            message = "你已关注" + storeName.getText().toString();
                        }
                        else if (result.equals("-1")) {
                            message = "关注失败";
                        }
                        else {
                            message = "你已成功关注" + storeName.getText().toString();
                            floating.setImageResource(R.drawable.nofollow);
                            followId = Integer.parseInt(result);
                        }

                        dialog.setMessage(message);
                        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        dialog.show();
                    }
                });
            }
        });
    }

    private void deleteFollow(Activity mActivity) {
        RequestBody requestBody = new FormBody.Builder()
                .add("id", String.valueOf(followId))
                .build();
        HttpUtil.postOkHttpRequest(followUrl + "/deleteFollow", requestBody, new Callback() {
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
                        AlertDialog.Builder dialog = new AlertDialog.Builder(mActivity);
                        String message = null;
                        if (result.equals("1")) {
                            message = "已成功取消关注" + storeName.getText().toString();
                            floating.setImageResource(R.drawable.follow);
                            followId = 0;
                        } else {
                            message = "取消关注失败";
                        }
                        dialog.setMessage(message);
                        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        dialog.show();
                    }
                });
            }
        });
    }
}