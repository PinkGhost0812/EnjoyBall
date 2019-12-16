package com.example.lenovo.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lenovo.enjoyball.GlideApp;
import com.example.lenovo.enjoyball.Info;
import com.example.lenovo.enjoyball.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

public class AgreementAdapter extends BaseAdapter{
    private List<Map<String,Object>> datasource;
    private int itemResId;
    private Context mContext;
    private Bitmap bitmap;
    public AgreementAdapter(List<Map<String,Object>> datasource,int itemResId,Context mContext){
        this.datasource = datasource;
        this.itemResId = itemResId;
        this.mContext = mContext;
    }
    @Override
    public int getCount() {
        if (null != datasource){
            return datasource.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (null != datasource){
            return datasource.get(position);
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
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(itemResId,null);
            viewHolder = new ViewHolder();
            viewHolder.tv_name = convertView.findViewById(R.id.tv_agreement_name);
            viewHolder.iv_head = convertView.findViewById(R.id.iv_agreement_head);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_name.setText(datasource.get(position).get("name").toString());
       if (datasource.get(position).get("status").equals("0")){
           viewHolder.iv_head.setImageDrawable((Drawable) datasource.get(position).get("head"));

       }else {
             GlideApp.with(mContext)
                     .load(datasource.get(position).get("head").toString())
                     .circleCrop()
                     .error(mContext.getResources().getDrawable(R.drawable.basketball))
                   .into(viewHolder.iv_head);
       }

        return convertView;
    }
    private class ViewHolder{
        public TextView tv_name;
        public ImageView iv_head;
    }
}
