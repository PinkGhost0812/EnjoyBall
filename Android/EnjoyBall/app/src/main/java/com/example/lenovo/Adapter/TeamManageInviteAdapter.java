package com.example.lenovo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.os.Message;
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
import com.example.lenovo.enjoyball.Info;
import com.example.lenovo.enjoyball.R;
import com.example.lenovo.entity.Team;
import com.example.lenovo.entity.User;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TeamManageInviteAdapter extends BaseAdapter {

    private List<Map<String, Object>> datasource;

    private int itemResId;

    private Context context;

    public TeamManageInviteAdapter(List<Map<String, Object>> datasource, int itemResId, Context context) {
        this.datasource = datasource;
        this.itemResId = itemResId;
        this.context = context;
    }

    @Override
    public int getCount() {
        if (null != datasource) {
            return datasource.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (null != datasource) {
            return datasource.get(position);
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
            convertView = LayoutInflater.from(context).inflate(itemResId, null);
            viewHolder = new ViewHolder();
            viewHolder.tvTeamManageMemberInviteUserNickname = convertView.findViewById(R.id.tv_team_manage_member_invite_user_nickname);
            viewHolder.tvTeamManageMemberInviteUserInvite = convertView.findViewById(R.id.tv_team_manage_member_invite_user_invite);
            viewHolder.ivTeamManageMemberInviteUserPortrait = convertView.findViewById(R.id.iv_team_manage_member_invite_user_portrait);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvTeamManageMemberInviteUserNickname.setText(datasource.get(position).get("names").toString());
        final User userInvited = (User) datasource.get(position).get("objects");
        final Team team = (Team) datasource.get(position).get("teams");

        viewHolder.tvTeamManageMemberInviteUserInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(Info.BASE_URL + "team/invite?teamId=" + team.getTeam_id() + "&userId=" + userInvited.getUser_id())
                        .build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Looper.prepare();
                        Looper.loop();
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        android.os.Message msg = new android.os.Message();
                        msg.what = 47;
                        EventBus.getDefault().post(msg);
                    }
                });


            }
        });

        RequestOptions options = new RequestOptions()
                .signature(new ObjectKey(System.currentTimeMillis()))
                .circleCrop();
        Glide.with(context)
                .load(Info.BASE_URL + datasource.get(position).get("heads"))
                .apply(options)
                .into(viewHolder.ivTeamManageMemberInviteUserPortrait);


        return convertView;
    }

    private class ViewHolder {
        private TextView tvTeamManageMemberInviteUserNickname;
        private TextView tvTeamManageMemberInviteUserInvite;
        private ImageView ivTeamManageMemberInviteUserPortrait;
    }
}
