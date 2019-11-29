package com.example.lenovo.enjoyball;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.lenovo.entity.User;

public class PerinfoActivity extends AppCompatActivity {

    private ImageView ivPerinfoPortrait;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.nonetitle);
        setContentView(R.layout.activity_perinfo);
        
        findView();
        
        User user= (User) getIntent().getSerializableExtra("user");
        
    }

    private void findView() {
    }
}
