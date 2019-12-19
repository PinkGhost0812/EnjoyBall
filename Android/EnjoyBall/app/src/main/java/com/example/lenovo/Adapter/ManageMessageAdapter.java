package com.example.lenovo.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.lenovo.Activity.HomepageActivity;
import com.example.lenovo.Activity.TeamDetailActivity;
import com.example.lenovo.Util.ApplyUtil;
import com.example.lenovo.enjoyball.GlideApp;
import com.example.lenovo.enjoyball.Info;
import com.example.lenovo.enjoyball.R;
import com.example.lenovo.entity.Team;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ManageMessageAdapter extends BaseAdapter {
    private List<ApplyUtil> datasource = null;
    private Context context = null;
    private Team team = null;

    public ManageMessageAdapter(List<ApplyUtil> datasource, Context context) {
        this.datasource = datasource;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_item_message_manage, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_sender = convertView.findViewById(R.id.tv_message_sender);
            viewHolder.iv_head = convertView.findViewById(R.id.iv_message_deal_head);
            viewHolder.tv_form = convertView.findViewById(R.id.tv_message_form);
            viewHolder.tv_place = convertView.findViewById(R.id.tv__message_place);
            viewHolder.tv_agree = convertView.findViewById(R.id.tv_message_agree);
            viewHolder.tv_disagree = convertView.findViewById(R.id.tv_message_disagree);
            viewHolder.tv_team = convertView.findViewById(R.id.tv_message_team);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }
        String[] places = context.getResources().getStringArray(R.array.address);
        viewHolder.tv_sender.setText(datasource.get(position).getUser().getUser_nickname());
        viewHolder.tv_place.setText(datasource.get(position).getDemand().getDemand_place());
        GlideApp.with(context)
                .load(Info.BASE_URL + datasource.get(position).getUser().getUser_headportrait())
                .circleCrop()
                .into(viewHolder.iv_head);
        if (datasource.get(position).getApplyInfo().getIsInvite() == 0) {
            viewHolder.tv_form.setText("邀请你加入在");
        } else {
            viewHolder.tv_form.setText("申请加入在");
        }
        String team = new String();
        if (datasource.get(position).getDemand().getDemand_oom() == 0) {
            if (datasource.get(position).getApplyInfo().getTeamId() == datasource.get(position).getDemand().getDemand_teama()) {
                team = "的个人约球a队";
            } else {
                team = "的个人约球b队";
            }
        } else {
            team = "的组队约球";
        }
        viewHolder.tv_team.setText(team);
        viewHolder.iv_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                if (datasource.get(position).getDemand().getDemand_oom() == 0) {
                    intent = new Intent(context, HomepageActivity.class);
                    intent.putExtra("user", datasource.get(position).getUser());
                } else {
                    int type = datasource.get(position).getDemand().getDemand_class();
                    int id = datasource.get(position).getApplyInfo().getSender();
                    getTeam(type,id);
                }
            }
        });
        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.tv_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (((TextView) v).getText().toString().equals("同意") && finalViewHolder.status == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("确认")
                            .setMessage("确认同意吗?")
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ((TextView) v).setText("已同意");
                                    finalViewHolder.status = 1;
                                    sendToServer("agree", datasource.get(position));
                                }
                            })
                            .setNegativeButton("取消", null);
                    AlertDialog adAgree = builder.create();
                    adAgree.show();
                }
            }
        });
        viewHolder.tv_disagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (((TextView) v).getText().toString().equals("拒绝") && finalViewHolder.status == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("确认")
                            .setMessage("确认拒绝吗?")
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ((TextView) v).setText("已拒绝");
                                    finalViewHolder.status = 1;
                                    sendToServer("disagree", datasource.get(position));
                                }
                            })
                            .setNegativeButton("取消", null);
                    AlertDialog adDisagree = builder.create();
                    adDisagree.show();
                }
            }
        });


        return convertView;
    }

    private Team getTeam(int type, int id) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Info.BASE_URL + "team/findByIdCls?id=" + id + "&&cls=" + type)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String teamJson = response.body().string();
                Log.e("test teamJsom", teamJson);
                if (!teamJson.equals("false")) {
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-mm-dd hh:mm:ss").create();
                    team = gson.fromJson(teamJson, Team.class);
                    Message msg=new Message();
                    msg.what=86;
                    msg.obj=team;
                    EventBus.getDefault().post(msg);
                }
            }
        });
        return team;
    }

    private void sendToServer(String message, ApplyUtil applyUtil) {
        String url = Info.BASE_URL;
        int userId = 0;
        int teamId = 0;
        int applyId = 0;
        int demandId = 0;
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = null;

        if (message.equals("agree")) {
           if (applyUtil.getDemand().getDemand_oom()==0){
               if(applyUtil.getApplyInfo().getIsInvite() == 0)
                   userId = applyUtil.getApplyInfo().getReceiver();
               else
                   userId = applyUtil.getApplyInfo().getSender();
               teamId = applyUtil.getApplyInfo().getTeamId();
               applyId = applyUtil.getApplyInfo().getId();
               request = new Request.Builder()
                       .url(url + "appointment/apply?userId=" + userId + "&teamId=" + teamId + "&&applyId=" + applyId)
                       .build();
           }
           else {
               teamId = applyUtil.getApplyInfo().getTeamId();
               demandId = applyUtil.getDemand().getDemand_id();
               applyId = applyUtil.getApplyInfo().getId();
               request = new Request.Builder()
                       .url(url + "appointment/acceptApply?teamId=" + teamId + "&&demandId=" + demandId+"&&applyId="+applyId)
                       .build();
           }
        } else {
            applyId = applyUtil.getApplyInfo().getId();
            request = new Request.Builder()
                    .url(url + "appointment/refuse?id=" + applyId)
                    .build();
        }
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(context, "断开连接", Toast.LENGTH_SHORT);
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("result", response.body().string());
            }
        });


    }

    private class ViewHolder {
        public TextView tv_sender;
        public ImageView iv_head;
        public TextView tv_form;
        public TextView tv_place;
        public TextView tv_agree;
        public TextView tv_disagree;
        public TextView tv_team;
        public int status = 0;
    }
}
