package com.example.lenovo.enjoyball;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class MessageAdapter extends BaseAdapter {
    private List<Map<String,Object>> datasource;
    private int itemResId;
    private Context mContext;
    public MessageAdapter(List<Map<String,Object>> datasource, int itemResId, Context mContext){
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
            viewHolder.tv_messagecontent = convertView.findViewById(R.id.tv_notificationmessage_content);
            viewHolder.iv_head = convertView.findViewById(R.id.iv_notificationmessage_head);
            viewHolder.tv_time = convertView.findViewById(R.id.tv_notificationmessaage_time);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_messagecontent.setText(datasource.get(position).get("content").toString());
        viewHolder.tv_time.setText(datasource.get(position).get("time").toString());
        viewHolder.iv_head.setImageDrawable((Drawable)datasource.get(position).get("head"));
        return convertView;
    }
    private class ViewHolder{
        public TextView tv_messagecontent;
        public ImageView iv_head;
        public TextView tv_time;
    }
}
