package com.example.lenovo.Activity;

import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.Util.RotatePan;
import com.example.lenovo.enjoyball.Info;
import com.example.lenovo.enjoyball.R;
import com.example.lenovo.entity.User;
import com.example.lenovo.view.LuckPanLayout;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LuckPanActivity extends AppCompatActivity implements LuckPanLayout.AnimationEndListener{
    private RotatePan rotatePan;
    private LuckPanLayout luckPanLayout;
    private ImageView goBtn;
    private ImageView yunIv;
    private OkHttpClient okHttpClient;
    private String[] strs ;
    private int my_score = 600;
    private int number=0;
    private TextView tvScore;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.nonetitle);
        setContentView(R.layout.activity_luck_pan);
        user = ((Info) getApplicationContext()).getUser();
        my_score = user.getUser_score();
        strs = getResources().getStringArray(R.array.names);
        luckPanLayout = (LuckPanLayout) findViewById(R.id.luckpan_layout);
        tvScore = findViewById(R.id.hit_user_tv);
        tvScore.setText(my_score+"");
        luckPanLayout.setAnimationEndListener(this);
        goBtn = (ImageView)findViewById(R.id.go);
        //yunIv = (ImageView)findViewById(R.id.yun);
    }

    public void rotation(View view){
        if(my_score>=100){
            my_score-=100;
            tvScore.setText(my_score+"");
            if (number<3){
                if (number==0){

                    luckPanLayout.rotate(7,100);
                    ++number;
                }else {

                    luckPanLayout.rotate(0,100);
                    ++number;
                }
            }else
                luckPanLayout.rotate(-1,100);
        }else {
            Toast.makeText(getApplicationContext(), "抱歉，您的积分不足", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void endAnimation(int position) {
        okHttpClient =new OkHttpClient();
        if (position%2!=0){
            final int score = Integer.parseInt(strs[position]);
            my_score+=score;
            tvScore.setText(my_score+"");

            Request request = new Request.Builder().url(Info.BASE_URL + "user/updateScore?id="+user.getUser_id()+"&score="+my_score).build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Looper.prepare();
                    Toast.makeText(LuckPanActivity.this, "抽奖失败，请稍后重试", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), "恭喜您抽中"+score+"积分！", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            });
        }else {
            Request request = new Request.Builder().url(Info.BASE_URL + "user/updateScore?id="+user.getUser_id()+"&score="+my_score).build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Looper.prepare();
                    Toast.makeText(LuckPanActivity.this, "抽奖失败，请稍后重试", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), "谢谢参与", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            });
        }
    }
}
