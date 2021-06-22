package com.example.carmaintance.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.se.omapi.Session;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carmaintance.R;
import com.example.carmaintance.MyApplication;
import com.example.carmaintance.adminPage.AdminMainActivity;
import com.example.carmaintance.bean.Admin;
import com.example.carmaintance.bean.Store;
import com.example.carmaintance.bean.User;
import com.example.carmaintance.storeMainPage.StoreMainActivity;
import com.example.carmaintance.util.HttpUtil;
import com.example.carmaintance.util.SessionUtil;
import com.example.carmaintance.util.ShareData;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;


public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText accountText;
    private EditText passwordText;
    private Button loginButton;
    private TextView registerText;
    private MyApplication application;
    private RadioButton carUser;
    private RadioButton storeKeeper;
    private RadioButton admin;

    private String userUrl;
    private String storeUrl;
    private String adminUrl;
    private String TAG = "LoginActivity";
    private String role = "角色";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        application = (MyApplication) getApplication();
        userUrl = application.getBaseUrl() + "/user/login";
        storeUrl = application.getBaseUrl() + "/store/login";
        adminUrl = application.getBaseUrl() + "/admin/login";
        accountText = (EditText) findViewById(R.id.account);
        passwordText = (EditText) findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.login_button);
        registerText = (TextView) findViewById(R.id.register_text);
        carUser = (RadioButton) findViewById(R.id.carUser);
        storeKeeper = (RadioButton) findViewById(R.id.storeKeeper);
        admin = (RadioButton) findViewById(R.id.admin);

        loginButton.setOnClickListener(this);
        registerText.setOnClickListener(this);
        carUser.setOnClickListener(this);
        storeKeeper.setOnClickListener(this);
        admin.setOnClickListener(this);
    }

    private void initView() {
        SharedPreferences userPreferences = getSharedPreferences("login_info", MODE_PRIVATE);
        if (userPreferences.getInt("id", 0) > 0) {
            Intent intent = new Intent(this, UserMainActivity.class);
            startActivity(intent);
            finish();
        }
        SharedPreferences storePreferences = getSharedPreferences("store_info", MODE_PRIVATE);
        if (storePreferences.getInt("id", 0) > 0) {
            Intent intent = new Intent(this, StoreMainActivity.class);
            startActivity(intent);
            finish();
        }
        SharedPreferences adminPreferences = getSharedPreferences("admin_info", MODE_PRIVATE);
        if (adminPreferences.getInt("id", 0) > 0) {
            Intent intent = new Intent(this, AdminMainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.carUser:
                carUser.setChecked(true);
                carUser.setTextColor(Color.RED);
                storeKeeper.setChecked(false);
                storeKeeper.setTextColor(Color.GRAY);
                admin.setChecked(false);
                admin.setTextColor(Color.GRAY);
                role = "车主";
                break;
            case R.id.storeKeeper:
                carUser.setChecked(false);
                carUser.setTextColor(Color.GRAY);
                storeKeeper.setChecked(true);
                storeKeeper.setTextColor(Color.RED);
                admin.setChecked(false);
                admin.setTextColor(Color.GRAY);
                role = "店主";
                break;
            case R.id.admin:
                carUser.setChecked(false);
                carUser.setTextColor(Color.GRAY);
                storeKeeper.setChecked(false);
                storeKeeper.setTextColor(Color.GRAY);
                admin.setChecked(true);
                admin.setTextColor(Color.RED);
                role = "管理员";
                break;
            case R.id.login_button:
                if (!TextUtils.isEmpty(accountText.getText()) && !TextUtils.isEmpty(passwordText.getText()))
                    if (!role.equals("角色")) {
                        if (role.equals("车主")) userLogin();
                        else if (role.equals("店主")) storeLogin();
                        else adminLogin();
                    } else {
                        Toast.makeText(this, "请选择角色", Toast.LENGTH_SHORT).show();
                    }

                else
                    Toast.makeText(this, "请输入账号密码", Toast.LENGTH_SHORT).show();
                break;
            case R.id.register_text:
                if (!role.equals("角色")) {
                    if (role.equals("车主")) {
                        Intent intent = new Intent(this, UserRegisterActivity.class);
                        startActivity(intent);
                    } else if (role.equals("店主")){
                        Intent intent = new Intent(this, StoreRegisterActivity.class);
                        startActivity(intent);
                    } else {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                        dialog.setMessage("不能注册管理员");
                        dialog.setCancelable(true);
                        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        dialog.show();
                    }

                } else {
                    Toast.makeText(this, "请选择角色", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    private void userLogin() {
        RequestBody requestBody = new FormBody.Builder()
                .add("account", accountText.getText().toString())
                .build();
        HttpUtil.postOkHttpRequest(userUrl, requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
               Log.e(TAG, Log.getStackTraceString(e));
               Log.e(TAG, "请求失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.e(TAG, result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        User user = gson.fromJson(result, User.class);
//                        Log.e(TAG, user.getPassword());
                        if (user != null && user.getPassword().equals(passwordText.getText().toString())) {
                            SharedPreferences preferences = getSharedPreferences("login_info", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putInt("id", user.getId());
                            editor.putString("account", user.getAccount());
                            editor.putString("password", user.getPassword());
                            editor.putString("nickname", user.getNickname());
                            editor.putString("head_url", user.getHead_url());
                            editor.putString("phone_number", user.getPhone_number());
                            editor.putString("car_brand", user.getCar_brand());
                            editor.putString("register_time", user.getRegister_time());
                            boolean commit = editor.commit();
                            Log.i(TAG, "userLogin: commitRet = " + commit);
                            Intent intent = new Intent(LoginActivity.this, UserMainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "账号密码不正确", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void storeLogin() {
        RequestBody requestBody = new FormBody.Builder()
                .add("account", accountText.getText().toString())
                .build();
        HttpUtil.postOkHttpRequest(storeUrl, requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, Log.getStackTraceString(e));
                Log.e(TAG, "请求失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.e(TAG, result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        Store store = gson.fromJson(result, Store.class);
                        Log.e(TAG, store.getPassword());
                        String password = passwordText.getText().toString();
                        if (store != null && store.getPassword().equals(password)) {
                            SharedPreferences preferences = getSharedPreferences("store_info", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putInt("id", store.getId());
                            editor.putString("account", store.getAccount());
                            editor.putString("password", store.getPassword());
                            editor.putString("phone_number", store.getPhone_number());
                            editor.putString("store_name", store.getStore_name());
                            editor.putString("store_address", store.getStore_address());
                            editor.putString("store_url", store.getStore_url());
                            editor.putString("register_time", store.getRegister_time());
                            editor.putInt("trading_volume", store.getTrading_volume());
                            boolean commit = editor.commit();
                            Intent intent = new Intent(LoginActivity.this, StoreMainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "账号密码不正确", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void adminLogin() {
        RequestBody requestBody = new FormBody.Builder()
                .add("account", accountText.getText().toString())
                .add("password", passwordText.getText().toString())
                .build();
        HttpUtil.postOkHttpRequest(adminUrl, requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.e(TAG, result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        Admin admin = gson.fromJson(result, Admin.class);
                        if (admin != null && admin.getPassword().equals(passwordText.getText().toString())) {
                            SharedPreferences preferences = getSharedPreferences("admin_info", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putInt("id", admin.getId());
                            editor.putString("account", admin.getAccount());
                            editor.putString("password", admin.getPassword());
                            boolean commit = editor.commit();
                            Intent intent = new Intent(LoginActivity.this, AdminMainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "账号密码不正确", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}