package com.example.lenovo.Adapter;

import android.content.Context;
import android.graphics.Color;
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
import com.example.lenovo.entity.TeamAndContest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameAdapter extends BaseAdapter {

    private List<TeamAndContest> dataSource = null;

    private Context context = null;

    private int item_layout_id;
    private SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
    private String[] status = {"已完场","进行中","未开始"};


    public GameAdapter(Context context,
                       List<TeamAndContest> dataSource,
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
            viewHolder.iv_imghome = convertView.findViewById(R.id.iv_game_team_imghome);
            viewHolder.iv_imgaway = convertView.findViewById(R.id.iv_game_team_imgaway);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //Contest contest = dataSource.get(position);
//        Glide.with(context)
//                .load(Info.BASE_URL+contest.get)

        viewHolder.tv_time.setText(sf.format(dataSource.get(position).getContest().getGame_time())+dataSource.get(position).getContest().getGame_result()+"");
        viewHolder.tv_teamhome.setText(dataSource.get(position).getTeamMap().get("nameA").toString());
        viewHolder.tv_teamaway.setText(dataSource.get(position).getTeamMap().get("nameB").toString());
        viewHolder.tv_scorehome.setText(dataSource.get(position).getContest().getGame_result().substring(0,1));
        viewHolder.tv_scoreaway.setText(dataSource.get(position).getContest().getGame_result().substring(2));
        viewHolder.tv_state.setText(status[dataSource.get(position).getContest().getGame_status()-1]);
        try {
            if (dataSource.get(position).getTeamMap().get("imgA").toString()!=null)
                Glide.with(convertView).load(Info.BASE_URL + dataSource.get(position).getTeamMap().get("imgA").toString()).into(viewHolder.iv_imghome);
            if (dataSource.get(position).getTeamMap().get("imgB").toString()!=null)
                Glide.with(convertView).load(Info.BASE_URL + dataSource.get(position).getTeamMap().get("imgB").toString()).into(viewHolder.iv_imgaway);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (dataSource.get(position).getContest().getGame_status()==2)
            viewHolder.tv_state.setTextColor(
                    Color.parseColor("#1afa29"));
        return convertView;
    }
    private class ViewHolder{
        TextView tv_time;
        TextView tv_teamhome;
        TextView tv_teamaway;
        TextView tv_scorehome;
        TextView tv_scoreaway;
        TextView tv_state;
        ImageView iv_imghome;
        ImageView iv_imgaway;
    }
}
