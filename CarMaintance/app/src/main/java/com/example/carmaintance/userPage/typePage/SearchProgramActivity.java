package com.example.carmaintance.userPage.typePage;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.carmaintance.MyApplication;
import com.example.carmaintance.R;
import com.example.carmaintance.activity.BaseActivity;
import com.example.carmaintance.adapter.SearchProgramAdapter;
import com.example.carmaintance.bean.Program;
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

public class SearchProgramActivity extends BaseActivity {

    private ImageView back;
    private TextView programTitle;
    private RecyclerView recyclerView;
    private SearchProgramAdapter adapter;
    private MyApplication myApplication;
    private String url;
    private List<Program> mDates = new ArrayList<>();
    private String TAG = "SearchProgramActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_program);

        Bundle bundle = this.getIntent().getExtras();
        String programName = bundle.getString("program");
        myApplication = (MyApplication) getApplication();
        url = myApplication.getBaseUrl() + "/program/searchProgram";

        back = (ImageView) findViewById(R.id.search_program_back);
        programTitle = (TextView) findViewById(R.id.search_program_title);
        programTitle.setText(programName);

        recyclerView = (RecyclerView) findViewById(R.id.search_program_recycleView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SearchProgramAdapter(this, myApplication.getImgUrl());
        recyclerView.setAdapter(adapter);
        initDates(programName);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initDates(String programName) {
        RequestBody requestBody = new FormBody.Builder()
                .add("program_name", programName)
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
                        Log.e(TAG, result);
                        mDates = gson.fromJson(result, new TypeToken<List<Program>>(){}.getType());
                        adapter.updateDates(mDates);
                    }
                });
            }
        });
    }
}