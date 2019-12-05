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
import com.example.lenovo.entity.Contest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameAdapter extends BaseAdapter {

    private List<Contest> dataSource = null;

    private Context context = null;

    private int item_layout_id;


    public GameAdapter(Context context,
                       List<Contest> dataSource,
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
            viewHolder.tv_time = convertView.findViewById(R.id.tv_game_time);
            viewHolder.tv_teamhome = convertView.findViewById(R.id.tv_game_team_home);
            viewHolder.tv_teamaway = convertView.findViewById(R.id.tv_game_team_away);
            viewHolder.tv_scorehome = convertView.findViewById(R.id.tv_game_score_home);
            viewHolder.tv_scoreaway = convertView.findViewById(R.id.tv_game_score_away);
            viewHolder.tv_state = convertView.findViewById(R.id.tv_game_state);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Contest contest = dataSource.get(position);
//        Glide.with(context)
//                .load(Info.BASE_URL+contest.get)
        viewHolder.tv_time.setText(contest.getGame_time()+"");
        viewHolder.tv_teamhome.setText(contest.getGame_home());
        viewHolder.tv_teamaway.setText(contest.getGame_away());
        viewHolder.tv_scorehome.setText(contest.getGame_grade());
        viewHolder.tv_scoreaway.setText(contest.getGame_grade());
        viewHolder.tv_state.setText(contest.getGame_status()+"");
        return convertView;
    }
    private class ViewHolder{
        TextView tv_time;
        TextView tv_teamhome;
        TextView tv_teamaway;
        TextView tv_scorehome;
        TextView tv_scoreaway;
        TextView tv_state;
    }
}
