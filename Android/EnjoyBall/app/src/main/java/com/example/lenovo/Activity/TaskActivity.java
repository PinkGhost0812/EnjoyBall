package com.example.lenovo.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.lenovo.enjoyball.Info;
import com.example.lenovo.enjoyball.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TaskActivity extends AppCompatActivity {
    private TextView tv_task_online;
    private TextView tv_task_signIn;
    private TextView tv_task_readNews;
    private TextView tv_task_createAgreement;
    private Button btn_task_signIn;
    private Button btn_task_readNews;
    private Button btn_task_createAgreement;
    private SharedPreferences sharedPreferences;
    private int id;
    private String url = Info.BASE_URL + "user/addScore?";
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        id = ((Info) getApplicationContext()).getUser().getUser_id();
        sharedPreferences = getApplicationContext().getSharedPreferences("" + id, Context.MODE_PRIVATE);
        //获取控件
        getTaskView();
        //设置累计在线任务完成次数
        setOnlineTimes();
        setSignIn();
        setCreateAgreement();
        setReadNews();
    }

    private void setReadNews() {
        long readNewsTime = sharedPreferences.getLong("readNewsTime", 0);
        long timeMin = readNewsTime / (1000 * 60);
        tv_task_readNews.setText("(" + timeMin + "/15");
        if (timeMin < 15) {
            tv_task_readNews.setText("(" + timeMin + "/15)");
            btn_task_readNews.setText("去完成");

        } else {
            tv_task_readNews.setText("(15/15)");
            btn_task_readNews.setText("完成");
        }
    }

    public class Onclicked implements View.OnClickListener {
        String text;

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_task_createAgreement:
                    text = btn_task_createAgreement.getText().toString();
                    if (text.equals("去完成")) {
                        intent = new Intent(TaskActivity.this, CreateAgreementActivity.class);
                        startActivity(intent);
                    } else {
                        sendToServer(id, 10);
                        btn_task_createAgreement.setEnabled(false);
                    }
                    break;
                case R.id.btn_task_readNews:
                    text = btn_task_readNews.getText().toString();
                    if (text.equals("去完成")) {
                        intent = new Intent();
                        startActivity(intent);
                    } else {
                        sendToServer(id, 10);
                        btn_task_readNews.setEnabled(false);
                    }
                    break;
                case R.id.btn_task_signIn:
                    intent = new Intent();
                    startActivity(intent);
                    break;

            }

        }
    }

    private void setCreateAgreement() {
        boolean createAgreement = sharedPreferences.getBoolean("createAgreement", false);
        if (createAgreement == true) {
            tv_task_createAgreement.setText("(1/1");
            btn_task_createAgreement.setText("完成");
        } else {
            btn_task_createAgreement.setText("去完成");
            tv_task_createAgreement.setText("(0/1)");
        }
    }

    private void setSignIn() {
        int flag = sharedPreferences.getInt("flag", 0);
        if (flag == 0) {
            tv_task_signIn.setText("(0/1)");
            btn_task_signIn.setText("去完成");
        } else {
            tv_task_signIn.setText("(1/1)");
            btn_task_signIn.setText("已完成");
            btn_task_signIn.setEnabled(false);
        }

    }

    private void getTaskView() {
        tv_task_online = findViewById(R.id.tv_task_online);
        tv_task_createAgreement = findViewById(R.id.tv_task_createAgreement);
        tv_task_readNews = findViewById(R.id.tv_task_readNews);
        tv_task_signIn = findViewById(R.id.tv_task_signIn);
        btn_task_createAgreement = findViewById(R.id.btn_task_createAgreement);
        btn_task_readNews = findViewById(R.id.btn_task_readNews);
        btn_task_signIn = findViewById(R.id.btn_task_signIn);
    }

    private void setOnlineTimes() {
        int times = sharedPreferences.getInt("frequency", 0);
        tv_task_online.setText("(" + times + "/3");

    }

    private void sendToServer(int id, int score) {
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url + "id=" + id + "&&score=" + score)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }
}
