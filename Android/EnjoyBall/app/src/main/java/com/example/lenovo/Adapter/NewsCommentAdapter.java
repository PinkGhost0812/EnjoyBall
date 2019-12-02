package com.example.lenovo.Adapter;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

<<<<<<< Updated upstream:Android/EnjoyBall/app/src/main/java/com/example/lenovo/Adapter/NewsCommentAdapter.java
import com.example.lenovo.enjoyball.Info;
import com.example.lenovo.enjoyball.R;
=======
import com.bumptech.glide.Glide;
>>>>>>> Stashed changes:Android/EnjoyBall/app/src/main/java/com/example/lenovo/enjoyball/NewsCommentAdapter.java
import com.example.lenovo.entity.Comment;
import com.example.lenovo.entity.User;
import com.google.gson.Gson;

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

public class NewsCommentAdapter extends BaseAdapter {

    private Context context;
    private List<Comment> dataSource;
    private int itemView;
    private OkHttpClient okHttpClient;
    private User author;


    public NewsCommentAdapter(Context context, List<Comment> dataSource, int itemView) {
        this.context = context;
        this.dataSource = dataSource;
        this.itemView = itemView;
//        EventBus.getDefault().register(context);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleMessage(String event){
        String[] cache = event.split("-");
        if(cache[0].equals("NewsCommentFindUser")){
            author = new Gson().fromJson(cache[1],User.class);
        }
        Log.e("author = ",author.toString());
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
        return dataSource.get(position).getComment_id();
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
            holder = (ViewHolder) convertView.getTag();
        }
        getAuthor(position);
        Log.e("datasource = " , dataSource.toString());
        //TODO:Author现在没信息拿不到，到时候记得改；   是一个空指针，可能是线程还没跑完，这个开始执行了
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Glide.with(convertView).load(Info.BASE_URL + author.getUser_headportrait()).into(holder.iv_headImg);
        holder.tv_authorName.setText(author.getUser_nickname());
//        holder.tv_authorName.setText("李烦烦");
        holder.tv_commentContent.setText(dataSource.get(position).getComment_content());
        holder.tv_releaseTime.setText(dataSource.get(position).getComment_time().toString());
        holder.tv_commentLikeNum.setText(dataSource.get(position).getComment_likenum() + "1");
        return convertView;
    }



    public void getAuthor(int position){
        okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(Info.BASE_URL + "user/find?id=" + dataSource.get(position).getComment_author()).build();
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
                String ans = response.body().string();
//                EventBus.getDefault().post("NewsCommentFindUser-" + ans);
                author = new Gson().fromJson(ans,User.class);
//                Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
                Log.e("author = ",author.toString());
            }
        });
    }

    static class ViewHolder{
        ImageView iv_headImg;
        TextView tv_authorName;
        TextView tv_commentContent;
        TextView tv_releaseTime;
        TextView tv_commentLikeNum;
    }
}
