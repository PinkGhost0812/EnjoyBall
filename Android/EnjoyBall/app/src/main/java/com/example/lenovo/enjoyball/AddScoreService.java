package com.example.lenovo.enjoyball;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AddScoreService extends IntentService {
    String url = Info.BASE_URL+"user?";
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
    protected void onHandleIntent( Intent intent) {
        int id = ((Info)getApplicationContext()).getUser().getUser_id();
        while (true){
            try {
                //每在线五分钟增加1积分
                Thread.sleep(1000*60*5);
                Log.e("tag","服务循环执行");
                sendToServer(id,1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private void sendToServer(int id, int score) {
        OkHttpClient client = new OkHttpClient();
        final Request request  = new Request.Builder()
                .url(url+id+"&&score="+score)
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
