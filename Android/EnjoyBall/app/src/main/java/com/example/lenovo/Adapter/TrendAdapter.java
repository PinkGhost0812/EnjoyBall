package com.example.lenovo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lenovo.Activity.TrendDetailActivity;
import com.example.lenovo.enjoyball.GlideApp;
import com.example.lenovo.enjoyball.Info;
import com.example.lenovo.enjoyball.R;
import com.example.lenovo.entity.PYQ;
import com.example.lenovo.entity.User;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TrendAdapter extends BaseAdapter {
    private List<PYQ> dataSource;
    private int itemResId;
    private Context mContext;
    private String url = Info.BASE_URL + "user/updateGood?";

    public TrendAdapter(List<PYQ> dataSource, int itemResId, Context mContext) {
        this.dataSource = dataSource;
        this.itemResId = itemResId;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        if (null != dataSource) {
            return dataSource.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (null != dataSource) {
            return dataSource.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(itemResId, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_trendName = convertView.findViewById(R.id.tv_trend_name);
            viewHolder.iv_trendHead = convertView.findViewById(R.id.iv_trend_head);
            viewHolder.tv_trendTime = convertView.findViewById(R.id.tv_trend_time);
            viewHolder.tv_trendBody = convertView.findViewById(R.id.tv_trend_body);
            viewHolder.iv_trendBodyImg = convertView.findViewById(R.id.iv_trend_bodyImg);
            viewHolder.iv_trendLike = convertView.findViewById(R.id.iv_trend_likePic);
            viewHolder.iv_trendComment = convertView.findViewById(R.id.iv_trend_comment);
            viewHolder.tv_likeNum = convertView.findViewById(R.id.tv_trend_likeNum);
            viewHolder.tv_commentNum = convertView.findViewById(R.id.tv_trend_commentNum);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (dataSource.get(position).getImg()!=null){
            Log.e("朋友圈图片",dataSource.get(position).getImg());
            GlideApp.with(mContext)
                    .load(dataSource.get(position).getImg())
                    .circleCrop()
                    .error(mContext.getResources().getDrawable(R.drawable.member))
                    .into(viewHolder.iv_trendHead);
        }
        viewHolder.tv_trendName.setText(dataSource.get(position).getUserName());
        viewHolder.tv_trendTime.setText(dataSource.get(position).getTime());
        viewHolder.tv_trendBody.setText(dataSource.get(position).getContent());
        viewHolder.tv_likeNum.setText(dataSource.get(position).getGood());
        viewHolder.tv_commentNum.setText(dataSource.get(position).getNumber());
        viewHolder.iv_trendComment.setOnClickListener(new OnClicked(viewHolder.ifGood,dataSource.get(position)));
        viewHolder.iv_trendLike.setOnClickListener(new OnClicked(convertView,dataSource.get(position).getId()));
        return convertView;
    }

    private class ViewHolder {
        public TextView tv_trendBody;
        public ImageView iv_trendHead;
        public TextView tv_trendName;
        public TextView tv_trendTime;
        public ImageView iv_trendBodyImg;
        public ImageView iv_trendLike;
        public ImageView iv_trendComment;
        public TextView tv_likeNum;
        public TextView tv_commentNum;
        public  boolean ifGood = false;
        }
    private class OnClicked implements View.OnClickListener{
        private TextView likeNum;
        private View convertView;
        private int id;
        private PYQ pyq;
        private boolean ifGood;
        public  OnClicked(){

        }
        public OnClicked(View convertView,int id){
            this.id = id;
            this.convertView = convertView;
            likeNum = convertView.findViewById(R.id.tv_trend_likeNum);
        }
        public OnClicked(boolean ifGood,PYQ pyq){
            this.pyq = pyq;
            this.ifGood=ifGood;

        }


        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_trend_comment:
                    Intent intent = new Intent(mContext, TrendDetailActivity.class);
                    String json = new Gson().toJson(pyq.getComments());
                    intent.putExtra("comment",json);
                    intent.putExtra("head",pyq.getUserImg());
                    intent.putExtra("name",pyq.getUserName());
                    intent.putExtra("time",pyq.getTime());
                    intent.putExtra("body",pyq.getContent());
                    intent.putExtra("bodyImg",pyq.getImg());
                    intent.putExtra("goodNum",pyq.getGood());
                    intent.putExtra("commentNum",pyq.getNumber());
                    intent.putExtra("id",pyq.getId());
                    intent.putExtra("ifGood",ifGood);

                    mContext.startActivity(intent);
                    break;
                case R.id.iv_trend_likePic:
                    ViewHolder viewHolder = (ViewHolder) convertView.getTag();
                    if (viewHolder.ifGood==false) {
                        viewHolder.ifGood=true;
                        ImageView iv = (ImageView) v;
                        iv.setImageResource(R.drawable.gooda);
                        int num = Integer.parseInt(likeNum.getText().toString());
                        likeNum.setText(num++);
                        setLike(id,num);
                    }else {
                        viewHolder.ifGood=false;
                        ImageView iv = (ImageView) v;
                        iv.setImageResource(R.drawable.gooda);
                        int num = Integer.parseInt(likeNum.getText().toString());
                        likeNum.setText(num--);
                        setLike(id,num);

                    }
                    break;





            }
        }


    }

    private void setLike(int id,int num) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url+"id="+id+"&&good="+num)
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
}
