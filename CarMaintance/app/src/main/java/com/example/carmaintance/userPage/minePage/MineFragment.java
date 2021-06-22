package com.example.carmaintance.userPage.minePage;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.carmaintance.MyApplication;
import com.example.carmaintance.R;
import com.example.carmaintance.activity.LoginActivity;
import com.example.carmaintance.userPage.BaseFragment;

import static android.content.Context.MODE_PRIVATE;


public class MineFragment extends BaseFragment implements View.OnClickListener {

    private MyApplication myApplication;
    private String imgUrl;
    private View mineView;
    private Activity bindActivity;
    private ImageView headImg;
    private TextView nickname;
    private TextView phone_number;
    private View appointingView;
    private View appointedView;
    private View evaluateView;
    private View evaluationView;
    private View followView;
    private View helpView;
    private View opinionView;
    private View aboutView;
    private Button account_button;
    private String TAG = "MineFragment";
    private int id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mineView = inflater.inflate(R.layout.fragment_mine, container, false);
        myApplication = (MyApplication) getActivity().getApplication();
        imgUrl = myApplication.getImgUrl();
        initView(mineView);
        return mineView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        appointingView.setOnClickListener(this);
        appointedView.setOnClickListener(this);
        evaluateView.setOnClickListener(this);
        evaluationView.setOnClickListener(this);
        followView.setOnClickListener(this);
        helpView.setOnClickListener(this);
        opinionView.setOnClickListener(this);
        aboutView.setOnClickListener(this);
        account_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.account_button:
                SharedPreferences preferences = bindActivity.getSharedPreferences("login_info", MODE_PRIVATE);
                preferences.edit().clear().commit();
                Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                getActivity().finish();
                startActivity(loginIntent);
                break;
            case R.id.appointing_tab:
                Intent appointingIntent = new Intent(bindActivity, AppointmentActivity.class);
                appointingIntent.putExtra("complete", 0);
                appointingIntent.putExtra("evaluate", 0);
                appointingIntent.putExtra("type", 0);
                startActivity(appointingIntent);
                break;
            case R.id.appointed_tab:
                Intent appointedIntent = new Intent(bindActivity, AppointmentActivity.class);
                appointedIntent.putExtra("complete", 1);
                appointedIntent.putExtra("evaluate", 0);
                appointedIntent.putExtra("type", 1);
                startActivity(appointedIntent);
                break;
            case R.id.evaluate_tab:
                Intent evaluateIntent = new Intent(bindActivity, AppointmentActivity.class);
                evaluateIntent.putExtra("complete", 1);
                evaluateIntent.putExtra("evaluate", 0);
                evaluateIntent.putExtra("type", 2);
                startActivity(evaluateIntent);
                break;
            case R.id.evaluation_tab:
                Intent evaluationIntent = new Intent(bindActivity, EvaluationActivity.class);
                evaluationIntent.putExtra("id", id);
                startActivity(evaluationIntent);
                break;
            case R.id.follow_tab:
                Intent followIntent = new Intent(bindActivity, FollowStoreActivity.class);
                startActivity(followIntent);
                break;
            default:
                break;

        }
    }

    private void initView(View view){
        bindActivity = getActivity();
        headImg = (ImageView)view.findViewById(R.id.head_img);
        nickname = (TextView)view.findViewById(R.id.nickname);
        phone_number = (TextView) view.findViewById(R.id.phone_number);
        appointingView = (View) view.findViewById(R.id.appointing_tab);
        appointedView = (View) view.findViewById(R.id.appointed_tab);
        evaluateView = (View) view.findViewById(R.id.evaluate_tab);
        evaluationView = (View) view.findViewById(R.id.evaluation_tab);
        followView = (View) view.findViewById(R.id.follow_tab);
        helpView = (View) view.findViewById(R.id.help);
        opinionView = (View) view.findViewById(R.id.opinion);
        aboutView = (View) view.findViewById(R.id.about_mine);
        account_button = (Button) view.findViewById(R.id.account_button);

        SharedPreferences login_info = bindActivity.getSharedPreferences("login_info", MODE_PRIVATE);
        id = login_info.getInt("id", 0);
        nickname.setText(login_info.getString("nickname", ""));
        phone_number.setText(login_info.getString("phone_number", ""));
        Glide.with(mineView).load(imgUrl + login_info.getString("head_url", "")).into(headImg);
    }
}