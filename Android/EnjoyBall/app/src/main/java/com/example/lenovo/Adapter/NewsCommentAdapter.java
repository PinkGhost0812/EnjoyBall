package com.example.lenovo.Adapter;

import android.content.Context;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.enjoyball.Info;
import com.example.lenovo.enjoyball.R;
import com.example.lenovo.entity.Comment;
import com.example.lenovo.entity.User;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NewsCommentAdapter extends BaseAdapter {

    private Context context;
    private List<Comment> dataSource;
    private int itemView;
    private OkHttpClient okHttpClient;


    public NewsCommentAdapter(Context context, List<Comment> dataSource, int itemView) {
        this.context = context;
        this.dataSource = dataSource;
        this.itemView = itemView;
        EventBus.getDefault().register(context);
    }

    @Subscribe
    private void handleMessage(){

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
        return dataSource.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.comment_item,null);
            holder = new ViewHolder();
            holder.iv_headImg = convertView.findViewById(R.id.iv_comment_headImg);
            holder.tv_authorName = convertView.findViewById(R.id.tv_comment_authorName);
            holder.tv_commentContent = convertView.findViewById(R.id.tv_comment_content);
            holder.tv_releaseTime = convertView.findViewById(R.id.tv_comment_releaseTime);
            holder.tv_commentLikeNum = convertView.findViewById(R.id.tv_comment_likeNum);
            convertView.setTag(holder);
        }else {

        }

        return convertView;
    }



    public User getUser(final int position){
        okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(Info.BASE_URL + "user/find?id=" + dataSource.get(position).getAuthor()).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(context, "服务器被炸了，小李正在修复呢", Toast.LENGTH_SHORT).show();
                Looper.loop();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                EventBus.getDefault().post(response.body().string());
            }
        });
        return null;
    }

    static class ViewHolder{
        ImageView iv_headImg;
        TextView tv_authorName;
        TextView tv_commentContent;
        TextView tv_releaseTime;
        TextView tv_commentLikeNum;
    }
}
