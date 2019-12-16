package com.example.lenovo.Fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.Adapter.GameAdapter;
import com.example.lenovo.enjoyball.Info;
import com.example.lenovo.enjoyball.R;
import com.example.lenovo.entity.Contest;
import com.example.lenovo.entity.News;
import com.example.lenovo.entity.Team;
import com.example.lenovo.entity.TeamAndContest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GameFragment extends Fragment {

    private TextView tvDate;
    private List<TeamAndContest> gameList = null;
    private OkHttpClient okHttpClient;
    private OkHttpClient srokHttpClient;
    private GameAdapter adapter;
    private ListView listView;
    private SmartRefreshLayout refreshLayout;
    private Call call;
    private String[] sql = {"select * from game_info where game_class = 0","select * from game_info where game_class = 1","select * from game_info where game_class = 2","select * from game_info where game_class = 3"
            ,"select * from game_info where game_class = 4"};
    private int x = 0;
    private int page = 1;


    private List<TeamAndContest> dataSource = null;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
//        setHasOptionsMenu(true);
        View view =  inflater.inflate(R.layout.tab_game_layout,
                container, false);
        return view;
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        getActivity().getMenuInflater().inflate(R.menu.game_menu,menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.item_game_my:
//                Toast.makeText(getContext(),item.getTitle(),Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.item_game_school:
//                Toast.makeText(getContext(),item.getTitle(),Toast.LENGTH_SHORT).show();
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void setContent(String msgs){
        switch (msgs){
            case "game":
                initDate(gameList);

                adapter = new GameAdapter(
                        getContext(),
                        dataSource,
                        R.layout.listview_item_game
                );

                listView.setAdapter(adapter);
                break;
            case "page":
                break;
        }


    }

    @Override
    public void onResume() {
        super.onResume();

        findView();
        setListeners();
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        x = (int)getArguments().get("ball");
        Log.e("收到",x+"");
        int y = x-1;
        page = 1;
        okHttpClient =new OkHttpClient();
        gameList = new ArrayList<>();

        if (x==0){
            Request request = new Request.Builder().url(Info.BASE_URL + "contest/list?page="+page).build();
            call = okHttpClient.newCall(request);

        }else {
            Request request = new Request.Builder().url(Info.BASE_URL + "contest/find?cls="+y+"&page="+page).build();
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
                Type listType = new TypeToken<List<TeamAndContest>>(){}.getType();
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
                intent.putExtra("id",dataSource.get(position).getContest().getGame_id());
                intent.setClass(getActivity(), Team.class);
                startActivity(intent);

            }
        });

    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        //initData();
//
//
//    }

    private void findView(){

        tvDate = getActivity().findViewById(R.id.tv_game_date);
        tvDate.setText(getTime()+"");

        listView = getActivity().findViewById(R.id.lv_game_game);
        refreshLayout = getActivity().findViewById(R.id.sr_game_refresh);
    }

    private void setListeners(){
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                GameListTask task = new GameListTask();
                task.execute();
            }
        });
    }

    private class GameListTask extends AsyncTask{
        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            //更新视图
            srokHttpClient =new OkHttpClient();
            gameList = new ArrayList<>();

            int y = x-1;
            page++;
            if (x==0){
                Request request = new Request.Builder().url(Info.BASE_URL + "contest/list?page="+page).build();
                call = srokHttpClient.newCall(request);

            }else {
                Request request = new Request.Builder().url(Info.BASE_URL + "contest/find?cls="+y+"&page="+page).build();
                call = srokHttpClient.newCall(request);
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
                    Type listType = new TypeToken<List<TeamAndContest>>(){}.getType();
                    //newsList = new ArrayList<>();
                    gameList = gson.fromJson(n,listType);
                    // Log.e("标题22",n);
                    EventBus.getDefault().post("page");
                }
            });

        }
    }

    private static String getTime(){
        SimpleDateFormat fromatter = new SimpleDateFormat("yyyy-MM-dd EEEE");
        Date time = new Date(System.currentTimeMillis());
        return fromatter.format(time);
    }

    private void initDate(List<TeamAndContest> gameList){
        dataSource = new ArrayList<>();
        for (int i=0;i<gameList.size();++i){
            dataSource.add(gameList.get(i));
            Log.e("test",dataSource.get(i).getTeamMap().toString());
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