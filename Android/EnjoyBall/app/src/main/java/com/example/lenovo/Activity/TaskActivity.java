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
    private  int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        id = ((Info)getApplicationContext()).getUser().getUser_id();
        //获取控件
        getTaskView();
        //设置累计在线任务完成次数
        setOnlineTimes();
    }

    private void getTaskView() {
        tv_task_online = findViewById(R.id.tv_task_online);
    }

    private void setOnlineTimes() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(""+id, Context.MODE_PRIVATE);
        int times = sharedPreferences.getInt("frequency",0);
        tv_task_online.setText("("+times+"/3");

    }
}
