package com.example.lenovo.Activity;

import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lenovo.enjoyball.R;

import org.greenrobot.eventbus.EventBus;

public class PerinfoEmailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.nonetitle);
        setContentView(R.layout.activity_perinfo_email);

        final EditText etPerinfoEmail=findViewById(R.id.et_perinfo_email);
        Button btnPerinfoEmail=findViewById(R.id.btn_perinfo_email);
        btnPerinfoEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etPerinfoEmail.getText().toString().equals("")||etPerinfoEmail.getText().toString()==null){
                    Toast.makeText(PerinfoEmailActivity.this,"邮箱不能为空凹~",Toast.LENGTH_SHORT).show();
                }else {
                    String email=etPerinfoEmail.getText().toString();
                    Message msg=new Message();
                    msg.what=5;
                    msg.obj=email;
                    EventBus.getDefault().post(msg);
                    finish();
                }
            }
        });

    }
}