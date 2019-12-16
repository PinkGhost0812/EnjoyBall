package com.example.lenovo.Activity;

import android.content.Intent;
import android.media.Image;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lenovo.Adapter.TeamDetailAdapter;
import com.example.lenovo.Adapter.TeamManageMemberAdapter;
import com.example.lenovo.enjoyball.Info;
import com.example.lenovo.enjoyball.MainActivity;
import com.example.lenovo.enjoyball.R;
import com.example.lenovo.entity.Team;
import com.example.lenovo.entity.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TeamManageMemberActivity extends AppCompatActivity {

    private Team team;

    private OkHttpClient okHttpClient;

    private List<User> userList = null;

    private ListView lvTeamManageMember;

    private TeamManageMemberAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.nonetitle);
        setContentView(R.layout.activity_team_manage_member);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        team = (Team) getIntent().getSerializableExtra("team");

        getTeamMember();

        lvTeamManageMember = findViewById(R.id.lv_team_manage_member);
        Button ivTeamManageMemberAdd = findViewById(R.id.iv_team_manage_memberAdd);
        LinearLayout aaa = findViewById(R.id.aaa);
        aaa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("test", "777");
            }
        });

        ivTeamManageMemberAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("test", "111");
                Intent intent = new Intent();
                intent.setClass(TeamManageMemberActivity.this, TeamManageInviteActivity.class);
                intent.putExtra("team", team);
                startActivity(intent);
            }
        });

        lvTeamManageMember.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(TeamManageMemberActivity.this, HomepageActivity.class);
                intent.putExtra("user", userList.get(position));
                startActivity(intent);
                Log.e("test", userList.get(position).toString());
            }
        });

    }

    private void getTeamMember() {

        okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Info.BASE_URL + "team/findMember?id=" + team.getTeam_id())
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(TeamManageMemberActivity.this, "获取队伍队员列表失败~", Toast.LENGTH_SHORT).show();
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
                msg.what = 19;
                Log.e("test", userList.toString());
                EventBus.getDefault().postSticky(msg);
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED, sticky = true)
    public void setMemberInfo(Message msg) {

        if (msg.what == 19) {

            adapter =
                    new TeamManageMemberAdapter(this, userList, R.layout.listview_item_member, team.getTeam_id());

            lvTeamManageMember.setAdapter(adapter);
        }
        Log.e("test", "4444");

        if (msg.what == 40) {
            Log.e("test msg obj",msg.obj.toString());
            int positon=Integer.parseInt(msg.obj.toString());
            userList.remove(positon);
            adapter.notifyDataSetChanged();
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void alarmInfo(android.os.Message msg) {


    }

    @Override
    public void onDestroy() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

}
