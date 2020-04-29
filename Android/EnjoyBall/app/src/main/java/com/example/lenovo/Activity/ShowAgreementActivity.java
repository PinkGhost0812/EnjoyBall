package com.example.lenovo.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.Adapter.AgreementAdapter;
import com.example.lenovo.enjoyball.Info;
import com.example.lenovo.enjoyball.R;
import com.example.lenovo.entity.DemandInfo;
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

public class ShowAgreementActivity extends AppCompatActivity {
    private GridView gv_joinagreement;
    private List<Map<String, Object>> datasource = null;
    private TextView tv_type;
    private TextView tv_leader;
    private TextView tv_address;
    private TextView tv_time;
    private TextView tv_message;
    private AgreementAdapter adapter = null;
    private Team team = null;
    private DemandInfo demandInfo;
    private String url = Info.BASE_URL;
    private final static int POST_DEMANDINFO = 1;
    private final static int POST_LEADER = 2;
    private final static int POST_GETTEAMB = 3;
    private final static int INITDATASOURCE = 4;
    private final static int POST_REALTEAM = 5;
    private OkHttpClient okHttpClient = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.nonetitle);
        setContentView(R.layout.activity_joinagreement);
        EventBus.getDefault().register(this);
        okHttpClient = new OkHttpClient();
        final Intent intent = getIntent();
        int demandId = intent.getIntExtra("id", 0);
        //Log.e("id",demandId);
        getView();
        getDemandInfo(demandId);
        /*--------------------------点击item加入或查看对方信息------------------------*/
        gv_joinagreement.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                if (demandInfo.getDemand_oom() == 0) {
                    if (datasource.get(i).get("object") != null) {
                        User user = (User) datasource.get(i).get("object");
                        Log.e("uesr", user.toString());
                        Intent intent1 = new Intent(ShowAgreementActivity.this, HomepageActivity.class);
                        intent1.putExtra("user", user);
                        startActivity(intent1);
                    }
                }else {
                    if (datasource.get(i).get("object")!=null){
                        Intent intent1 = new Intent(ShowAgreementActivity.this,TeamDetailActivity.class);
                        intent1.putExtra("name",(Team)datasource.get(0).get("object"));
                        startActivity(intent1);
                    }
                }

            }
        });
        /*----------------------------------结束-------------------------------------*/


    }

    //获取队长为当前用户的队伍信息
    private void getTeams() {
        Team myTeam = null;
        int id = 0;
        //int id = getApplication().getUser().getUser_id();
        url = url + "team/findByIdCls?id=" + id + "&cls=" + demandInfo.getDemand_class();
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String teamJson = response.body().string();
                if (!teamJson.equals("false")) {
                    team = new Gson().fromJson(teamJson, Team.class);
                }


            }
        });

    }

    //获取约球信息
    private void getDemandInfo(final int demandId) {
        Log.e("url", url + "appointment/findById?id=" + demandId);
        Request request = new Request.Builder()
                .url(url + "appointment/findById?id=" + demandId)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(ShowAgreementActivity.this, "服务器连接失败", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonInfo = response.body().string();
                Log.e("约球信息", jsonInfo);
                Gson gson = new GsonBuilder().setDateFormat("YYYY-MM-DD hh:mm:ss").create();
                demandInfo = gson.fromJson(jsonInfo, DemandInfo.class);
                Message message = new Message();
                message.what = POST_DEMANDINFO;
                message.obj = demandInfo;
                EventBus.getDefault().post(message);


            }
        });

    }

    //根据约球信息中的demand_user找到发起人的信息
    private void getLeader(int id) {
        Request request = new Request.Builder()
                .url(url + "user/find?id=" + id)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String userJson = response.body().string();
                User user = new Gson().fromJson(userJson, User.class);
                Message message = new Message();
                message.what = POST_LEADER;
                message.obj = user;
                EventBus.getDefault().post(message);
            }
        });
    }

    //根据约球信息查找对阵双方
    private void getTeam(final DemandInfo info, final String teamName) {
        int id = 0;
        int offset = 0;
        if (teamName.equals("a")) {
            id = info.getDemand_teama();
            offset = 0;
        } else if (teamName.equals("b")) {
            id = info.getDemand_teamb();
            offset = 1;
        }
        Request.Builder builder = new Request.Builder();
        Request request = null;
        if (info.getDemand_oom() == 0) {
            request = builder.url(url + "user/findByDTeamId?id=" + id).build();
        } else if (info.getDemand_oom() == 1) {

            request = builder.url(url + "user/findById?id=" + info.getDemand_teama()).build();
        }
        Call call = okHttpClient.newCall(request);
        final int finalOffset = offset;
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                if (info.getDemand_oom() == 0) {
                    Type type = new TypeToken<List<User>>() {
                    }.getType();
                    List<User> users = new Gson().fromJson(json, type);

                    for (int i = 0; i < users.size(); i++) {
                        Log.e(teamName + "查到的队员", users.get(i).getUser_nickname());
                        Map map = new HashMap<String, Object>();
                        map.put("name", users.get(i).getUser_nickname());
                        map.put("head", Info.BASE_URL + users.get(i).getUser_headportrait());
                        map.put("object", users.get(i));
                        map.put("status", "1");
                        //保证同一队的队员在同一列，A队在左边,B队在右边
                        datasource.set(i * 2 + finalOffset, map);

                    }
                    Log.e("当前数据源", datasource.toString());

                    Message message = new Message();
                    if (finalOffset == 0) {
                        message.what = POST_GETTEAMB;
                        message.obj = info;
                    } else if (finalOffset == 1) {
                        message.what = INITDATASOURCE;
                    }
                    EventBus.getDefault().post(message);


                } else if (info.getDemand_oom() == 1) {
                    Team team = new Gson().fromJson(json, Team.class);
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("name", team.getTeam_name());
                    map.put("head", team.getTeam_logo());
                    map.put("object", team);
                    map.put("status", 1);
                    datasource.add(0, map);


                }
            }
        });


    }

    private void getView() {
        gv_joinagreement = findViewById(R.id.gv_joinagreement);
        tv_type = findViewById(R.id.tv_joinagreement_type);
        tv_leader = findViewById(R.id.tv_joinagreement_leader);
        tv_address = findViewById(R.id.tv_joinagreement_address);
        tv_time = findViewById(R.id.tv_joinagreement_time);
        tv_message = findViewById(R.id.tv_joinagreement_message);

    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void setView(Message message) {
        switch (message.what) {
            case POST_DEMANDINFO:
                demandInfo = (DemandInfo) message.obj;
                datasource = new ArrayList<Map<String, Object>>();
                for (int i = 0; i < demandInfo.getDemand_num(); i++) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    datasource.add(map);
                }
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm");
                tv_time.setText(simpleDateFormat.format(demandInfo.getDemand_time()));
                tv_message.setText(demandInfo.getDemand_description());
                tv_address.setText(demandInfo.getDemand_place());
                String[] types = getResources().getStringArray(R.array.type);
                tv_type.setText(types[demandInfo.getDemand_class()]);
                getLeader(demandInfo.getDemand_user());
                if (demandInfo.getDemand_oom() == 0) {
                    getTeam(demandInfo, "a");
                } else {
                    getRealTeam(demandInfo.getDemand_teama());
                }

                break;
            case POST_LEADER:
                User user = (User) message.obj;
                tv_leader.setText(user.getUser_nickname());
                break;
            case POST_GETTEAMB:
                DemandInfo info1 = (DemandInfo) message.obj;
                getTeam(info1, "b");
                break;
            case INITDATASOURCE:
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("name", "虚位以待");
                map.put("head", getResources().getDrawable(R.drawable.head_girl));
                map.put("obj", null);
                map.put("status", 0);
                for (int i = 0; i < datasource.size(); i++) {
                    if (datasource.get(i).size() == 0) {
                        datasource.set(i, map);
                    }
                }
                adapter = new AgreementAdapter(datasource, R.layout.listview_item_agreement, this);
                gv_joinagreement.setAdapter(adapter);
        }


    }

    private void getRealTeam(final Integer teamId) {
        final Request request = new Request.Builder()
                .url(url + "team/findById?id=" + teamId)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Team team = null;
                Map<String, Object> map = new HashMap<String, Object>();
                if (!result.equals("false")) {
                    Gson gson = new GsonBuilder()
                            .setDateFormat("YYYY-MM-DD HH:MM")
                            .create();
                    team = gson.fromJson(result, Team.class);
                    map.put("head", Info.BASE_URL+team.getTeam_logo());
                    map.put("name", team.getTeam_name());
                    map.put("object", team);
                    map.put("status", "1");
                    Log.e("team", team.toString());
                    if (teamId == demandInfo.getDemand_teama()) {
                        Log.e("2310","test");
                        datasource.set(0, map);
                        if (demandInfo.getDemand_teamb() != null) {
                            getRealTeam(demandInfo.getDemand_teamb());
                        }

                    } else{
                        datasource.set(1, map);
                    }
                    Message message = new Message();
                    message.what = INITDATASOURCE;
                    EventBus.getDefault().post(message);
                }

            }
        });


    }

    private User getUser() {
        Info info = (Info) getApplication();
        User user = info.getUser();
        return user;
    }

}

