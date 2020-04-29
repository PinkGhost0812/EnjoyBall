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

public class HomepageFansAdapter extends BaseAdapter {

    private List<Map<String,Object>> dataSource = null;
    private Context context = null;
    private int item_layout_id;

    public HomepageFansAdapter(Context context,
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
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder=null;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(item_layout_id, null);
            viewHolder = new ViewHolder();
            viewHolder.ivHomepageFansPortrait=convertView.findViewById(R.id.iv_homepage_fans_portrait);
            viewHolder.tvHomepageFansNickname = convertView.findViewById(R.id.tv_homepage_fans_nickname);
            viewHolder.tvHomepageFansSex = convertView.findViewById(R.id.tv_homepage_fans_sex);
            viewHolder.tvHomepageFansAge = convertView.findViewById(R.id.tv_homepage_fans_age);
            viewHolder.ivHomepageFansFollow=convertView.findViewById(R.id.iv_homepage_fans_follow);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        RequestOptions options = new RequestOptions()
                .circleCrop();
        Glide.with(context)
                .load(Info.BASE_URL+dataSource.get(position).get("portraits").toString())
                .apply(options)
                .into(viewHolder.ivHomepageFansPortrait);
        viewHolder.tvHomepageFansNickname.setText(dataSource.get(position).get("nicknames").toString());
        viewHolder.tvHomepageFansSex.setText(dataSource.get(position).get("sexs").toString());
        viewHolder.tvHomepageFansAge.setText(dataSource.get(position).get("ages").toString());

        viewHolder.ivHomepageFansFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(Info.BASE_URL + "user/getfans?id=")
                        .build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Looper.prepare();
                        Toast.makeText(context, "添加关注失败~", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                    }
                });
            }
        });

        return convertView;
    }

    private class ViewHolder {
        public TextView tvHomepageFansNickname;
        public TextView tvHomepageFansSex;
        public TextView tvHomepageFansAge;
        public ImageView ivHomepageFansFollow;
        public ImageView ivHomepageFansPortrait;
    }
}
