package com.example.carmaintance.storeMainPage;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.carmaintance.MyApplication;
import com.example.carmaintance.R;
import com.example.carmaintance.userPage.BaseFragment;
import com.example.carmaintance.util.HttpUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

public class SendFragment extends BaseFragment implements View.OnClickListener{
    public static final int HEAD_IMAGE = 1;
    public static final int DETAILS_IMAGE = 2;
    private static final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpg");

    private FloatingActionButton floatingActionButton;
    private ImageView programImg;
    private ImageView detailsImg;
    private LinearLayout linear_head;
    private LinearLayout linear_detail;
    private EditText publicProgramName;
    private EditText publicProgramPrice;
    private View view;
    private Activity mActivity;
    private String imagePath = null;

    private String imagePath1;
    private String imagePath2;
    private MyApplication myApplication;
    private String url;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_send, container, false);
        mActivity = getActivity();
        myApplication = (MyApplication) mActivity.getApplication();
        url = myApplication.getBaseUrl() + "/program/releaseProgram";

        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.public_float);
        programImg = (ImageView)view.findViewById(R.id.img_head_pick);
        linear_head = (LinearLayout)view.findViewById(R.id.linear_head);
        detailsImg = (ImageView)view.findViewById(R.id.img_detail_pick);
        linear_detail = (LinearLayout)view.findViewById(R.id.linear_detail);
        publicProgramName = (EditText) view.findViewById(R.id.public_program_name);
        publicProgramPrice = (EditText) view.findViewById(R.id.public_program_price);
        linear_head.setOnClickListener(this);
        linear_detail.setOnClickListener(this);
        floatingActionButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linear_head:
                requestPermission(HEAD_IMAGE);
                break;
            case R.id.linear_detail:
                requestPermission(DETAILS_IMAGE);
                break;
            case R.id.public_float:
                if (programImg.getDrawable() == null || detailsImg == null || TextUtils.isEmpty(publicProgramName.getText().toString())
                || TextUtils.isEmpty(publicProgramPrice.getText().toString())) {
                    Toast.makeText(mActivity, "请填写完整信息", Toast.LENGTH_SHORT).show();
                } else {
                    releaseProgram();
                }
        }
    }

    private void requestPermission(int type) {
        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }else {
            openAlbum(type);
        }
    }

    private void openAlbum(int type){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        switch (type) {
            case HEAD_IMAGE:
                startActivityForResult(intent, HEAD_IMAGE); //打开相册
                break;
            case DETAILS_IMAGE:
                startActivityForResult(intent, DETAILS_IMAGE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openAlbum(requestCode);
                }else {
                    Toast.makeText(mActivity,"你拒绝了该权限",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK && data != null){
                handleImageOnKitKat(data, requestCode);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handleImageOnKitKat(Intent data, int type){
        Uri uri = data.getData();
        if(DocumentsContract.isDocumentUri(mActivity,uri)){
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
        displayImage(imagePath, type); //根据图片路径显示图片
    }

    //将选择的图片Uri转换为路径
    private String getImagePath(Uri uri,String selection){
        String path = null;
        //通过Uri和selection来获取真实的图片路径
        Cursor cursor = mActivity.getContentResolver().query(uri,null,selection,null,null);
        if(cursor!= null){
            if(cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    //展示图片
    private void displayImage(String imagePath, int type){
        if(imagePath !=null && !imagePath.equals("")){
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            switch (type) {
                case HEAD_IMAGE:
                    programImg.setImageBitmap(bitmap);
                    imagePath1 = imagePath;
                    break;
                case DETAILS_IMAGE:
                    detailsImg.setImageBitmap(bitmap);
                    imagePath2 = imagePath;
                    break;
            }

        }else {
            Toast.makeText(mActivity,"获取图片失败1",Toast.LENGTH_SHORT).show();
        }
    }

    private void releaseProgram() {
        SharedPreferences preferences = mActivity.getSharedPreferences("store_info", Context.MODE_PRIVATE);
        int store_id = preferences.getInt("id", 0);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("program_name", publicProgramName.getText().toString())
                .addFormDataPart("price", publicProgramPrice.getText().toString())
                .addFormDataPart("store_id", String.valueOf(store_id))
                .addFormDataPart("programImg", imagePath1.substring(imagePath.lastIndexOf("/")),
                        RequestBody.create(MEDIA_TYPE_JPG, new File(imagePath1)))
                .addFormDataPart("detailsImg", imagePath2.substring(imagePath2.lastIndexOf("/")),
                        RequestBody.create(MEDIA_TYPE_JPG, new File(imagePath2)))
                .build();
        HttpUtil.postOkHttpRequest(url, requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("发布评论失败", Log.getStackTraceString(e));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.e("upload", result);
                if (result.equals("1")) {
                    showDialog("发布成功", result);
                }else {
                    showDialog("发布失败", "0");
                }

            }
        });
    }

    private void showDialog(String message, String result) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder dialog = new AlertDialog.Builder(mActivity);
                dialog.setMessage(message);
                dialog.setCancelable(false);    //设置是否可以通过点击对话框外区域或者返回按键关闭对话框
                //点击确定，修改数据库状态/更新RecyclerView
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                //点击取消，什么也不做
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
                if (result.equals("1")) {
                    programImg.setImageResource(R.drawable.photo);
                    detailsImg.setImageResource(R.drawable.photo);
                    publicProgramName.setText("");
                    publicProgramPrice.setText("");
                }
            }
        });
    }

}
