package com.example.lenovo.Activity;

import android.content.Intent;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lenovo.Adapter.ManageMessageAdapter;
import com.example.lenovo.Util.ApplyUtil;
import com.example.lenovo.enjoyball.Info;
import com.example.lenovo.enjoyball.R;
import com.example.lenovo.entity.Team;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ManageMessageActivity extends AppCompatActivity {
    private ListView lv_manage = null;
    private List<ApplyUtil> messages = null;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private String url = Info.BASE_URL;
    private ManageMessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        setTheme(R.style.nonetitle);
        setContentView(R.layout.activity_message_manage);
        getView();
        getMessages();
         lv_manage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int demandId = messages.get(position).getDemand().getDemand_id();
                Log.e("id", demandId + "");
                Intent intent = new Intent(ManageMessageActivity.this, ShowAgreementActivity.class);
                intent.putExtra("id", demandId);
                startActivity(intent);

            }
        });
    }

    //获取消息
    private void getMessages() {
        int id = ((Info) getApplicationContext()).getUser().getUser_id();
        Log.e("test curid", id + "");
        Request request = new Request.Builder()
                .url(url + "appointment/messageList?id=" + id)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(ManageMessageActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String messagesJson = response.body().string();
                Log.e("json", messagesJson + ".");
                Type type = new TypeToken<List<ApplyUtil>>() {
                }.getType();

                if (!messagesJson.equals("false")) {
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                    messages = gson.fromJson(messagesJson, type);
                    Log.e("收到的消息", messages.toString());
                    EventBus.getDefault().post("OK");
                }
            }
        });


    }

    private void getView() {
        lv_manage = findViewById(R.id.lv_message_manage);


    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void setView(String message) {
        if (message.equals("OK")) {
            Log.e("消息", messages.toString());
            adapter = new ManageMessageAdapter(messages, this);
            lv_manage.setAdapter(adapter);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void visitTeam(Message msg) {
        if (msg.what == 86) {
            Team team = (Team) msg.obj;
            Log.e("test visitteam", team.toString());
            Intent intent = new Intent();
            intent.setClass(this, TeamVisitDetailActivity.class);
            intent.putExtra("team", team);
            startActivity(intent);
        }
    }
}
