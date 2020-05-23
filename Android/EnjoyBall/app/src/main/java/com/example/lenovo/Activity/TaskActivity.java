package com.example.lenovo.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.lenovo.enjoyball.Info;
import com.example.lenovo.enjoyball.R;

public class TaskActivity extends AppCompatActivity {
    private TextView tv_task_online;
    private TextView tv_task_signIn;
    private  TextView tv_task_readNews;
    private TextView tv_task_createAgreement;
    private  SharedPreferences sharedPreferences;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        id = ((Info) getApplicationContext()).getUser().getUser_id();
        sharedPreferences =  getApplicationContext().getSharedPreferences("" + id, Context.MODE_PRIVATE);
        //获取控件
        getTaskView();
        //设置累计在线任务完成次数
        setOnlineTimes();
        setSignIn();
        setCreateAgreement();
    }

    private void setCreateAgreement() {
        boolean createAgreement = sharedPreferences.getBoolean("createAgreement",false);
        if (createAgreement==true){
            tv_task_createAgreement.setText("(1/1");
        }else {
            tv_task_createAgreement.setText("(0/0)");
        }
    }

    private void setSignIn() {
        int flag = sharedPreferences.getInt("flag",0);
        if (flag==0){
            tv_task_signIn.setText("(0/1)");
        }else {
            tv_task_signIn.setText("(1/1)");
        }

    }

    private void getTaskView() {
        tv_task_online = findViewById(R.id.tv_task_online);
        tv_task_createAgreement = findViewById(R.id.tv_task_createAgreement);
        tv_task_readNews = findViewById(R.id.tv_task_readNews);
        tv_task_signIn = findViewById(R.id.tv_task_signIn);
    }

    private void setOnlineTimes() {
        int times = sharedPreferences.getInt("frequency", 0);
        tv_task_online.setText("(" + times + "/3");

    }
}
