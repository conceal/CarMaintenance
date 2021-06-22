package com.example.carmaintance.userPage.maintenanceInfoPage;

import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.carmaintance.MyApplication;
import com.example.carmaintance.R;
import com.example.carmaintance.adapter.MaintainInfoAdapter;
import com.example.carmaintance.bean.Type;
import com.example.carmaintance.userPage.BaseFragment;
import com.example.carmaintance.util.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class maintenanceInfoFragment extends BaseFragment {

    private MyApplication myApplication;
    private String url;
    private View view;
    private List<Type> mData = new ArrayList<>();
    private RecyclerView recyclerView;
    private MaintainInfoAdapter mAdapter;
    private String TAG = "maintenanceInfoFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_maintenanceinfo, container, false);

        myApplication = (MyApplication) getActivity().getApplication();
        url = myApplication.getBaseUrl() + "/type/getType";
        recyclerView = (RecyclerView) view.findViewById(R.id.maintain_info_recyclerview);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new MaintainInfoAdapter(getActivity());
        recyclerView.setAdapter(mAdapter);
        serHeader(recyclerView);
        initData();
        return view;
    }

    private void serHeader(RecyclerView view) {
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.maintain_program_header, view, false);
        mAdapter.setHeaderView(header);
    }

    private void initData() {
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.e(TAG, result);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        mData = gson.fromJson(result, new TypeToken<List<Type>>(){}.getType());
                        mAdapter.updateDates(mData);
                    }
                });
            }
        });

    }
}