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
import com.example.lenovo.entity.DemandInfo;

import java.text.SimpleDateFormat;
import java.util.List;

public class DemandAdapter extends BaseAdapter {

    View view = null;

    private List<DemandInfo> dataSource = null;

    private Context context = null;

    private int item_layout_id;
    private int[] ball = {R.drawable.football,R.drawable.basketball,R.drawable.volleyball,R.drawable.badminton,R.drawable.tabletennis};
    private String[] type = {"个人约球","队伍约球"};
    private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd  HH:mm");


    public DemandAdapter(Context context,
                         List<DemandInfo> dataSource,
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
        ViewHolder viewHolder = null;
        if(null == convertView) {
            convertView = LayoutInflater.from(context).inflate(item_layout_id,null);
            viewHolder = new ViewHolder();
            viewHolder.iv_img = convertView.findViewById(R.id.iv_time_img);
            viewHolder.tv_time = convertView.findViewById(R.id.tv_time_time);
            viewHolder.tv_place = convertView.findViewById(R.id.tv_time_place);
            viewHolder.tv_dp = convertView.findViewById(R.id.tv_time_dp);
            viewHolder.tv_type = convertView.findViewById(R.id.tv_time_type);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.iv_img.setImageResource(ball[dataSource.get(position).getDemand_class()]);
        viewHolder.tv_time.setText(sf.format(dataSource.get(position).getDemand_time()));
        viewHolder.tv_place.setText(dataSource.get(position).getDemand_place()+"");
        viewHolder.tv_dp.setText(dataSource.get(position).getDemand_description()+"");
        viewHolder.tv_type.setText(type[dataSource.get(position).getDemand_oom()]);
        return convertView;
    }

    private class ViewHolder{
        ImageView iv_img;
        TextView tv_time;
        TextView tv_place;
        TextView tv_dp;
        TextView tv_type;
    }
}
