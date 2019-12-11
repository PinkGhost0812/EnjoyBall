package com.example.lenovo.Activity;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lenovo.enjoyball.R;

import org.greenrobot.eventbus.EventBus;

public class TeamManageNameActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.nonetitle);
        setContentView(R.layout.activity_team_manage_name);

        final EditText etTeamManageName=findViewById(R.id.et_team_manage_name);
        Button btnTeamManageName=findViewById(R.id.btn_team_manage_name);
        btnTeamManageName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etTeamManageName.getText().toString().equals("")||etTeamManageName.getText().toString()==null){
                    Toast.makeText(TeamManageNameActivity.this,"球队名不能为空凹~",Toast.LENGTH_SHORT).show();
                }else {
                    String name=etTeamManageName.getText().toString();
                    Message msg=new Message();
                    msg.what=31;
                    msg.obj=name;
                    EventBus.getDefault().post(msg);
                    finish();
                }
            }
        });

    }
}