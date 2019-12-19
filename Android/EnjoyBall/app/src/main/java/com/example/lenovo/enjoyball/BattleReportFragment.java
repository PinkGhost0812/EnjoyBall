package com.example.lenovo.enjoyball;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lenovo.Activity.NewsDetailActivity;
import com.example.lenovo.entity.News;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BattleReportFragment extends Fragment {
    private List<Map<String, Object>> dataSource = null;
    private OkHttpClient okHttpClient;
    private String jsonarray;
    private List<News> Battlenews;
    private ListView listView;
    private List<String> name;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_battle_report,container,false);

        EventBus.getDefault().register(this);
        listView = view.findViewById(R.id.lv_contestDetail_battle_report);
//        查询并初始化数据

        int gameid = getArguments().getInt("game");
//        查询数据存到list集合里
        FindNews(gameid);


        return view;
    }

    private void init() {


        //用list集合建立Listview
        dataSource = new ArrayList<>();
        for(int i=0;i<1;++i){
            Map<String,Object> map= new HashMap<>();
            map.put("title",Battlenews.get(i).getNews_title());
            map.put("author",name.get(i));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            map.put("time",simpleDateFormat.format(Battlenews.get(i).getNews_time()));
            map.put("image",Battlenews.get(i).getNews_image());
//            map.put("title","这场比赛很精彩");
//            map.put("author","郑文涛");
//            map.put("time","2019-12-04");
//            map.put("image",R.drawable.data);
            dataSource.add(map);
        }

    }

    private void FindNews(int gameid) {
        okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(Info.BASE_URL+"news/findByContest?id="+gameid)
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

                if (jsonarray.equals("false")){
                    Looper.prepare();
                    Toast.makeText(getActivity(),"该比赛无战报嗷~",Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }else{
                    Battlenews = new ArrayList<>();

                    Gson gson = new GsonBuilder()
                            .setDateFormat("yyyy-MM-dd hh:mm:ss")
                            .setPrettyPrinting()
                            .serializeNulls()
                            .create();
                    Type listType = new TypeToken<List<News>>(){}.getType();
                    Battlenews= gson.fromJson(jsonarray,listType);
                    Log.e("time",Battlenews.toString());
                    EventBus.getDefault().post("查询成功");
                }
            }
        });
    }
    private void FindName(int userid, final int num) {
        okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(Info.BASE_URL+"user/find?id="+userid)
                .build();


        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                name = new ArrayList<>();
                jsonarray = response.body().string();
                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd hh:mm:ss")
                        .setPrettyPrinting()
                        .serializeNulls()
                        .create();
                User author = gson.fromJson(jsonarray,User.class);
                name.add(author.getUser_nickname());
                Log.e("name",name.toString());
                if(num==Battlenews.size()-1)
                    EventBus.getDefault().post("作者名字已查到");
            }
        });
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void inits(String msg){
        switch (msg){
            case "查询成功":
                for(int i=0;i<Battlenews.size();++i){
                    FindName(Battlenews.get(i).getNews_author(),i);
                }
                break;
            case "作者名字已查到":
                init();



                final BattleReportAdapter adapter = new BattleReportAdapter(
                        this.getContext(),
                        dataSource,
                        R.layout.battle_report_item
                );

                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent();
                        intent.setClass(getContext(), NewsDetailActivity.class);
                        intent.putExtra("id",Battlenews.get(position).getNews_id().toString());
                        startActivity(intent);
                    }
                });
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
