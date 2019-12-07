package com.example.lenovo.enjoyball;

import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.entity.Team;
import com.example.lenovo.entity.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class InviteActivity extends AppCompatActivity {
    private EditText et_search = null;
    private TextView tv_invite = null;
    private ListView lv_invite = null;
    private List<Map<String, Object>> datasource = new ArrayList<Map<String, Object>>();
    private String url = Info.BASE_URL;
    private InviteAdapter adapter;
    private String table = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        setContentView(R.layout.activity_agreeement_invite);
        Intent intent = getIntent();
        table = intent.getStringExtra("table");
        Log.e("table",table);
        String hint = intent.getStringExtra("hint");
        getView();
        et_search.setHint(hint);
        InviteAdapter adapter = new InviteAdapter(datasource, R.layout.listview_item_searchresult, this,table);
        lv_invite.setAdapter(adapter);
        tv_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object object = et_search.getText();
                String info = new String();
                if (object != null) {
                    info = object.toString();
                }
                sendToServer(table, info);
            }
        });
    }

    /*向服务器发送请求*/
    private void sendToServer(final String table, final String info) {
        String rUrl = url;
        if (table.equals("user")) {
            rUrl = url + "user/findByPhoneNumber?phone=" + info;
        } else {
            rUrl = url + "team/find?teamName=" + info;
        }
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(rUrl)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(InviteActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                Looper.loop();

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonStr = response.body().string();

                if (jsonStr.equals(false)) {
                    return;
                }
                if (table.equals("user")) {
                    User user = new Gson().fromJson(jsonStr, User.class);
                    Log.e("查到的用户信息",user.toString());
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("name", user.getUser_nickname());
                    map.put("object", user);
                    map.put("head", user.getUser_headportrait());
                    datasource.add(map);


                } else if (table.equals("team")) {
                    Log.e("搜索队伍结果",jsonStr);
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();
                    Team team = gson.fromJson(jsonStr,Team.class);
                    Log.e("查到的队伍",team.toString());
                    Map map = new HashMap<String,Object>();
                    map.put("name",team.getTeam_name());
                    map.put("head",team.getTeam_logo());
                    map.put("object",team);
                    datasource.add(map);

                }
                EventBus.getDefault().post("OK");
            }
        });


    }

   private void initData() {
        Map map1 = new HashMap();
        Map map2 = new HashMap();
        map1.put("name","王旭");
        map2.put("name","李嘉铭");
        map1.put("head",getResources().getDrawable(R.drawable.head_girl));
        map2.put("head",getResources().getDrawable(R.drawable.head_girl));
        datasource.add(map1);
        datasource.add(map2);

    }

    private void getView() {
        et_search = findViewById(R.id.et_invite_search);
        tv_invite = findViewById(R.id.tv_invite_search);
        lv_invite = findViewById(R.id.lv_invite);
    }
    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void setView(String message){
        adapter = new InviteAdapter(datasource,R.layout.listview_item_searchresult,this,table);
        lv_invite.setAdapter(adapter);
    }


    @Override
    protected void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }
}
