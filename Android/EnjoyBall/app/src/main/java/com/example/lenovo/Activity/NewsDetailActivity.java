package com.example.lenovo.Activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lenovo.Adapter.NewsCommentAdapter;
import com.example.lenovo.Util.AuthorAndComment;
import com.example.lenovo.enjoyball.ApplicationUtil;
import com.example.lenovo.enjoyball.GlideApp;
import com.example.lenovo.enjoyball.MyAppGlideModule;
import com.example.lenovo.enjoyball.Info;
import com.example.lenovo.enjoyball.LinearLayoutForListView;
import com.example.lenovo.enjoyball.R;
import com.example.lenovo.entity.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    private TextView tv_dislike;
    private ListView lv_comment;
    private LinearLayoutForListView myLineraLayout;
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
    private List<AuthorAndComment> dataSource;
    private DateFormat df;
    private boolean collectFlag = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.nonetitle);
        setContentView(R.layout.activity_news);

        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        EventBus.getDefault().register(this);
        getViews();
        getNews();//拿到访问的新闻
        //getUser();//拿到当前的用户，暂时，以后知己APPlicationutil里面拿


//        getCollectType();


        TextColorNormal = tv_likeNum.getCurrentTextColor();
        TextColorSelect = ContextCompat.getColor(getApplicationContext(), android.R.color.holo_red_light);


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
                        dataSource = gson.fromJson((String)msg.obj,new TypeToken<List<AuthorAndComment>>(){}.getType());
                        adapter = new NewsCommentAdapter(getBaseContext(),dataSource,R.layout.comment_item);
                        myLineraLayout.setAdapter(adapter);
                        break;
                    case 3:   //点击不喜欢之后更新主界面
                        tv_dislike.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_red_light));
                        break;
                    case 4:   //发布新的评论之后更新dataSource
                        AuthorAndComment aac = new AuthorAndComment();
                        Info info = (Info) getApplicationContext();
                        aac.setAuthor(info.getUser());
                        aac.setComment((Comment)msg.obj);
                        dataSource.add(aac);
                        myLineraLayout.setAdapter(adapter);
                        ed_comment.setText("");
                        InputMethodManager inputManager =
                                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                        ed_comment.clearFocus();
//                        adapter.notifyDataSetChanged();
                        break;
                    case 5:   //临时获取用户设置到application
                        User u = gson.fromJson((String)msg.obj,User.class);
                        Info info1 = (Info) getApplicationContext();
                        info1.setUser(u);
//                        getCollectType();
                        break;
                }
            }
        };

        setOnclick();
        getCommentAndAuthor();
