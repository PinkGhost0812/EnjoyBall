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

public class DemandAdapter extends BaseAdapter {

    View view = null;

    private List<Map<String, Object>> dataSource = null;

    private Context context = null;

    private int item_layout_id;

    private ImageView ivId;
    private TextView tvNews;
    private TextView tvHeat;

    public DemandAdapter(Context context,
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
        TextView tv_time = convertView.findViewById(R.id.tv_time_time);
        TextView tv_place = convertView.findViewById(R.id.tv_time_place);
        TextView tv_team = convertView.findViewById(R.id.tv_time_team);
        TextView tv_dp = convertView.findViewById(R.id.tv_time_dp);

        Map<String,Object> map = dataSource.get(position);
        tv_time.setText(map.get("time").toString());
        tv_place.setText(map.get("place").toString());
        tv_team.setText(map.get("team").toString());
        tv_dp.setText(map.get("dp").toString());
        return convertView;
    }
}
