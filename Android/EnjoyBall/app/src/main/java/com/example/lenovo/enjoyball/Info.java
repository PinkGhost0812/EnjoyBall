package com.example.lenovo.enjoyball;

import android.app.Application;
import android.util.Log;

import com.example.lenovo.entity.*;

import java.io.IOException;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Info extends Application{



    public static final String BASE_URL = "http://192.168.2.102:8080/EnjoyBallServer/";

    public static String registrationId ;
    private long startTime = 0;//应用启动的时间
    private long finalTime = 0;//应用关闭的时间
    private long useTime = 0;//应用的使用时间
    private long minTime = 10*1000;//增加积分的最小使用时间：五分钟



    @Override
    public void onCreate() {
        super.onCreate();
        startTime = System.currentTimeMillis();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

//        String registrationId = JPushInterface.getRegistrationID(this);
        Log.e("1099", "run:--------->registrationId： "+registrationId );

        registrationId = JPushInterface.getRegistrationID(this);
        Log.e("1099","id"+registrationId);

    }

    //当前登录的用户的信息
    private User user;
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    //当前用户头像存储地址
//    public static final String HEADPORTRAIT_URL= "/data/user/0/com.example.lenovo.enjoyball/files/HeadPortrait.jpg";

    @Override
    public void onTerminate() {
        super.onTerminate();
        finalTime = System.currentTimeMillis();
        useTime = finalTime - startTime;
        if (useTime>minTime){
            sendToServer(useTime);
        }

    }

    private void sendToServer(long useTime) {
        OkHttpClient client = new OkHttpClient();
        final Request request  = new Request.Builder()
                .url(BASE_URL +"user?id="+getUser().getUser_id()+"&&addScore="+useTime/(10*1000))
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }


}
