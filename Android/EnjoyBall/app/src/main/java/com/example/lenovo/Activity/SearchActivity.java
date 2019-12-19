package com.example.lenovo.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.Adapter.NewsSearchAdapter;
import com.example.lenovo.enjoyball.Info;
import com.example.lenovo.enjoyball.R;
import com.example.lenovo.entity.News;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
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

public class SearchActivity extends Activity {

    private EditText etSearch;
    private ImageView ivBackMain;
    private ImageView ivSearch;
    private ImageView ivAll;
    private ImageView ivFootball;
    private ImageView ivBasketball;
    private ImageView ivBadminton;
    private ImageView ivTabletennis;
    private ImageView ivVolleyball;
    private LinearLayout llTop;
    private TextView tvAll;
    private TextView tvFootball;
    private TextView tvBasketbal;
    private TextView tvBadminton;
    private TextView tvTabletennis;
    private TextView tvVolleyball;
    private int tabtop = 0;
    private List<News> dataSource = null;
    private OkHttpClient okHttpClient;
    private OkHttpClient srokHttpClient;
    private NewsSearchAdapter adapter;
    private SmartRefreshLayout refreshLayout;
    private List<News> newsList = null;
    private List<News> pageList = null;
    private ListView listView;
    private Call call;
    private int x = 0;
    private int page = 0;

    private class MyTabSpec{
        private ImageView imageView = null;
        private TextView textView = null;
        private int num = 0;
        private void setSelectTop(boolean b) {
            if (b){
                imageView.setImageResource(R.drawable.underline11);
                textView.setTextColor(
                        Color.parseColor("#ffffff"));
            } else {
                imageView.setImageResource(R.drawable.underlinegreen);
                textView.setTextColor(
                        Color.parseColor("#8B8682")
                );
            }
        }

        private void setNewsNum(String s){
            if (num>0)
                textView.setText(s + "(" + num + ")");
            else
                textView.setText(s);
        }

        public int getNum() {
            return num;
        }

        public void setNum(boolean b) {
            if (b)
                num++;
            else
                num = 0;
        }

        public ImageView getImageView() {
            return imageView;
        }

        public void setImageView(ImageView imageView) {
            this.imageView = imageView;
        }

        public TextView getTextView() {
            return textView;
        }

        public void setTextView(TextView textView) {
            this.textView = textView;
        }

    }

