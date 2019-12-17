package com.example.lenovo.Adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lenovo.Util.AuthorAndComment;
//import com.example.lenovo.enjoyball.GlideApp;
import com.example.lenovo.enjoyball.GlideApp;
import com.example.lenovo.enjoyball.Info;
import com.example.lenovo.enjoyball.R;
import com.example.lenovo.entity.User;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NewsCommentAdapter extends BaseAdapter {

    private Context context;
    private List<AuthorAndComment> dataSource;
    private int itemView;
    private OkHttpClient okHttpClient;
    private User author = null;
    private DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");


    public NewsCommentAdapter(Context context, List<AuthorAndComment> dataSource, int itemView) {
        this.context = context;
        this.dataSource = dataSource;
        this.itemView = itemView;
//        EventBus.getDefault().register(context);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleMessage(String event) {
        String[] cache = event.split("-");
        if (cache[0].equals("NewsCommentFindUser")) {
            author = new Gson().fromJson(cache[1], User.class);
        }
        Log.e("author = ", author.toString());
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
        return dataSource.get(position).getComment().getComment_id();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.comment_item, null);
            holder = new ViewHolder();
            holder.iv_headImg = convertView.findViewById(R.id.iv_comment_headImg);
            holder.tv_authorName = convertView.findViewById(R.id.tv_comment_authorName);
            holder.tv_commentContent = convertView.findViewById(R.id.tv_comment_content);
            holder.tv_releaseTime = convertView.findViewById(R.id.tv_comment_releaseTime);
            holder.tv_commentLikeNum = convertView.findViewById(R.id.tv_comment_likeNum);
            holder.iv_likeImg = convertView.findViewById(R.id.iv_comment_likeImg);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        GlideApp.with(convertView).load(Info.BASE_URL + dataSource.get(position).getAuthor().getUser_headportrait()).circleCrop().into(holder.iv_headImg);
        holder.tv_authorName.setText(dataSource.get(position).getAuthor().getUser_nickname());
//        holder.tv_authorName.setText("李烦烦");
        holder.tv_commentContent.setText(dataSource.get(position).getComment().getComment_content());
        holder.tv_releaseTime.setText(df.format(dataSource.get(position).getComment().getComment_time()));
        holder.tv_commentLikeNum.setText(dataSource.get(position).getComment().getComment_likenum() + " ");

        final ViewHolder finalHolder = holder;
        holder.iv_likeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalHolder.tv_commentLikeNum.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_light));
                finalHolder.tv_commentLikeNum.setText(dataSource.get(position).getComment().getComment_likenum()+1+"");
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder().url(Info.BASE_URL + "information/likeComment?id=" + dataSource.get(position).getComment().getComment_id()).build();
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


        return convertView;
    }

    static class ViewHolder {
        ImageView iv_headImg;
        ImageView iv_likeImg;
        TextView tv_authorName;
        TextView tv_commentContent;
        TextView tv_releaseTime;
        TextView tv_commentLikeNum;
    }
}
