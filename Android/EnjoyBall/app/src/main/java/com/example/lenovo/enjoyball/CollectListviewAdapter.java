package com.example.lenovo.enjoyball;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class CollectListviewAdapter extends BaseAdapter {
    private List<Map<String,Object>> datasource = null;
    private Context context = null;
    private int item_layout_id;


    public CollectListviewAdapter(Context context,
                         List<Map<String, Object>> dataSource,
                         int item_layout_id) {
        this.context = context;
        this.datasource = dataSource;
        this.item_layout_id = item_layout_id;
    }

    @Override
    public int getCount() {
        return datasource.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(null == convertView) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(item_layout_id, null);
        }

        TextView tv_heads = convertView.findViewById(R.id.tv_collectItem_heads);
        TextView tv_authors = convertView.findViewById(R.id.tv_collectItem_authors);
        TextView tv_times = convertView.findViewById(R.id.tv_collectItem_times);
        ImageView imgs = convertView.findViewById(R.id.img_collectItem_imgs);

        Map<String,Object> map = datasource.get(position);
        tv_heads.setText(map.get("heads").toString());
        tv_authors.setText(map.get("authors").toString());
        tv_times.setText(map.get("times").toString());
        imgs.setImageResource((int)map.get("imgs"));

        return convertView;
    }
}
