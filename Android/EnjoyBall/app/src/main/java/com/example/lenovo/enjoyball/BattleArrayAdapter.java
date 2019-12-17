package com.example.lenovo.enjoyball;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lenovo.Activity.HomepageActivity;
import com.example.lenovo.entity.User;

import java.util.List;
import java.util.Map;

public class BattleArrayAdapter extends BaseAdapter {
    // 原始数据
    private List<Map<String, Object>> dataSource = null;
    // 上下文环境
    private Context context = null;
    // item对应的布局文件
    private int item_layout_id;

    private List<User> team1;
    private List<User> team2;
    @Override
    public int getCount() {
        return dataSource.size();
    }

    public BattleArrayAdapter(
            List<User> team1,
            List<User> team2,
            Context context,
                         List<Map<String, Object>> dataSource,
                         int item_layout_id) {
        this.team1 = team1;
        this.team2 = team2;
        this.context = context;
        this.dataSource = dataSource;
        this.item_layout_id = item_layout_id;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(item_layout_id,null);
            viewHolder = new ViewHolder();
            viewHolder.Team1Member = convertView.findViewById(R.id.tv_battleArray_badminton_name1);
            viewHolder.Team2Member = convertView.findViewById(R.id.tv_battleArray_badminton_name2);
            viewHolder.Team1Img = convertView.findViewById(R.id.iv_battleArray_badminton_headImg1);
            viewHolder.Team2Img = convertView.findViewById(R.id.iv_battleArray_badminton_headImg2);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.Team1Member.setText(dataSource.get(position).get("nickname1").toString());
        viewHolder.Team2Member.setText(dataSource.get(position).get("nickname2").toString());
        Glide.with(convertView).load(Info.BASE_URL+dataSource.get(position).get("photo1")).into(viewHolder.Team1Img);
        Glide.with(convertView).load(Info.BASE_URL+dataSource.get(position).get("photo2")).into(viewHolder.Team2Img);

        final View finalConvertView = convertView;
        viewHolder.Team1Img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(finalConvertView.getContext(), HomepageActivity.class);
                intent.putExtra("user",team1.get(position));
                context.startActivity(intent);
            }
        });
        viewHolder.Team2Img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(finalConvertView.getContext(), HomepageActivity.class);
                intent.putExtra("user",team2.get(position));
                context.startActivity(intent);
            }
        });
        return convertView;
    }


    private class ViewHolder{
        public TextView Team1Member;
        public TextView Team2Member;
        public ImageView Team1Img;
        public ImageView Team2Img;
    }
}