//        sml.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
//                adapter.notifyDataSetChanged();
//                sml.finishRefresh();
//            }
//        });
    }

    private void sendComment(){
        String content = ed_comment.getText().toString();
        if(content.equals("") || content == null){
            Looper.prepare();
            Toast.makeText(getApplicationContext(), "啥也没写，发啥评论", Toast.LENGTH_SHORT).show();
            Looper.loop();
            return;
        }

        Info info = (Info)getApplication();
        Comment comment = new Comment();
        Integer author = info.getUser().getUser_id();
        Integer cla = news.getNews_class();
        Integer likeNum = 0;
        Date date = new Date();
        Integer belong =  news.getNews_id();

        //属性赋值
        comment.setComment_author(author);
        comment.setComment_class(cla);
        comment.setComment_content(content);
        comment.setComment_likenum(likeNum);
        comment.setComment_time(date);
        comment.setComment_belone(belong);

        Request request = new Request.Builder().url(Info.BASE_URL + "information/addComment?info=" + gson.toJson(comment)).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(getApplicationContext(), "实训项目写完了？还在这发评论", Toast.LENGTH_SHORT).show();
                Looper.loop();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String ans = response.body().string();
                Comment newComment = gson.fromJson(ans,Comment.class);
                Message msg = Message.obtain();
                msg.what = 4;
                msg.obj = newComment;
                handler.sendMessage(msg);
            }
        });
    }

    private void getCollectType(){
        Collection collection = new Collection();
        Info info = (Info)getApplication();
        collection.setNew_id(news.getNews_id());
        collection.setUser_id(info.getUser().getUser_id());
        Request newCollectRequest = new Request.Builder().url(Info.BASE_URL + "news/collectType?info=" + gson.toJson(collection)).build();
        Call newCollectCall = okHttpClient.newCall(newCollectRequest);
        newCollectCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.body().string().equals("true")) {
                    EventBus.getDefault().post("collect");
                    collectFlag = true;
                } else {
                    EventBus.getDefault().post("unCollect");
                    collectFlag = false;
                }
                EventBus.getDefault().post("getCollectType");
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleMessage(String event){
        switch (event){
            case "news":
                getNewsAuthor();
                break;
            case "newsAuthor":
                getCollectType();
                break;
            case "getCollectType":
                setContent();
                break;
            case "notify":
                adapter.notifyDataSetChanged();
                break;
            case "collect":
                Glide.with(this).load(R.drawable.cols).into(iv_collectNews);
                break;
            case "unCollect":
                Glide.with(getApplicationContext()).load(R.drawable.col).into(iv_collectNews);
                break;
            case "commentLike":
                Log.e("like","shoudao");
                break;
        }
    }

    private void collect(){
        Collection collection = new Collection();
        Info info = (Info)getApplication();
        collection.setNew_id(news.getNews_id());
        collection.setUser_id(info.getUser().getUser_id());
        Request newCollectRequest = new Request.Builder().url(Info.BASE_URL + "news/collect?info=" + gson.toJson(collection)).build();
        Call newCollectCall = okHttpClient.newCall(newCollectRequest);
        newCollectCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.body().string().equals("true")) {
                    EventBus.getDefault().post("collect");
                    collectFlag = true;
                }
            }
        });
    }

    private void unCollect(){
        Collection collection = new Collection();
        Info info = (Info)getApplication();
        collection.setNew_id(news.getNews_id());
        collection.setUser_id(info.getUser().getUser_id());
        Request request = new Request.Builder().url(Info.BASE_URL + "news/unCollect?info=" + gson.toJson(collection)).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String ans = response.body().string();
                if(ans.equals("true")){
                    EventBus.getDefault().post("unCollect");
                    collectFlag = false;
                }
            }
        });
    }

    private void getCommentAndAuthor() {
        //TODO：这里的newsId
        String id = getIntent().getStringExtra("id");
        Request request = new Request.Builder().url(Info.BASE_URL + "information/getCommentAndAuthor?id=" + Integer.parseInt(id)).build();
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
        Request request = new Request.Builder().url(Info.BASE_URL + "news/findById?id=" + Integer.parseInt(id)).build();
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
                newsAuthor = gson.fromJson(ans,User.class);
                EventBus.getDefault().post("newsAuthor");
            }
        });
    }

    private void setContent(){
        tv_title.setText(news.getNews_title());
        GlideApp.with(this).load(Info.BASE_URL + newsAuthor.getUser_headportrait()).circleCrop().into(iv_headImg);
        tv_authorName.setText(newsAuthor.getUser_nickname());
        tv_releaseTime.setText(df.format(news.getNews_time()));
        tv_newsBody.setText(news.getNews_content());
        tv_likeNum.setText(news.getNews_likenum()+"");
        if(collectFlag)
            Glide.with(getApplicationContext()).load(R.drawable.cols).into(iv_collectNews);
    }

    private void setOnclick(){
        btn_attention.setOnClickListener(this);
        ll_like.setOnClickListener(this);
        ll_dislike.setOnClickListener(this);
        iv_releaseComment.setOnClickListener(this);
        iv_collectNews.setOnClickListener(this);
    }

    private void getViews(){
        okHttpClient = new OkHttpClient();
//        sml = findViewById(R.id.srl_news_lv);
        tv_title = findViewById(R.id.tv_news_title);
        iv_headImg = findViewById(R.id.iv_news_headImg);
        tv_authorName = findViewById(R.id.tv_news_authorName);
        tv_releaseTime = findViewById(R.id.tv_news_releaseTime);
        btn_attention = findViewById(R.id.btn_news_attention);
        tv_newsBody = findViewById(R.id.tv_news_body);
        ll_like = findViewById(R.id.ll_news_like);
        ll_dislike = findViewById(R.id.ll_news_dislike);
        tv_likeNum = findViewById(R.id.tv_news_likeNum);
        tv_dislike = findViewById(R.id.tv_news_dislike);
//        lv_comment = findViewById(R.id.lv_news_comments);
        myLineraLayout = findViewById(R.id.llflv_news_comments);
        ed_comment = findViewById(R.id.ed_news_commentContent);
        iv_releaseComment = findViewById(R.id.iv_news_releaseComment);
        iv_collectNews = findViewById(R.id.iv_news_collect);
        iv_remind = findViewById(R.id.iv_news_remindWho);
    }

//    private void getUser(){
//        Request request = new Request.Builder().url(Info.BASE_URL + "user/findByPhoneNumber?phone=18103106427").build();
//        Call call = okHttpClient.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Looper.prepare();
//                Toast.makeText(getApplicationContext(), "获取用户信息失败", Toast.LENGTH_SHORT).show();
//                Looper.loop();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                Message msg = Message.obtain();
//                msg.what = 5;
//                msg.obj = response.body().string();
//                handler.sendMessage(msg);
//            }
//        });
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_news_attention:
                Info info1 = (Info) getApplication();
                int authorId = news.getNews_author();
                int userId =info1.getUser().getUser_id();
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
                if(tv_likeNum.getCurrentTextColor() != TextColorNormal || tv_dislike.getCurrentTextColor() != TextColorNormal)
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
                break;

            case R.id.ll_news_dislike:
                if(tv_dislike.getCurrentTextColor() != TextColorNormal || tv_likeNum.getCurrentTextColor() == TextColorSelect)
                    break;
                Message msg = Message.obtain();
                msg.what = 3;
                handler.sendMessage(msg);
                break;
            case R.id.iv_news_releaseComment:
                sendComment();
                break;

            case  R.id.iv_news_collect:
                if(collectFlag){
                    unCollect();
                }else {
                    collect();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


}
