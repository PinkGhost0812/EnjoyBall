package com.example.lenovo.Adapter;

import android.content.Context;
import android.util.Log;
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
import com.example.lenovo.entity.User;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class CollectListviewAdapter extends BaseAdapter {
    private List<Map<String,Object>> datasource = null;
    private Context context = null;
    private int item_layout_id;
    ViewHolder viewHolder = null;


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
            viewHolder = new ViewHolder();
            viewHolder.tv_heads = convertView.findViewById(R.id.tv_collectItem_heads);
            viewHolder.tv_authors = convertView.findViewById(R.id.tv_collectItem_authors);
            viewHolder.tv_times = convertView.findViewById(R.id.tv_collectItem_times);
            viewHolder.imgs = convertView.findViewById(R.id.img_collectItem_imgs);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        Map<String,Object> map = datasource.get(position);
        News news=(News)map.get("newsList");
        User user=(User)map.get("authorList");
        Log.e("test user",user.toString());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        viewHolder.tv_heads.setText(news.getNews_title());
        viewHolder.tv_authors.setText(user.getUser_nickname()+"");
        viewHolder.tv_times.setText( simpleDateFormat.format(news.getNews_time()));
        Glide.with(convertView).load(Info.BASE_URL+news.getNews_image()).into(viewHolder.imgs);

        return convertView;
    }

    private class ViewHolder{
        public TextView tv_heads;
        public TextView tv_authors;
        public TextView tv_times;
        public ImageView imgs ;
    }
}
