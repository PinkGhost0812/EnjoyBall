package com.example.lenovo.enjoyball;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lenovo.Activity.HomepageActivity;
import com.example.lenovo.Util.AuthorAndComment;
import com.example.lenovo.entity.Comment;
import com.example.lenovo.entity.User;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CommentAdapter extends BaseAdapter {
    private Handler handler;
    // 原始数据
    private List<Map<String, Object>> dataSource = null;
    // 上下文环境
    private Context context = null;
    // item对应的布局文件
    private int item_layout_id;
    private List<User> authors;
    private List<Comment> comments;
    private List<AuthorAndComment> list;
    ViewHolder viewHolder = null;

    public CommentAdapter(
            List<Map<String,Object>> dataSource,
            Context context,
                           int item_layout_id) {
        this.dataSource = dataSource;
        this.context = context;
        this.item_layout_id = item_layout_id;
    }
    @Override
    public int getCount() {
        return dataSource.size();
    }

    @Override
    public Object getItem(int position) {
        return dataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(item_layout_id,null);
            viewHolder = new ViewHolder();
            viewHolder.head = convertView.findViewById(R.id.iv_comment_headImg);
            viewHolder.nickname = convertView.findViewById(R.id.tv_comment_authorName);
            viewHolder.commentcontent = convertView.findViewById(R.id.tv_comment_content);
            viewHolder.releasetime = convertView.findViewById(R.id.tv_comment_releaseTime);
            viewHolder.likenum = convertView.findViewById(R.id.tv_comment_likeNum);
            viewHolder.heart = convertView.findViewById(R.id.iv_comment_likeImg);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.nickname.setText(dataSource.get(position).get("nicknames").toString());
        viewHolder.commentcontent.setText(dataSource.get(position).get("contents").toString());
        viewHolder.releasetime.setText(dataSource.get(position).get("times").toString());
        viewHolder.likenum.setText(dataSource.get(position).get("likenums").toString());
        Glide.with(convertView).load(Info.BASE_URL+dataSource.get(position).get("heads")).into(viewHolder.head);
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:   //更新新闻点赞的数字
                        int likeNum = Integer.parseInt(viewHolder.likenum.getText().toString());

                        viewHolder.likenum.setText(++likeNum + "");
                        viewHolder.likenum.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_light));
                        break;
                }
            };
        };
        viewHolder.heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.likenum.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_light));
                viewHolder.likenum.setText(Integer.parseInt(dataSource.get(position).get("likenums").toString())+1+"");
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder().url(Info.BASE_URL + "information/likeComment?id=" + dataSource.get(position).get("ids")).build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                    }
                });
            }
        });
        final View finalConvertView = convertView;
        viewHolder.head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(finalConvertView.getContext(), HomepageActivity.class);
                intent.putExtra("user",(User)dataSource.get(position).get("users"));
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    private void addnum(String comment) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request newsLikeRequest = new Request.Builder().url(Info.BASE_URL + "news/like?id=" + comment).build();
        Call newsLikeCall = okHttpClient.newCall(newsLikeRequest);
        newsLikeCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

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

    private class ViewHolder{
        public ImageView head;
        public TextView nickname;
        public TextView commentcontent;
        public TextView releasetime;
        public TextView likenum;
        public ImageView heart;
    }
}
