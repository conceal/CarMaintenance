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
import com.example.carmaintance.adapter.StoreAppointmentItemAdapter;
import com.example.carmaintance.bean.AppointmentForStore;
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

public class StoreAppointmentFragment extends BaseFragment {

    private Activity bindActivity;
    private MyApplication application;
    private String url;
    private RecyclerView recyclerView;
    private StoreAppointmentItemAdapter adapter;
    private View view;
    private List<AppointmentForStore> appointmentList = new ArrayList<>();
    private SwipeRefreshLayout refreshLayout;
    private String TAG = "OrderFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_store_appointment, container, false);
        application = (MyApplication) getActivity().getApplication();
        url = application.getBaseUrl() + "/appointment/storeCompleteAppointment";
        initView();
        return view;
    }

    private void initView(){
        bindActivity = getActivity();
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_order);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity());
        recyclerView.setLayoutManager(layoutManager);
        initData();
        adapter = new StoreAppointmentItemAdapter(requireActivity(), application.getBaseUrl(), application.getImgUrl());
        recyclerView.setAdapter(adapter);

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.store_appointment_refresh);
        refreshLayout.setColorSchemeResources(R.color.design_default_color_primary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });
    }

    private void initData(){
        SharedPreferences preferences = bindActivity.getSharedPreferences("store_info", MODE_PRIVATE);
        int store_id = preferences.getInt("id", 0);
        RequestBody requestBody = new FormBody.Builder()
                .add("store_id", String.valueOf(store_id))
                .add("complete", String.valueOf(0))
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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        appointmentList = gson.fromJson(result, new TypeToken<List<AppointmentForStore>>(){}.getType());
                        adapter.updateData(appointmentList);
                        refreshLayout.setRefreshing(false);
                    }
                });
            }
        });

    }
}
