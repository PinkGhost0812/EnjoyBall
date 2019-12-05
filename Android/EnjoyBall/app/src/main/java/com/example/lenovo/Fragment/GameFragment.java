package com.example.lenovo.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.lenovo.Adapter.GameAdapter;
import com.example.lenovo.enjoyball.Info;
import com.example.lenovo.enjoyball.R;
import com.example.lenovo.entity.Contest;
import com.example.lenovo.entity.News;
import com.example.lenovo.entity.Team;
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

import cn.smssdk.gui.util.Const;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GameFragment extends Fragment {

    private List<Contest> gameList = null;
    private OkHttpClient okHttpClient;
    private ListView listView;
    private Call call;

    private List<Contest> dataSource = null;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.tab_game_layout,
                container, false);
        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void setContent(List<Contest> list){

        initData(list);

        GameAdapter adapter = new GameAdapter(
                getContext(),
                dataSource,
                R.layout.listview_item_home
        );

        listView.setAdapter(adapter);

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //initData();

        ListView listView = getActivity().findViewById(R.id.lv_game_game);

        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        int x = 0;
        x = (int)getArguments().get("ball");
        Log.e("收到",x+"");
        okHttpClient =new OkHttpClient();
        gameList = new ArrayList<>();

        if (x==0){
            Request request = new Request.Builder().url(Info.BASE_URL + "contest/list").build();
            call = okHttpClient.newCall(request);

        }else {
            Request request = new Request.Builder().url(Info.BASE_URL + "contest/find?sql="+x).build();
            call = okHttpClient.newCall(request);
            Log.e("x = ", x+"");
        }
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String n = response.body().string();
                Log.e("contest = ", n);
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                Type listType = new TypeToken<List<Contest>>(){}.getType();
                //newsList = new ArrayList<>();
                gameList = gson.fromJson(n,listType);
                // Log.e("标题22",n);
                EventBus.getDefault().post(gameList);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("详情","success");

                Intent intent = new Intent();
                intent.putExtra("id",dataSource.get(position).getGame_id());
                intent.setClass(getActivity(), Team.class);
                startActivity(intent);

            }
        });

    }

    private void initData(List<Contest>gameList){
        dataSource = new ArrayList<>();
        for (int i=0;i<gameList.size();++i){
            dataSource.add(gameList.get(i));
            Log.e("test",dataSource.toString());
        }
    }

    @Override
    public void onDestroy() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        super.onDestroy();
    }



//    private void initData() {
//
//        String[] times = {"01：00 德甲 第10轮","01：00 德甲 第10轮","01：00 德甲 第10轮","01：00 德甲 第10轮","01：00 德甲 第10轮"};
//        String[] team1 = {"奥格斯堡","奥格斯堡","奥格斯堡","奥格斯堡","奥格斯堡"};
//        String[] team2 = {"沙尔克04","沙尔克04","沙尔克04","沙尔克04","沙尔克04"};
//        String[] score1 = {"3","2","3","0","0"};
//        String[] score2 = {"2","1","2","1","0"};
//        String[] state = {"已完场","已完场","已完场","进行中","未开始"};
//        //dataSource = new ArrayList<>();
//        for(int i=0; i<times.length; ++i) {
//            Map<String, Object> map = new HashMap<>();
//            map.put("time", times[i]);
//            map.put("team1", team1[i]);
//            map.put("team2", team2[i]);
//            map.put("score1", score1[i]);
//            map.put("score2", score2[i]);
//            map.put("state", state[i]);
//            //dataSource.add(map);
//        }
//    }
}