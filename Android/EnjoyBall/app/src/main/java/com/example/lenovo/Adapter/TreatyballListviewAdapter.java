package com.example.lenovo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lenovo.enjoyball.R;

import java.util.List;
import java.util.Map;

public class TreatyballListviewAdapter extends BaseAdapter {
    ViewHolder viewHolder;
    private List<Map<String,Object>> datasource = null;
    private Context context = null;
    private int item_layout_id;
    public TreatyballListviewAdapter(Context context,
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
            viewHolder.tv_dates = convertView.findViewById(R.id.tv_treatyball_date);
            viewHolder.tv_type = convertView.findViewById(R.id.tv_treatyball_type);
            viewHolder.tv_status = convertView.findViewById(R.id.tv_treatyball_symbol);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Map<String,Object> map = datasource.get(position);
        viewHolder.tv_dates.setText(map.get("dates").toString());
        switch ((int)map.get("types")){
            case 0:
                viewHolder.tv_type.setText("足球");
                break;
            case 1:
                viewHolder.tv_type.setText("篮球");
                break;
            case 2:
                viewHolder.tv_type.setText("排球");
                break;
            case 3:
                viewHolder.tv_type.setText("羽毛球");
                break;
            case 4:
                viewHolder.tv_type.setText("乒乓球");
                break;
        }
        switch ((int)map.get("status")){
            case 0:
                viewHolder.tv_status.setText("未开始");
                break;
            case 1:
                viewHolder.tv_status.setText("未开始");
                break;
            case 2:
                viewHolder.tv_status.setText("进行中");
                break;
            case 3:
                viewHolder.tv_status.setText("已结束");
                break;
        }
        return convertView;
    }

    private class ViewHolder{
        public TextView tv_dates;
        public TextView tv_type;
        public TextView tv_status;
    }
}
