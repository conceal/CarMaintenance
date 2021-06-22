package com.example.carmaintance.activity;

import android.Manifest;
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
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.carmaintance.MyApplication;
import com.example.carmaintance.R;
import com.example.carmaintance.util.HttpUtil;
import com.example.carmaintance.util.TimeUtil;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserRegisterActivity extends BaseActivity implements View.OnClickListener {

    private EditText acount;
    private EditText password;
    private EditText phone_number;
    private EditText nickname;
    private EditText brand;
    private Button register;
    private ImageView backImage;
    private ImageView userImg;
    private MyApplication application;
    private String url;
    private String TAG = "UserRegisterActivity";
    private String imagePath;
    private static final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpg");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_rigister);
        application = (MyApplication) getApplication();
        url = application.getBaseUrl() + "/user/register";

        acount = (EditText) findViewById(R.id.register_account);
        password = (EditText) findViewById(R.id.register_password);
        phone_number = (EditText) findViewById(R.id.register_phone_number);
        nickname = (EditText) findViewById(R.id.register_nickname);
        brand = (EditText) findViewById(R.id.register_brand);
        register = (Button) findViewById(R.id.register_button);
        backImage = (ImageView) findViewById(R.id.back_button);
        userImg = (ImageView) findViewById(R.id.register_user_img);

        userImg.setOnClickListener(this);
        register.setOnClickListener(this);
        backImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_button:
                if (TextUtils.isEmpty(acount.getText()) && TextUtils.isEmpty(password.getText())
                        && TextUtils.isEmpty(phone_number.getText()) && TextUtils.isEmpty(nickname.getText())
                        && TextUtils.isEmpty(brand.getText())) {
                    Toast.makeText(UserRegisterActivity.this, "请填写完整信息", Toast.LENGTH_SHORT).show();
                } else {
                    register();
                }
                break;
            case R.id.back_button:
                finish();
                break;
            case R.id.register_user_img:
                requestPermission();
                return;
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
            userImg.setImageBitmap(bitmap);
        }else {
            Toast.makeText(this,"获取图片失败1",Toast.LENGTH_SHORT).show();
        }
    }

    private void register() {
        RequestBody requestBody = new MultipartBody.Builder()
                .addFormDataPart("head_url", imagePath.substring(imagePath.lastIndexOf("/")),
                        RequestBody.create(MEDIA_TYPE_JPG, new File(imagePath)))
                .addFormDataPart("id", "0")
                .addFormDataPart("account", acount.getText().toString())
                .addFormDataPart("password", password.getText().toString())
                .addFormDataPart("nickname", nickname.getText().toString())
                .addFormDataPart("phone_number", phone_number.getText().toString())
                .addFormDataPart("car_brand", brand.getText().toString())
                .addFormDataPart("register_time", TimeUtil.getCurrentTime())
                .build();
        HttpUtil.postOkHttpRequest(url, requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "请求失败");
                Log.e(TAG, Log.getStackTraceString(e));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (result.equals("0")){
                            Toast.makeText(application, "账号已注册", Toast.LENGTH_SHORT).show();
                        } else if (result.equals("2")) {
                            Toast.makeText(application, "昵称已注册", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(application, "注册成功", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
            }
        });
    }
}