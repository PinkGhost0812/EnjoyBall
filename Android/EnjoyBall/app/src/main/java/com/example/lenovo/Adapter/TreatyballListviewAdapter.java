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
        }
        TextView tv_troop1 = convertView.findViewById(R.id.tv_treatyball_troops1);
        TextView tv_troop2 = convertView.findViewById(R.id.tv_treatyball_troops2);
        TextView tv_scores1 = convertView.findViewById(R.id.tv_treatyball_score1);
        TextView tv_score2 = convertView.findViewById(R.id.tv_treatyball_score2);
        TextView tv_times = convertView.findViewById(R.id.tv_treatyball_time);
        TextView tv_dates = convertView.findViewById(R.id.tv_treatyball_date);
        TextView tv_place = convertView.findViewById(R.id.tv_treatyball_place);
        TextView tv_symbol = convertView.findViewById(R.id.tv_treatyball_symbol);
        TextView tv_type = convertView.findViewById(R.id.tv_treatyball_type);

        Map<String,Object> map = datasource.get(position);
        tv_troop1.setText(map.get("teams1").toString());
        tv_troop2.setText(map.get("teams2").toString());
        tv_scores1.setText(map.get("scores1").toString());
        tv_score2.setText(map.get("scores2").toString());
        tv_times.setText(map.get("times").toString());
        tv_place.setText(map.get("places").toString());
        tv_dates.setText(map.get("dates").toString());
        tv_type.setText(map.get("types").toString());
        tv_symbol.setText(map.get("symbols").toString());
        return convertView;
    }
}
