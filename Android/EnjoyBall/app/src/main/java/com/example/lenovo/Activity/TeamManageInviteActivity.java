package com.example.lenovo.Activity;

import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.Adapter.InviteAdapter;
import com.example.lenovo.Adapter.TeamManageInviteAdapter;
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

public class TeamManageInviteActivity extends AppCompatActivity {

    private EditText etTeamManageInvite;

    private TextView tvTeamManageSearch;

    private ListView lvTeamManageInviteUser;

    private List<Map<String, Object>> datasource = new ArrayList<Map<String, Object>>();

    private String data;

    private List<User> userList;

    private Team team;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.nonetitle);
        setContentView(R.layout.activity_team_manage_invite);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        findView();

        team= (Team) getIntent().getSerializableExtra("team");

        tvTeamManageSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                data = etTeamManageInvite.getText().toString();

                getUserList();
            }
        });

    }

    private void getUserList() {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Info.BASE_URL + "user/findManyUser?phone=" + data)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(TeamManageInviteActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                Looper.loop();

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String jsonStr = response.body().string();

                if (jsonStr.equals(false)) {

                    Message msg=new Message();
                    msg.what=58;
                    EventBus.getDefault().post(msg);

                } else {

                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                    Type listType = new TypeToken<List<User>>() {}.getType();
                    userList = gson.fromJson(jsonStr, listType);
                    Log.e("查到的用户信息", userList.toString());

                    Message msg = new Message();
                    msg.what = 27;
                    msg.obj = userList;

                    EventBus.getDefault().post(msg);
                }
            }

        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void setAdapter(Message msg) {
        if (msg.what == 27) {

            List<User> lists = (List<User>) msg.obj;
            initData(lists);
            TeamManageInviteAdapter adapter =
                    new TeamManageInviteAdapter(datasource, R.layout.listview_item_team_manage_member_invite, this);
            lvTeamManageInviteUser.setAdapter(adapter);

        }else if (msg.what==58){
            Toast.makeText(TeamManageInviteActivity.this, "该手机号未注册凹~", Toast.LENGTH_SHORT);
        }

    }

    private void initData(List<User> userList) {

        for (int i = 0; i < userList.size(); i++) {

            Map map = new HashMap<String, Object>();
            map.put("teams",team.getTeam_id());
            map.put("names", userList.get(i).getUser_nickname().toString());
            map.put("heads", userList.get(i).getUser_headportrait().toString());
            map.put("objects", userList.get(i));

            datasource.add(map);
        }
    }

    private void findView() {
        etTeamManageInvite = findViewById(R.id.et_team_manage_invite);
        tvTeamManageSearch = findViewById(R.id.tv_team_manage_search);
        lvTeamManageInviteUser = findViewById(R.id.lv_team_manage_inviteUser);
    }

    @Override
    protected void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }
}
