package com.example.carmaintance.userPage.typePage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.example.carmaintance.MyApplication;
import com.example.carmaintance.R;
import com.example.carmaintance.activity.BaseActivity;
import com.example.carmaintance.adapter.ProgramDetailsEvaluationItemAdapter;
import com.example.carmaintance.bean.Evaluation;
import com.example.carmaintance.bean.EvaluationForProgram;
import com.example.carmaintance.bean.Program;
import com.example.carmaintance.bean.ProgramDetails;
import com.example.carmaintance.bean.ProgramDetailsEvaluation;
import com.example.carmaintance.util.HttpUtil;
import com.example.carmaintance.util.ImageUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.framed.Header;

public class ProgramActivity extends BaseActivity implements View.OnClickListener {

    private MyApplication myApplication;
    private String programUrl;
    private String evaluationUrl;
    private String appointUrl;
    private String appointingTimeUrl;
    private String storeBusinessTimeUrl;
    private String imgUrl;
    private int id;
    private int type;
    private ProgramDetails programDetails;
    private AppointAlertDialog dialog;

    private ImageView back;
    private ImageView programImg;
    private SubsamplingScaleImageView programDetailsImg;

    private TextView programDetailsTab;
    private TextView programEvaluationTab;
    private TextView programName;
    private TextView storeName;
    private TextView programTrading;
    private TextView programPrice;
    private TextView store_address;
    private TextView evaluationVolume;

    private RatingBar programRationBar;
    private View navigationTab;
    private NestedScrollView scrollView;
    private View evaluationLayout;
    private FloatingActionButton floating;

    private RecyclerView recyclerView;
    private ProgramDetailsEvaluationItemAdapter adapter;
    private List<EvaluationForProgram> mDates = new ArrayList<>();
    private String TAG = "ProgramActivity";
    private MyHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = this;
        Activity mActivity = this;
        setContentView(R.layout.activity_program);

        handler = new MyHandler(this);
        myApplication = (MyApplication) getApplication();
        programUrl = myApplication.getBaseUrl() + "/program/selectProgram";
        evaluationUrl = myApplication.getBaseUrl() + "/evaluation/selectProgramEvaluation";
        appointUrl = myApplication.getBaseUrl() + "/appointment/addAppointment";
        appointingTimeUrl = myApplication.getBaseUrl() + "/appointment/selectProgramTime";
        storeBusinessTimeUrl = myApplication.getBaseUrl() + "/store/selectStoreBusinessTime";
        imgUrl = myApplication.getImgUrl();

        Bundle bundle = getIntent().getExtras();
        id = bundle.getInt("id");
        type = bundle.getInt("type");

        back = (ImageView) findViewById(R.id.program_details_back);
        programImg = (ImageView) findViewById(R.id.program_details_program_img);

        programDetailsTab = (TextView) findViewById(R.id.program_details_program_tab);
        programEvaluationTab = (TextView) findViewById(R.id.program_details_evaluation_tab);
        programName = (TextView) findViewById(R.id.program_name);
        storeName = (TextView) findViewById(R.id.program_details_store_name);
        programTrading = (TextView) findViewById(R.id.program_details_program_volume);
        programPrice = (TextView) findViewById(R.id.program_details_program_price);
        store_address = (TextView) findViewById(R.id.program_details_store_address);
        evaluationVolume = (TextView) findViewById(R.id.evaluation_volume);

        programRationBar = (RatingBar) findViewById(R.id.program_details_ratingBar);
        navigationTab = (View) findViewById(R.id.program_details_store_navigation);
        scrollView = (NestedScrollView) findViewById(R.id.program_details_scroll_view);
        evaluationLayout = (View) findViewById(R.id.program_details_evaluation_layout);
        programDetailsImg = (SubsamplingScaleImageView) findViewById(R.id.program_details_img);
        floating = (FloatingActionButton) findViewById(R.id.appointment_float);
        if (type == 1) {
            floating.hide();
        }

