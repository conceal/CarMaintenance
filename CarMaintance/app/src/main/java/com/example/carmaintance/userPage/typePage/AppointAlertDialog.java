package com.example.carmaintance.userPage.typePage;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carmaintance.R;
import com.example.carmaintance.adapter.SelectTimeAdapter;
import com.example.carmaintance.bean.ProgramDetails;
import com.example.carmaintance.util.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AppointAlertDialog implements View.OnClickListener {

    private String url;
    private String appointingTimeUrl;
    private String storeBusinessTimeUrl;
    private Activity mActivity;
    private ProgramDetails programDetails;
    private android.app.AlertDialog.Builder dialog;
    private android.app.AlertDialog showDialog;
    private RecyclerView recyclerView;
    private SelectTimeAdapter adapter;
    private List<String> mDates = new ArrayList<>();
    private Button monday;
    private Button tuesday;
    private Button wednesday;
    private Button thursday;
    private Button friday;
    private Button saturday;
    private Button sunday;
    private ImageView lastWeek;
    private ImageView nextWeek;
    private TextView selectData;
    private Date currentSundayData;
    private Date selectCurrentData;
    private List<Date> dataList = new ArrayList<>();
    private String TAG = "AppointAlertDialog";
    private String businessTime;
    private ProgramActivity.MyHandler handler;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public AppointAlertDialog(Activity activity, String url, String appointingTimeUrl, String storeBusinessTimeUrl, ProgramDetails programDetails, ProgramActivity.MyHandler handler) {
        this.mActivity = activity;
        this.url = url;
        this.programDetails = programDetails;
        this.appointingTimeUrl = appointingTimeUrl;
        this.storeBusinessTimeUrl = storeBusinessTimeUrl;
        this.handler = handler;

        dialog = new android.app.AlertDialog.Builder(mActivity);
        LayoutInflater layoutInflater = LayoutInflater.from(mActivity);
        View view = (View) layoutInflater.inflate(R.layout.select_time, null);
        dialog.setView(view);
        showDialog = dialog.show();

        monday = (Button) view.findViewById(R.id.monday);
        tuesday = (Button) view.findViewById(R.id.tuesday);
        wednesday = (Button) view.findViewById(R.id.wednesday);
        thursday = (Button) view.findViewById(R.id.thursday);
        friday = (Button) view.findViewById(R.id.friday);
        saturday = (Button) view.findViewById(R.id.saturday);
        sunday = (Button) view.findViewById(R.id.sunday);
        lastWeek = (ImageView) view.findViewById(R.id.last_week);
        nextWeek = (ImageView) view.findViewById(R.id.next_week);
        selectData = (TextView) view.findViewById(R.id.select_data);

        Button appointProgram = (Button) view.findViewById(R.id.appoint_program_button);
        recyclerView = (RecyclerView) view.findViewById(R.id.select_time_recycle);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SelectTimeAdapter(mActivity);
        recyclerView.setAdapter(adapter);
        initDates();

        monday.setOnClickListener(this);
        tuesday.setOnClickListener(this);
        wednesday.setOnClickListener(this);
        thursday.setOnClickListener(this);
        friday.setOnClickListener(this);
        saturday.setOnClickListener(this);
        sunday.setOnClickListener(this);
        lastWeek.setOnClickListener(this);
        nextWeek.setOnClickListener(this);
        appointProgram.setOnClickListener(this);
    }

    private void initDates() {
        RequestBody requestBody = new FormBody.Builder()
                .add("id", String.valueOf(programDetails.getStore_id()))
                .build();
        HttpUtil.postOkHttpRequest(storeBusinessTimeUrl, requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = Message.obtain();
                        message.what = 2;
                        message.obj = result;
                        handler.sendMessage(message);
                    }
                });
            }
        });
    }

    public void setBusinessTime(String businessTime) {
        currentSundayData = getThisWeekData();
        String selectDataText = dateFormat.format(selectCurrentData);
        this.businessTime = businessTime;
        selectData.setText(selectDataText);
        setData(currentSundayData);
    }

    private void calculateTime(String date, String business_time) {
        RequestBody requestBody = new FormBody.Builder()
                .add("program_id", String.valueOf(programDetails.getId()))
                .add("date", date)
                .add("business_time", business_time)
                .build();
        HttpUtil.postOkHttpRequest(appointingTimeUrl, requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.e(TAG, result);
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        mDates = gson.fromJson(result, new TypeToken<List<String>>(){}.getType());
                        adapter.updateDates(mDates);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    //设置日期
    private void setData(Date currentSundayData) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentSundayData);

        int day = calendar.get(Calendar.DATE);
        Log.e(TAG, String.valueOf(day));
        dataList.add(0, calendar.getTime());
        sunday.setText(String.valueOf(day));
        initSelectDay(calendar.getTime(), sunday);

        calendar.set(Calendar.DAY_OF_MONTH, day + 1);
        dataList.add(1, calendar.getTime());
        monday.setText(String.valueOf(calendar.get(Calendar.DATE)));
        initSelectDay(calendar.getTime(), monday);

        calendar.set(Calendar.DAY_OF_MONTH, day + 2);
        dataList.add(2, calendar.getTime());
        tuesday.setText(String.valueOf(calendar.get(Calendar.DATE)));
        initSelectDay(calendar.getTime(), tuesday);

        calendar.set(Calendar.DAY_OF_MONTH, day + 3);
        dataList.add(3, calendar.getTime());
        wednesday.setText(String.valueOf(calendar.get(Calendar.DATE)));
        initSelectDay(calendar.getTime(), wednesday);

        calendar.set(Calendar.DAY_OF_MONTH, day + 4);
        dataList.add(4, calendar.getTime());
        thursday.setText(String.valueOf(calendar.get(Calendar.DATE)));
        initSelectDay(calendar.getTime(), thursday);

        calendar.set(Calendar.DAY_OF_MONTH, day + 5);
        dataList.add(5, calendar.getTime());
        friday.setText(String.valueOf(calendar.get(Calendar.DATE)));
        initSelectDay(calendar.getTime(), friday);

        calendar.set(Calendar.DAY_OF_MONTH, day + 6);
        dataList.add(6, calendar.getTime());
        saturday.setText(String.valueOf(calendar.get(Calendar.DATE)));
        initSelectDay(calendar.getTime(), saturday);

        for (int i = 0; i < 7; i++) {
            Log.e(TAG, dateFormat.format(dataList.get(i)));
        }
    }

    private void initSelectDay(Date date, Button button) {
        if (selectCurrentData.equals(date)) {
            onClick(button);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.appoint_program_button:
                appointProgram();
                break;
            case R.id.monday:
                setWeekParam();
                monday.setBackgroundResource(R.drawable.select_circle_button);
                monday.setTextColor(Color.WHITE);
                selectCurrentData = dataList.get(1);
                selectData.setText(dateFormat.format(selectCurrentData));
                calculateTime(selectData.getText().toString(), businessTime);
                break;
            case R.id.tuesday:
                setWeekParam();
                tuesday.setBackgroundResource(R.drawable.select_circle_button);
                tuesday.setTextColor(Color.WHITE);
                selectCurrentData = dataList.get(2);
                selectData.setText(dateFormat.format(selectCurrentData));
                calculateTime(selectData.getText().toString(), businessTime);
                break;
            case R.id.wednesday:
                setWeekParam();
                wednesday.setBackgroundResource(R.drawable.select_circle_button);
                wednesday.setTextColor(Color.WHITE);
                selectCurrentData = dataList.get(3);
                selectData.setText(dateFormat.format(selectCurrentData));
                calculateTime(selectData.getText().toString(), businessTime);
                break;
            case R.id.thursday:
                setWeekParam();
                thursday.setBackgroundResource(R.drawable.select_circle_button);
                thursday.setTextColor(Color.WHITE);
                selectCurrentData = dataList.get(4);
                selectData.setText(dateFormat.format(selectCurrentData));
                calculateTime(selectData.getText().toString(), businessTime);
                break;
            case R.id.friday:
                setWeekParam();
                friday.setBackgroundResource(R.drawable.select_circle_button);
                friday.setTextColor(Color.WHITE);
                selectCurrentData = dataList.get(5);
                selectData.setText(dateFormat.format(selectCurrentData));
                calculateTime(selectData.getText().toString(), businessTime);
                break;
            case R.id.saturday:
                setWeekParam();
                saturday.setBackgroundResource(R.drawable.select_circle_button);
                saturday.setTextColor(Color.WHITE);
                selectCurrentData = dataList.get(6);
                selectData.setText(dateFormat.format(selectCurrentData));
                calculateTime(selectData.getText().toString(), businessTime);
                break;
            case R.id.sunday:
                setWeekParam();
                sunday.setBackgroundResource(R.drawable.select_circle_button);
                sunday.setTextColor(Color.WHITE);
                selectCurrentData = dataList.get(0);
                selectData.setText(dateFormat.format(selectCurrentData));
                calculateTime(selectData.getText().toString(), businessTime);
                break;
            case R.id.last_week:
                setWeekData(-7);
                break;
            case R.id.next_week:
                setWeekData(7);
                break;

        }
    }

    private void setWeekParam() {
        monday.setBackgroundResource(R.drawable.circle_button);
        monday.setTextColor(Color.BLACK);
        tuesday.setBackgroundResource(R.drawable.circle_button);
        tuesday.setTextColor(Color.BLACK);
        wednesday.setBackgroundResource(R.drawable.circle_button);
        wednesday.setTextColor(Color.BLACK);
        thursday.setBackgroundResource(R.drawable.circle_button);
        thursday.setTextColor(Color.BLACK);
        friday.setBackgroundResource(R.drawable.circle_button);
        friday.setTextColor(Color.BLACK);
        saturday.setBackgroundResource(R.drawable.circle_button);
        saturday.setTextColor(Color.BLACK);
        sunday.setBackgroundResource(R.drawable.circle_button);
        sunday.setTextColor(Color.BLACK);
    }

    private void setWeekData(int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentSundayData);
        //获取下周日的日期,-7则是获取上周一日期
        calendar.add(Calendar.DATE,day);
        currentSundayData = calendar.getTime();
        setData(currentSundayData);

        calendar.clear();
        calendar.setTime(selectCurrentData);
        calendar.add(Calendar.DATE, day);
        selectCurrentData = calendar.getTime();
        selectData.setText(dateFormat.format(selectCurrentData));
    }

    //获取日历中一周第一天（周日）的时间
    private Date getThisWeekData(){
        //获取当天时间
        Calendar c = Calendar.getInstance();
        selectCurrentData = c.getTime();
        c.set(Calendar.DAY_OF_WEEK, 1);
        return c.getTime();
    }

    private void appointProgram() {
        if (adapter.getSelectTime() == null) {
            android.app.AlertDialog.Builder timeDialog = new android.app.AlertDialog.Builder(mActivity);
            timeDialog.setMessage("请选择预约时间段");
            timeDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            timeDialog.show();
            return;
        }
        String time = selectData.getText().toString() + " " + adapter.getSelectTime();
        showDialog.dismiss();
        SharedPreferences preferences = mActivity.getSharedPreferences("login_info", Context.MODE_PRIVATE);
        int user_id = preferences.getInt("id", 0);
        RequestBody requestBody = new FormBody.Builder()
                .add("user_id", String.valueOf(user_id))
                .add("program_id", String.valueOf(programDetails.getId()))
                .add("store_id", String.valueOf(programDetails.getStore_id()))
                .add("appointment_time", time)
                .add("complete", "0")
                .add("evaluate", "0")
                .build();
        HttpUtil.postOkHttpRequest(url, requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                String message;
                if (result.equals("1")) {
                    message = "预约成功";
                } else {
                    message = "预约失败";
                }
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        android.app.AlertDialog.Builder timeDialog = new android.app.AlertDialog.Builder(mActivity);
                        timeDialog.setMessage(message);
                        timeDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (message.equals("预约成功")) {
                                    closeShowDialog();
                                }
                            }
                        });
                        timeDialog.show();
                    }
                });
            }
        });

    }

    private void closeShowDialog() {
        showDialog.dismiss();
    }
}
