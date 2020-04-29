package com.example.lenovo.Activity;

import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lenovo.Adapter.CollectListviewAdapter;
import com.example.lenovo.Util.NewsAndAuthor;
import com.example.lenovo.enjoyball.Info;
import com.example.lenovo.enjoyball.R;
import com.example.lenovo.entity.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

public class CollectActivity extends AppCompatActivity {

    private OkHttpClient okHttpClient;

    private final int TAG_MESSAGE_LIST_NEWSANDAUTHOR=456;
    private String jsonStr = null;
    private User user;
    private List<Map<String, Object>> datasource = null;
    private List<NewsAndAuthor> collection;
    private CollectListviewAdapter collectListviewAdapter;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.nonetitle);
        setContentView(R.layout.activity_collect);

        EventBus.getDefault().register(this);

        listView = findViewById(R.id.lv_collect_news);

        Intent intent = getIntent();

        user = (User) intent.getSerializableExtra("user");

        findCollection(user.getUser_id());

        TextView tv_search = findViewById(R.id.tv_collect_edit);
        final EditText ed = findViewById(R.id.et_collect_search);
        tv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String context = ed.getText().toString();
                List<Map<String, Object>> datasource1 = new ArrayList<>();
                for (int i = 0; i < collection.size(); ++i) {
                    if (collection.get(i).getNews().getNews_title().indexOf(context) != -1) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("newsList", collection.get(i).getNews());
                        map.put("authorList", collection.get(i).getAuthor());
                        datasource1.add(map);
                    }
                }
                collectListviewAdapter = new CollectListviewAdapter(
                        getApplicationContext(),
                        datasource1,
                        R.layout.collect_item
                );
                ListView listView = findViewById(R.id.lv_collect_news);
                listView.setAdapter(collectListviewAdapter);

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(CollectActivity.this, NewsDetailActivity.class);
                intent.putExtra("id", collection.get(position).getNews().getNews_id().toString());
                startActivity(intent);
            }
        });

    }

    private void findCollection(int userid) {
        okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(Info.BASE_URL + "news/findByUserId?id=" + userid)
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                collection = new ArrayList<>();
                jsonStr = response.body().string();
                Log.e("mes", jsonStr);
                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd hh:mm:ss")
                        .setPrettyPrinting()
                        .serializeNulls()
                        .create();
                Type listType = new TypeToken<List<NewsAndAuthor>>() {
                }.getType();
                collection = gson.fromJson(jsonStr, listType);
                Log.e("test collection tltle", collection.get(2).getNews().getNews_title());
                Log.e("test collection antuor", collection.get(2).getAuthor().getUser_nickname());
                Message msg = new Message();
                msg.obj = collection;
                msg.what = TAG_MESSAGE_LIST_NEWSANDAUTHOR;
                EventBus.getDefault().post(msg);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void detailMsg(Message msg) {
        switch (msg.what) {
            case TAG_MESSAGE_LIST_NEWSANDAUTHOR:

                List<NewsAndAuthor> newsAndAuthorList = (List<NewsAndAuthor>) msg.obj;
                initData(newsAndAuthorList);
                collectListviewAdapter = new CollectListviewAdapter(
                        this,
                        datasource,
                        R.layout.collect_item
                );
                listView.setAdapter(collectListviewAdapter);

                break;
        }
    }


    private void initData(List<NewsAndAuthor> newsAndAuthorList) {
        datasource = new ArrayList<>();
        for (int i = 0; i < newsAndAuthorList.size(); ++i) {
            Map<String, Object> map = new HashMap<>();
            map.put("authorList", newsAndAuthorList.get(i).getAuthor());
            Log.e("test authorListname",newsAndAuthorList.get(i).getAuthor().getUser_nickname());
            map.put("newsList", newsAndAuthorList.get(i).getNews());
            datasource.add(map);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
