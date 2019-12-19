package com.example.lenovo.Activity;

import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.Adapter.ModifyLocationAdapter;
import com.example.lenovo.enjoyball.Info;
import com.example.lenovo.enjoyball.R;
import com.example.lenovo.Adapter.TreatyballListviewAdapter;
import com.example.lenovo.entity.DemandInfo;
import com.example.lenovo.entity.News;
import com.example.lenovo.entity.Team;
import com.example.lenovo.entity.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SimpleTimeZone;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TreatyballActivity extends AppCompatActivity {
    private List<DemandInfo> userdemandinfo;
    private User user;
    private List<Map<String,Object>> datasource = null;
    private List<String> list;
    private OkHttpClient okHttpClient;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.nonetitle);
        setContentView(R.layout.activity_treatyball);

        EventBus.getDefault().register(this);

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");

        Log.e("mes",user.getUser_id().toString());
        FindTeamByUserId(user.getUser_id());

    }


    private void  FindTeamByUserId(int user_id) {
        okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Info.BASE_URL+"appointment/findByUserId?id="+user_id)
                .build();


        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonstr = response.body().string();
                Log.e("demand",jsonstr);
                if(jsonstr.equals("false")){
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(),"用户暂无约球信息",Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }else{
                    Gson gson = new GsonBuilder()
                            .setDateFormat("yyyy-MM-dd hh:mm:ss")
                            .setPrettyPrinting()
                            .serializeNulls()
                            .create();
                    Type listType = new TypeToken<List<DemandInfo>>(){}.getType();
                    userdemandinfo= gson.fromJson(jsonstr,listType);
                    EventBus.getDefault().post("约球信息列表");
                }


            }
        });
    }

    private void init() {
        datasource = new ArrayList<>();
        for(int i=0;i<userdemandinfo.size();++i){
            Map<String,Object> map = new HashMap<>();
            map.put("status",userdemandinfo.get(i).getDemand_visibility());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            map.put("dates",simpleDateFormat.format(userdemandinfo.get(i).getDemand_time()));
            map.put("types",userdemandinfo.get(i).getDemand_class());

//            map.put("teams1",teams1[i]);
//            map.put("teams2",teams2[i]);
//            map.put("scores1",scores1[i]);
//            map.put("scores2",scores2[i]);
//            map.put("dates",dates[i]);
//            map.put("times",times[i]);
//            map.put("places",places[i]);
//            map.put("symbols",symbols[i]);
//            map.put("types",types[i]);
            datasource.add(map);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void init(String msg){
        if(msg.equals("约球信息列表")){
            init();
            TreatyballListviewAdapter myadapter = new TreatyballListviewAdapter(
                    this,
                    datasource,
                    R.layout.listview_item_treatyball
            );
            listView = findViewById(R.id.lv_treatyball_match);
            listView.setAdapter(myadapter);


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent1 = new Intent(TreatyballActivity.this, ModifyLocationActivity.class);
                    Log.e("xxx",userdemandinfo.get(position).getDemand_id().toString());
                    intent1.putExtra("user",user);
                    intent1.putExtra("demandinfo",userdemandinfo.get(position));
                    startActivity(intent1);
                }
            });
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
