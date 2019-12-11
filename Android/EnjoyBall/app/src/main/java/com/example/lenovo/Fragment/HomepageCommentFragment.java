package com.example.lenovo.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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
import com.example.lenovo.Adapter.HomepageCommentAdapter;
import com.example.lenovo.Util.CommentAndNews;
import com.example.lenovo.enjoyball.Info;
import com.example.lenovo.enjoyball.R;
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


public class HomepageCommentFragment extends Fragment {

    private View getView;

    private ListView lvHomepageComment;

    private List<Map<String, Object>> dataSource = null;

    private List<CommentAndNews> list;

    private OkHttpClient okHttpClient;

    List<Map<String, Object>> mapList = null;

    private User user = null;

    private Handler handler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_homepage_comment, container, false);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        user = (User) getActivity().getIntent().getSerializableExtra("user");

        getView = view;

        findView();

        getComment();

        lvHomepageComment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(getContext(), NewsDetailActivity.class);
                Log.e("test", list.get(position).getNews().getNews_id().toString());
                intent.putExtra("homepage_comment_news_id", list.get(position).getNews().getNews_id().toString());
                startActivity(intent);
            }
        });

        return view;

    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void setInfo(Message msg) {

        if (msg.what == 10) {

            List<CommentAndNews> list = (List<CommentAndNews>) msg.obj;

            initData(list);

            HomepageCommentAdapter adapter = new HomepageCommentAdapter
                    (getContext(), dataSource, R.layout.listview_item_comment);

            lvHomepageComment.setAdapter(adapter);
        }

    }

    private void initData(List<CommentAndNews> list) {

        dataSource = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("comments", list.get(i).getComment().getComment_content().toString());
            map.put("news", list.get(i).getNews().getNews_title().toString());
            dataSource.add(map);
        }

    }

    private void getComment() {

        okHttpClient = new OkHttpClient();
        Log.e("test", user.getUser_id().toString());
        final Request request = new Request.Builder()
                .url(Info.BASE_URL + "information/findSaying?id=" + user.getUser_id())
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(getActivity(), "获取评论列表失败~", Toast.LENGTH_SHORT).show();
                Looper.loop();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String data = response.body().string();

                if (data.equals("false")) {
                    //Toast.makeText(getActivity(), "用户无评论~", Toast.LENGTH_SHORT).show();
                } else {
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                    Type type = new TypeToken<List<CommentAndNews>>() {
                    }.getType();
                    list = gson.fromJson(data, type);
                    Log.e("test", list.get(1).getComment().getComment_content());
                    Message msg = new Message();
                    msg.obj = list;
                    msg.what = 10;
                    EventBus.getDefault().post(msg);
                }
            }
        });

    }

    private void findView() {
        lvHomepageComment = getView.findViewById(R.id.lv_homepage_comment);
    }

    @Override
    public void onDestroy() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);

        super.onDestroy();
    }
}
