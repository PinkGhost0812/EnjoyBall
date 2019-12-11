package com.example.lenovo.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.lenovo.Activity.NewsDetailActivity;
import com.example.lenovo.Adapter.HomepageFollowAdapter;
import com.example.lenovo.Adapter.NewsAdapter;
import com.example.lenovo.enjoyball.Info;
import com.example.lenovo.enjoyball.R;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeFragment extends Fragment {

    private List<News> dataSource = null;
    //private List<Map<String,Object>> dataSource = null;
    private OkHttpClient okHttpClient;
    private ListView lvHome;
    private List<News> newsList = null;

    private ListView listView;
    private Call call;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.tab_home_layout,
                container, false);

//        int x = 0;
//        x = (int)getArguments().get("ball");
//        Log.e("收到",x+"");


        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void setContent(List<News> list){

        initData(list);

        NewsAdapter adapter = new NewsAdapter(
                getContext(),
                dataSource,
                R.layout.listview_item_home
        );

        listView.setAdapter(adapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        listView = getActivity().findViewById(R.id.lv_home_news);

        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        int x = 0;
        x = (int)getArguments().get("ball");
        Log.e("收到",x+"");
        okHttpClient =new OkHttpClient();
        newsList = new ArrayList<>();
        //initData();
        if (x==0){
            Request request = new Request.Builder().url(Info.BASE_URL + "news/list").build();
            call = okHttpClient.newCall(request);

        }else {
            x=x-1;
            Request request = new Request.Builder().url(Info.BASE_URL + "news/findByCls?cls=" +x).build();
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
                Log.e("news = ", n);
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                Type listType = new TypeToken<List<News>>(){}.getType();
                //newsList = new ArrayList<>();
                newsList = gson.fromJson(n,listType);
                // Log.e("标题22",n);
                EventBus.getDefault().post(newsList);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("详情",dataSource.get(position).getNews_id()+"");

                Intent intent = new Intent();
                intent.putExtra("id",dataSource.get(position).getNews_id()+"");
                intent.setClass(getActivity(), NewsDetailActivity.class);
                startActivity(intent);

            }
        });

    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
////        lvHome = getActivity().findViewById(R.id.lv_home_news);
////        lvHome.setAdapter(new SimpleAdapter(this,dataSource,R.layout.listview_item_home,));
//
//
//    }


    private void initData(List<News> newsList) {

        dataSource = new ArrayList<>();
        for(int i=0;i<newsList.size();++i){
            for (int j=0;j<6;++j) {
                dataSource.add(newsList.get(i));
            }
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
//        int[] imgs = {R.drawable.test1,R.drawable.test1,R.drawable.test1,R.drawable.test1,R.drawable.test1,R.drawable.test1};
//        String[] contents = {"欧洲冠军大逃亡:2连败 净胜球...","本地测试数据...","欧洲冠军大逃亡:2连败 净胜球...","欧洲冠军大逃亡:2连败 净胜球...","本地测试数据..","本地测试数据..."};
//        String[] heat = {"1222","55","8888","9999","5554","8886"};
//        //dataSource = new ArrayList<>();
//        for(int i=0; i<imgs.length; ++i) {
//            Map<String, Object> map = new HashMap<>();
//            map.put("img",  imgs[i]);
//            map.put("content", contents[i]);
//            map.put("heat", heat[i]);
//            //dataSource.add(map);
//        }
//    }

}