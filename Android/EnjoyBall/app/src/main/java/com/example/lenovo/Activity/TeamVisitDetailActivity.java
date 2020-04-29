package com.example.lenovo.Activity;

import android.content.DialogInterface;
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
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TeamVisitDetailActivity extends AppCompatActivity {

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

    private List<User> memberList;
    private ArrayList<String> list = new ArrayList<>();

    private OkHttpClient okHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.nonetitle);
        setContentView(R.layout.activity_team_detail);

        user=((Info)getApplicationContext()).getUser();

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        if (getIntent().getStringExtra("invite")!=null && getIntent().getStringExtra("invite").equals("invite")){
            android.app.AlertDialog.Builder adBuilder = new android.app.AlertDialog.Builder(TeamVisitDetailActivity.this);
            adBuilder.setTitle("加 入？");
            adBuilder.setMessage("球队邀请你加入？");
            adBuilder.setPositiveButton("我已准备好", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    okHttpClient = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(Info.BASE_URL + "team/inviteOk?teamId="+team.getTeam_id()+"&userId=" + user.getUser_id())
                            .build();
                    Call call = okHttpClient.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Looper.prepare();
                            Toast.makeText(TeamVisitDetailActivity.this, "获取队伍详细信息失败~", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            Looper.prepare();
                            String data = response.body().string();
                            if (data.equals("true")){
                                Toast.makeText(TeamVisitDetailActivity.this,"加入成功~",Toast.LENGTH_SHORT).show();
//                                Intent intent=new Intent();
//                                intent.setClass(TeamVisitDetailActivity.this,MainActivity.class);
//                                startActivity(intent);
                                finish();
                                Looper.loop();
                            }else{
                                Toast.makeText(TeamVisitDetailActivity.this,"加入失败~",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
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

        findView();

        btnTeamDetailDissolve.setVisibility(View.GONE);
        
        team= (Team) getIntent().getSerializableExtra("team");
        
        getCaptain();

        Log.e("test team",team.toString());

        getMemberList();

        setInfo();

    }

    private void getCaptain() {

        okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Info.BASE_URL + "user/find?id=" + team.getTeam_captain())
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                //Toast.makeText(TeamVisitDetailActivity.this, "获取队伍详细信息失败~", Toast.LENGTH_SHORT).show();
                Looper.loop();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String data = response.body().string();
                Gson gson = new GsonBuilder().create();
                captain = gson.fromJson(data, User.class);
                Message msg=new Message();
                msg.what=55;
                msg.obj=captain.getUser_nickname();
                EventBus.getDefault().postSticky(msg);

            }
        });

    }

    private void getMemberList() {

        okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Info.BASE_URL + "team/findMember?id=" + team.getTeam_id())
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                //Toast.makeText(TeamVisitDetailActivity.this, "获取队伍详细信息失败~", Toast.LENGTH_SHORT).show();
                Looper.loop();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String data = response.body().string();
                Gson gson = new GsonBuilder().create();
                Type listType = new TypeToken<List<User>>() {
                }.getType();
                memberList = gson.fromJson(data, listType);
                Message msg = new Message();
                msg.obj = memberList;
                msg.what = 69;
                Log.e("test", memberList.toString());
                EventBus.getDefault().post(msg);
            }
        });
    }

    private void setInfo() {
        tvTeamDetailName.setText(team.getTeam_name());
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
        Glide.with(TeamVisitDetailActivity.this)
                .load(Info.BASE_URL + team.getTeam_logo())
                .apply(options)
                .into(ivTeamDetailLogo);

        Log.e("test DetailMemberList", list.toString());
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

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void initMsgData(Message msg) {

        if (msg.what == 69) {
            Log.e("test 69" ,msg.obj.toString());
            memberList = (List<User>) msg.obj;
            for (int i = 0; i < memberList.size(); i++) {
                list.add(memberList.get(i).getUser_nickname());
            }
            TeamDetailAdapter membersAdapter =
                    new TeamDetailAdapter(this, list, R.layout.listview_item_team_detail);
            membersAdapter.notifyDataSetChanged();
            lvTeamDetailMember.setAdapter(membersAdapter);
            membersAdapter.notifyDataSetChanged();
        }else if (msg.what==55){
            Log.e("test 55" ,msg.obj.toString());
            tvTeamDetailCaptain.setText(msg.obj.toString());
        }

    }

    @Override
    protected void onDestroy() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

}
