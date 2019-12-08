package com.example.lenovo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.lenovo.Activity.PerinfoActivity;
import com.example.lenovo.Activity.TeamActivity;
import com.example.lenovo.Activity.TeamDetailActivity;
import com.example.lenovo.enjoyball.Info;
import com.example.lenovo.enjoyball.R;
import com.example.lenovo.entity.Team;

import java.util.List;
import java.util.Map;

public class TeamAdapter extends BaseAdapter {

    private List<Map<String,Object>> dataSource = null;
    private Context context = null;
    private int item_layout_id;
    private ViewHolder viewHolder=null;

    public TeamAdapter(Context context,
                       List<Map<String, Object>> dataSource,
                       int item_layout_id) {
        this.context = context;
        this.dataSource = dataSource;
        this.item_layout_id = item_layout_id;
    }

    @Override
    public int getCount() {
        if (dataSource != null) {
            return dataSource.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (dataSource != null) {
            return dataSource.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(item_layout_id, null);
            viewHolder = new ViewHolder();
            viewHolder.tvTeamName = convertView.findViewById(R.id.tv_team_name);
            viewHolder.tvTeamCaptain = convertView.findViewById(R.id.tv_team_captain);
            viewHolder.tvTeamMemberNum=convertView.findViewById(R.id.tv_team_num);
            viewHolder.ivTeamLogo=convertView.findViewById(R.id.iv_team_logo);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvTeamName.setText(dataSource.get(position).get("names").toString());
        viewHolder.tvTeamCaptain.setText(dataSource.get(position).get("captains").toString());
        viewHolder.tvTeamMemberNum.setText(dataSource.get(position).get("nums").toString());

        RequestOptions options = new RequestOptions()
                .circleCrop();
        Glide.with(context)
                .load(Info.BASE_URL+dataSource.get(position).get("logos").toString())
                .apply(options)
                .into(viewHolder.ivTeamLogo);

        return convertView;
    }

    private class ViewHolder {
        public TextView tvTeamName;
        public TextView tvTeamCaptain;
        public TextView tvTeamMemberNum;
        public ImageView ivTeamLogo;
    }

}
