package com.example.lenovo.enjoyball;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTheme(R.style.nonetitle);
        setContentView(R.layout.activity_team_create);
    }
}
