package com.example.lenovo.Fragment;

import android.content.Intent;
import android.os.Bundle;
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

    private User user;

    private OkHttpClient okHttpClient;

    private List<User> userList;

    private HomepageFollowAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_homepage_follow,container, false);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        getView=view;

        user= (User) getActivity().getIntent().getSerializableExtra("user");

        findView();

        getFollow();

        lvHomepageFollow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent();
                intent.setClass(getContext(), HomepageActivity.class);
                user=userList.get(position);
                intent.putExtra("user",user);
                startActivity(intent);
            }
        });

        return view;

    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void setInfo(Message msg){

        if (msg.what==9){

            initData(userList);

            adapter=new HomepageFollowAdapter
                    (getContext(),dataSource,R.layout.listview_item_follow);

            lvHomepageFollow.setAdapter(adapter);
        }else if (msg.what==100){

            dataSource.remove(Integer.parseInt(msg.obj.toString()));
            adapter.notifyDataSetChanged();
            Toast.makeText(getContext(),"取关成功~",Toast.LENGTH_SHORT).show();

        }else if (msg.what==101){
            Toast.makeText(getContext(),"取关失败~",Toast.LENGTH_SHORT).show();
        }

    }

    private void initData(List<User> userList) {

        dataSource = new ArrayList<>();
        for(int i=0;i<userList.size();++i){
            Map<String,Object> map = new HashMap<>();
            map.put("portraits",userList.get(i).getUser_headportrait());
            map.put("nicknames",userList.get(i).getUser_nickname());
            map.put("sexs",userList.get(i).getUser_sex());
            map.put("ages",userList.get(i).getUser_age());
            map.put("ids",userList.get(i).getUser_id());
            dataSource.add(map);
        }
    }

    private void getFollow() {

        okHttpClient = new OkHttpClient();
        Log.e("test",user.getUser_id().toString());
        Request request = new Request.Builder()
                .url(Info.BASE_URL + "user/getfollow?id=" + user.getUser_id())
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(getActivity(), "获取关注列表失败~", Toast.LENGTH_SHORT).show();
                Looper.loop();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String data=response.body().string();

                Log.e("test follow",data);

                if (data.equals("false")){
                    Looper.prepare();
                    Toast.makeText(getActivity(), "用户无关注~", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }else{
                    Gson gson = new GsonBuilder()
                            .create();
                    Type listType = new TypeToken<List<User>>(){}.getType();
                    userList = gson.fromJson(data,listType);
                    Message msg=new Message();
                    msg.what=9;
                    msg.obj=userList;
                    EventBus.getDefault().post(msg);
                }

            }
        });

    }

    private void findView() {
        lvHomepageFollow=getView.findViewById(R.id.lv_homepage_follow);
    }

    @Override
    public void onDestroy() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);

        super.onDestroy();
    }

}
