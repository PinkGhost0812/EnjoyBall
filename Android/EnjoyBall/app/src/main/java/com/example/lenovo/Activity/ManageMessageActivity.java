package com.example.lenovo.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.lenovo.Adapter.MessageAdapter;
import com.example.lenovo.enjoyball.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManageMessageActivity extends AppCompatActivity {
    private ListView lv_message = null;
    private MessageAdapter adapter;
    private List<Map<String,Object>> datasource = new ArrayList<Map<String,Object>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_notification);
        getView();
        initData();
        adapter = new MessageAdapter(datasource,R.layout.listview_item_message_notification,this);
        lv_message.setAdapter(adapter);
    }

    private void initData() {
        Map<String,Object> map1 = new HashMap<String, Object>();
        Map<String,Object> map2 = new HashMap<String, Object>();
        map1.put("content","郑文涛申请加入你的约球队伍");
        map1.put("head",getResources().getDrawable(R.drawable.head_girl));
        map1.put("time","2019/10/11");
        datasource.add(map1);
    }

    private void getView() {
        lv_message = findViewById(R.id.lv_message_notification);
    }
}
