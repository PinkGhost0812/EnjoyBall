package com.example.lenovo.Activity;

import android.content.Intent;
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
import com.example.lenovo.Adapter.NewsCommentAdapter;
import com.example.lenovo.enjoyball.Info;
import com.example.lenovo.enjoyball.R;
import com.example.lenovo.entity.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.List;

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
    private User newsAuthor;
    private OkHttpClient okHttpClient;
    private Handler handler;
    private int TextColorNormal;
    private int TextColorSelect;
    private NewsCommentAdapter adapter;
    private Gson gson;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.nonetitle);
        setContentView(R.layout.activity_news);

        Intent intent=getIntent();
        String id=intent.getStringExtra("homepage_news_id");
        Log.e("test",id.toString());

        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        EventBus.getDefault().register(this);
        getViews();
        getNews();
//        getNewsAuthor();

//        setContent();

        TextColorNormal = tv_likeNum.getCurrentTextColor();
        TextColorSelect = ContextCompat.getColor(getApplicationContext(), android.R.color.holo_red_light);
        Log.e("color",TextColorNormal+"");

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:   //更新新闻点赞的数字
                        int likeNum = Integer.parseInt(tv_likeNum.getText().toString());
                        tv_likeNum.setText(++likeNum + "");
                        tv_likeNum.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_red_light));
                        break;

                    case 2:   //获得了评论的全部内容，传给Adapter
                        List<Comment> list = gson.fromJson((String)msg.obj,new TypeToken<List<Comment>>(){}.getType());
                        adapter = new NewsCommentAdapter(getBaseContext(),list,R.layout.comment_item);
                        lv_comment.setAdapter(adapter);
                        break;
                }
            }
        };



        btn_attention.setOnClickListener(this);
        ll_like.setOnClickListener(this);
        getComment();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleMessage(String event){
        switch (event){
            case "news":
                Log.e("news = ",news.toString());
                getNewsAuthor();
                break;
            case "newsAuthor":
                setContent();
                break;
        }
    }


    private void getComment(){
        //TODO：这里的newsId
        Request request = new Request.Builder().url(Info.BASE_URL + "information/newscomment?belone=" + 1).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("获取当前新闻的评论","失败了");
                Looper.prepare();
                Toast.makeText(getApplicationContext(), "服务器被炸了，小李正在修复呢", Toast.LENGTH_SHORT).show();
                Looper.loop();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message msg = Message.obtain();
                msg.what = 2;
                msg.obj = response.body().string();
                handler.sendMessage(msg);
            }
        });
    }

    private void getNews(){
        String id = getIntent().getStringExtra("id");
        //TODO:拿到ID然后查找新闻
        Request request = new Request.Builder().url(Info.BASE_URL + "news/findById?id=" + 1).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(getApplicationContext(), "你距离网络有十万八千里", Toast.LENGTH_SHORT).show();
                Looper.loop();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String ans = response.body().string();
                Log.e("ans = ", ans);
                news = gson.fromJson(ans,News.class);
                EventBus.getDefault().post("news");
            }
        });
    }

    private void getNewsAuthor(){
        Integer id = news.getNews_author();
        Request request = new Request.Builder().url(Info.BASE_URL + "user/find?id=" + id).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(getApplicationContext(), "你距离网络有十万八千里", Toast.LENGTH_SHORT).show();
                Looper.loop();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String ans = response.body().string();
                Log.e("Author = ",ans);
                newsAuthor = gson.fromJson(ans,User.class);
                EventBus.getDefault().post("newsAuthor");
            }
        });
    }

    private void setContent(){
        Log.e("news = " , news.toString());
        tv_title.setText(news.getNews_title());
        Glide.with(this).load(Info.BASE_URL + "img/pm.png").into(iv_headImg);
        tv_authorName.setText(newsAuthor.getUser_nickname());
        tv_releaseTime.setText(news.getNews_time()+"");
        tv_newsBody.setText(news.getNews_content());
        tv_likeNum.setText(news.getNews_likenum()+"");
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
                int authorId = news.getNews_author();
                int userId = new Info().getUser().getUser_id();
                UserFans userFans = new UserFans(null,authorId,userId);
                String info = new Gson().toJson(userFans);
                Request request = new Request.Builder().url(Info.BASE_URL + "user/follow?userFans=" + info).build();
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

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
