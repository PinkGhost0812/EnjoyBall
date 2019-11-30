package com.example.lenovo.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lenovo.enjoyball.Info;
import com.example.lenovo.enjoyball.R;
import com.example.lenovo.entity.*;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NewsDetailActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView tv_title;
    private ImageView iv_headImg;
    private TextView tv_authorName;
    private TextView tv_releaseTime;
    private Button btn_attention;
    private TextView tv_newsBody;
    private LinearLayout ll_like;
    private LinearLayout ll_dislike;
    private TextView tv_likeNum;
    private ListView lv_comment;
    private EditText ed_comment;
    private ImageView iv_releaseComment;
    private ImageView iv_collectNews;
    private ImageView iv_remind;
    private News news;
    private OkHttpClient okHttpClient;
    private Handler handler;
    private int TextColorNormal;
    private int TextColorSelect;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.nonetitle);
        setContentView(R.layout.activity_news);
        getViews();
        //setContent();

        TextColorNormal = tv_likeNum.getCurrentTextColor();
        Log.e("color",TextColorNormal+"");

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        int likeNum = Integer.parseInt(tv_likeNum.getText().toString());
                        tv_likeNum.setText(++likeNum + "");
                        tv_likeNum.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_red_light));
                        break;
                }
            }
        };

        String json = getIntent().getStringExtra("news");
        news = new Gson().fromJson(json, News.class);

        btn_attention.setOnClickListener(this);
        ll_like.setOnClickListener(this);
    }



    private void setContent(){
        tv_title.setText(news.getTitle());
        Glide.with(this).load(com.example.lenovo.enjoyball.Info.BASE_URL + "img/pm.png").into(iv_headImg);
        tv_authorName.setText(news.getAuthor());
        tv_releaseTime.setText(news.getTime()+"");
        tv_newsBody.setText(news.getContent());
        tv_likeNum.setText(news.getLikeNum());
    }

    private void getViews(){
        okHttpClient = new OkHttpClient();
        tv_title = findViewById(R.id.tv_news_title);
        iv_headImg = findViewById(R.id.iv_news_headImg);
        tv_authorName = findViewById(R.id.tv_news_authorName);
        tv_releaseTime = findViewById(R.id.tv_news_releaseTime);
        btn_attention = findViewById(R.id.btn_news_attention);
        tv_newsBody = findViewById(R.id.tv_news_body);
        ll_like = findViewById(R.id.ll_news_like);
        ll_dislike = findViewById(R.id.ll_news_dislike);
        tv_likeNum = findViewById(R.id.tv_news_likeNum);
        lv_comment = findViewById(R.id.lv_news_comments);
        ed_comment = findViewById(R.id.ed_news_commentContent);
        iv_releaseComment = findViewById(R.id.iv_news_releaseComment);
        iv_collectNews = findViewById(R.id.iv_news_collect);
        iv_remind = findViewById(R.id.iv_news_remindWho);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_news_attention:
                int authorId = news.getAuthor();
                int userId = new com.example.lenovo.enjoyball.Info().getUser().getUser_id();
                UserFans userFans = new UserFans(null,authorId,userId);
                String info = new Gson().toJson(userFans);
                Request request = new Request.Builder().url(com.example.lenovo.enjoyball.Info.BASE_URL + "user/attention?userFans=" + info).build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(), "关注失败", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.body().string().equals("true")) {
                            Looper.prepare();
                            Toast.makeText(getApplicationContext(), "关注成功", Toast.LENGTH_SHORT).show();

                            Looper.loop();
                        } else {
                            Looper.prepare();
                            Toast.makeText(getApplicationContext(), "你已经关注了", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                    }
                });
                break;

            case R.id.ll_news_like:
                //TODO:传参的ID没改呢,点赞之后会变红，没有写第二次点的时候变回去和点不喜欢之后不可以点赞
                if(tv_likeNum.getCurrentTextColor() != TextColorNormal)
                    break;
                Request newsLikeRequest = new Request.Builder().url(Info.BASE_URL + "news/like?id=" + 1).build();
                Call newsLikeCall = okHttpClient.newCall(newsLikeRequest);
                newsLikeCall.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(), "服务器被炸了，小李正在修复呢", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if(response.body().string().equals("true")){
                            Message msg = Message.obtain();
                            msg.what = 1;
                            handler.sendMessage(msg);
                        }
                    }
                });
        }
    }
}
