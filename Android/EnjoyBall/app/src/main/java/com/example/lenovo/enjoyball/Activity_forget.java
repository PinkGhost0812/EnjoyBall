package com.example.lenovo.enjoyball;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Activity_forget extends AppCompatActivity {

    private ForgetLiseners forgetLiseners;
    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

        findView();
        setListeners();
    }

    private void setListeners(){
        forgetLiseners = new ForgetLiseners();
        ivBack.setOnClickListener(forgetLiseners);
    }

    private void findView(){
        ivBack = findViewById(R.id.iv_forget_back);
    }

    public class ForgetLiseners implements View.OnClickListener{

        @Override
        public void onClick(View v){
            switch (v.getId()){
                case R.id.iv_forget_back:
                    Intent intent = new Intent();
                    intent.setClass(Activity_forget.this,Activity_login.class);
                    startActivity(intent);
            }
        }
    }
}
