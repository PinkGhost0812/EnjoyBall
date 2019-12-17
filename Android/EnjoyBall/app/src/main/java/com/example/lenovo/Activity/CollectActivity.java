package com.example.lenovo.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lenovo.Adapter.CollectListviewAdapter;
import com.example.lenovo.enjoyball.Info;
import com.example.lenovo.enjoyball.R;
import com.example.lenovo.entity.News;
import com.example.lenovo.entity.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.mob.MobSDK.getContext;

public class CollectActivity extends AppCompatActivity {
    private OkHttpClient okHttpClient;
    String jsonstr = null;
    private User user ;
    private User author;
    private List<Map<String,Object>> datasource = null;
    private List<News> usercollection;
    private List<String> name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.nonetitle);
        setContentView(R.layout.activity_collect);

        EventBus.getDefault().register(this);

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");

        FindCollection(user.getUser_id());

    }

    private void FindCollection(int userid) {
        okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(Info.BASE_URL+"news/findByUserId?id="+userid)
                .build();


        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                usercollection = new ArrayList<>();
                jsonstr = response.body().string();
                Log.e("mes",jsonstr);
                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd hh:mm:ss")
                        .setPrettyPrinting()
                        .serializeNulls()
                        .create();
                Type listType = new TypeToken<List<News>>(){}.getType();
                usercollection = gson.fromJson(jsonstr,listType);
                EventBus.getDefault().post("收藏信息已收到");
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void inits(String msg){
        switch (msg){
            case "收藏信息已收到":
                for(int i=0;i<usercollection.size();++i){
                    FindName(usercollection.get(i).getNews_author(),i);
                }
                break;
            case "作者名字已查到":
                init();
                Log.e("mes1","haha");
                final CollectListviewAdapter collectListviewAdapter = new CollectListviewAdapter(
                        this,
                        datasource,
                        R.layout.collect_item
                );
                ListView listView = findViewById(R.id.lv_collect_news);
                listView.setAdapter(collectListviewAdapter);

                TextView tv_search = findViewById(R.id.tv_collect_edit);
                final EditText ed = findViewById(R.id.et_collect_search);
                tv_search.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String context = ed.getText().toString();
                        List<Map<String,Object>> datasource1 = new ArrayList<>();
                        for(int i=0;i<usercollection.size();++i){
                            if(usercollection.get(i).getNews_title().indexOf(context)!=-1){
                                Map<String,Object> map = new HashMap<>();
                                map.put("heads",usercollection.get(i).getNews_title());
                                map.put("authors",name.get(i));
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                map.put("times",simpleDateFormat.format(usercollection.get(i).getNews_time()));
                                map.put("imgs",usercollection.get(i).getNews_image());
                                datasource1.add(map);
                            }
                        }
                        CollectListviewAdapter collectListviewAdapter1= new CollectListviewAdapter(
                                getApplicationContext(),
                                datasource1,
                                R.layout.collect_item
                        );
                        ListView listView = findViewById(R.id.lv_collect_news);
                        listView.setAdapter(collectListviewAdapter1);

                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent();
                        intent.setClass(CollectActivity.this, NewsDetailActivity.class);
                        intent.putExtra("id",usercollection.get(position).getNews_id().toString());
                        startActivity(intent);
                    }
                });
        }
    }

    private void FindName(int userid, final int num) {
        okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(Info.BASE_URL+"user/find?id="+userid)
                .build();


        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                name = new ArrayList<>();
                jsonstr = response.body().string();
                Log.e("mes",jsonstr);
                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd hh:mm:ss")
                        .setPrettyPrinting()
                        .serializeNulls()
                        .create();
                author = gson.fromJson(jsonstr,User.class);
                name.add(author.getUser_nickname());
                if(num==usercollection.size()-1)
                    EventBus.getDefault().post("作者名字已查到");
            }
        });
    }

    private void init() {
        datasource = new ArrayList<>();
        for(int i=0;i<usercollection.size();++i){
            Map<String,Object> map = new HashMap<>();
            map.put("heads",usercollection.get(i).getNews_title());
            map.put("authors",name.get(i));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            map.put("times",simpleDateFormat.format(usercollection.get(i).getNews_time()));
            map.put("imgs",usercollection.get(i).getNews_image());
            datasource.add(map);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
