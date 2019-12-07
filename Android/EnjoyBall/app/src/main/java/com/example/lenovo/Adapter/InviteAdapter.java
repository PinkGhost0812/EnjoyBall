package com.example.lenovo.enjoyball;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lenovo.entity.Team;
import com.example.lenovo.entity.User;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Map;

public class InviteAdapter extends BaseAdapter {
    private List<Map<String,Object>> datasource;
    private int itemResId;
    private Context mContext;
    private String table;
    private static int SAVEUSER = 2;
    private static int SAVETEAM = 3;
    public InviteAdapter(List<Map<String,Object>> datasource,int itemResId,Context mContext,String table){
        this.datasource = datasource;
        this.itemResId = itemResId;
        this.mContext = mContext;
        this.table = table;
    }
    @Override
    public int getCount() {
        if (null != datasource){
            return datasource.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (null != datasource){
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
        InviteAdapter.ViewHolder viewHolder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(itemResId,null);
            viewHolder = new InviteAdapter.ViewHolder();
            viewHolder.tv_name = convertView.findViewById(R.id.tv_searchresult_name);
            viewHolder.tv_invite = convertView.findViewById(R.id.tv_searchresult_invite);
            viewHolder.iv_head = convertView.findViewById(R.id.iv_searchresult_head);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (InviteAdapter.ViewHolder) convertView.getTag();
        }
        viewHolder.tv_name.setText(datasource.get(position).get("name").toString());
        viewHolder.tv_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message();
                message.what = SAVEUSER;
                if (table.equals("user")){
                    User user = (User)datasource.get(position).get("object");
                    message.obj = user.getUser_id();
                }else {
                    Team team = (Team) datasource.get(position).get("object");
                    message.obj = team.getTeam_id();
                }
                EventBus.getDefault().post(message);


            }
        });
        Glide.with(mContext)
                .load(Info.BASE_URL+datasource.get(position).get("head"))
                .circleCrop()
                .into(viewHolder.iv_head);


        return convertView;
    }
    private class ViewHolder{
        private TextView tv_name;
        private TextView tv_invite;
        private ImageView iv_head;
    }
}
