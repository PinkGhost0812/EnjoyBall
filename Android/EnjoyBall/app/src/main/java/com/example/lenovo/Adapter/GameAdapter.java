package com.example.lenovo.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lenovo.Fragment.AddGameDiaglog;
import com.example.lenovo.enjoyball.Info;
import com.example.lenovo.enjoyball.R;
import com.example.lenovo.entity.TeamAndContest;

import java.text.SimpleDateFormat;
import java.util.List;

public class GameAdapter extends BaseAdapter {

    private List<TeamAndContest> dataSource = null;

    private Context context = null;

    private int item_layout_id;
    private int identity = 0;
    private SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
    private String[] status = {"已完场","进行中","未开始"};


    public GameAdapter(Context context,
                       List<TeamAndContest> dataSource,
                       int item_layout_id,int identity) {
        this.context = context;
        this.dataSource = dataSource;
        this.item_layout_id = item_layout_id;
        this.identity = identity;
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
            viewHolder.tv_time = convertView.findViewById(R.id.tv_game_time);
            viewHolder.tv_teamhome = convertView.findViewById(R.id.tv_game_team_home);
            viewHolder.tv_teamaway = convertView.findViewById(R.id.tv_game_team_away);
            viewHolder.tv_scorehome = convertView.findViewById(R.id.tv_game_score_home);
            viewHolder.tv_scoreaway = convertView.findViewById(R.id.tv_game_score_away);
            viewHolder.tv_state = convertView.findViewById(R.id.tv_game_state);
            viewHolder.iv_imghome = convertView.findViewById(R.id.iv_game_team_imghome);
            viewHolder.iv_imgaway = convertView.findViewById(R.id.iv_game_team_imgaway);
            viewHolder.iv_addgame = convertView.findViewById(R.id.iv_addgame);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //Contest contest = dataSource.get(position);
//        Glide.with(context)
//                .load(Info.BASE_URL+contest.get)

        //viewHolder.tv_time.setText(sf.format(dataSource.get(position).getContest().getGame_time())+" "+dataSource.get(position).getContest().getGame_result()+"");
        viewHolder.tv_time.setText(sf.format(dataSource.get(position).getContest().getGame_time())+"");
        String[] result = dataSource.get(position).getContest().getGame_result().split("-");

        try {
            viewHolder.tv_teamhome.setText(dataSource.get(position).getTeamMap().get("nameA").toString());
            viewHolder.tv_teamaway.setText(dataSource.get(position).getTeamMap().get("nameB").toString());
            viewHolder.tv_scorehome.setText(result[0]+"");
            viewHolder.tv_scoreaway.setText(result[1]+"");
            viewHolder.tv_state.setText(status[dataSource.get(position).getContest().getGame_status()-1]+"");
            if (dataSource.get(position).getTeamMap().get("imgA").toString()!=null)
                Glide.with(convertView).load(Info.BASE_URL + dataSource.get(position).getTeamMap().get("imgA").toString()).into(viewHolder.iv_imghome);
            if (dataSource.get(position).getTeamMap().get("imgB").toString()!=null)
                Glide.with(convertView).load(Info.BASE_URL + dataSource.get(position).getTeamMap().get("imgB").toString()).into(viewHolder.iv_imgaway);
            if (dataSource.get(position).getContest().getGame_status()!=2)
                viewHolder.tv_state.setTextColor(
                        Color.parseColor("#000000"));
            else {
                viewHolder.tv_state.setTextColor(
                        Color.parseColor("#1afa29"));
            }
            if (identity==3 && dataSource.get(position).getContest().getGame_status()!=1){
                viewHolder.iv_addgame.setVisibility(convertView.VISIBLE);
            }
            else {
                viewHolder.iv_addgame.setVisibility(convertView.GONE);
            }
            viewHolder.iv_addgame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OutDialog(position);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        ImageView iv_addgame;
    }
    private void OutDialog(int i){
        FragmentManager manager = ((FragmentActivity)context).getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        AddGameDiaglog addGameDiaglog = new AddGameDiaglog();
        if (!addGameDiaglog.isAdded()){
            Bundle bundle = new Bundle();
            Log.e("比赛id",dataSource.get(i).getContest().getGame_id().toString());
            bundle.putInt("gameid",dataSource.get(i).getContest().getGame_id());
            bundle.putString("teama",dataSource.get(i).getTeamMap().get("nameA").toString());
            bundle.putString("teamb",dataSource.get(i).getTeamMap().get("nameB").toString());
            addGameDiaglog.setArguments(bundle);
            transaction.add(addGameDiaglog,"dialog_tag");
        }
        transaction.show(addGameDiaglog);
        transaction.commit();

    }
}
