package com.example.lenovo.Activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.Adapter.TrendAdapter;
import com.example.lenovo.Adapter.TrendCommentAdapter;
import com.example.lenovo.enjoyball.GlideApp;
import com.example.lenovo.enjoyball.Info;
import com.example.lenovo.enjoyball.R;
import com.example.lenovo.entity.PYQ;
import com.example.lenovo.entity.PYQComment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TrendDetailActivity extends AppCompatActivity {
    private ImageView iv_head;
    private TextView tv_name;
    private TextView tv_time;
    private TextView tv_body;
    private ImageView iv_bodyImg;
    private ImageView iv_like;
    private TextView tv_likeNum;
    private ListView lv_comment;
    private EditText et_comment;
    private Button btn_sendComment;
    private Handler handler;
    private int id;
    private int trendId;
    private boolean ifGood;
    private TrendCommentAdapter trendCommentAdapter;
    private String url = Info.BASE_URL + "user/";
    private List<PYQComment> dataSource;
    private OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trend_detail);
        id = ((Info) getApplicationContext()).getUser().getUser_id();
        EventBus.getDefault().register(this);
        getView();
        //给页面上各个控件赋值
        Intent intent = getIntent();
        Type type = new TypeToken<List<PYQComment>>(){}.getType();
        dataSource = new Gson().fromJson(intent.getStringExtra("comment"),type);
        trendCommentAdapter = new TrendCommentAdapter(dataSource,R.layout.activity_item_trendcomment,this);
        lv_comment.setAdapter(trendCommentAdapter);
        ifGood = intent.getBooleanExtra("ifGood",false);
        GlideApp.with(this)
                .load(Info.BASE_URL+intent.getStringExtra("head"))
                .circleCrop()
                .error(getResources().getDrawable(R.drawable.member))
                .into(iv_head);
        tv_name.setText(intent.getStringExtra("name"));
        tv_time.setText(intent.getStringExtra("time"));
        tv_body.setText(intent.getStringExtra("body"));
        trendId = intent.getIntExtra("id",404);
        if (intent.getStringExtra("bodyImg")!=null){
            GlideApp.with(this)
                    .load(Info.BASE_URL+intent.getStringExtra("bodyImg"))
                    .error(getResources().getDrawable(R.drawable.member))
                    .into(iv_bodyImg);
        }else{
            iv_bodyImg.setVisibility(View.GONE);
        }
        if (intent.getBooleanExtra("ifGood",false)){
            iv_like.setImageResource(R.drawable.gooda);
        }else {
            iv_like.setImageResource(R.drawable.goodb);
        }
        tv_likeNum.setText(intent.getIntExtra("likeNum",0)+" ");

    }
    private void getView() {
        iv_head = findViewById(R.id.iv_trend_head);
        tv_name = findViewById(R.id.tv_trend_name);
        tv_time = findViewById(R.id.tv_trend_time);
        tv_body = findViewById(R.id.tv_trend_body);
        iv_bodyImg = findViewById(R.id.iv_trend_bodyImg);
        iv_like = findViewById(R.id.iv_trend_likePic);
        tv_likeNum = findViewById(R.id.tv_trend_likeNum);
        lv_comment = findViewById(R.id.lv_trendDetail_comment);
        et_comment = findViewById(R.id.et_trendDetail_comment);
        btn_sendComment = findViewById(R.id.btn_trendDetail_send);
        //点赞
        iv_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.parseInt(tv_likeNum.getText().toString());
                if (ifGood){
                    ifGood=false;
                    iv_like.setImageResource(R.drawable.goodb);
                    tv_likeNum.setText(--num);
                }else {
                    ifGood = true;
                    iv_like.setImageResource(R.drawable.goodb);
                    tv_likeNum.setText(++num);

                }
                setLike(id,num);
            }
        });
        //发评论
        btn_sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = et_comment.getText().toString();
                if (comment==null){
                    Toast.makeText(getApplicationContext(),"评论内容不能为空",Toast.LENGTH_SHORT);
                    return;
                }
                sendComment(comment);
            }
        });
    }

    private void sendComment(String content) {
        PYQComment pyqComment = new PYQComment();
        pyqComment.setAuthor(id);
        pyqComment.setBelong(trendId);
        pyqComment.setContent(content);
        pyqComment.setUserImg(((Info) getApplicationContext()).getUser().getUser_headportrait());
        pyqComment.setUserName(((Info) getApplicationContext()).getUser().getUser_nickname());
        dataSource.add(pyqComment);
        String gson = new Gson().toJson(pyqComment);
        Request request = new Request.Builder()
                .url(url+"cnm?info="+gson)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message message = new Message();
                message.what = 1;
                EventBus.getDefault().post(message);
            }
        });

    }

    private void setLike(int id,int num) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url+"updateGood?id="+id+"&&good="+num)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("点赞","失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });


    }
    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void setAdapter(Message message) {
        switch (message.what){
            case 1:
                Log.e("更新评论列表","");
                trendCommentAdapter = new TrendCommentAdapter(dataSource,R.layout.listview_item_trend,this);
                lv_comment.setAdapter(trendCommentAdapter);
                break;

        }

    }
}
