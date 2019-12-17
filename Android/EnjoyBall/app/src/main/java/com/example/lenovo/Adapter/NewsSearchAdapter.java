package com.example.lenovo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lenovo.enjoyball.Info;
import com.example.lenovo.enjoyball.R;
import com.example.lenovo.entity.News;

import java.util.List;
import java.util.Map;

public class NewsSearchAdapter extends BaseAdapter {

    //private List<Map<String,Object>> dataSource = null;

    Map<String, Object> map = null;

    private List<News> dataSource = null;

    private Context context = null;

    private int item_layout_id;

    public NewsSearchAdapter(Context context,
                             List<News> dataSource,
                             int item_layout_id) {
        this.context = context;
        this.dataSource = dataSource;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        NewsSearchAdapter.ViewHolder viewHolder = null;
        if (null == convertView) {
            convertView = LayoutInflater.from(context).inflate(item_layout_id,null);
            viewHolder = new NewsSearchAdapter.ViewHolder();
            viewHolder.iv_img = convertView.findViewById(R.id.iv_search_img);
            viewHolder.tv_news = convertView.findViewById(R.id.tv_search_news);
            viewHolder.tv_heat = convertView.findViewById(R.id.tv_search_heat);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (NewsSearchAdapter.ViewHolder) convertView.getTag();
        }

        Glide.with(convertView).load(Info.BASE_URL + dataSource.get(position).getNews_image()).into(viewHolder.iv_img);
        if (dataSource.get(position).getNews_title().length()>15)
            viewHolder.tv_news.setText(dataSource.get(position).getNews_title().substring(0,15)+"...");
        else
            viewHolder.tv_news.setText(dataSource.get(position).getNews_title());
        viewHolder.tv_heat.setText(dataSource.get(position).getNews_heat()+"");

        return convertView;
    }

    private class ViewHolder{
        ImageView iv_img;
        TextView tv_news;
        TextView tv_heat;
    }
}