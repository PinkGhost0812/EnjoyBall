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

public class HomepageFansFragment extends Fragment {

    private View getView;

    private ListView lvHomepageFans;

    private List<Map<String, Object>> dataSource = null;

    List<Map<String, Object>> mapList = null;

    private Info info;

    private User user;

    private List<User> userList;

    private OkHttpClient okHttpClient;


    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_homepage_fans,container, false);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        getView=view;

        user= (User) getActivity().getIntent().getSerializableExtra("user");

        findView();

        getFans();

        lvHomepageFans.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

    private void initData(List<User> userList) {

        dataSource = new ArrayList<>();
        for(int i=0;i<userList.size();++i){
            Map<String,Object> map = new HashMap<>();
            Log.e("testnicknames",userList.get(i).getUser_nickname().toString());
            map.put("portraits",userList.get(i).getUser_headportrait().toString());
            map.put("nicknames",userList.get(i).getUser_nickname().toString());
            map.put("sexs",userList.get(i).getUser_sex().toString());
            map.put("ages",userList.get(i).getUser_age().toString());
            dataSource.add(map);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void setInfo(Message msg){

        if (msg.what==8){

            initData(userList);

            HomepageFansAdapter adapter=new HomepageFansAdapter
                    (getContext(),dataSource,R.layout.listview_item_fans);

            lvHomepageFans.setAdapter(adapter);
        }

    }

    private void getFans() {

        okHttpClient = new OkHttpClient();
        Log.e("test",user.getUser_id().toString());
        Request request = new Request.Builder()
                .url(Info.BASE_URL + "user/getfans?id=" + user.getUser_id())
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(getActivity(), "获取粉丝列表失败~", Toast.LENGTH_SHORT).show();
                Looper.loop();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String data=response.body().string();

                if (data.equals("false")){
                    Looper.prepare();
                    Toast.makeText(getActivity(), "用户无粉丝~", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }else{

                    Gson gson = new GsonBuilder()
                            .create();
                    Type listType = new TypeToken<List<User>>(){}.getType();
                    userList = gson.fromJson(data,listType);
                    Message msg=new Message();
                    msg.obj=userList;
                    msg.what=8;
                    Log.e("test",userList.toString());
                    EventBus.getDefault().post(msg);
                }

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
