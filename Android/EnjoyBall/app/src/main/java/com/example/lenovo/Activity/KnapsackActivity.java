package com.example.lenovo.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.lenovo.Adapter.KnapsackAdapter;
import com.example.lenovo.enjoyball.R;
import com.example.lenovo.entity.Knapsack;
import com.example.lenovo.entity.User;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;

public class KnapsackActivity extends AppCompatActivity {

    private ImageView ivBack;
    private List<Knapsack> dataSource = null;
    private OkHttpClient okHttpClient;
    private List<Knapsack> knapsackList = null;
    private ListView listView;
    private Call call;
    private KnapsackAdapter adapter;
    private User user;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setContent(String msgs) {
        switch (msgs){
            case "knapsack":
                initData();
                adapter = new KnapsackAdapter(
                        KnapsackActivity.this,
                        dataSource,
                        R.layout.listview_item_knapsack
                );
                listView.setAdapter(adapter);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.nonetitle);
        setContentView(R.layout.activity_knapsack);

        Intent intent = getIntent();

        user = (User) intent.getSerializableExtra("user");
        listView = findViewById(R.id.lv_knapsack);
        ivBack = findViewById(R.id.iv_knapsack_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KnapsackActivity.this.finish();
            }
        });

        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        EventBus.getDefault().post("knapsack");
    }

    private void initData() {

        String[] name = {"改名卡","积分卡"};
        int[] img = {R.drawable.addgame, R.drawable.changename};
        int[] num = {10,5};
        dataSource = new ArrayList<>();
        for(int i=0; i<name.length; ++i) {
            Knapsack knapsack = new Knapsack();
            knapsack.setKnapsack_name(name[i]);
            knapsack.setKnapsack_img(img[i]);
            knapsack.setKnapsack_num(num[i]);
            dataSource.add(knapsack);
        }
    }

    @Override
    public void onDestroy() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
