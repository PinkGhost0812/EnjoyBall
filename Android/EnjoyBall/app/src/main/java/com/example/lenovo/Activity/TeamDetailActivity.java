package com.example.lenovo.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.example.lenovo.Adapter.TeamDetailAdapter;
import com.example.lenovo.enjoyball.Info;
import com.example.lenovo.enjoyball.R;
import com.example.lenovo.entity.Team;
import com.example.lenovo.entity.User;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TeamDetailActivity extends AppCompatActivity {

    private TextView tvTeamDetailName;
    private TextView tvTeamDetailCaptain;
    private TextView tvTeamDetailAddress;
    private TextView tvTeamDetailTime;
    private TextView tvTeamDetailSlogan;
    private TextView tvTeamDetailType;

    private ImageView ivTeamDetailLogo;

    private ListView lvTeamDetailMember;

    private Button btnTeamDetailDissolve;

    private Team team;

    private User captain;
    private User user;

    private List<User> userList;

    private ArrayList<String> list = new ArrayList<>();

    private OkHttpClient okHttpClient;

    private TeamDetailAdapter membersAdapter;

    private final int TAG_MESSAGE_TEAM_QUIT=76;
    private final int TAG_MESSAGE_TEAM_DISSOLVE=77;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.nonetitle);
        setContentView(R.layout.activity_team_detail);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        team = (Team) getIntent().getSerializableExtra("team");
        captain = (User) getIntent().getSerializableExtra("captain");

        user = ((Info) getApplicationContext()).getUser();

        findView();

        setInfo();

        if (user.getUser_id().equals(captain.getUser_id())) {
            btnTeamDetailDissolve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    android.app.AlertDialog.Builder adBuilder = new android.app.AlertDialog.Builder(TeamDetailActivity.this);
                    adBuilder.setTitle("解 散？");
                    adBuilder.setMessage("确认解散球队吗？");
                    adBuilder.setPositiveButton("狠心确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dissolveTeam();
                        }
                    });
                    adBuilder.setNegativeButton("我再想想", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    android.app.AlertDialog alertDialog = adBuilder.create();
                    alertDialog.show();
                }
            });
        } else {
            btnTeamDetailDissolve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    android.app.AlertDialog.Builder adBuilder = new android.app.AlertDialog.Builder(TeamDetailActivity.this);
                    adBuilder.setTitle("退 出？");
                    adBuilder.setMessage("确认退出球队吗？");
                    adBuilder.setPositiveButton("狠心确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            quitTeam();
                        }
                    });
                    adBuilder.setNegativeButton("我再想想", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    android.app.AlertDialog alertDialog = adBuilder.create();
                    alertDialog.show();
                }
            });
        }


    }

    private void quitTeam() {

        okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Info.BASE_URL + "team/out?userId=" + user.getUser_id() + "&teamId=" + team.getTeam_id())
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(TeamDetailActivity.this, "网络走丢咯~~", Toast.LENGTH_SHORT).show();
                Looper.loop();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String data = response.body().string();

                Message msg = new Message();
                msg.what = TAG_MESSAGE_TEAM_QUIT;
                msg.obj = data;
                EventBus.getDefault().postSticky(msg);
            }
        });

    }

    private void dissolveTeam() {

        okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Info.BASE_URL + "team/dissolve?id=" + team.getTeam_id())
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(TeamDetailActivity.this, "网络走丢咯~~", Toast.LENGTH_SHORT).show();
                Looper.loop();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String data = response.body().string();

                Message msg = new Message();
                msg.what = TAG_MESSAGE_TEAM_DISSOLVE;
                msg.obj = data;
                EventBus.getDefault().postSticky(msg);

            }
        });

    }

    private void setInfo() {

        tvTeamDetailName.setText(team.getTeam_name());
        tvTeamDetailCaptain.setText(captain.getUser_nickname());
        tvTeamDetailAddress.setText(team.getTeam_region());
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        tvTeamDetailTime.setText(sf.format(team.getTeam_time()));
        tvTeamDetailSlogan.setText(team.getTeam_slogan());
        switch (team.getTeam_class()){
            case 0:
                tvTeamDetailType.setText("足球");
                break;
            case 1:
                tvTeamDetailType.setText("篮球");
                break;
            case 2:
                tvTeamDetailType.setText("排球");
                break;
            case 3:
                tvTeamDetailType.setText("羽毛球");
                break;
            case 4:
                tvTeamDetailType.setText("乒乓球");
                break;
        }

        RequestOptions options = new RequestOptions()
                .signature(new ObjectKey(System.currentTimeMillis()))
                .circleCrop();
        Glide.with(TeamDetailActivity.this)
                .load(Info.BASE_URL + team.getTeam_logo())
                .apply(options)
                .into(ivTeamDetailLogo);
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED, sticky = true)
    public void initMsgData(Message msg) {

        Log.e("test hand?", msg.obj.toString());

        if (msg.what == 20) {
            Log.e("test 20", msg.obj.toString());
            userList = (List<User>) msg.obj;
            for (int i = 0; i < userList.size(); i++) {
                list.add(userList.get(i).getUser_nickname());
            }
            Log.e("test DetailMemberList", list.toString());

            membersAdapter = new TeamDetailAdapter(this, list, R.layout.listview_item_team_detail);
            membersAdapter.notifyDataSetChanged();
            lvTeamDetailMember.setAdapter(membersAdapter);
        } else if (msg.what == TAG_MESSAGE_TEAM_DISSOLVE) {
            String data = (String) msg.obj;
            if (data.equals("true")) {
                Toast.makeText(TeamDetailActivity.this, "解散球队成功~", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(TeamDetailActivity.this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(TeamDetailActivity.this, "解散球队失败~", Toast.LENGTH_SHORT).show();
            }
        } else if (msg.what == TAG_MESSAGE_TEAM_QUIT) {
            String data = (String) msg.obj;

            if (data.equals("true")) {

                Toast.makeText(TeamDetailActivity.this, "退出球队成功~", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(TeamDetailActivity.this, MainActivity.class);
                startActivity(intent);

            } else {
                Toast.makeText(TeamDetailActivity.this, "退出球队失败~", Toast.LENGTH_SHORT).show();
            }
        }else if(msg.what==66){
            String data=(String)msg.obj;
            tvTeamDetailCaptain.setText(data);
        }

    }

    private void findView() {

        tvTeamDetailName = findViewById(R.id.tv_team_detail_name);
        tvTeamDetailCaptain = findViewById(R.id.tv_team_detail_captain);
        tvTeamDetailAddress = findViewById(R.id.tv_team_detail_address);
        tvTeamDetailTime = findViewById(R.id.tv_team_detail_time);
        tvTeamDetailSlogan = findViewById(R.id.tv_team_detail_slogan);
        tvTeamDetailType=findViewById(R.id.tv_team_detail_type);

        ivTeamDetailLogo = findViewById(R.id.iv_team_detail_logo);

        lvTeamDetailMember = findViewById(R.id.lv_team_detail_member);

        btnTeamDetailDissolve = findViewById(R.id.btn_team_detail_dissolve);

    }

    @Override
    protected void onDestroy() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
