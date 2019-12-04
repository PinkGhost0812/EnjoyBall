package com.example.lenovo.Activity;

import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lenovo.Adapter.TeamAdapter;
import com.example.lenovo.enjoyball.Info;
import com.example.lenovo.enjoyball.R;
import com.example.lenovo.entity.Comment;
import com.example.lenovo.entity.News;
import com.example.lenovo.entity.Team;
import com.example.lenovo.entity.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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

public class TeamActivity extends AppCompatActivity {

    private Button btnTeamCreate;

    private ListView lvTeam;

    private List<Map<String, Object>> dataSource = null;
    private List<Map<User, Team>> list = null;

    private User user;

    private OkHttpClient okHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.nonetitle);
        setContentView(R.layout.activity_team);

        findView();

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        Info info = new Info();

        user = info.getUser();

        user=new User(1,"2","3","4","5","6","7","8","9",10,11,12);

        getTeam();

        btnTeamCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(TeamActivity.this, TeamCreateActivity.class);
                startActivity(intent);
            }
        });

        lvTeam.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(TeamActivity.this, TeamDetailActivity.class);
                startActivity(intent);
            }
        });
    }

    @Subscribe
    public void setInfo(List<Map<User, Team>> list) {

        initData(list);

        TeamAdapter adapter = new TeamAdapter
                (getApplicationContext(), dataSource, R.layout.listview_item_team);

        lvTeam.setAdapter(adapter);

    }

    private void initData(List<Map<User, Team>> list) {

        int i = 0;

        for (User user : list.get(i).keySet()) {
            Map<String, Object> map = new HashMap<>();
            map.put("names", list.get(i).get(user).getName());
            map.put("captains", user.getUser_nickname());
            //map.put("memberNum",list.get(i).get(user).getMemberNum);
            dataSource.add(map);
            i++;
        }


    }

    private void getTeam() {

        okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Info.BASE_URL + "team/findByPerson?id=" + user.getUser_id())
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(TeamActivity.this, "获取队伍信息失败~", Toast.LENGTH_SHORT).show();
                Looper.loop();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.body().string().equals("false")) {
                    Toast.makeText(TeamActivity.this, "您还没有加入队伍凹~", Toast.LENGTH_SHORT).show();
                } else {
                    Gson gson = new GsonBuilder()
                            .create();
                    Type listType = new TypeToken<List<User>>() {
                    }.getType();
                    list = gson.fromJson(response.body().string(), listType);
                    EventBus.getDefault().post(list);
                }
            }
        });

    }

    private void findView() {

        btnTeamCreate = findViewById(R.id.btn_team_create);

        lvTeam = findViewById(R.id.lv_team);
    }

    @Override
    public void onDestroy() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
