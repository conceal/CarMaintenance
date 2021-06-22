package com.example.carmaintance.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.carmaintance.MyApplication;
import com.example.carmaintance.R;
import com.example.carmaintance.util.HttpUtil;
import com.example.carmaintance.util.TimeUtil;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StoreRegisterActivity extends BaseActivity implements View.OnClickListener {

    private MyApplication myApplication;
    private String ak;
    private String registerUrl;
    private String location = " ";
    private ImageView storeImg;
    private EditText account;
    private EditText password;
    private EditText phone_number;
    private EditText store_name;
    private EditText address;
    private Button register;
    private ImageView backImage;
    private String TAG = "StoreRegisterActivity";
    private String imagePath;
    private MyHandler handler = new MyHandler(this);
    private static final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpg");

    static class MyHandler extends Handler {

        WeakReference<StoreRegisterActivity> weakReference;
        public MyHandler(StoreRegisterActivity mActivity) {
            weakReference = new WeakReference<StoreRegisterActivity>(mActivity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            StoreRegisterActivity mActivity = weakReference.get();
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    mActivity.location = (String) msg.obj;
                    mActivity.register();
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_register);
        myApplication = (MyApplication) getApplication();
        ak = myApplication.getAk();
        registerUrl = myApplication.getBaseUrl() + "/store/register";


        storeImg = (ImageView) findViewById(R.id.register_store_img);
        account = (EditText) findViewById(R.id.register_store_account);
        password = (EditText) findViewById(R.id.register_store_password);
        phone_number = (EditText) findViewById(R.id.register_store_phone_number);
        store_name = (EditText) findViewById(R.id.register_store_name);
        address = (EditText) findViewById(R.id.register_store_address);
        register = (Button) findViewById(R.id.register_store_button);
        backImage = (ImageView) findViewById(R.id.back_button);

        storeImg.setOnClickListener(this);
        register.setOnClickListener(this);
        backImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_store_button:
                if (TextUtils.isEmpty(account.getText()) && TextUtils.isEmpty(password.getText())
                        && TextUtils.isEmpty(phone_number.getText()) && TextUtils.isEmpty(store_name.getText())
                        && TextUtils.isEmpty(address.getText())) {
                    Toast.makeText(StoreRegisterActivity.this, "请填写完整信息", Toast.LENGTH_SHORT).show();
                } else {
                    getLocation();
                }
                break;
            case R.id.back_button:
                finish();
                break;
            case R.id.register_store_img:
                requestPermission();
                break;
        }
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }else {
            openAlbum();
        }
    }

    private void openAlbum(){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, 1); //打开相册

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }else {
                    Toast.makeText(this,"你拒绝了该权限",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data != null){
            handleImageOnKitKat(data);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handleImageOnKitKat(Intent data){
        Uri uri = data.getData();
        if(DocumentsContract.isDocumentUri(this,uri)){
            //如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1];  //解析出数字格式的id
                String selection = MediaStore.Images.Media._ID+"="+id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public downloads"),Long.valueOf(docId));
                imagePath = getImagePath(contentUri,null);
            }
        }else if("content".equalsIgnoreCase(uri.getScheme())){
            //如果是file类型的Uri，直接获取图片路径即可
            imagePath = getImagePath(uri,null);
        }else if("file".equalsIgnoreCase(uri.getScheme())){
            //如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath); //根据图片路径显示图片
    }

    //将选择的图片Uri转换为路径
    private String getImagePath(Uri uri,String selection){
        String path = null;
        //通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if(cursor!= null){
            if(cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    //展示图片
    private void displayImage(String imagePath){
        if(imagePath !=null && !imagePath.equals("")){
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            storeImg.setImageBitmap(bitmap);
        }else {
            Toast.makeText(this,"获取图片失败1",Toast.LENGTH_SHORT).show();
        }
    }

    private void getLocation() {
        String store_address = address.getText().toString();
        String addressUrl = "http://api.map.baidu.com/geocoding/v3/?address="+ store_address + "&output=json&ak=" + ak +"&callback=showLocation";
        HttpUtil.sendOkHttpRequest(addressUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                String data = result.substring(27, result.length() - 1);
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    String lat = jsonObject.getJSONObject("result").getJSONObject("location").getString("lat");
                    String lng = jsonObject.getJSONObject("result").getJSONObject("location").getString("lng");
                    Message message = Message.obtain();
                    message.what = 1;
                    message.obj = lat + "," + lng;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void register() {
        RequestBody requestBody = new MultipartBody.Builder()
                    .addFormDataPart("store_url", imagePath.substring(imagePath.lastIndexOf("/")),
                            RequestBody.create(MEDIA_TYPE_JPG, new File(imagePath)))
                    .addFormDataPart("id", "0")
                    .addFormDataPart("account", account.getText().toString())
                    .addFormDataPart("password", password.getText().toString())
                    .addFormDataPart("phone_number", phone_number.getText().toString())
                    .addFormDataPart("store_name", store_name.getText().toString())
                    .addFormDataPart("store_address", address.getText().toString())
                    .addFormDataPart("register_time", TimeUtil.getCurrentTime())
                    .addFormDataPart("score", "0")
                    .addFormDataPart("trading_volume", "0")
                    .addFormDataPart("location", location)
                    .build();
            HttpUtil.postOkHttpRequest(registerUrl, requestBody, new Callback() {
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
                            if (result.equals("1")) {
                                if (result.equals("0")){
                                    Toast.makeText(myApplication, "账号已注册", Toast.LENGTH_SHORT).show();
                                } else if (result.equals("2")) {
                                    Toast.makeText(myApplication, "店铺已注册", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(myApplication, "注册成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                        }
                    });
                }
            });
    }


}