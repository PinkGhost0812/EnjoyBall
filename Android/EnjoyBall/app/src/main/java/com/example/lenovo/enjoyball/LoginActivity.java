package com.example.lenovo.enjoyball;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    private LoginListeners loginListeners;
    private Button btLogin;
    private TextView tvRegistered;
    private TextView tvForget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findView();

        setListeners();
    }

    private void setListeners() {
        loginListeners = new LoginListeners();
        btLogin.setOnClickListener(loginListeners);
        tvRegistered.setOnClickListener(loginListeners);
        tvForget.setOnClickListener(loginListeners);

    }
    private void findView(){
        btLogin = findViewById(R.id.bt_login_lg);
        tvRegistered = findViewById(R.id.tv_login_registered);
        tvForget = findViewById(R.id.tv_login_forget);
    }

    private class LoginListeners implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.bt_login_lg:
                    Intent intent=new Intent();
                    intent.setClass(LoginActivity.this,MainActivity.class);
                    startActivity(intent);

                    break;
                case R.id.tv_login_forget:
                    Intent intent1=new Intent();
                    intent1.setClass(LoginActivity.this,ForgetActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.tv_login_registered:
                    Intent intent2=new Intent();
                    intent2.setClass(LoginActivity.this,RegisteredActivity.class);
                    startActivity(intent2);
                    break;
            }
        }
    }
}
