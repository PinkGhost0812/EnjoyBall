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

import com.example.lenovo.Activity.NewsDetailActivity;
import com.example.lenovo.Adapter.DemandAdapter;
import com.example.lenovo.enjoyball.Info;
import com.example.lenovo.enjoyball.R;
import com.example.lenovo.entity.DemandInfo;
import com.example.lenovo.entity.News;
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

public class TimeFragment extends Fragment {

    private List<DemandInfo> dataSource = null;
    private List<DemandInfo> demandInfoList = null;
    private OkHttpClient okHttpClient;
    private ListView listView;
    private Call call;
    private String[] sql = {"select * from demand_info where demand_class = 0","select * from demand_info where demand_class = 1","select * from demand_info where demand_class = 2",
            "select * from demand_info where demand_class = 3","select * from demand_info where demand_class = 4"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.tab_demand_layout,
                container, false);
        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void setContent(List<DemandInfo> list){

        initData(list);

        DemandAdapter adapter = new DemandAdapter(
                getContext(),
                dataSource,
                R.layout.listview_item_demand
        );

        listView.setAdapter(adapter);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //initData();
        listView = getActivity().findViewById(R.id.lv_time_demand);

        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        int x = 0;
        x = (int)getArguments().get("ball");
        Log.e("收到",x+"");
        okHttpClient =new OkHttpClient();
        demandInfoList = new ArrayList<>();

        if (x==0){
            Request request = new Request.Builder().url(Info.BASE_URL + "appointment/list").build();
            call = okHttpClient.newCall(request);
        }else {
            Request request = new Request.Builder().url(Info.BASE_URL + "appointment/find?sql="+sql[x-1]).build();
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
                Log.e("appointment = ", n);
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                Type listType = new TypeToken<List<DemandInfo>>(){}.getType();
                //newsList = new ArrayList<>();
                demandInfoList = gson.fromJson(n,listType);
                // Log.e("标题22",n);
                EventBus.getDefault().post(demandInfoList);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("详情","success");

                Intent intent = new Intent();
                intent.putExtra("id",dataSource.get(position).getDemand_id());
                intent.setClass(getActivity(), NewsDetailActivity.class);
                startActivity(intent);

            }
        });
    }

    private void initData(List<DemandInfo> list){
        dataSource = new ArrayList<>();
        for (int i = 0;i<list.size();++i){
            dataSource.add(list.get(i));

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
//        String[] times = {"2019/11/8      14:00","2019/11/8      14:00","2019/11/8      14:00","2019/11/8      14:00"};
//        String[] place = {"河北师范大学篮球场","河北师范大学篮球场","河北师范大学篮球场","河北师范大学篮球场"};
//        String[] team = {"巴塞罗那","巴塞罗那","巴塞罗那","巴塞罗那"};
//        String[] dp = {"缺少2名队友，求躺之ID反击拿福利君安法律","缺少2名队友，求躺之ID反击拿福利君安法律","缺少2名队友，求躺","缺少2名队友，求躺"};
//        //dataSource = new ArrayList<>();
//        for(int i=0; i<times.length; ++i) {
//            Map<String, Object> map = new HashMap<>();
//            map.put("time", times[i]);
//            map.put("place", place[i]);
//            map.put("team", team[i]);
//            map.put("dp", dp[i]);
//            //dataSource.add(map);
//        }
//    }
}
