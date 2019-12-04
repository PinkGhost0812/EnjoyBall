package com.example.lenovo.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lenovo.Adapter.HomepageCommentAdapter;
import com.example.lenovo.enjoyball.Info;
import com.example.lenovo.enjoyball.R;
import com.example.lenovo.entity.News;
import com.example.lenovo.entity.User;
import com.example.lenovo.Util.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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

public class HomepageCommentFragment extends Fragment {

    private View getView;

    private ListView lvHomepageComment;

    private List<Map<String, Object>> dataSource = null;
    private List<Map<String,News>> list=null;

    private OkHttpClient okHttpClient;

    private int user_id;

    private Info info;

    private User user=null;

    private Handler handler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_homepage_comment,container, false);

        getView=view;

        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }

        info=new Info();

        user=info.getUser();

        user=new User();

        findView();

        getComment();

        return view;

    }

    @Subscribe
    public void setInfo(Message msg) {

        initData(msg.list);

        HomepageCommentAdapter adapter=new HomepageCommentAdapter
                (getContext(),dataSource,R.layout.listview_item_comment);

        lvHomepageComment.setAdapter(adapter);

    }

    private void initData(List<Map<String,News>> list) {

        Log.e("test12345",list.toString());

        dataSource = new ArrayList<>();

        int i=0;

        for (String comment : list.get(i).keySet()) {
            Map<String,Object> map = new HashMap<>();
            map.put("comments",comment);
            map.put("newsTitle",list.get(i).get(comment).getNews_title());
            dataSource.add(map);
            i++;
        }

    }

    private void getComment() {

        okHttpClient=new OkHttpClient();
        Request request = new Request.Builder()
                .url(Info.BASE_URL + "information/findSaying?id="+1)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(getContext(), "获取评论信息失败~", Toast.LENGTH_SHORT).show();
                Looper.loop();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String ans = response.body().string();
                List<Map<String,News>> list = gson.fromJson(ans, new TypeToken<List<Map<String,News>>>(){}.getType());
                Log.e("test",list.toString());
                Message msg=new Message();
                msg.list=list;
                EventBus.getDefault().post(msg);
            }
        });

    }

    private void findView() {
        lvHomepageComment=getView.findViewById(R.id.lv_homepage_comment);
    }

    @Override
    public void onDestroy() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
