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

public class PerinfoNicknameActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.nonetitle);
        setContentView(R.layout.activity_perinfo_nickname);

        final EditText etPerinfoNickname=findViewById(R.id.et_perinfo_nickname);
        Button btnPerinfoNickname=findViewById(R.id.btn_perinfo_nickname);
        btnPerinfoNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etPerinfoNickname.getText().toString().equals("")||etPerinfoNickname.getText().toString()==null){
                    Toast.makeText(PerinfoNicknameActivity.this,"用户名不能为空凹~",Toast.LENGTH_SHORT).show();
                }else {
                    String nickname=etPerinfoNickname.getText().toString();
                    Message msg=new Message();
                    msg.what=4;
                    msg.obj=nickname;
                    EventBus.getDefault().post(msg);
                    finish();
                }
            }
        });

    }
}