        recyclerView = (RecyclerView) findViewById(R.id.program_details_evaluation_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ProgramDetailsEvaluationItemAdapter(myApplication.getImgUrl());
        recyclerView.setAdapter(adapter);
        initData(mActivity);

        programDetailsTab.setTextColor(Color.RED);

        back.setOnClickListener(this);
        floating.setOnClickListener(this);
        navigationTab.setOnClickListener(this);
        programDetailsTab.setOnClickListener(this);
        programEvaluationTab.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.program_details_back:
                finish();
                break;
            case R.id.program_details_program_tab:
                //设置服务Tab为红色，评论Tab为灰色，页面平滑到顶部
                programDetailsTab.setTextColor(Color.RED);
                programEvaluationTab.setTextColor(Color.GRAY);
                scrollView.fullScroll(NestedScrollView.FOCUS_UP);
                break;
            case R.id.program_details_evaluation_tab:
                //设置服务Tab为灰色，评论Tab为红色，页面平滑到评论部分
                programDetailsTab.setTextColor(Color.GRAY);
                programEvaluationTab.setTextColor(Color.RED);
                scrollView.smoothScrollTo(0, evaluationLayout.getTop());
                break;
            case R.id.program_details_store_navigation:
                Intent naviagionInten = new Intent();
                String uri = "baidumap://map/navi?query=" + store_address.getText().toString() + storeName.getText().toString() + "&src=andr.baidu.openAPIdemo";
                naviagionInten.setData(Uri.parse(uri));
                startActivity(naviagionInten);
                break;
            case R.id.appointment_float:
                selectTime();
                break;
        }
    }

    private void initData(Activity mActivity) {
        RequestBody requestBody1 = new FormBody.Builder()
                .add("id", String.valueOf(id))
                .build();
        HttpUtil.postOkHttpRequest(programUrl, requestBody1, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Message message = Message.obtain();
                message.what = 1;
                message.obj = result;
                handler.sendMessage(message);
            }
        });
    }

    private void initEvaluation() {
        RequestBody requestBody2 = new FormBody.Builder()
                .add("program_id", String.valueOf(id))
                .build();
        HttpUtil.postOkHttpRequest(evaluationUrl, requestBody2, new Callback() {
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
                        mDates = gson.fromJson(result, new TypeToken<List<EvaluationForProgram>>(){}.getType());
                        evaluationVolume.setText(String.valueOf(mDates.size()) + "条评论");
                        adapter.updateDate(mDates);
                    }
                });
            }
        });
    }

    private void selectTime() {
        dialog = new AppointAlertDialog(this, appointUrl, appointingTimeUrl, storeBusinessTimeUrl, programDetails, handler);
    }


    static class MyHandler extends Handler {

        WeakReference<ProgramActivity> weakReference;

        public MyHandler(ProgramActivity mActivity) {
            this.weakReference = new WeakReference<ProgramActivity>(mActivity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            ProgramActivity mActivity = weakReference.get();
            switch (msg.what) {
                case 1:
                    String result = (String) msg.obj;
                    Gson gson = new Gson();
                    mActivity.programDetails = gson.fromJson(result, ProgramDetails.class);
                    Glide.with(mActivity).load(mActivity.imgUrl + mActivity.programDetails.getProgram_url()).into(mActivity.programImg);
                    mActivity.programName.setText(mActivity.programDetails.getProgram_name());
                    mActivity.storeName.setText(mActivity.programDetails.getStore_name());
                    mActivity.programTrading.setText(String.valueOf("已售" + mActivity.programDetails.getTrading_volume()) + "单");
                    mActivity.programPrice.setText("¥ " + String.valueOf(mActivity.programDetails.getPrice()));
                    mActivity.programRationBar.setRating(mActivity.programDetails.getScore());
                    mActivity.store_address.setText(mActivity.programDetails.getStore_address());
                    RequestManager requestManager = Glide.with(mActivity);
                    //根据Url下载图片到本地
                    requestManager.load(mActivity.imgUrl + mActivity.programDetails.getDetails())
                            .downloadOnly(new SimpleTarget<File>() {
                                @Override
                                public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                                    //计算图片的尺寸
                                    float scale = ImageUtil.getImageScale(mActivity, resource.getAbsolutePath());
                                    //根据图片的尺寸将图片剪切分块加载
                                    mActivity.programDetailsImg.setImage(ImageSource.uri(resource.getAbsolutePath()),
                                            new ImageViewState(scale, new PointF(0, 0), 0));
                                }
                            });
                    mActivity.initEvaluation();
                    break;
                case 2:
                    mActivity.dialog.setBusinessTime((String) msg.obj);
                    break;
            }
        }
    }
}