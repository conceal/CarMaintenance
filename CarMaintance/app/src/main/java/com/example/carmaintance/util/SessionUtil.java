package com.example.carmaintance.util;

import android.content.SharedPreferences;

import java.util.List;

import okhttp3.Headers;

public class SessionUtil {
    public static void getSession(Headers headers){

        List<String> cookies = headers.values("Set-Cookie");
        String session = cookies.get(0);
        // 取出cookie 保存在全局变量中
        ShareData.cookie = session.substring(0,session.indexOf(";"));
    }
}
