package com.example.lenovo.enjoyball;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuessFragment extends Fragment{
    int t1,t2;
    private List<Map<String, Integer>> dataSource = null;
    private int UserScore;
    private ListView listView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guess,container,false);
        listView = view.findViewById(R.id.lv_contestDetail_guess);
//        EventBus.getDefault().register(this);
        t1 = getArguments().getInt("team1");
        t2 = getArguments().getInt("team2");
        UserScore = getArguments().getInt("userscore");
        init();

        GuessArrayAdapter adapter = new GuessArrayAdapter(
                this.getContext(),
                dataSource,
                R.layout.list_item_guess
        );
        listView.setAdapter(adapter);


        return view;
    }

    //
    //从服务器获取数据初始化界面
    private void init(){
        dataSource = new ArrayList<>();
        Map<String,Integer> map= new HashMap<>();
        map.put("leftscore",100);
        map.put("rightscore",100);
        map.put("userscore",UserScore);
        dataSource.add(map);
    }
}
