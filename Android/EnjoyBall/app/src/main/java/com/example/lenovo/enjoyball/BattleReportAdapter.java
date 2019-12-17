package com.example.lenovo.enjoyball;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Map;

public class BattleReportAdapter extends BaseAdapter {

    private int layout_id;
    private List<Map<String,Object>> datasource;
    private Context context;
    ViewHolder viewHolder = null;

    public BattleReportAdapter(Context context,List<Map<String,Object>> datasource,int layout_id){
        this.context = context;
        this.datasource = datasource;
        this.layout_id = layout_id;
    }

    @Override
    public int getCount() {
        return datasource.size();
    }

    @Override
    public Object getItem(int position) {
        return datasource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(layout_id,null);
            viewHolder = new ViewHolder();
            viewHolder.newstitle = convertView.findViewById(R.id.tv_battle_report_content);
            viewHolder.newsauthor = convertView.findViewById(R.id.tv_battle_report_messageSource);
            viewHolder.newstime = convertView.findViewById(R.id.tv_battle_report_releaseTime);
            viewHolder.newsimage = convertView.findViewById(R.id.iv_battle_report_img);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.newsauthor.setText(datasource.get(position).get("author").toString());
        viewHolder.newstime.setText(datasource.get(position).get("time").toString());
        viewHolder.newstitle.setText(datasource.get(position).get("title").toString());
        Glide.with(convertView).load(Info.BASE_URL+datasource.get(position).get("image")).into(viewHolder.newsimage);


        return convertView;
    }

    private class ViewHolder{
        public TextView newstitle;
        public TextView newstime;
        public TextView newsauthor;
        public ImageView newsimage;
    }
}
