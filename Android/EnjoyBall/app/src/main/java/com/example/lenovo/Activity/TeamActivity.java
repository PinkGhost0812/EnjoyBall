package com.example.lenovo.Activity;

import android.content.Intent;
import android.os.Looper;
import android.os.Message;
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

import com.example.lenovo.entity.Team;
import com.example.lenovo.entity.User;
import com.example.lenovo.entity.UserAndTeam;
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


public class TeamActivity extends AppCompatActivity {

    private Button btnTeamCreate;

    private ListView lvTeam;

    private List<Map<String, Object>> dataSource = null;

    private List<UserAndTeam> list = null;

    private User captain;
    private User curUser;

    private Team team;

    private OkHttpClient okHttpClient;

    private List<User> userList = new ArrayList<>();

    private final int TAG_MESSAGE_TEAMANDCAPTAIN=11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.nonetitle);
        setContentView(R.layout.activity_team);

        findView();

        EventBus.getDefault().register(this);

        curUser = ((Info) getApplicationContext()).getUser();

        getTeam();

        lvTeam.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                team = list.get(position).getTeam();
                captain = list.get(position).getUser();

                getTeamDetailInfo();

                Intent intent = new Intent();
                intent.putExtra("team", team);
                intent.putExtra("captain", captain);
                intent.setClass(TeamActivity.this, TeamDetailActivity.class);

                startActivity(intent);

            }
        });

        btnTeamCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(TeamActivity.this, TeamCreateActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getTeamDetailInfo() {

        okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Info.BASE_URL + "team/findMember?id=" + team.getTeam_id())
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(TeamActivity.this, "获取队伍详细信息失败~", Toast.LENGTH_SHORT).show();
                Looper.loop();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String data = response.body().string();
                Gson gson = new GsonBuilder().create();
                Type listType = new TypeToken<List<User>>() {
                }.getType();
                userList = gson.fromJson(data, listType);
                Message msg = new Message();
                msg.obj = userList;
                msg.what = 20;
                Log.e("test", userList.toString());
                EventBus.getDefault().postSticky(msg);
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void setTeamInfo(Message msg) {

        if (msg.what == TAG_MESSAGE_TEAMANDCAPTAIN) {

            list = (List<UserAndTeam>) msg.obj;
            initData(list);
            TeamAdapter adapter = new TeamAdapter
                    (this, dataSource, R.layout.listview_item_team);
            lvTeam.setAdapter(adapter);
        }

    }

    private void initData(List<UserAndTeam> list) {

        dataSource = new ArrayList<>();

        Log.e("test 111", list.size() + "");
        Log.e("test 222", list.get(0).getUser().getUser_nickname());

        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("logos", list.get(i).getTeam().getTeam_logo());
            map.put("names", list.get(i).getTeam().getTeam_name());
            map.put("captains", list.get(i).getUser().getUser_nickname());
            map.put("captainsId", list.get(i).getUser().getUser_id());
            map.put("nums", list.get(i).getTeam().getTeam_number());
            map.put("teams", list.get(i).getTeam());
            Log.e("test teamstring", list.get(i).getTeam().toString());
            dataSource.add(map);
        }

    }

    private void getTeam() {

        okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Info.BASE_URL + "team/findByPerson?id=" + curUser.getUser_id())
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
                String data = response.body().string();
                if (data.equals("false")) {
                    Looper.prepare();
                    Toast.makeText(TeamActivity.this,"您还没有加入球队凹~",Toast.LENGTH_SHORT).show();
                    Looper.loop();
                } else {
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                    Type listType = new TypeToken<List<UserAndTeam>>() {
                    }.getType();
                    list = gson.fromJson(data, listType);
                    Log.e("test captains", list.get(0).getUser().getUser_id().toString());
                    Message msg = new Message();
                    msg.what = TAG_MESSAGE_TEAMANDCAPTAIN;
                    msg.obj = list;
                    EventBus.getDefault().post(msg);
                }
            }
        });

    }

    private void findView() {

        btnTeamCreate = findViewById(R.id.btn_team_create);

        lvTeam = findViewById(R.id.lv_team);
    }

    @Override
    protected void onDestroy() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

}
