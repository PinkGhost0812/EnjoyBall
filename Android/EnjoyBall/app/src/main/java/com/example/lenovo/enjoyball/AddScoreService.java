package com.example.lenovo.enjoyball;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AddScoreService extends IntentService {
    String url = Info.BASE_URL+"user/addScore?";
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public AddScoreService(String name) {
        super(name);
    }
    public AddScoreService(){
        super("AddScoreService");

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("service", "service test OK");
    }

    @Override
    protected void onHandleIntent( Intent intent) {
        int id = ((Info)getApplicationContext()).getUser().getUser_id();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String signInTime = sdf.format(new Date());
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("signinTime", Context.MODE_PRIVATE);
        if (!signInTime.equals(sharedPreferences.getString("lastSignInTime",""))){
            ((Info)getApplication()).setOnlineAddScoreFrequency(0);
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lastSignInTime",signInTime);
        editor.apply();
        int frequency =  ((Info)getApplication()).getOnlineAddScoreFrequency();
        while (true){
            if (frequency<3){
                try {
                    //每在线10分钟增加3积分
                    Thread.sleep(60*1000*10);
                    Log.e("tag","服务循环执行");
                    sendToServer(id,3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ((Info)getApplication()).setOnlineAddScoreFrequency(frequency+1);
            }
            else {
                break;
            }


        }

    }

    private void sendToServer(int id, int score) {
        OkHttpClient client = new OkHttpClient();
        final Request request  = new Request.Builder()
                .url(url+"id=" + id + "&&score=" + score)
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
