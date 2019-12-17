package com.example.lenovo.Fragment;

import android.content.Intent;
import android.os.AsyncTask;
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
import com.example.lenovo.Util.ImageLoadBanner;
import com.example.lenovo.Util.UnScrollListView;
import com.example.lenovo.enjoyball.Info;
import com.example.lenovo.enjoyball.R;
import com.example.lenovo.entity.News;
import com.example.lenovo.entity.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

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
    private OkHttpClient okHttpClient;
    private OkHttpClient okHttpClientBan;
    private OkHttpClient srokHttpClient;
    private NewsAdapter adapter;
    private SmartRefreshLayout refreshLayout;
    private List<News> newsList = null;
    private List<News> banList = null;
    private Banner banner;
    private UnScrollListView listView;
    private Call call;
    private int x = 0;
    private int page = 0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.tab_home_layout,
                container, false);


        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setContent(String msgs) {
        switch (msgs){
            case "news":
                initData(newsList);

                adapter = new NewsAdapter(
                        getContext(),
                        dataSource,
                        R.layout.listview_item_home
                );

                listView.setAdapter(adapter);
                break;
            case "ban":
                createBan(banList);
                break;
            case "page":
                initPage(newsList);
                adapter.notifyDataSetChanged();
                //结束加载更多的动画
                refreshLayout.finishLoadMore();
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
        int y = x-1;
        page = 1;
        okHttpClient =new OkHttpClient();
        newsList = new ArrayList<>();
        if (x==0){
            Request request = new Request.Builder().url(Info.BASE_URL + "news/newsPage?page="+page).build();
            call = okHttpClient.newCall(request);

        }else {
            Request request = new Request.Builder().url(Info.BASE_URL + "news/newsPageByClass?page=" +page+"&cls="+y).build();
            call = okHttpClient.newCall(request);

        }
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String n = response.body().string();
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                Type listType = new TypeToken<List<News>>(){}.getType();
                newsList = gson.fromJson(n,listType);
                EventBus.getDefault().post("news");
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent();
                intent.putExtra("id",dataSource.get(position).getNews_id()+"");
                intent.setClass(getActivity(), NewsDetailActivity.class);
                startActivity(intent);

            }
        });

    }

    private void findView(){
        refreshLayout = getActivity().findViewById(R.id.sr_home_refresh);
        listView = getActivity().findViewById(R.id.lv_home_news);

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
                NewsListTask task = new NewsListTask();
                task.execute();
            }
        });
    }

    private  class NewsListTask extends AsyncTask{
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
            newsList = new ArrayList<>();

            int y = x-1;
            page++;
            if (x==0){
                Request request = new Request.Builder().url(Info.BASE_URL + "news/newsPage?page="+page).build();
                call = srokHttpClient.newCall(request);

            }else {
                Request request = new Request.Builder().url(Info.BASE_URL + "news/newsPageByClass?page=" +page+"&cls="+y).build();
                call = srokHttpClient.newCall(request);

            }
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String n = response.body().string();
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                    Type listType = new TypeToken<List<News>>(){}.getType();
                    newsList = gson.fromJson(n,listType);
                    EventBus.getDefault().post("page");
                }
            });
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        lvHome = getActivity().findViewById(R.id.lv_home_news);
//        lvHome.setAdapter(new SimpleAdapter(this,dataSource,R.layout.listview_item_home,));
        banner = getActivity().findViewById(R.id.bn_home_banner);

        okHttpClientBan = new OkHttpClient();
        banList = new ArrayList<>();
        Request requests = new Request.Builder().url(Info.BASE_URL + "news/getThree").build();
        Call calls = okHttpClientBan.newCall(requests);
        calls.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String ban = response.body().string();
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                Type listType = new TypeToken<List<News>>(){}.getType();
                banList = gson.fromJson(ban,listType);

                EventBus.getDefault().post("ban");
            }
        });

        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Intent intent = new Intent();
                intent.putExtra("id",dataSource.get(position).getNews_id()+"");
                intent.setClass(getActivity(), NewsDetailActivity.class);
                startActivity(intent);
            }
        });

    }

    private void createBan(List<News> list){
        ArrayList<String> title = new ArrayList<>();
        ArrayList<String> imgs = new ArrayList<>();
        for (int i=0;i<list.size();++i){
            imgs.add(Info.BASE_URL+list.get(i).getNews_image());
            title.add(list.get(i).getNews_title());
        }
//        ArrayList<Integer> imgs = new ArrayList<>();
//        imgs.add(R.drawable.ban1);
//        imgs.add(R.drawable.ban2);
//        imgs.add(R.drawable.ban3);
//        imgs.add(R.drawable.ban4);
////        imgs.add("http://10.7.88.233:8080/EnjoyBallServer/img/default.png");
////        imgs.add("http://10.7.88.233:8080/EnjoyBallServer/img/default.png");
////        imgs.add("http://10.7.88.233:8080/EnjoyBallServer/img/default.png");
////        imgs.add("http://10.7.88.233:8080/EnjoyBallServer/img/default.png");
//
//        ArrayList<String> title = new ArrayList<>();
//        title.add("新闻1");
//        title.add("新闻2");
//        title.add("新闻3");
//        title.add("新闻4");

        banner.setImages(imgs);
        banner.setImageLoader(new ImageLoadBanner());
        banner.setBannerTitles(title);
        banner.setDelayTime(2000);
        banner.isAutoPlay(true);
        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.setBannerAnimation(Transformer.Accordion);
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        banner.start();
    }

    private void initData(List<News> newsList) {

        dataSource = new ArrayList<>();
        for(int i=0;i<newsList.size();++i){
            dataSource.add(newsList.get(i));
        }
    }

    private void initPage(List<News> list){
        for (int i = 0;i<list.size();++i) {
            dataSource.add(dataSource.size(), list.get(i));
        }
    }

    @Override
    public void onDestroy() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

}