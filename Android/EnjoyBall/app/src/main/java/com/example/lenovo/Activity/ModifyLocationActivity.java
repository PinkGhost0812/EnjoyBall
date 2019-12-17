package com.example.lenovo.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.example.lenovo.Adapter.ModifyLocationAdapter;
import com.example.lenovo.enjoyball.Info;
import com.example.lenovo.enjoyball.R;
import com.example.lenovo.entity.DemandInfo;
import com.example.lenovo.entity.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ModifyLocationActivity extends AppCompatActivity {
    private OkHttpClient okHttpClient;
    private User user;
    private DemandInfo demandInfo;
    private List<Map<String,Object>> datasource;
    private List<User> userlistA;
    private List<User> userlistB;
    private List<String> nickname;
    private List<String> headsphoto;
    private int flag = -1;
    private int a = -1,b=-1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifylocation);

        EventBus.getDefault().register(this);

        //获取intent数据
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        demandInfo = (DemandInfo) intent.getSerializableExtra("demandinfo");
        nickname = Arrays.asList(new String[demandInfo.getDemand_num()]);
        headsphoto = Arrays.asList(new String[demandInfo.getDemand_num()]);
        //获取数据库数据并应用
        FindUserAByDemandinfo(demandInfo.getDemand_id());


    }

    private void FindUserAByDemandinfo(int demandid) {
        okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Info.BASE_URL+"appointment/teamA?demandId="+demandid)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                userlistA = null;
                String jsonstr = response.body().string();
                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd hh:mm:ss")
                        .setPrettyPrinting()
                        .serializeNulls()
                        .create();
                Log.e("mes3",jsonstr);
                if(jsonstr.equals("false")){
                    EventBus.getDefault().post("A队信息已收到");
                }else{
                    userlistA = new ArrayList<>();
                    Type listType = new TypeToken<List<User>>(){}.getType();
                    userlistA = gson.fromJson(jsonstr,listType);
                    EventBus.getDefault().post("A队信息已收到");
                }
            }
        });
    }

    private void FindUserBByDemandinfo(int demandid) {
        Request request = new Request.Builder()
                .url(Info.BASE_URL+"appointment/teamB?demandId="+demandid)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonstr = response.body().string();
                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd hh:mm:ss")
                        .setPrettyPrinting()
                        .serializeNulls()
                        .create();
                Log.e("mes2",jsonstr);
                if(jsonstr.equals("false")){
                    EventBus.getDefault().post("B队信息已收到");
                }else{
                    userlistB = new ArrayList<>();
                    Type listType = new TypeToken<List<User>>(){}.getType();
                    userlistB = gson.fromJson(jsonstr,listType);
                    EventBus.getDefault().post("B队信息已收到");
                }

            }
        });
    }

    private void init() {
        if(userlistA.size()!=0){
            for(int i=0;i<userlistA.size()-1;++i){
                nickname.set(2*i,userlistA.get(i).getUser_nickname());
                headsphoto.set(2*i,userlistA.get(i).getUser_headportrait());
            }
        }
        Log.e("user",userlistB.toString());
        if(userlistB.size()!=0){
            for(int i=0;i<userlistB.size()-1;++i){
                nickname.set(2*i+1,userlistB.get(i).getUser_nickname());
                headsphoto.set(2*i+1,userlistB.get(i).getUser_headportrait());
            }
        }


        datasource = new ArrayList<>();
        for(int i=0;i<demandInfo.getDemand_num();++i){
            Map<String,Object> map = new HashMap();
            map.put("nickname",nickname.get(i));
            map.put("headsphoto",headsphoto.get(i));

            Log.e("nickname"+i,Integer.toString(flag));
            datasource.add(map);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void inits(String msg){
        switch (msg){
            case "A队信息已收到":
                FindUserBByDemandinfo(demandInfo.getDemand_id());
                break;
            case "B队信息已收到":
                //初始化数据
                init();
                Log.e("mes1",nickname.toString());
                Log.e("mes2",headsphoto.toString());
                final GridView gridView = findViewById(R.id.gv_modifylocation);
                final ModifyLocationAdapter adapter = new ModifyLocationAdapter(
                        this,
                        datasource,
                        R.layout.gridview_item_location
                );
                for(int i=0;i<nickname.size();++i){
                    if(nickname.get(i)!=null&&nickname.get(i).equals(user.getUser_nickname())){
                        flag = i;
                        if(i%2==0){
                            a = 1;
                        }else{
                            a=0;
                        }
                    }
                }
                Log.e("fa",Integer.toString(flag));
                gridView.setAdapter(adapter);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if(datasource.get(position).get("nickname")==null){
                            datasource.get(position).put("nickname",user.getUser_nickname());
                            datasource.get(position).put("headsphoto",user.getUser_headportrait());
                            datasource.get(flag).put("nickname",null);
                            datasource.get(flag).put("headsphoto",null);
                            if(position%2==0){
                                b=1;
                            }else{
                                b=0;
                            }
                            flag = position;
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
                Button btn_save = findViewById(R.id.btn_modifylocation_save);
                btn_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(a!=b&&b==1){
                            changeteam(demandInfo.getDemand_teamb(),user.getUser_id(),demandInfo.getDemand_teama());
                        }else if(a!=b&&b==0){
                            changeteam(demandInfo.getDemand_teama(),user.getUser_id(),demandInfo.getDemand_teamb());
                        } else{
                            finish();
                        }
                    }
                });
                Button btn_cancel = findViewById(R.id.btn_modifylocation_cancel);
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
                break;
            case "已保存":
                finish();

        }
    }

    private void changeteam(Integer demand_teama, Integer user_id, Integer demand_teamb) {
        Request request = new Request.Builder()
                .url(Info.BASE_URL+"appointment/change?teama="+demand_teama+"&userId="+user_id+"&teamb="+demand_teamb)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonstr = response.body().string();
                Log.e("ok",jsonstr);
                EventBus.getDefault().post("已保存");

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