    private Map<String,MyTabSpec> mapTop = new HashMap<>();
    private String[] tabStrTopId = {"全部","足球","篮球","排球","羽毛球","乒乓球"};

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setContent(String msgs) {
        switch (msgs){
            case "news":
                initData(newsList,tabtop);

                adapter = new NewsSearchAdapter(
                        SearchActivity.this,
                        dataSource,
                        R.layout.listview_item_search
                );

                listView.setAdapter(adapter);


                for (String key : mapTop.keySet()) {
                    mapTop.get(key).setNum(false);
                }

                for (int i=0;i<newsList.size();i++){
                    for (int n=0;n<5;n++){
                        if (newsList.get(i).getNews_class()==n)
                            mapTop.get(tabStrTopId[n+1]).setNum(true);
                    }
                }

                for (int i = 0;i<6;i++){
                    mapTop.get(tabStrTopId[i]).setNewsNum(tabStrTopId[i]);
                }

                llTop.setVisibility(View.VISIBLE);
                break;
            case "page":
                Log.e("下一页",pageList+"");
                initPage(pageList,tabtop);
                adapter.notifyDataSetChanged();
                //结束加载更多的动画
                refreshLayout.finishLoadMore();
                break;
            case "ball":
                Log.e("ball",tabtop+"");
                initData(newsList,tabtop);
                adapter = new NewsSearchAdapter(
                        SearchActivity.this,
                        dataSource,
                        R.layout.listview_item_search
                );
                listView.setAdapter(adapter);
                //adapter.notifyDataSetInvalidated();
                //adapter.notifyDataSetChanged();
                break;
            case "refresh":
                adapter.notifyDataSetChanged();
                refreshLayout.finishRefresh();
                break;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.nonetitle);
        setContentView(R.layout.activity_search);

        findView();
        setTop();
        setListeners();
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        changeTab(tabStrTopId[0]);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("id",dataSource.get(position).getNews_id()+"");
                intent.setClass(SearchActivity.this, NewsDetailActivity.class);
                startActivity(intent);
            }
        });
    }

    private class MyListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_search_search:

                    if (etSearch.getText().toString().trim().isEmpty()){
                        Toast.makeText(getApplicationContext(), "输入内容不能为空", Toast.LENGTH_SHORT).show();


                    }else {

                        page = 1;
                        okHttpClient = new OkHttpClient();
                        newsList = new ArrayList<>();
                        Request request = new Request.Builder().url(Info.BASE_URL + "news/search?content="+etSearch.getText().toString()+"&page="+page).build();
                        call = okHttpClient.newCall(request);
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
                                newsList = gson.fromJson(n,listType);
                                EventBus.getDefault().post("news");
                            }
                        });
                    }
                    break;
                case R.id.iv_search_back:
                    SearchActivity.this.finish();
                    break;
                case R.id.tab_spec_search_topall:
                    tabtop =  0;
                    changeTab(tabStrTopId[tabtop]);
                    EventBus.getDefault().post("ball");
                    break;
                case R.id.tab_spec_search_football:
                    tabtop =  1;
                    changeTab(tabStrTopId[tabtop]);
                    EventBus.getDefault().post("ball");
                    break;
                case R.id.tab_spec_search_basketball:
                    tabtop =  2;
                    changeTab(tabStrTopId[tabtop]);
                    EventBus.getDefault().post("ball");
                    break;
                case R.id.tab_spec_search_volleyball:
                    tabtop =  3;
                    changeTab(tabStrTopId[tabtop]);
                    EventBus.getDefault().post("ball");
                    break;
                case R.id.tab_spec_search_badminton:
                    tabtop =  4;
                    changeTab(tabStrTopId[tabtop]);
                    EventBus.getDefault().post("ball");
                    break;
                case R.id.tab_spec_search_tabletennis:
                    tabtop =  5;
                    changeTab(tabStrTopId[tabtop]);
                    EventBus.getDefault().post("ball");
                    break;

            }
        }
    }

    private void changeTab(String s){
        changeImage(s);
    }

    private void changeImage(String s) {
        // 1 所有Tab的图片和字体颜色恢复默认
        for (String key : mapTop.keySet()) {
            mapTop.get(key).setSelectTop(false);
        }

        // 2 设置选中的Tab的图片和字体颜色
        mapTop.get(s).setSelectTop(true);
    }

    public void getNums(){
        for (int i=1;i<6;i++){
            for (int j=0;j<6;j++)
            mapTop.get(tabStrTopId[i]).setNum(true);
        }
        for (int i = 0;i<6;i++){
            mapTop.get(tabStrTopId[i]).setNewsNum(tabStrTopId[i]);
        }
    }

    private void findView(){
        etSearch = findViewById(R.id.et_search_search);
        ivSearch = findViewById(R.id.iv_search_search);
        ivBackMain = findViewById(R.id.iv_search_back);
        ivAll = findViewById(R.id.img_search_topall);
        ivFootball = findViewById(R.id.img_search_football);
        ivBasketball = findViewById(R.id.img_search_basketball);
        ivBadminton = findViewById(R.id.img_search_badminton);
        ivTabletennis = findViewById(R.id.img_search_tabletennis);
        ivVolleyball = findViewById(R.id.img_search_volleyball);
        tvAll = findViewById(R.id.tv_search_topall);
        tvFootball = findViewById(R.id.tv_search_football);
        tvBasketbal = findViewById(R.id.tv_search_basketball);
        tvBadminton = findViewById(R.id.tv_search_badminton);
        tvTabletennis = findViewById(R.id.tv_search_tabletennis);
        tvVolleyball = findViewById(R.id.tv_search_volleyball);
        llTop = findViewById(R.id.tab_search_widget);

        listView = findViewById(R.id.lv_search_news);
        refreshLayout = findViewById(R.id.sr_search_refresh);

        refreshLayout.setRefreshHeader(new ClassicsHeader(this));


    }

    private void setListeners(){
        LinearLayout linearLayout1 = findViewById(R.id.tab_spec_search_topall);
        LinearLayout linearLayout2 = findViewById(R.id.tab_spec_search_football);
        LinearLayout linearLayout3 = findViewById(R.id.tab_spec_search_basketball);
        LinearLayout linearLayout4 = findViewById(R.id.tab_spec_search_badminton);
        LinearLayout linearLayout5 = findViewById(R.id.tab_spec_search_tabletennis);
        LinearLayout linearLayout6 = findViewById(R.id.tab_spec_search_volleyball);
        MyListener listener = new MyListener();
        linearLayout1.setOnClickListener(listener);
        linearLayout2.setOnClickListener(listener);
        linearLayout3.setOnClickListener(listener);
        linearLayout4.setOnClickListener(listener);
        linearLayout5.setOnClickListener(listener);
        linearLayout6.setOnClickListener(listener);
        ivSearch.setOnClickListener(listener);
        ivBackMain.setOnClickListener(listener);

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                NewsListTask task = new NewsListTask();
                task.execute();
            }
        });

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //不能执行网络操作，需要使用多线程
                new Thread(){
                    @Override
                    public void run() {
                        EventBus.getDefault().post("refresh");
                    }
                }.start();

            }
        });

    }

    private  class NewsListTask extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {
            return null;
        }

        @Override
        protected void onPreExecute() {
            //更新视图
            if (!etSearch.getText().toString().trim().isEmpty()){
                srokHttpClient =new OkHttpClient();
                pageList = new ArrayList<>();
                page++;

                Request request = new Request.Builder().url(Info.BASE_URL + "news/search?content="+etSearch.getText().toString()+"&page="+page).build();
                call = srokHttpClient.newCall(request);
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
                        pageList = gson.fromJson(n,listType);
                        EventBus.getDefault().post("page");
                    }
                });
            }

        }
    }

    private void setTop(){
        mapTop.put(tabStrTopId[0], new MyTabSpec());
        mapTop.put(tabStrTopId[1], new MyTabSpec());
        mapTop.put(tabStrTopId[2], new MyTabSpec());
        mapTop.put(tabStrTopId[3], new MyTabSpec());
        mapTop.put(tabStrTopId[4], new MyTabSpec());
        mapTop.put(tabStrTopId[5], new MyTabSpec());


        mapTop.get(tabStrTopId[0]).setImageView(ivAll);
        mapTop.get(tabStrTopId[0]).setTextView(tvAll);

        mapTop.get(tabStrTopId[1]).setImageView(ivFootball);
        mapTop.get(tabStrTopId[1]).setTextView(tvFootball);

        mapTop.get(tabStrTopId[2]).setImageView(ivBasketball);
        mapTop.get(tabStrTopId[2]).setTextView(tvBasketbal);

        mapTop.get(tabStrTopId[3]).setImageView(ivVolleyball);
        mapTop.get(tabStrTopId[3]).setTextView(tvVolleyball);

        mapTop.get(tabStrTopId[4]).setImageView(ivBadminton);
        mapTop.get(tabStrTopId[4]).setTextView(tvBadminton);

        mapTop.get(tabStrTopId[5]).setImageView(ivTabletennis);
        mapTop.get(tabStrTopId[5]).setTextView(tvTabletennis);

    }

    private void initData(List<News> newsList, int id) {

        dataSource = new ArrayList<>();
        if (id == 0){
            for(int i=0;i<newsList.size();++i){
                dataSource.add(newsList.get(i));
            }
        }else {
            id--;
            for(int i=0;i<newsList.size();++i)
                if (newsList.get(i).getNews_class()==id)
                    dataSource.add(newsList.get(i));
        }

    }

    private void initPage(List<News> list, int id){
        if (id == 0){
            for(int i=0;i<list.size();++i){
                dataSource.add(dataSource.size(), list.get(i));
            }
        }else {
            id--;
            for(int i=0;i<list.size();++i)
                if (list.get(i).getNews_class()==id)
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
