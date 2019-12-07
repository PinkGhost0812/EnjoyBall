package com.example.lenovo.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lenovo.Activity.HomepageActivity;
import com.example.lenovo.Adapter.HomepageFansAdapter;
import com.example.lenovo.Adapter.HomepageFollowAdapter;
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

public class HomepageFollowFragment extends Fragment {

    private View getView;

    private ListView lvHomepageFollow;

    private List<Map<String, Object>> dataSource = null;

    List<Map<String, Object>> mapList = null;

    private Info info;

    private User user;

    private OkHttpClient okHttpClient;

    private List<User> userList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_homepage_follow,container, false);

        getView=view;

        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }

        info=new Info();

        user=info.getUser();

        user=new User();
        user=new User(1,"2","3","4","5","6","7","8","9",10,11,12,13);

        findView();

        getFollow();

        lvHomepageFollow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent();
                intent.setClass(getContext(), HomepageActivity.class);
                intent.putExtra("user_id",user.getUser_id().toString());
                startActivity(intent);
            }
        });

        return view;

    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void setInfo(List<User> userList){

        initData(userList);

        HomepageFollowAdapter adapter=new HomepageFollowAdapter
                (getContext(),dataSource,R.layout.listview_item_follow);

        lvHomepageFollow.setAdapter(adapter);

    }



    private void initData(List<User> userList) {

        dataSource = new ArrayList<>();
        for(int i=0;i<userList.size();++i){
            Map<String,Object> map = new HashMap<>();
            map.put("nicknames",userList.get(i).getUser_nickname());
            map.put("sexs",userList.get(i).getUser_sex());
            map.put("ages",userList.get(i).getUser_age());
            dataSource.add(map);
        }
    }

    private void getFollow() {

        okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Info.BASE_URL + "user/getfollow?id=" + user.getUser_id())
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(getActivity().getApplicationContext(), "获取关注列表失败~", Toast.LENGTH_SHORT).show();
                Looper.loop();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String data=response.body().string();

                if (data.equals("false")){
                    Toast.makeText(getActivity().getApplicationContext(), "用户无关注~", Toast.LENGTH_SHORT).show();
                }else{
                    Gson gson = new GsonBuilder()
                            .create();
                    Type listType = new TypeToken<List<User>>(){}.getType();
                    userList = gson.fromJson(data,listType);
                    EventBus.getDefault().post(userList);
                }

            }
        });

    }

    private void findView() {
        lvHomepageFollow=getView.findViewById(R.id.lv_homepage_follow);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
