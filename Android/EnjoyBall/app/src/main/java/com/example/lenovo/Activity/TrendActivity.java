package com.example.lenovo.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.lenovo.Adapter.TrendAdapter;
import com.example.lenovo.enjoyball.Info;
import com.example.lenovo.enjoyball.R;
import com.example.lenovo.entity.PYQ;
import com.example.lenovo.entity.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TrendActivity extends AppCompatActivity {
    private ListView lv_trend = null;
    private TrendAdapter adapter;
    private List<PYQ> dataSource = new ArrayList<PYQ>();
    private Button btn_createTrend;
    private String url = Info.BASE_URL + "user/pyq?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.nonetitle);
        setContentView(R.layout.activity_trend);
        EventBus.getDefault().register(this);
        getView();
        getData();
        adapter = new TrendAdapter(dataSource,R.layout.listview_item_trend,this);
        lv_trend.setAdapter(adapter);
    }

    private void getData() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("pyq","获取动态失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                Type type = new TypeToken<List<PYQ>>(){}.getType();
                List<PYQ> trends = new Gson().fromJson(json,type);
                dataSource = trends;
                Message message = new Message();
                message.what = 1;
                EventBus.getDefault().post(message);
            }
        });

    }

    private void getView() {
        lv_trend = findViewById(R.id.lv_trend);
        lv_trend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TrendActivity.this,TrendDetailActivity.class);
            }
        });
        //点击发布跳转到发布界面
        btn_createTrend = findViewById(R.id.btn_trend_createTrend);
        btn_createTrend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrendActivity.this, AnnounceActivity.class);
                startActivity(intent);
            }
        });
    }
    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void setAdapter(Message message) {
        switch (message.what){
            case 1:
                adapter = new TrendAdapter(dataSource,R.layout.listview_item_trend,this);
                lv_trend.setAdapter(adapter);
                break;

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
