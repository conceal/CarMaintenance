package com.example.carmaintance.storeMainPage;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.carmaintance.R;
import com.example.carmaintance.activity.LoginActivity;

public class StoreMainActivity extends FragmentActivity implements View.OnClickListener {
    private StoreProgramFragment page1;
    private SendFragment page2;
    private StoreAppointmentFragment page3;
    private View bindingView;

    private View protectTab;
    private View sendTab;
    private View orderTab;

    private ImageView protectImg;
    private ImageView sendImg;
    private ImageView orderImg;
    private ImageView storeBack;

    private TextView protectText;
    private TextView sendText;
    private TextView orderText;
    private TextView storeTitle;

    private int selectTabId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_main);
        initView();
        setTabSelected(0);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    private void initView() {
        protectTab = findViewById(R.id.info_tab);
        sendTab = findViewById(R.id.type_tab);
        orderTab = findViewById(R.id.store_tab);

        protectImg = (ImageView) findViewById(R.id.info_img);
        sendImg = (ImageView) findViewById(R.id.type_img);
        orderImg = (ImageView) findViewById(R.id.store_img);
        storeBack = (ImageView) findViewById(R.id.store_back);

        protectText = (TextView) findViewById(R.id.info_text);
        sendText = (TextView) findViewById(R.id.type_text);
        orderText = (TextView) findViewById(R.id.store_text);
        storeTitle = (TextView) findViewById(R.id.store_title);

        bindingView = (View) findViewById(R.id.bindingView);

        SharedPreferences preferences = getSharedPreferences("store_info", MODE_PRIVATE);

        storeTitle.setText(preferences.getString("store_name", "保养店"));
        protectImg.setColorFilter(Color.RED);
        sendImg.setColorFilter(Color.GRAY);
        orderImg.setColorFilter(Color.GRAY);

        protectText.setTextColor(Color.RED);
        sendText.setTextColor(Color.GRAY);
        orderText.setTextColor(Color.GRAY);

        protectTab.setOnClickListener(this);
        sendTab.setOnClickListener(this);
        orderTab.setOnClickListener(this);
        storeBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.info_tab:
                selectTabId = 0;
                protectImg.setColorFilter(Color.RED);
                sendImg.setColorFilter(Color.GRAY);
                orderImg.setColorFilter(Color.GRAY);

                protectText.setTextColor(Color.RED);
                sendText.setTextColor(Color.GRAY);
                orderText.setTextColor(Color.GRAY);
                setTabSelected(0);
                break;
            case R.id.type_tab:
                selectTabId = 1;
                protectImg.setColorFilter(Color.GRAY);
                sendImg.setColorFilter(Color.RED);
                orderImg.setColorFilter(Color.GRAY);
                protectText.setTextColor(Color.GRAY);
                sendText.setTextColor(Color.RED);
                orderText.setTextColor(Color.GRAY);
                setTabSelected(1);
                break;
            case R.id.store_tab:
                selectTabId = 2;
                protectImg.setColorFilter(Color.GRAY);
                sendImg.setColorFilter(Color.GRAY);
                orderImg.setColorFilter(Color.RED);
                protectText.setTextColor(Color.GRAY);
                sendText.setTextColor(Color.GRAY);
                orderText.setTextColor(Color.RED);
                setTabSelected(2);
                break;
            case R.id.store_back:
                SharedPreferences preferences = getSharedPreferences("store_info", MODE_PRIVATE);
                preferences.edit().clear().commit();
                Intent loginIntent = new Intent(this, LoginActivity.class);
                finish();
                startActivity(loginIntent);
                break;
            default:
                break;
        }
    }

    private void setTabSelected (int index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideFragment(transaction);
        switch (index) {
            case 0:
                if (page1 == null) {
                    page1 = new StoreProgramFragment();
                    transaction.add(R.id.container, page1);
                } else {
                    transaction.show(page1);
                }
                break;
            case 1:
                if (page2 == null) {
                    page2 = new SendFragment();
                    transaction.add(R.id.container, page2);
                } else {
                    transaction.show(page2);
                }
                break;
            case 2:
                if (page3 == null) {
                    page3 = new StoreAppointmentFragment();
                    transaction.add(R.id.container, page3);
                } else {
                    transaction.show(page3);
                }
                break;
            default:
                break;
        }
        transaction.commit();

    }

    private void hideFragment(FragmentTransaction transaction) {
        if (page1 != null) {
            transaction.hide(page1);
        }
        if (page2 != null) {
            transaction.hide(page2);
        }
        if (page3 != null) {
            transaction.hide(page3);
        }

    }
}

