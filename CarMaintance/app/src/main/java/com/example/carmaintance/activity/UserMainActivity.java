package com.example.carmaintance.activity;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.carmaintance.R;
import com.example.carmaintance.userPage.maintenanceInfoPage.maintenanceInfoFragment;
import com.example.carmaintance.userPage.minePage.MineFragment;
import com.example.carmaintance.userPage.storePage.StoreFragment;
import com.example.carmaintance.userPage.typePage.TypeFragment;

public class UserMainActivity extends FragmentActivity implements View.OnClickListener {

    private maintenanceInfoFragment page1;
    private TypeFragment page2;
    private StoreFragment page3;
    private MineFragment page4;

    private View infoTab;
    private View typeTab;
    private View storeTab;
    private View mineTab;
    private View bindingView;

    private ImageView infoImg;
    private ImageView typeImg;
    private ImageView storeImg;
    private ImageView mineImg;
    private TextView infoText;
    private TextView typeText;
    private TextView storeText;
    private TextView mineText;
    private int selectTabId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
        initView();
        setTabSelected(0);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    private void initView() {
        infoTab = findViewById(R.id.info_tab);
        typeTab = findViewById(R.id.type_tab);
        storeTab = findViewById(R.id.store_tab);
        mineTab = findViewById(R.id.mine_tab);

        infoImg = (ImageView) findViewById(R.id.info_img);
        typeImg = (ImageView) findViewById(R.id.type_img);
        storeImg = (ImageView) findViewById(R.id.store_img);
        mineImg = (ImageView) findViewById(R.id.mine_img);
        infoText = (TextView) findViewById(R.id.info_text);
        typeText = (TextView) findViewById(R.id.type_text);
        storeText = (TextView) findViewById(R.id.store_text);
        mineText = (TextView) findViewById(R.id.mine_text);
        bindingView = (View) findViewById(R.id.bindingView);

        infoImg.setColorFilter(Color.RED);
        typeImg.setColorFilter(Color.GRAY);
        storeImg.setColorFilter(Color.GRAY);
        mineImg.setColorFilter(Color.GRAY);
        infoText.setTextColor(Color.RED);
        typeText.setTextColor(Color.GRAY);
        storeText.setTextColor(Color.GRAY);
        mineText.setTextColor(Color.GRAY);

        infoTab.setOnClickListener(this);
        typeTab.setOnClickListener(this);
        storeTab.setOnClickListener(this);
        mineTab.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.info_tab:
                selectTabId = 0;
                infoImg.setColorFilter(Color.RED);
                typeImg.setColorFilter(Color.GRAY);
                storeImg.setColorFilter(Color.GRAY);
                mineImg.setColorFilter(Color.GRAY);
                infoText.setTextColor(Color.RED);
                typeText.setTextColor(Color.GRAY);
                storeText.setTextColor(Color.GRAY);
                mineText.setTextColor(Color.GRAY);
                setTabSelected(0);
                break;
            case R.id.type_tab:
                selectTabId = 1;
                infoImg.setColorFilter(Color.GRAY);
                typeImg.setColorFilter(Color.RED);
                storeImg.setColorFilter(Color.GRAY);
                mineImg.setColorFilter(Color.GRAY);
                infoText.setTextColor(Color.GRAY);
                typeText.setTextColor(Color.RED);
                storeText.setTextColor(Color.GRAY);
                mineText.setTextColor(Color.GRAY);
                setTabSelected(1);
                break;
            case R.id.store_tab:
                selectTabId = 2;
                infoImg.setColorFilter(Color.GRAY);
                typeImg.setColorFilter(Color.GRAY);
                storeImg.setColorFilter(Color.RED);
                mineImg.setColorFilter(Color.GRAY);
                infoText.setTextColor(Color.GRAY);
                typeText.setTextColor(Color.GRAY);
                storeText.setTextColor(Color.RED);
                mineText.setTextColor(Color.GRAY);
                setTabSelected(2);
                break;
            case R.id.mine_tab:
                selectTabId = 3;
                infoImg.setColorFilter(Color.GRAY);
                typeImg.setColorFilter(Color.GRAY);
                storeImg.setColorFilter(Color.GRAY);
                mineImg.setColorFilter(Color.RED);
                infoText.setTextColor(Color.GRAY);
                typeText.setTextColor(Color.GRAY);
                storeText.setTextColor(Color.GRAY);
                mineText.setTextColor(Color.RED);
                setTabSelected(3);
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
                    page1 = new maintenanceInfoFragment();
                    transaction.add(R.id.container, page1);
                } else {
                    transaction.show(page1);
                }
                break;
            case 1:
                if (page2 == null) {
                    page2 = new TypeFragment();
                    transaction.add(R.id.container, page2);
                } else {
                    transaction.show(page2);
                }
                break;
            case 2:
                if (page3 == null) {
                    page3 = new StoreFragment();
                    transaction.add(R.id.container, page3);
                } else {
                    transaction.show(page3);
                }
                break;
            case 3:
                if (page4 == null) {
                    page4 = new MineFragment();
                    transaction.add(R.id.container, page4);
                } else {
                    transaction.show(page4);
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
        if (page4 != null) {
            transaction.hide(page4);
        }
    }
}

