package com.example.carmaintance.userPage.minePage;

import androidx.appcompat.app.AlertDialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.carmaintance.MyApplication;
import com.example.carmaintance.R;
import com.example.carmaintance.activity.BaseActivity;
import com.example.carmaintance.util.HttpUtil;
import com.example.carmaintance.util.TimeUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EvaluateAppointmentActivity extends BaseActivity implements View .OnClickListener{

    private Activity mActivity;
    private MyApplication myApplication;
    private String url;
    private ImageView back;
    private ImageView programImg;
    private ImageView star1;
    private ImageView star2;
    private ImageView star3;
    private ImageView star4;
    private ImageView star5;
    private EditText editText;
    private Button releaseButton;
    private int score;
    private int id;
    private int program_id;
    private int store_id;
    private int color = Color.rgb(250, 215, 0);
    private String TAG = "EvaluateActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate_appointment);

        mActivity = this;
        myApplication = (MyApplication) getApplication();
        url = myApplication.getBaseUrl() + "/evaluation/addEvaluation";
        Bundle bundle = getIntent().getExtras();
        id = bundle.getInt("id");
        program_id = bundle.getInt("program_id");
        store_id = bundle.getInt("store_id");

        String program_url = bundle.getString("program_url");
        back = (ImageView) findViewById(R.id.evaluate_back);
        programImg = (ImageView) findViewById(R.id.evaluate_program_img);
        star1 = (ImageView) findViewById(R.id.star1);
        star2 = (ImageView) findViewById(R.id.star2);
        star3 = (ImageView) findViewById(R.id.star3);
        star4 = (ImageView) findViewById(R.id.star4);
        star5 = (ImageView) findViewById(R.id.star5);
        editText = (EditText) findViewById(R.id.evaluate_text);
        releaseButton = (Button) findViewById(R.id.release_evaluate_button);

        back.setOnClickListener(this);
        releaseButton.setOnClickListener(this);
        Glide.with(this).load(myApplication.getImgUrl() + program_url).into(programImg);
        star1.setOnClickListener(this);
        star2.setOnClickListener(this);
        star3.setOnClickListener(this);
        star4.setOnClickListener(this);
        star5.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.star1:
                score = 1;
                star1.setColorFilter(color);
                star1.setImageResource(R.drawable.star);
                star2.setColorFilter(Color.BLACK);
                star2.setImageResource(R.drawable.star_outline);
                star3.setColorFilter(Color.BLACK);
                star3.setImageResource(R.drawable.star_outline);
                star4.setColorFilter(Color.BLACK);
                star4.setImageResource(R.drawable.star_outline);
                star5.setColorFilter(Color.BLACK);
                star5.setImageResource(R.drawable.star_outline);
                break;
            case R.id.star2:
                score = 2;
                star1.setColorFilter(color);
                star1.setImageResource(R.drawable.star);
                star2.setColorFilter(color);
                star2.setImageResource(R.drawable.star);
                star3.setColorFilter(Color.BLACK);
                star3.setImageResource(R.drawable.star_outline);
                star4.setColorFilter(Color.BLACK);
                star4.setImageResource(R.drawable.star_outline);
                star5.setColorFilter(Color.BLACK);
                star5.setImageResource(R.drawable.star_outline);
                break;
            case R.id.star3:
                score = 3;
                star1.setColorFilter(color);
                star1.setImageResource(R.drawable.star);
                star2.setColorFilter(color);
                star2.setImageResource(R.drawable.star);
                star3.setColorFilter(color);
                star3.setImageResource(R.drawable.star);
                star4.setColorFilter(Color.BLACK);
                star4.setImageResource(R.drawable.star_outline);
                star5.setColorFilter(Color.BLACK);
                star5.setImageResource(R.drawable.star_outline);
                break;
            case R.id.star4:
                score = 4;
                star1.setColorFilter(color);
                star1.setImageResource(R.drawable.star);
                star2.setColorFilter(color);
                star2.setImageResource(R.drawable.star);
                star3.setColorFilter(color);
                star3.setImageResource(R.drawable.star);
                star4.setColorFilter(color);
                star4.setImageResource(R.drawable.star);
                star5.setColorFilter(Color.BLACK);
                star5.setImageResource(R.drawable.star_outline);
                break;
            case R.id.star5:
                score = 5;
                star1.setColorFilter(color);
                star1.setImageResource(R.drawable.star);
                star2.setColorFilter(color);
                star2.setImageResource(R.drawable.star);
                star3.setColorFilter(color);
                star3.setImageResource(R.drawable.star);
                star4.setColorFilter(color);
                star4.setImageResource(R.drawable.star);
                star5.setColorFilter(color);
                star5.setImageResource(R.drawable.star);
                break;
            case R.id.evaluate_back:
                finish();
                break;
            case R.id.release_evaluate_button:
                releaseEvaluate();
                break;
            default:
                break;

        }
    }

    private void releaseEvaluate() {
        SharedPreferences preferences = getSharedPreferences("login_info", MODE_PRIVATE);
        int user_id = preferences.getInt("id", 0);
        RequestBody requestBody = new FormBody.Builder()
                .add("appointment_id", String.valueOf(id))
                .add("user_id", String.valueOf(user_id))
                .add("program_id", String.valueOf(program_id))
                .add("store_id", String.valueOf(store_id))
                .add("score", String.valueOf(score))
                .add("evaluation", editText.getText().toString())
                .add("evaluate_time", TimeUtil.getCurrentTime())
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
                        AlertDialog.Builder dialog = new AlertDialog.Builder(mActivity);
                        String message;
                        if (result.equals("1")) message = "评价成功";
                        else message = "评价失败";
                        dialog.setMessage(message);
                        dialog.setCancelable(true);
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