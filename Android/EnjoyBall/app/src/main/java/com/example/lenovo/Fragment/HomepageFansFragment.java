package com.example.lenovo.Fragment;

import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lenovo.Adapter.HomepageFansAdapter;
import com.example.lenovo.enjoyball.Info;
import com.example.lenovo.enjoyball.R;
import com.example.lenovo.entity.User;
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

public class HomepageFansFragment extends Fragment {

    private View getView;

    private ListView lvHomepageFans;

    private List<Map<String, Object>> dataSource = null;


    private OkHttpClient okHttpClient;

    private List<User> userList;

    private Info info;

    private User user;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_homepage_fans,container, false);

        getView=view;

        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }

        info=new Info();

        user=info.getUser();

        user=new User(1,"2","3","4","5","6","7","8","9",10,11,12);

        findView();

        getFans();

        return view;

    }

    @Subscribe
    public void setInfo(List<User> users) {

        initData(users);

        HomepageFansAdapter adapter=new HomepageFansAdapter
                (getContext(),dataSource,R.layout.listview_item_fans);

        lvHomepageFans.setAdapter(adapter);

    }

    private void initData(List<User> users) {

        dataSource = new ArrayList<>();
        for(int i=0;i<users.size();++i){
            Map<String,Object> map = new HashMap<>();
            map.put("nickname",users.get(i).getUser_nickname());
            map.put("sex",users.get(i).getUser_sex());
            //map.put("age",ages[i]);
            dataSource.add(map);
        }
    }

    private void getFans() {

        okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Info.BASE_URL + "user/getfans?id=" + user.getUser_id())
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(getContext(), "获取粉丝失败~", Toast.LENGTH_SHORT).show();
                Looper.loop();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new GsonBuilder()
                        .create();
                Type listType = new TypeToken<List<User>>(){}.getType();
                userList = gson.fromJson(response.body().string(),listType);
                EventBus.getDefault().post(userList);
            }
        });

    }

    private void findView() {
        lvHomepageFans=getView.findViewById(R.id.lv_homepage_fans);
    }

    @Override
    public void onDestroy() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
