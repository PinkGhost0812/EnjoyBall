package com.example.lenovo.enjoyball;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class BattleArrayFragment extends Fragment {

    int t1;
    int t2;
    private List<Map<String, Object>> dataSource = null;
    private OkHttpClient okHttpClient;
    private Team team1;
    private Team team2;
    private String jsonarray;
    private List<User> team1Member = new ArrayList<>();
    private List<User> team2Member = new ArrayList<>();
    private ListView listView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_battle_array,container,false);
        listView = view.findViewById(R.id.lv_contestDetail_battle_array);
        EventBus.getDefault().register(this);
        t1 = getArguments().getInt("team1");
        t2 = getArguments().getInt("team2");
        FindTeamMember1(t1);


        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void init() {
        dataSource = new ArrayList<>();
        for(int i=0;i<team1Member.size();++i){
            Map<String,Object> map= new HashMap<>();
            map.put("nickname1",team1Member.get(i).getUser_nickname());
            map.put("nickname2",team2Member.get(i).getUser_nickname());
            map.put("photo1",team1Member.get(i).getUser_headportrait());
            map.put("photo2",team2Member.get(i).getUser_headportrait());
            dataSource.add(map);
        }
    }

    private void FindTeamMember1(Integer teamid) {
        okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(Info.BASE_URL+"user/findByTeamId?teamId="+teamid)
                .build();


        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                jsonarray = response.body().string();
                Log.e("pep",jsonarray);
                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd hh:mm:ss")
                        .setPrettyPrinting()
                        .serializeNulls()
                        .create();
                Type listType = new TypeToken<List<User>>(){}.getType();
                team1Member = gson.fromJson(jsonarray,listType);
                EventBus.getDefault().post("接收到队伍1的人");
            }
        });
    }

    private void FindTeamMember2(Integer teamid) {
        okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(Info.BASE_URL+"user/findByTeamId?teamId="+teamid)
                .build();


        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                jsonarray = response.body().string();
                Log.e("pep1",jsonarray);
                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd hh:mm:ss")
                        .setPrettyPrinting()
                        .serializeNulls()
                        .create();
                Type listType = new TypeToken<List<User>>(){}.getType();
                team2Member = gson.fromJson(jsonarray,listType);
                EventBus.getDefault().post("接收到队伍2的人");
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void inits(String msg){
        switch (msg){
            case "接收到队伍1的人":
                FindTeamMember2(t2);
                break;
            case "接收到队伍2的人":
                init();

                final BattleArrayAdapter adapter = new BattleArrayAdapter(
                        team1Member,
                        team2Member,
                        this.getContext(),
                        dataSource,
                        R.layout.battle_array_item
                );
                listView.setAdapter(adapter);

        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
