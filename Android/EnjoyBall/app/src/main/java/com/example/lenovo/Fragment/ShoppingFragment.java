package com.example.lenovo.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.lenovo.Activity.CreateAgreementActivity;
import com.example.lenovo.Activity.LuckPanActivity;
import com.example.lenovo.Adapter.ShoppingAdapter;
import com.example.lenovo.enjoyball.Info;
import com.example.lenovo.enjoyball.R;
import com.example.lenovo.entity.StuffInfo;
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
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ShoppingFragment extends Fragment {
    private List<StuffInfo> shoppingList = null;
    private OkHttpClient okHttpClient;
    private ShoppingAdapter adapter;
    private ListView listView;
    private Call call;
    private FloatingActionButton fabPrize;
    private List<StuffInfo> dataSource = null;
    private int myscore = 0;
    private User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_shopping_layout,container,false);

        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void setContent(String msgs){
        switch (msgs){
            case "shopping":
                initDate(shoppingList);
                //initData();

                adapter = new ShoppingAdapter(
                        getContext(),
                        dataSource,
                        R.layout.listview_item_shopping,
                        user);

                listView.setAdapter(adapter);
                break;

        }
    }

    @Override
    public void onResume() {
        super.onResume();

        listView = getActivity().findViewById(R.id.lv_shopping_shopping);
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }

        //myscore = (int)getArguments().get("myscore");
        user = (User)getArguments().getSerializable("user");
        EventBus.getDefault().post("shopping");
        okHttpClient =new OkHttpClient();
        shoppingList = new ArrayList<>();

        Request request = new Request.Builder().url(Info.BASE_URL + "information/shopList").build();
        call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String n = response.body().string();
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                Type listType = new TypeToken<List<StuffInfo>>(){}.getType();
                shoppingList = gson.fromJson(n,listType);
                EventBus.getDefault().post("shopping");
            }
        });


    }

    private void initDate(List<StuffInfo> shoppingList){
        dataSource = new ArrayList<>();
        for (int i=0;i<shoppingList.size();++i){
            dataSource.add(shoppingList.get(i));
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fabPrize = getActivity().findViewById(R.id.fab_shopping_prize);
        fabPrize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intents = new Intent();
                intents.setClass(getActivity(), LuckPanActivity.class);
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



//    private void initData() {
//
//        String[] name = {"改名卡","积分卡"};
//        int[] img = {R.drawable.addgame, R.drawable.changename};
//        int[] score = {1500,1000};
//        dataSource = new ArrayList<>();
//        for(int i=0; i<name.length; ++i) {
//            Shopping shopping = new Shopping();
//            shopping.setShopping_name(name[i]);
//            shopping.setShopping_img(img[i]);
//            shopping.setShopping_price(score[i]);
//            dataSource.add(shopping);
//        }
//    }
}