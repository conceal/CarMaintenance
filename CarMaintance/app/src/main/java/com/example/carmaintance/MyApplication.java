package com.example.carmaintance;

import android.app.Application;

public class MyApplication extends Application {

    public static final String ip = "192.168.43.38";
    private static final String baseAk = "MsW26RdbA419IRraQdPxiP2uN2aGTvpR";
    private String baseUrl;
    private String imgUrl;
    private String ak;
    private String cookie;

    @Override
    public void onCreate()
    {
        super.onCreate();
        setBaseUrl(ip);
        setImgUrl(ip);
        setAk(baseAk);
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String ip) {
        this.baseUrl = "http://" + ip + ":8080";
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = "http://" + ip + ":8081/img/";
    }

    public void setAk(String ak) {
        this.ak = ak;
    }

    public  String getAk() {
        return ak;
    }
}
