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

public class GameAdapter extends BaseAdapter {

    View view = null;

    Map<String, Object> map = null;

    private List<Map<String, Object>> dataSource = null;

    private Context context = null;

    private int item_layout_id;

    private ImageView ivId;
    private TextView tvNews;
    private TextView tvHeat;

    public GameAdapter(Context context,
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

        TextView tv_time = convertView.findViewById(R.id.tv_game_time);
        TextView tv_team1 = convertView.findViewById(R.id.tv_game_team_home);
        TextView tv_team2 = convertView.findViewById(R.id.tv_game_team_away);
        TextView tv_score1 = convertView.findViewById(R.id.tv_game_score_home);
        TextView tv_score2 = convertView.findViewById(R.id.tv_game_score_away);
        TextView tv_state = convertView.findViewById(R.id.tv_game_state);

        Map<String,Object> map = dataSource.get(position);
        tv_time.setText(map.get("time").toString());
        tv_team1.setText(map.get("team1").toString());
        tv_team2.setText(map.get("team2").toString());
        tv_score1.setText(map.get("score1").toString());
        tv_score2.setText(map.get("score2").toString());
        tv_state.setText(map.get("state").toString());
        return convertView;
    }
}
