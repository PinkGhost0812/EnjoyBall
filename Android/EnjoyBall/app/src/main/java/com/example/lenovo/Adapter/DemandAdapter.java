package com.example.lenovo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lenovo.enjoyball.R;
import com.example.lenovo.entity.DemandInfo;

import java.util.List;

public class DemandAdapter extends BaseAdapter {

    View view = null;

    private List<DemandInfo> dataSource = null;

    private Context context = null;

    private int item_layout_id;

    private ImageView ivId;
    private TextView tvNews;
    private TextView tvHeat;

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
            viewHolder.tv_time = convertView.findViewById(R.id.tv_time_time);
            viewHolder.tv_place = convertView.findViewById(R.id.tv_time_place);
            viewHolder.tv_dp = convertView.findViewById(R.id.tv_time_dp);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        DemandInfo demandInfo = dataSource.get(position);
        viewHolder.tv_time.setText(demandInfo.getDemand_time()+"");
        viewHolder.tv_place.setText(demandInfo.getDemand_place());
        viewHolder.tv_dp.setText(demandInfo.getDemand_description()+"");
        return convertView;
    }

    private class ViewHolder{
        TextView tv_time;
        TextView tv_place;
        TextView tv_dp;
    }
}
