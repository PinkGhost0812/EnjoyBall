package com.example.lenovo.enjoyball;

import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lenovo.entity.DemandInfo;
import com.example.lenovo.entity.Team;
import com.example.lenovo.entity.User;
import com.google.gson.Gson;
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

public class JoinAgreementActivity extends AppCompatActivity {
    private GridView gv_joinagreement;
    private List<Map<String, Object>> datasource = null;
    private TextView tv_type;
    private TextView tv_leader;
    private TextView tv_address;
    private TextView tv_time;
    private TextView tv_message;
    private AgreementAdapter adapter = null;
    private  Team team = null;
    private DemandInfo demandInfo;
    private String url = Info.BASE_URL;
    private static int POST_DEMANDINFO = 1;
    private static int POST_LEADER = 2;
    private static  int POST_GETTEAMB=3;
    private static int INITDATASOURCE=4;
    private OkHttpClient okHttpClient = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joinagreement);
        EventBus.getDefault().register(this);
        okHttpClient = new OkHttpClient();
        final Intent intent = getIntent();
        String demandId = intent.getStringExtra("id");
        getView();
        getDemandInfo(demandId);
        gv_joinagreement.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (demandInfo.getDemand_oom()==0){
                    if (datasource.get(i).get("object")==null){
                        android.app.AlertDialog.Builder adBuilder = new android.app.AlertDialog.Builder(JoinAgreementActivity.this);
                        adBuilder.setTitle("确认");
                        adBuilder.setMessage("确认加入约球吗");
                        adBuilder.setPositiveButton("加入", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.e("create？", "true");
                                sendToServer();
                            }
                        });
                        adBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.e("create?", "false");
                            }
                        });
                        android.app.AlertDialog alertDialog = adBuilder.create();
                        alertDialog.show();
                    }else{
                        User user = (User)datasource.get(i).get("object");
                        Intent intent1 = new Intent();
                        intent.putExtra("id",user.getUser_id());
                        startActivity(intent1);
                    }
                }else if (demandInfo.getDemand_oom()==1){
                    if (datasource.get(i).get("object")==null){
                        getTeams();
                        if (team!=null){
                            sendToServer();
                        }else {
                            Toast.makeText(JoinAgreementActivity.this,"您没有该类型队伍，无法参加",Toast.LENGTH_SHORT);
                        }

                    }else {
                        Intent intent1 = new Intent(JoinAgreementActivity.this,TeamDetailActivity.class);
                        intent1.putExtra("id",demandInfo.getDemand_teamb());
                        startActivity(intent1);
                    }
                }

            }
        });


    }
    private void getTeams() {
         Team myTeam = null;
        int id = 0;
        //int id = getApplication().getUser().getUser_id();
        url = url+"team/findByIdCls?id="+id+"&cls="+demandInfo.getDemand_class();
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
                if (!teamJson.equals("false")){
                    team = new Gson().fromJson(teamJson,Team.class);
                }


            }
        });

    }

    private void sendToServer() {
    }

    //获取约球信息
    private void getDemandInfo(final String demandId) {
        Request request = new Request.Builder()
                .url(url + "appoint/findById?id=" + demandId)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(JoinAgreementActivity.this, "服务器连接失败", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonInfo = response.body().string();
                demandInfo = new Gson().fromJson(jsonInfo, DemandInfo.class);
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
    private void getTeam(final DemandInfo info,String teamName){
        int id = 0;
        int offset = 0;
        if (teamName.equals("a")){
            id = info.getDemand_teama();
            offset = 0;
        }else if (teamName.equals("b")){
            id = info.getDemand_teamb();
            offset = 1;
        }
        Request.Builder builder = new Request.Builder();
        Request request = null;
        if (info.getDemand_oom()==0){
            datasource = new ArrayList<>(info.getDemand_num()*2);
            request = builder.url(url+"user/findByDTeamId?id="+id).build();
        }else if(info.getDemand_oom()==1) {
            datasource = new ArrayList<Map<String,Object>>(2);
            request = builder.url(url+"user/findById?id="+info.getDemand_teama()).build();
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
                if (info.getDemand_oom()==0){
                    datasource = new ArrayList<Map<String,Object>>(info.getDemand_num());
                    Type type = new TypeToken<List<User>>(){}.getType();
                    List<User> users = new Gson().fromJson(json,type);
                    for (int i = 0; i <users.size();i++){
                        ImageView imageView = null;
                        Glide.with(JoinAgreementActivity.this)
                                .load(users.get(i).getUser_headportrait())
                                .into(imageView);
                        Drawable drawable = imageView.getDrawable();
                        Map map = new HashMap<String,Object>();
                        map.put("name",users.get(i).getUser_nickname());
                        map.put("head",drawable);
                        map.put("object",users.get(i));
                        //保证同一队的队员在同一列，A队在左边,B队在右边
                        datasource.add(i*2+ finalOffset,map);
                        Message message = new Message();
                        if (finalOffset==0){
                            message.what = POST_GETTEAMB;
                            message.obj = info;
                        }else if (finalOffset==1){
                            message.what = INITDATASOURCE;
                        }
                        EventBus.getDefault().post(message);

                    }




                }else if (info.getDemand_oom()==1){
                    datasource = new ArrayList<Map<String,Object>>(2);
                    Team team = new Gson().fromJson(json,Team.class);
                    ImageView imageView = null;
                    Glide.with(JoinAgreementActivity.this)
                            .load(team.getTeam_logo())
                            .into(imageView);
                    Drawable drawable = imageView.getDrawable();
                    Map<String,Object> map = new HashMap<String,Object>();
                    map.put("name",team.getTeam_name());
                    map.put("head",drawable);
                    map.put("object",team);
                    datasource.add(0,map);



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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setView(Message message) {
        switch (message.what) {
            case 1:
                DemandInfo info = (DemandInfo) message.obj;
                tv_time.setText(info.getDemand_time().toString());
                tv_message.setText(info.getDemand_description());
                tv_address.setText(info.getDemand_place());
                String[] types = getResources().getStringArray(R.array.type);
                tv_type.setText(types[info.getDemand_class()]);
                getLeader(info.getDemand_user());
                getTeam(info,"a");

                break;
            case 2:
                User user = (User)message.obj;
                tv_leader.setText(user.getUser_nickname());
                break;
            case 3:
                DemandInfo info1 = (DemandInfo)message.obj;
                getTeam(info1,"b");
                break;
            case 4:
                Map<String,Object> map = new HashMap<String, Object>();
                map.put("name","虚位以待");
                map.put("head",getResources().getDrawable(R.drawable.head_girl));
                map.put("obj",null);
                for (int i = 0 ; i<datasource.size();i++){
                    if (datasource.get(i)==null){
                        datasource.add(i,map);
                    }
                }
                adapter = new AgreementAdapter(datasource, R.layout.listview_item_agreement, this);
                gv_joinagreement.setAdapter(adapter);
        }


    }

}
