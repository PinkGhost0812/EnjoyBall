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

public class TeamManageSloganActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.nonetitle);
        setContentView(R.layout.activity_team_manage_slogan);

        final EditText etTeamManageSlogan=findViewById(R.id.et_team_manage_slogan);
        Button btnTeamManageSlogan=findViewById(R.id.btn_team_manage_slogan);
        btnTeamManageSlogan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etTeamManageSlogan.getText().toString().equals("")||etTeamManageSlogan.getText().toString()==null){
                    Toast.makeText(TeamManageSloganActivity.this,"球队口号不能为空凹~",Toast.LENGTH_SHORT).show();
                }else {
                    String slogan=etTeamManageSlogan.getText().toString();
                    Message msg=new Message();
                    msg.what=32;
                    msg.obj=slogan;
                    EventBus.getDefault().post(msg);
                    finish();
                }
            }
        });

    }
}