package com.example.carmaintance.userPage.storePage;

import android.app.Activity;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.example.carmaintance.MyApplication;
import com.example.carmaintance.R;
import com.example.carmaintance.adapter.StoreAdapter;
import com.example.carmaintance.bean.Store;
import com.example.carmaintance.userPage.BaseFragment;
import com.example.carmaintance.util.HttpUtil;
import com.example.carmaintance.util.LocationUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import android.graphics.Color;

import org.json.JSONArray;
import org.json.JSONObject;

public class StoreFragment extends BaseFragment implements View.OnClickListener{

    private Activity mActivity;
    private EditText searchStore;
    private TextView search;
    private View storeScoreTab;
    private View storeVolumeTab;
    private View storeDistanceTab;
    private TextView storeScoreText;
    private TextView storeVolumeText;
    private TextView storeDistanceText;
    private RecyclerView recyclerView;
    private StoreAdapter adapter;
    private MyApplication myApplication;
    private String url;
    private String mLocation = "29.538277594656888,106.61153424988501";
    private String TAG = "StoreFragment";
    private List<Store> mDates = new ArrayList<>();
    private int index = 0, position = 0;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store, container, false);
        myApplication = (MyApplication) getActivity().getApplication();
        url = myApplication.getBaseUrl() + "/store/selectAllStore";
        searchStore = (EditText) view.findViewById(R.id.search_store_text);
        search = (TextView) view.findViewById(R.id.store_search);
        storeScoreTab = (View) view.findViewById(R.id.store_score_tab);
        storeVolumeTab = (View) view.findViewById(R.id.store_volume_tab);
        storeDistanceTab = (View) view.findViewById(R.id.store_distance_tab);
        storeScoreText = (TextView) view.findViewById(R.id.store_score_text);
        storeVolumeText = (TextView) view.findViewById(R.id.store_volume_text);
        storeDistanceText = (TextView) view.findViewById(R.id.store_distance_text);
        recyclerView = (RecyclerView) view.findViewById(R.id.store_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new StoreAdapter(getActivity(), 0, myApplication.getBaseUrl(), myApplication.getImgUrl());
        recyclerView.setAdapter(adapter);

        storeScoreTab.setOnClickListener(this);
        storeVolumeTab.setOnClickListener(this);
        storeDistanceTab.setOnClickListener(this);
        storeScoreText.setTextColor(Color.RED);
        storeVolumeText.setTextColor(Color.GRAY);
        storeDistanceText.setTextColor(Color.GRAY);

        swipeRefreshLayout = view.findViewById(R.id.fragment_store_swipe);
        swipeRefreshLayout.setColorSchemeResources(R.color.design_default_color_primary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initDate();
            }
        });
        initDate();
        return view;
    }

    private void initDate() {
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        mDates = gson.fromJson(result, new TypeToken<List<Store>>(){}.getType());
                        sortDatesByScore(mDates);
                        calculateDistance(mDates);
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.store_score_tab:
                storeScoreText.setTextColor(Color.RED);
                storeVolumeText.setTextColor(Color.GRAY);
                storeDistanceText.setTextColor(Color.GRAY);
                sortDatesByScore(mDates);
                adapter.updateDates(mDates);
                break;
            case R.id.store_volume_tab:
                storeScoreText.setTextColor(Color.GRAY);
                storeVolumeText.setTextColor(Color.RED);
                storeDistanceText.setTextColor(Color.GRAY);
                sortDatesByTradingVolume(mDates);
                adapter.updateDates(mDates);
                break;
            case R.id.store_distance_tab:
                storeScoreText.setTextColor(Color.GRAY);
                storeVolumeText.setTextColor(Color.GRAY);
                storeDistanceText.setTextColor(Color.RED);
                sortDatesByDistance(mDates);
                adapter.updateDates(mDates);
                break;
        }
    }

    private void sortDatesByScore(List<Store> dates) {
        if (!dates.isEmpty()) {
            Collections.sort(dates, new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    //降序
                    Store store1 = (Store) o1;
                    Store store2 = (Store) o2;
                    if (store1.getScore() < store2.getScore()) {
                        return 1;
                    } else if (store1.getScore() == store2.getScore()) {
                        return 0;
                    } else {
                        return -1;
                    }
                }
            });
        }
    }

    private void sortDatesByTradingVolume(List<Store> dates) {
        if (!dates.isEmpty()) {
            Collections.sort(dates, new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    //降序
                    Store store1 = (Store) o1;
                    Store store2 = (Store) o2;
                    if (store1.getTrading_volume() < store2.getTrading_volume()) {
                        return 1;
                    } else if (store1.getTrading_volume() == store2.getTrading_volume()) {
                        return 0;
                    } else {
                        return -1;
                    }
                }
            });
        }

    }

    private void sortDatesByDistance(List<Store> dates) {
        if (!dates.isEmpty()) {
            Collections.sort(dates, new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    //降序
                    Store store1 = (Store) o1;
                    Store store2 = (Store) o2;
                    if (store1.getDistance() > store2.getDistance()) {
                        return 1;
                    } else if (store1.getDistance() == store2.getDistance()) {
                        return 0;
                    } else {
                        return -1;
                    }
                }
            });
        }
    }

    private void calculateDistance(List<Store> mDates) {
        MyHandler handler = new MyHandler();
        String baseUrl1 = "http://api.map.baidu.com/routematrix/v2/driving?output=json&origins="+ mLocation +"&destinations=";
        String baseUrl2 = "&ak=MsW26RdbA419IRraQdPxiP2uN2aGTvpR";
        LocationUtil.getLocation(getActivity(), new MyLocationListener());
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < mDates.size(); i=i+40) {
            int len = mDates.size() - i > 40 ? 40 : mDates.size() - i;
            for (int j = 0; j < len; j++) {
                stringBuilder.append(mDates.get(index).getLocation()).append("|");
                index++;
            }
            String str = stringBuilder.toString();
            String locationUrl = baseUrl1 + str.substring(0, str.length()-1) + baseUrl2;
            Log.e(TAG, locationUrl);
            HttpUtil.sendOkHttpRequest(locationUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, Log.getStackTraceString(e));
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Message message = Message.obtain();
                    message.what = 1;
                    message.arg1 = len;
                    message.obj = response.body().string();
                    handler.sendMessage(message);
                }
            });
        }
    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location){
            StringBuilder currentPosition = new StringBuilder();
            currentPosition.append(location.getLatitude()).append(",").append(location.getLongitude());
        }
    }

    class MyHandler extends Handler {

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    String result = (String)msg.obj;
                    int len = msg.arg1;
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int n = 0; n < len; n++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(position);
                            String str = jsonObject1.getJSONObject("distance").getString("text");
                            double distance = Double.parseDouble(str.substring(0, str.length()-2));
                            mDates.get(position).setDistance(distance);
                            Log.e(TAG, String.valueOf(mDates.get(position).getDistance()));
                            position++;
                        }
                        if (position == mDates.size()) {
                            adapter.updateDates(mDates);
                            index = 0;
                            position = 0;
                            swipeRefreshLayout.setRefreshing(false);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }
}