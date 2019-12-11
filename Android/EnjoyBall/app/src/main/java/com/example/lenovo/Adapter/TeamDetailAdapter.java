package com.example.lenovo.Adapter;

import android.content.Context;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.lenovo.Activity.TeamDetailActivity;
import com.example.lenovo.enjoyball.Info;
import com.example.lenovo.enjoyball.R;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TeamDetailAdapter extends BaseAdapter {

    private Context context = null;

    private int item_layout_id;

    private List<String> list;

    public TeamDetailAdapter(Context context,
                             List<String> list,
                             int item_layout_id) {
        this.context = context;
        this.list = list;
        this.item_layout_id = item_layout_id;
    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (list != null) {
            return list.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder=null;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(item_layout_id, null);
            viewHolder = new ViewHolder();
            viewHolder.tvTeamDetailNickname=convertView.findViewById(R.id.tv_team_detail_nickname);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvTeamDetailNickname.setText(list.get(position).toString());

        return convertView;
    }

    private class ViewHolder {
        public TextView tvTeamDetailNickname;
    }

}
