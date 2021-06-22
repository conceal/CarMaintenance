package com.example.carmaintance.storeMainPage;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.carmaintance.MyApplication;
import com.example.carmaintance.R;
import com.example.carmaintance.adapter.ProtectItemAdapter;
import com.example.carmaintance.bean.Program;
import com.example.carmaintance.userPage.BaseFragment;
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

import static android.content.Context.MODE_PRIVATE;

public class StoreProgramFragment extends BaseFragment {

    private Activity bindActivity;
    private MyApplication myApplication;
    private String url;
    private RecyclerView recyclerView;
    private ProtectItemAdapter adapter;
    private View view;
    private List<Program> mDates = new ArrayList<>();
    private SwipeRefreshLayout swipeRefresh;


    public StoreProgramFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_store_program, container, false);
        initView();
        return view;
    }

    private void initView(){
        bindActivity = getActivity();
        myApplication = (MyApplication) getActivity().getApplication();
        url = myApplication.getBaseUrl() + "/program/selectProgramByStoreId";
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.store_program_swipe_refresh);
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_protect);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity());
        recyclerView.setLayoutManager(layoutManager);
        initData();
        adapter = new ProtectItemAdapter(requireActivity(), myApplication.getImgUrl());
        recyclerView.setAdapter(adapter);

        swipeRefresh.setColorSchemeResources(R.color.design_default_color_primary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });
    }

    //数据填充，网络请求
    private void initData(){
        SharedPreferences preferences = bindActivity.getSharedPreferences("store_info", MODE_PRIVATE);
        int store_id = preferences.getInt("id", 0);
        RequestBody requestBody = new FormBody.Builder()
                .add("store_id", String.valueOf(store_id))
                .build();
        HttpUtil.postOkHttpRequest(url, requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("ProtectFragment", Log.getStackTraceString(e));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.e("StoreProgram", result);
                bindActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        mDates = gson.fromJson(result, new TypeToken<List<Program>>(){}.getType());
                        adapter.updateData(mDates);
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
    }
}
