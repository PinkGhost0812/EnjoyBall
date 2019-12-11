package com.example.lenovo.Activity;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.lenovo.enjoyball.Info;
import com.example.lenovo.enjoyball.R;
import com.example.lenovo.entity.Team;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TeamManageActivity extends AppCompatActivity {

    private ImageView ivTeamManageLogo;

    private TextView tvTeamManageName;
    private TextView tvTeamManageCaptain;
    private TextView tvTeamManageMember;
    private TextView tvTeamManageSlogan;
    private TextView tvTeamManageSave;

    private LinearLayout llTeamManageLogo;
    private LinearLayout llTeamManageName;
    private LinearLayout llTeamManageCaptain;
    private LinearLayout llTeamManageMember;
    private LinearLayout llTeamManageSlogan;

    private Team team;

    private String captain;
    private String memberNum;
    private String logoPath;

    private Intent intent;

    private OkHttpClient okHttpClient;
    private OkHttpClient okHttpClientLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.nonetitle);
        setContentView(R.layout.activity_team_manage);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        team= (Team) getIntent().getSerializableExtra("team");
        captain=getIntent().getStringExtra("captain");
        memberNum=getIntent().getStringExtra("memberNum");

        findView();
        
        setInfo();

        setListeners();

    }
    private void setInfo() {

        RequestOptions options = new RequestOptions()
                .circleCrop();
        Glide.with(TeamManageActivity.this)
                .load(Info.BASE_URL+team.getTeam_logo())
                .apply(options)
                .into(ivTeamManageLogo);

        tvTeamManageName.setText(team.getTeam_name());
        tvTeamManageCaptain.setText(captain);
        tvTeamManageMember.setText(memberNum);
        tvTeamManageSlogan.setText(team.getTeam_slogan());

    }

    private class TeamManageListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_team_manage_logo:
                    ActivityCompat.requestPermissions(TeamManageActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            100);
                    break;
                case R.id.ll_team_manage_name:
                    intent = new Intent();
                    intent.setClass(TeamManageActivity.this, TeamManageNameActivity.class);
                    startActivity(intent);
                    break;
                case R.id.ll_team_manage_captain:
                    break;
                case R.id.ll_team_manage_member:
                    intent =new Intent();
                    intent.setClass(TeamManageActivity.this,TeamManageMemberActivity.class);
                    intent.putExtra("team",team);
                    startActivity(intent);
                    break;
                case R.id.ll_team_manage_slogan:
                    intent = new Intent();
                    intent.setClass(TeamManageActivity.this, TeamManageSloganActivity.class);
                    startActivity(intent);
                    break;
                case R.id.tv_team_manage_save:
                    save();
                    break;

            }
        }
    }

    private void save() {

        //更新球队基本信息
        okHttpClient = new OkHttpClient();
        Gson gson = new GsonBuilder().create();
        String teamJson=gson.toJson(team);
        Request request = new Request.Builder()
                .url(Info.BASE_URL + "team/update?info=" + teamJson)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(getApplicationContext(), "世界上最远的距离就是没网络o(╥﹏╥)o", Toast.LENGTH_SHORT).show();
                Looper.loop();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Looper.prepare();
                if (response.body().string().equals("true")){
                    Toast.makeText
                            (TeamManageActivity.this,"更新成功凹~",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent();
                    intent.setClass(TeamManageActivity.this,MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText
                            (TeamManageActivity.this,"更新失败凹~",Toast.LENGTH_SHORT).show();
                }
                Looper.loop();
            }
        });

        //更新头像信息
        if (logoPath.equals("0")) {
            Log.e("test", "未更新logo");
        } else {
            uploadLogo(logoPath);
        }

        //更新队员信息

    }

    private void uploadLogo(String logoPath) {

        File file = new File(logoPath);
        Log.e("test-upimgpath",logoPath);
        okHttpClientLogo=new OkHttpClient();
        RequestBody body = RequestBody.create(MediaType.parse("image/*"),
                file);
        Request request = new Request.Builder()
                .url(Info.BASE_URL+"team/uploadImg?id="+team.getTeam_id())
                .post(body)
                .build();
        Call call = okHttpClientLogo.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(getApplicationContext(), "世界上最远的距离就是没网络o(╥﹏╥)o", Toast.LENGTH_SHORT).show();
                Looper.loop();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Looper.prepare();
                String data=response.body().string();
                if(data.equals("true")){
                    Toast.makeText(TeamManageActivity.this,"更新logo成功~",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent();
                    intent.setClass(TeamManageActivity.this,MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(TeamManageActivity.this,"更新logo失败~请重试",Toast.LENGTH_SHORT).show();
                }
                Looper.loop();
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void setMsgInfo(Message msg) {
        switch (msg.what) {
            case 31:
                Log.e("testnickname",msg.obj.toString());
                tvTeamManageName.setText(msg.obj.toString());
                team.setTeam_name(msg.obj.toString());
                break;
            case 32:
                tvTeamManageSlogan.setText(msg.obj.toString());
                team.setTeam_slogan(msg.obj.toString());
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100){
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent,200);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == RESULT_OK){
            Uri uri = data.getData();
            Cursor cursor = getContentResolver().query(uri,null,null,
                    null,null);
            if (cursor.moveToFirst()){
                String imgPath = cursor.getString(cursor.getColumnIndex("_data"));
                RequestOptions options = new RequestOptions()
                        .circleCrop();
                Glide.with(TeamManageActivity.this)
                        .load(imgPath)
                        .apply(options)
                        .into(ivTeamManageLogo);
                this.logoPath=imgPath;
            }
        }
    }

    private void setListeners() {

        TeamManageListener teamManageListener=new TeamManageListener();

        llTeamManageLogo.setOnClickListener(teamManageListener);
        llTeamManageName.setOnClickListener(teamManageListener);
        llTeamManageCaptain.setOnClickListener(teamManageListener);
        llTeamManageMember.setOnClickListener(teamManageListener);
        llTeamManageSlogan.setOnClickListener(teamManageListener);

        tvTeamManageSave.setOnClickListener(teamManageListener);
    }

    private void findView() {

        ivTeamManageLogo=findViewById(R.id.iv_team_manage_logo);

        tvTeamManageName=findViewById(R.id.tv_team_manage_name);
        tvTeamManageCaptain=findViewById(R.id.tv_team_manage_captain);
        tvTeamManageMember=findViewById(R.id.tv_team_manage_member);
        tvTeamManageSlogan=findViewById(R.id.tv_team_manage_slogan);
        tvTeamManageSave=findViewById(R.id.tv_team_manage_save);

        llTeamManageLogo=findViewById(R.id.ll_team_manage_logo);
        llTeamManageName=findViewById(R.id.ll_team_manage_name);
        llTeamManageCaptain=findViewById(R.id.ll_team_manage_captain);
        llTeamManageMember=findViewById(R.id.ll_team_manage_member);
        llTeamManageSlogan=findViewById(R.id.ll_team_manage_slogan);

    }

    @Override
    protected void onDestroy() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

}
