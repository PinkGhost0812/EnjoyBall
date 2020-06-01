package com.example.lenovo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lenovo.enjoyball.R;
import com.example.lenovo.entity.Knapsack;

import java.util.List;

public class KnapsackAdapter extends BaseAdapter {

    private List<Knapsack> dataSource = null;

    private Context context = null;

    private int item_layout_id;


    public KnapsackAdapter(Context context,
                           List<Knapsack> dataSource,
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(null == convertView) {
            convertView = LayoutInflater.from(context).inflate(item_layout_id,null);
            viewHolder = new ViewHolder();
            viewHolder.tv_name = convertView.findViewById(R.id.tv_knapsack_name);
            viewHolder.tv_num = convertView.findViewById(R.id.tv_knapsack_num);
            viewHolder.iv_knapsack = convertView.findViewById(R.id.iv_knapsack_img);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_name.setText(dataSource.get(position).getKnapsack_name());
        viewHolder.tv_num.setText(dataSource.get(position).getKnapsack_num()+"");
        viewHolder.iv_knapsack.setImageResource(dataSource.get(position).getKnapsack_img());
        //Glide.with(convertView).load(Info.BASE_URL + dataSource.get(position).getKnapsack_image().toString()).into(viewHolder.iv_knapsack);

        return convertView;
    }
    private class ViewHolder{
        TextView tv_name;
        TextView tv_num;
        ImageView iv_knapsack;
    }


}
