package com.example.lenovo.Activity;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.lenovo.enjoyball.Info;
import com.example.lenovo.enjoyball.R;
import com.example.lenovo.entity.Team;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TeamCreateActivity extends AppCompatActivity {

    private ImageView ivTeamCreateLogo;
    private ImageView ivTeamCreateMember;
    private ImageView ivTeamCreateAddress;

    private EditText etTeamCreateName;
    private EditText etTeamCreateSlogan;

    private Spinner spTeamCreateFixture;

    private Button btnTeamCreateSave;

    private Team team;

    private String imgPath="0";

    private OkHttpClient okHttpClient;
    private OkHttpClient okHttpClientLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.nonetitle);
        setContentView(R.layout.activity_team_create);

        findView();

        team=new Team();

        setListeners();

    }

    private void setListeners() {

        TeamCreateListener listener = new TeamCreateListener();

        ivTeamCreateLogo.setOnClickListener(listener);
        btnTeamCreateSave.setOnClickListener(listener);
        spTeamCreateFixture.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Log.e("test",spTeamCreateFixture.getSelectedItem().toString());
                team.setTeam_class(spTeamCreateFixture.getSelectedItemPosition());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private class TeamCreateListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_team_create_logo:
                    ActivityCompat.requestPermissions(TeamCreateActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            100);
                    break;
                case R.id.et_team_create_name:
                    break;
                case R.id.iv_team_create_member:
                    break;
                case R.id.iv_team_create_address:
                    break;
                case R.id.et_team_create_slogan:
                    break;
                case R.id.btn_team_create_save:
                    saveInfo();
                    break;
            }
        }
    }

    private void saveInfo() {

        Date curDate = new Date(System.currentTimeMillis());
        Log.e("test",curDate.toString());

        team.setTeam_name(etTeamCreateName.getText().toString());
        team.setTeam_slogan(etTeamCreateSlogan.getText().toString());
        team.setTeam_captain(((Info)getApplicationContext()).getUser().getUser_id());
        team.setTeam_time(curDate);

        if (imgPath.equals("0")||team.getTeam_name().equals("")||team.getTeam_name()==null){
            Toast.makeText(this,"请完善必要信息哦~",Toast.LENGTH_SHORT).show();
        }else{
            uploadInfo(team);
        }

    }

    private void uploadInfo(Team team) {

        okHttpClient = new OkHttpClient();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        String teamJson=gson.toJson(team);
        Request request = new Request.Builder()
                .url(Info.BASE_URL + "team/regist?info=" + teamJson)
                .build();
        Log.e("test team json",teamJson.toString());
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(getApplicationContext(), "世界上最远的距离就是没网络凹~", Toast.LENGTH_SHORT).show();
                Looper.loop();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Looper.prepare();
                String data=response.body().string();
                if (data.equals("false")){
                    Toast.makeText(TeamCreateActivity.this,"创建失败~",Toast.LENGTH_SHORT);
                }else {
                    Log.e("test",data.toString());
                    uploadLogo(imgPath,data);
                    Toast.makeText
                            (TeamCreateActivity.this,"创建成功~",Toast.LENGTH_SHORT);
                    Intent intent=new Intent();
                    intent.setClass(TeamCreateActivity.this,MainActivity.class);
                    startActivity(intent);
                }
                Looper.loop();
            }
        });

    }

    private void uploadLogo(String imgPath,String data) {

        File file = new File(imgPath);
        Log.e("test-upimgpath",imgPath);
        okHttpClientLogo=new OkHttpClient();
        RequestBody body = RequestBody.create(MediaType.parse("image/*"),
                file);
        Request request = new Request.Builder()
                .url(Info.BASE_URL+"team/uploadImg?id="+data)
                .post(body)
                .build();
        Call call = okHttpClientLogo.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(getApplicationContext(), "世界上最远的距离就是没网络凹~", Toast.LENGTH_SHORT).show();
                Looper.loop();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("test",response.body().string());
            }
        });

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
                Glide.with(TeamCreateActivity.this)
                        .load(imgPath)
                        .apply(options)
                        .into(ivTeamCreateLogo);
                this.imgPath=imgPath;
            }
        }
    }

    private void findView() {

        ivTeamCreateLogo = findViewById(R.id.iv_team_create_logo);
        ivTeamCreateMember = findViewById(R.id.iv_team_create_member);
        ivTeamCreateAddress = findViewById(R.id.iv_team_create_address);

        etTeamCreateName = findViewById(R.id.et_team_create_name);
        etTeamCreateSlogan = findViewById(R.id.et_team_create_slogan);

        spTeamCreateFixture = findViewById(R.id.sp_team_create_fixture);

        btnTeamCreateSave = findViewById(R.id.btn_team_create_save);

    }

}