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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.example.lenovo.Activity.HomepageActivity;
import com.example.lenovo.Activity.TeamManageActivity;
import com.example.lenovo.enjoyball.Info;
import com.example.lenovo.enjoyball.R;
import com.example.lenovo.entity.Team;
import com.example.lenovo.entity.User;

import java.util.List;

public class TeamManageMemberAdapter extends BaseAdapter {

    private Context context = null;

    private int item_layout_id;

    private List<User> userList;

    public TeamManageMemberAdapter(Context context,
                                   List<User> list,
                                   int item_layout_id) {
        this.context = context;
        this.userList = list;
        this.item_layout_id = item_layout_id;
    }

    @Override
    public int getCount() {
        if (userList != null) {
            return userList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (userList != null) {
            return userList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(item_layout_id, null);
            viewHolder = new ViewHolder();
            viewHolder.tvTeamManageMemberNickname = convertView.findViewById(R.id.tv_team_manage_member_nickname);
            viewHolder.tvTeamManageMemberSex = convertView.findViewById(R.id.tv_team_manage_member_sex);
            viewHolder.tvTeamManageMemberAge = convertView.findViewById(R.id.tv_team_manage_member_age);
            viewHolder.ivTeamManageMemberDelete = convertView.findViewById(R.id.iv_team_manage_member_delete);
            viewHolder.ivTeamManageMemberLogo = convertView.findViewById(R.id.iv_team_manage_member_logo);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Log.e("test adapter","111");

        Log.e("test nickname",userList.get(position).getUser_nickname().toString());

        RequestOptions options = new RequestOptions()
                .signature(new ObjectKey(System.currentTimeMillis()))
                .circleCrop();
        Glide.with(context)
                .load(Info.BASE_URL + userList.get(position).getUser_headportrait())
                .apply(options)
                .into(viewHolder.ivTeamManageMemberLogo);
        Log.e("test nickname",userList.get(position).getUser_nickname().toString());
        viewHolder.tvTeamManageMemberNickname.setText(userList.get(position).getUser_nickname().toString());
        viewHolder.tvTeamManageMemberSex.setText(userList.get(position).getUser_sex().toString());
        viewHolder.tvTeamManageMemberAge.setText(userList.get(position).getUser_age().toString()+"岁");

        viewHolder.ivTeamManageMemberDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userList.get(position).getUser_id()==((Info)context.getApplicationContext()).getUser().getUser_id()){
                    Toast.makeText(context,"您是球队队长，不能删除自己~",Toast.LENGTH_SHORT).show();
                }else{
                    //删除队员
                }
            }
        });

        return convertView;
    }

    private class ViewHolder {
        public ImageView ivTeamManageMemberLogo;
        public TextView tvTeamManageMemberNickname;
        public TextView tvTeamManageMemberSex;
        public TextView tvTeamManageMemberAge;
        public ImageView ivTeamManageMemberDelete;
    }

}
