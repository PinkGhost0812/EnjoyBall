package com.example.lenovo.enjoyball;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lenovo.Activity.TeamDetailActivity;
import com.example.lenovo.Activity.TeamVisitDetailActivity;
import com.example.lenovo.entity.Contest;
import com.example.lenovo.entity.Team;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ContestActivity extends AppCompatActivity {
    private OkHttpClient okHttpClient;
    private ImageView team1logo;
    private ImageView team2logo;
    private TextView team1name;
    private TextView team2name;
    private TextView team1score;
    private TextView team2score;
    private Button subscribe;
    private String name1 ;
    private String name2 ;
    private String logo1;
    private String logo2;
    private Integer gameid;
    private Team team1;
    private Team team2;

    private Contest contest1;
    private Map<String,MyTabSpec> map = new HashMap<>();
    private String[] tabStrId = {"阵容","战报","评论"};

    private Fragment curFragment = null;

    private class  MyTabSpec{
        private TextView textView = null;
        private Fragment fragment = null;

        private void setSelect(boolean b){
            if(b){
                textView.setTextColor(Color.parseColor("#00FF00"));
            }else{
                textView.setTextColor(Color.parseColor("#000000"));
            }
        }

        public Fragment getFragment() {
            return fragment;
        }

        public void setFragment(Fragment fragment) {
            this.fragment = fragment;
        }

        public TextView getTextView() {
            return textView;
        }

        public void setTextView(TextView textView) {
            this.textView = textView;
        }
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contest);

        gameid = getIntent().getIntExtra("id",-1);
        EventBus.getDefault().register(this);
        Intent intent = getIntent();
        gameid = intent.getIntExtra("id",-1);

        findview();
        requestData(gameid);


    }

    private void initFragment() {
        map.put(tabStrId[0],new MyTabSpec());
        map.put(tabStrId[1],new MyTabSpec());
        map.put(tabStrId[2],new MyTabSpec());

        setFragment();

        findView();

        setListener();

        changeTab(tabStrId[0]);
    }

    private void setListener() {
        TextView tv1 = findViewById(R.id.tv_contest_battleArray);
        TextView tv2 = findViewById(R.id.tv_contest_battleReport);
        TextView tv3 = findViewById(R.id.tv_contest_comment);

        MyListener myListener = new MyListener();
        tv1.setOnClickListener(myListener);
        tv2.setOnClickListener(myListener);
        tv3.setOnClickListener(myListener);


    }

    private class MyListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_contest_battleArray:
                    changeTab(tabStrId[0]);
                    break;
                case R.id.tv_contest_battleReport:
                    changeTab(tabStrId[1]);
                    break;
                case R.id.tv_contest_comment:
                    changeTab(tabStrId[2]);
                    break;
            }
        }
    }

    private void changeTab(String s) {
        changeFragment(s);


        changeText(s);
    }

    private void changeText(String s) {
        for (String key : map.keySet()) {
            map.get(key).setSelect(false);
        }
        map.get(s).setSelect(true);
    }

    private void changeFragment(String s) {
        Fragment fragment = map.get(s).getFragment();

        if(curFragment==fragment) return;

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

//        向fragment传递数据,方便fragment查询队伍信息
        Bundle bundle = new Bundle();
        bundle.putInt("team1",contest1.getGame_home());
        bundle.putInt("team2",contest1.getGame_away());
        bundle.putInt("game",gameid);
        fragment.setArguments(bundle);

        if(curFragment!=null){
            transaction.hide(curFragment);
        }
        if(!fragment.isAdded()){
            transaction.add(R.id.fl_contest_tabContent,fragment);
        }

        transaction.show(fragment);
        curFragment = fragment;

        transaction.commit();
    }

    private void findView() {
        TextView tv1 = findViewById(R.id.tv_contest_battleArray);
        TextView tv2 = findViewById(R.id.tv_contest_battleReport);
        TextView tv3 = findViewById(R.id.tv_contest_comment);

        map.get(tabStrId[0]).setTextView(tv1);
        map.get(tabStrId[1]).setTextView(tv2);
        map.get(tabStrId[2]).setTextView(tv3);

    }

    private void setFragment() {
        map.get(tabStrId[0]).setFragment(new BattleArrayFragment());
        map.get(tabStrId[1]).setFragment(new BattleReportFragment());
        map.get(tabStrId[2]).setFragment(new CommentFragment());

    }

    private void findview() {

        team1logo = findViewById(R.id.im_contest_teamLogo1);
        team2logo = findViewById(R.id.im_contest_teamLogo2);
        team1score = findViewById(R.id.tv_contest_teamScore1);
        team2score = findViewById(R.id.tv_contest_teamScore2);
        team1name = findViewById(R.id.tv_contest_teamName1);
        team2name = findViewById(R.id.tv_contest_teamName2);
        subscribe = findViewById(R.id.btn_contest_subscription);
    }

    private void requestData(int id) {
        okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Info.BASE_URL+"contest/findById?id="+id)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String msg = response.body().string();
                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd hh:mm:ss")
                        .setPrettyPrinting()
                        .serializeNulls()
                        .create();
                Log.e("xx",msg);
                contest1 = gson.fromJson(msg,Contest.class);
                EventBus.getDefault().post("已接收比赛详情信息");
            }
        });
    }




    private void init() {
        team1name.setText(name1);
        team2name.setText(name2);
        String[] spilt = contest1.getGame_result().split("-");
        team1score.setText(spilt[0]);
        team2score.setText(spilt[1]);
        if(contest1.getGame_status()==3){
            subscribe.setText("未开始");
            subscribe.setTextColor(Color.parseColor("#000000"));
        }
        if(contest1.getGame_status()==2){
            subscribe.setText("进行中");
            subscribe.setTextColor(Color.parseColor("#1afa29"));
        }
        if(contest1.getGame_status()==1){
            subscribe.setText("已完成");
            subscribe.setTextColor(Color.parseColor("#000000"));
        }
        Glide.with(this).load(Info.BASE_URL+logo1).into(team1logo);
        Glide.with(this).load(Info.BASE_URL+logo2).into(team2logo);
    }

    private void FindTeamName2(Integer away) {
        okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Info.BASE_URL+"team/findById?id="+away)
                .build();


        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonstr = response.body().string();
                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd hh:mm:ss")
                        .setPrettyPrinting()
                        .serializeNulls()
                        .create();
                team2 = gson.fromJson(jsonstr,Team.class);
                Log.e("mes1",team2.toString());
                name2 = team2.getTeam_name().toString();
                logo2 = team2.getTeam_logo().toString();
                EventBus.getDefault().post("队伍名称已查到");
            }
        });
    }

    private void FindTeamName1(Integer teamid) {
        okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Info.BASE_URL+"team/findById?id="+teamid)
                .build();


        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonstr = response.body().string();
                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd hh:mm:ss")
                        .setPrettyPrinting()
                        .serializeNulls()
                        .create();
                team1 = gson.fromJson(jsonstr,Team.class);
                Log.e("mes2",team1.toString());
                name1 = team1.getTeam_name();
                logo1 = team1.getTeam_logo();
                EventBus.getDefault().post("队伍名称1已查到");
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void inits(String msg){
        switch (msg){
            case "已接收比赛详情信息":
                FindTeamName1(contest1.getGame_home());
                break;
            case "队伍名称1已查到":
                FindTeamName2(contest1.getGame_away());
                break;
            case "队伍名称已查到":
                init();
                initFragment();
                LinearLayout lteam1 = findViewById(R.id.ll_contest_team1);
                LinearLayout lteam2 = findViewById(R.id.ll_contest_team2);
                lteam1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(getApplicationContext(), TeamVisitDetailActivity.class);
                        intent.putExtra("team",team1);
                        startActivity(intent);
                    }
                });
                lteam2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(getApplicationContext(), TeamVisitDetailActivity.class);
                        intent.putExtra("team",team2);
                        startActivity(intent);
                    }
                });
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
