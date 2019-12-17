package com.example.lenovo.Adapter;

import android.content.Context;
import android.os.Looper;
import android.os.Message;
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
import com.example.lenovo.entity.UserFans;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomepageFollowAdapter extends BaseAdapter {

    private List<Map<String, Object>> dataSource = null;
    private Context context = null;
    private int item_layout_id;

    public HomepageFollowAdapter(Context context,
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

        ViewHolder viewHolder = null;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(item_layout_id, null);
            viewHolder = new ViewHolder();
            viewHolder.ivHomepageFollowPortrait = convertView.findViewById(R.id.iv_homepage_follow_portrait);
            viewHolder.tvHomepageFollowNickname = convertView.findViewById(R.id.tv_homepage_follow_nickname);
            viewHolder.tvHomepageFollowSex = convertView.findViewById(R.id.tv_homepage_follow_sex);
            viewHolder.tvHomepageFollowAge = convertView.findViewById(R.id.tv_homepage_follow_age);
            viewHolder.ivHomepageUnfollow = convertView.findViewById(R.id.iv_homepage_follow_unfollow);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        RequestOptions options = new RequestOptions()
                .circleCrop();
        Glide.with(context)
                .load(Info.BASE_URL + dataSource.get(position).get("portraits").toString())
                .apply(options)
                .into(viewHolder.ivHomepageFollowPortrait);
        viewHolder.tvHomepageFollowNickname.setText(dataSource.get(position).get("nicknames").toString());
        viewHolder.tvHomepageFollowSex.setText(dataSource.get(position).get("sexs").toString());
        viewHolder.tvHomepageFollowAge.setText(dataSource.get(position).get("ages").toString());

        viewHolder.ivHomepageUnfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserFans userFans = new UserFans();
                userFans.setFans_id(((Info) context.getApplicationContext()).getUser().getUser_id());
                userFans.setUser_id((Integer) dataSource.get(position).get("ids"));
                Gson gson = new GsonBuilder().create();
                String jsonStr = gson.toJson(userFans);
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(Info.BASE_URL + "user/stopFollow?userFans="+jsonStr)
                        .build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Looper.prepare();
                        //Toast.makeText(context, "添加关注失败~", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Message msg=new Message();
                        String data = response.body().string();
                        if (data.equals("true")) {
                            msg.what=100;
                            msg.obj=position;
                            //Toast.makeText(context, "取关成功~", Toast.LENGTH_SHORT).show();
                        } else {
                            msg.what=101;
                            //Toast.makeText(context, "取关失败~", Toast.LENGTH_SHORT).show();
                        }
                        EventBus.getDefault().post(msg);
                    }
                });
            }
        });

        return convertView;
    }

    private class ViewHolder {
        public TextView tvHomepageFollowNickname;
        public TextView tvHomepageFollowSex;
        public TextView tvHomepageFollowAge;
        public ImageView ivHomepageUnfollow;
        public ImageView ivHomepageFollowPortrait;
    }


}
