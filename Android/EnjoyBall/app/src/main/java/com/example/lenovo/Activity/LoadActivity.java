package com.example.lenovo.Activity;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.view.WindowManager;

import com.example.lenovo.enjoyball.R;

public class LoadActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.nonetitle);
        setContentView(R.layout.activity_load);

        getWindow().setFormat(PixelFormat.RGBA_8888);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);
        setContentView(R.layout.activity_load);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                //Go to main activity, and finish load activity
                Intent mainIntent = new Intent(LoadActivity.this, MainActivity.class);
                LoadActivity.this.startActivity(mainIntent);
                LoadActivity.this.finish();
            }
        }, 1000);

    }

}
