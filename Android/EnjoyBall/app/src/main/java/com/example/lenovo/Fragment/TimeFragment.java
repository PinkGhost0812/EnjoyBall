package com.example.lenovo.Fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.lenovo.Activity.CreateAgreementActivity;
import com.example.lenovo.Activity.JoinAgreementActivity;
import com.example.lenovo.Activity.NewsDetailActivity;
import com.example.lenovo.Adapter.DemandAdapter;
import com.example.lenovo.enjoyball.Info;
import com.example.lenovo.enjoyball.R;
import com.example.lenovo.entity.DemandInfo;
import com.example.lenovo.entity.News;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

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
    private OkHttpClient srokHttpClient;
    private ListView listView;
    private DemandAdapter adapter;
    private SmartRefreshLayout refreshLayout;
    private Call call;
    private boolean isGetData = false;
    private int x = 0;
    private int page = 1;
    private FloatingActionButton fabAdd;

//    private Handler handler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what)
//        }
//    };

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
    public void setContent(String msgs){
        switch (msgs){
            case "first":
                initData(demandInfoList);
                adapter = new DemandAdapter(
                        getContext(),
                        dataSource,
                        R.layout.listview_item_demand
                );
                listView.setAdapter(adapter);
                break;
            case "page":
                initPage(demandInfoList);
                adapter.notifyDataSetChanged();
                //结束加载更多的动画
                refreshLayout.finishLoadMore();

        }
    }

    @Override
    public void onResume() {
        super.onResume();


        findVIew();
        setListeners();
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        x = (int)getArguments().get("ball");
        Log.e("收到",x+"");
        int y = x-1;
        page = 1;
        okHttpClient =new OkHttpClient();
        demandInfoList = new ArrayList<>();

        if (x==0){
            Request request = new Request.Builder().url(Info.BASE_URL + "appointment/list?page="+page).build();
            call = okHttpClient.newCall(request);
        }else {
            Request request = new Request.Builder().url(Info.BASE_URL + "appointment/findByClass?page="+page+"&cls="+y).build();
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
                EventBus.getDefault().post("first");
            }
        });



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("详情","success"+dataSource.get(position).getDemand_id()+"");

                Intent intent = new Intent();
                intent.putExtra("id",dataSource.get(position).getDemand_id());
                intent.setClass(getActivity(), JoinAgreementActivity.class);
                startActivity(intent);

            }
        });
    }

    private void findVIew(){

        refreshLayout = getActivity().findViewById(R.id.sr_demand_refresh);
        listView = getActivity().findViewById(R.id.lv_time_demand);
    }
    private void setListeners(){
//        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
//
//            }
//        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                DemandListTask task = new DemandListTask();
                task.execute();
            }
        });
    }

    private class DemandListTask extends AsyncTask {
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
        protected void onPostExecute(Object o) {
            //更新视图
            srokHttpClient =new OkHttpClient();
            demandInfoList = new ArrayList<>();

            int y = x-1;
            page++;
            if (x==0){
                Request request = new Request.Builder().url(Info.BASE_URL + "appointment/list?page="+page).build();
                call = srokHttpClient.newCall(request);
            }else {
                Request request = new Request.Builder().url(Info.BASE_URL + "appointment/findByClass?page="+page+"&cls="+y).build();
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
                    Log.e("appointment = ", n);
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                    Type listType = new TypeToken<List<DemandInfo>>(){}.getType();
                    //newsList = new ArrayList<>();
                    demandInfoList = gson.fromJson(n,listType);
                    // Log.e("标题22",n);
                    EventBus.getDefault().post("page");
                }
            });
        }
    }

    private void initData(List<DemandInfo> list){
        dataSource = new ArrayList<>();
        for (int i = 0;i<list.size();++i){
            dataSource.add(list.get(i));

        }
    }

    private void initPage(List<DemandInfo> list){
        for (int i = 0;i<list.size();++i){
            //dataSource.add(list.get(i));
            dataSource.add(dataSource.size(),list.get(i));

        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //initData();

        fabAdd = getActivity().findViewById(R.id.fab_demand_add);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("新建约球","555");
                Intent intents = new Intent();
                intents.setClass(getActivity(), CreateAgreementActivity.class);
                startActivity(intents);
            }
        });
    }

    @Override
    public void onDestroy() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

}
