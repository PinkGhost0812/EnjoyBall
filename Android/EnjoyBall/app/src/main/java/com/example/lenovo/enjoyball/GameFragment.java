package com.example.lenovo.enjoyball;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameFragment extends Fragment {


    private List<Map<String,Object>> dataSource = null;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.tab_game_layout,
                container, false);
        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();

        ListView listView = getActivity().findViewById(R.id.lv_game_game);
        final GameAdapter adapter = new GameAdapter(
                getContext(),
                dataSource,
                R.layout.listview_item_game
        );

        listView.setAdapter(adapter);
    }

    private void initData() {

        String[] times = {"01：00 德甲 第10轮","01：00 德甲 第10轮","01：00 德甲 第10轮","01：00 德甲 第10轮","01：00 德甲 第10轮"};
        String[] team1 = {"奥格斯堡","奥格斯堡","奥格斯堡","奥格斯堡","奥格斯堡"};
        String[] team2 = {"沙尔克04","沙尔克04","沙尔克04","沙尔克04","沙尔克04"};
        String[] score1 = {"3","2","3","0","0"};
        String[] score2 = {"2","1","2","1","0"};
        String[] state = {"已完场","已完场","已完场","进行中","未开始"};
        dataSource = new ArrayList<>();
        for(int i=0; i<times.length; ++i) {
            Map<String, Object> map = new HashMap<>();
            map.put("time", times[i]);
            map.put("team1", team1[i]);
            map.put("team2", team2[i]);
            map.put("score1", score1[i]);
            map.put("score2", score2[i]);
            map.put("state", state[i]);
            dataSource.add(map);
        }
    }
}