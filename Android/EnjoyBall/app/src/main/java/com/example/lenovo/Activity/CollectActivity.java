package com.example.lenovo.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.lenovo.Adapter.CollectListviewAdapter;
import com.example.lenovo.enjoyball.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollectActivity extends AppCompatActivity {
    private List<Map<String,Object>> datasource = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        
        init();

        CollectListviewAdapter collectListviewAdapter = new CollectListviewAdapter(
                this,
                 datasource,
                 R.layout.collect_item
        );
        ListView listView = findViewById(R.id.lv_collect_news);

        listView.setAdapter(collectListviewAdapter);

    }

    private void init() {
        int[] imgs = {R.drawable.sword,R.drawable.sword1};
        String[] heads = {"剑网3海量Q币大放送","五十岁母猪为何频频失窃"};
        String[] authors = {"始于初见","止于终年"};
        String[] times = {"08-31","09-01"};

        datasource = new ArrayList<>();
        for(int i=0;i<heads.length;++i){
            Map<String,Object> map = new HashMap<>();
            map.put("heads",heads[i]);
            map.put("authors",authors[i]);
            map.put("times",times[i]);
            map.put("imgs",imgs[i]);
            datasource.add(map);
        }
    }
}
