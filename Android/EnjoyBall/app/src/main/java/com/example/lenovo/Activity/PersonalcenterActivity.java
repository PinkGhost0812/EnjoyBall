package com.example.lenovo.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.enjoyball.Info;
import com.example.lenovo.enjoyball.R;
import com.example.lenovo.entity.User;
import com.example.lenovo.Util.*;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PersonalcenterActivity extends AppCompatActivity {

    private ImageView ivPersonalcenterPortrait;
    private ImageView ivPersonalcenterComment;
    private ImageView ivPersonalcenterFollow;
    private ImageView ivPersonalcenterFans;

    private TextView tvPersonalcenterNickname;
    private TextView tvPersonalcenterSignature;
    private TextView tvPersonalcenterScores;
    private TextView tvPersonalcenterCommentnum;
    private TextView tvPersonalcenterFollownum;
    private TextView tvPersonalcenterFansnum;

    private LinearLayout llPersonalcenterData;
    private LinearLayout llPersonalcneterTeam;
    private LinearLayout llPersonalcneterYueqiu;
    private LinearLayout llPersonalcneterCollect;
    private LinearLayout llPersonalcneterVip;
    private LinearLayout llPersonalcneterFeedback;
    private LinearLayout llPersonalcenterLogout;

    private Intent intent;

    private User user = null;

    private int user_id;
    private int commentNum;
    private int followNum;
    private int fansNum;
    private int score;

    private OkHttpClient okHttpClient;

    private Info info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.nonetitle);
        setContentView(R.layout.activity_personalcenter);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        findView();

        getInfo();

        setInfo();

        getCommentNum();

        getFollowNum();

        getFansNum();

        setListeners();

    }

    private void getFansNum() {

        okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Info.BASE_URL + "user/fans?id=" + 1)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(getApplicationContext(), "获取粉丝数量失败~", Toast.LENGTH_SHORT).show();
                Looper.loop();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message msg=new Message();
                msg.what=3;
                msg.obj=response.body().string();
                EventBus.getDefault().post(msg);
            }
        });

    }

    private void getFollowNum() {

        okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(Info.BASE_URL + "user/follownum?id=" + 1)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(getApplicationContext(), "获取关注数量失败~", Toast.LENGTH_SHORT).show();
                Looper.loop();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message msg=new Message();
                msg.what=2;
                msg.obj=response.body().string();
                EventBus.getDefault().post(msg);
            }
        });

    }

    private void getCommentNum() {

        okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Info.BASE_URL + "information/count?id=" + 1)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(getApplicationContext(), "获取评论数量失败~", Toast.LENGTH_SHORT).show();
                Looper.loop();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message msg=new Message();
                msg.what=1;
                msg.obj=response.body().string();
                EventBus.getDefault().post(msg);
            }
        });

    }

    private void setInfo() {
        tvPersonalcenterNickname.setText(user.getUser_nickname());
        tvPersonalcenterSignature.setText(user.getUser_signature());
    }

    @Subscribe
    public void setHttpInfo(Message msg) {

        switch (msg.what){
            case 1:
                tvPersonalcenterCommentnum.setText(msg.obj.toString());
                break;
            case 2:
                tvPersonalcenterFollownum.setText(msg.obj.toString());
                break;
            case 3:
                tvPersonalcenterFansnum.setText(msg.obj.toString());
                break;
        }

    }

    private class PersonalcenterListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_personalcenter_portrait:
                    //点击头像
                    intent = new Intent();
                    intent.putExtra("user", user);
                    intent.setClass(PersonalcenterActivity.this, HomepageActivity.class);
                    startActivity(intent);
                    break;
                case R.id.iv_personalcenter_comment:
                    //点击评论图片
                    intent = new Intent();
                    intent.setClass(PersonalcenterActivity.this, HomepageActivity.class);
                    intent.putExtra("tag", "comment");
                    intent.putExtra("user", user);
                    startActivity(intent);
                    break;
                case R.id.iv_personalcenter_follow:
                    //点击关注图片
                    intent = new Intent();
                    intent.setClass(PersonalcenterActivity.this, HomepageActivity.class);
                    intent.putExtra("tag", "follow");
                    intent.putExtra("user", user);
                    startActivity(intent);
                    break;
                case R.id.iv_personalcenter_fans:
                    //点击粉丝图片
                    intent = new Intent();
                    intent.setClass(PersonalcenterActivity.this, HomepageActivity.class);
                    intent.putExtra("tag", "fans");
                    intent.putExtra("user", user);
                    startActivity(intent);
                    break;
                case R.id.tv_personalcenter_nickname:
                    //点击用户名
                    intent = new Intent();
                    intent.setClass(PersonalcenterActivity.this, HomepageActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                    break;
                case R.id.tv_personalcenter_signature:
                    //点击个性签名
                    break;
                case R.id.ll_personalcenter_data:
                    //点击个人资料
                    intent = new Intent();
                    intent.putExtra("user", user);
                    intent.setClass(PersonalcenterActivity.this, PerinfoActivity.class);
                    startActivity(intent);
                    break;
                case R.id.ll_personalcenter_team:
                    //点击球队
                    intent = new Intent();
                    intent.setClass(PersonalcenterActivity.this, TeamActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                    break;
                case R.id.ll_personalcenter_yueqiu:
                    //点击约球
                    break;
                case R.id.ll_personalcenter_collect:
                    //点击收藏
                    break;
                case R.id.ll_personalcenter_vip:
                    //点击vip
                    break;
                case R.id.ll_personalcenter_feedback:
                    //点击反馈
                    break;
                case R.id.ll_personalcenter_logout:
                    //点击注销
                    break;

            }
        }
    }


    private void getInfo() {

        info = new Info();
        user = info.getUser();
        user = new User();
        user_id = user.getUser_id();

    }

    private void setListeners() {

        PersonalcenterListener personalcenterListener = new PersonalcenterListener();

        ivPersonalcenterPortrait.setOnClickListener(personalcenterListener);
        ivPersonalcenterComment.setOnClickListener(personalcenterListener);
        ivPersonalcenterFollow.setOnClickListener(personalcenterListener);
        ivPersonalcenterFans.setOnClickListener(personalcenterListener);

        tvPersonalcenterNickname.setOnClickListener(personalcenterListener);
        tvPersonalcenterSignature.setOnClickListener(personalcenterListener);

        llPersonalcenterData.setOnClickListener(personalcenterListener);
        llPersonalcneterTeam.setOnClickListener(personalcenterListener);
        llPersonalcneterYueqiu.setOnClickListener(personalcenterListener);
        llPersonalcneterCollect.setOnClickListener(personalcenterListener);
        llPersonalcneterVip.setOnClickListener(personalcenterListener);
        llPersonalcneterFeedback.setOnClickListener(personalcenterListener);
        llPersonalcenterLogout.setOnClickListener(personalcenterListener);

    }

    private void findView() {

        ivPersonalcenterPortrait = findViewById(R.id.iv_personalcenter_portrait);
        ivPersonalcenterComment = findViewById(R.id.iv_personalcenter_comment);
        ivPersonalcenterFollow = findViewById(R.id.iv_personalcenter_follow);
        ivPersonalcenterFans = findViewById(R.id.iv_personalcenter_fans);

        tvPersonalcenterNickname = findViewById(R.id.tv_personalcenter_nickname);
        tvPersonalcenterSignature = findViewById(R.id.tv_personalcenter_signature);
        tvPersonalcenterScores = findViewById(R.id.tv_personalcenter_scores);
        tvPersonalcenterCommentnum = findViewById(R.id.tv_personalcenter_commentNum);
        tvPersonalcenterFollownum = findViewById(R.id.tv_personalcenter_followNum);
        tvPersonalcenterFansnum = findViewById(R.id.tv_personalcneter_fansNum);

        llPersonalcenterData = findViewById(R.id.ll_personalcenter_data);
        llPersonalcneterTeam = findViewById(R.id.ll_personalcenter_team);
        llPersonalcneterYueqiu = findViewById(R.id.ll_personalcenter_yueqiu);
        llPersonalcneterCollect = findViewById(R.id.ll_personalcenter_collect);
        llPersonalcneterVip = findViewById(R.id.ll_personalcenter_vip);
        llPersonalcneterFeedback = findViewById(R.id.ll_personalcenter_feedback);
        llPersonalcenterLogout = findViewById(R.id.ll_personalcenter_logout);

    }

    @Override
    protected void onDestroy() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
