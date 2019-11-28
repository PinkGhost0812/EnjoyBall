package com.example.lenovo.enjoyball;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class NewsAdapter extends BaseAdapter {

    View view = null;

    Map<String, Object> map = null;

    private List<Map<String, Object>> dataSource = null;

    private Context context = null;

    private int item_layout_id;

    private ImageView ivId;
    private TextView tvNews;
    private TextView tvHeat;

    public NewsAdapter(Context context,
                                     List<Map<String, Object>> dataSource,
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
        if(null == convertView) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(item_layout_id, null);
        }
        ImageView iv_img = convertView.findViewById(R.id.iv_home_img);
        TextView tv_news = convertView.findViewById(R.id.tv_home_news);
        TextView tv_heat = convertView.findViewById(R.id.tv_home_heat);

        Map<String,Object> map = dataSource.get(position);
        iv_img.setImageResource((int)map.get("img"));
        tv_news.setText(map.get("content").toString());
        tv_heat.setText(map.get("heat").toString());
        return convertView;
    }
}
