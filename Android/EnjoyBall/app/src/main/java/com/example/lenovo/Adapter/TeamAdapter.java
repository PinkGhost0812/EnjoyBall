package com.example.lenovo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.example.lenovo.Activity.PerinfoActivity;
import com.example.lenovo.Activity.TeamActivity;
import com.example.lenovo.Activity.TeamDetailActivity;
import com.example.lenovo.Activity.TeamManageActivity;
import com.example.lenovo.enjoyball.Info;
import com.example.lenovo.enjoyball.R;
import com.example.lenovo.entity.Team;
import com.example.lenovo.entity.User;

import java.util.List;
import java.util.Map;

public class TeamAdapter extends BaseAdapter {

    private List<Map<String, Object>> dataSource = null;

    private Context context = null;

    private int item_layout_id;

    private ViewHolder viewHolder = null;

    private User user;

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
            viewHolder.tvTeamMemberNum = convertView.findViewById(R.id.tv_team_num);
            viewHolder.ivTeamLogo = convertView.findViewById(R.id.iv_team_logo);
            viewHolder.llTeamSetting = convertView.findViewById(R.id.ll_team_setting);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvTeamName.setText(dataSource.get(position).get("names").toString());
        viewHolder.tvTeamCaptain.setText(dataSource.get(position).get("captains").toString());
        viewHolder.tvTeamMemberNum.setText(dataSource.get(position).get("nums").toString());

        RequestOptions options = new RequestOptions()
                .signature(new ObjectKey(System.currentTimeMillis()))
                .circleCrop();

        Glide.with(context)
                .load(Info.BASE_URL + dataSource.get(position).get("logos").toString())
                .apply(options)
                .into(viewHolder.ivTeamLogo);

        user = ((Info) context.getApplicationContext()).getUser();

        Log.e("test curuser", user.getUser_id().toString());
        Log.e("test captainId", dataSource.get(position).get("captainsId").toString());

        viewHolder.llTeamSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getUser_id() == dataSource.get(position).get("captainsId")) {
                    Log.e("test team captains id", dataSource.get(position).get("captainsId").toString());
                    Intent intent = new Intent();
                    intent.putExtra("team", (Team) dataSource.get(position).get("teams"));
                    intent.putExtra("captain", dataSource.get(position).get("captains").toString());
                    intent.putExtra("memberNum", dataSource.get(position).get("nums").toString());
                    intent.setClass(context, TeamManageActivity.class);
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "您不是该球队队长，无法编辑球队凹~", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return convertView;
    }

    private class ViewHolder {
        public TextView tvTeamName;
        public TextView tvTeamCaptain;
        public TextView tvTeamMemberNum;
        public ImageView ivTeamLogo;
        public LinearLayout llTeamSetting;
    }

}
