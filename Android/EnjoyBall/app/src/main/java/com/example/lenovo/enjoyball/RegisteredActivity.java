package com.example.lenovo.enjoyball;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class RegisteredActivity extends AppCompatActivity {

    private RegisteredLiseners forgetLiseners;
    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered);

        findView();
        setListeners();
    }

    private void setListeners(){
        forgetLiseners = new RegisteredLiseners();
        ivBack.setOnClickListener(forgetLiseners);
    }

    private void findView(){
        ivBack = findViewById(R.id.iv_registered_back);
    }

    public class RegisteredLiseners implements View.OnClickListener{

        @Override
        public void onClick(View v){
            switch (v.getId()){
                case R.id.iv_registered_back:
                    Intent intent = new Intent();
                    intent.setClass(RegisteredActivity.this,LoginActivity.class);
                    startActivity(intent);
            }
        }
    }
}
