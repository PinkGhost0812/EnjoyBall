package com.example.lenovo.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.lenovo.enjoyball.R;

public class ForgetActivity extends AppCompatActivity {

    private ForgetLiseners forgetLiseners;
    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.nonetitle);
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
                    intent.setClass(ForgetActivity.this,LoginActivity.class);
                    startActivity(intent);
            }
        }
    }
}
