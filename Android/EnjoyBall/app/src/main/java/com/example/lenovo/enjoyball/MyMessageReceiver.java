package com.example.lenovo.enjoyball;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.lenovo.Activity.InviteActivity;
import com.example.lenovo.Activity.ManageMessageActivity;
import com.example.lenovo.Activity.TeamVisitDetailActivity;
import com.example.lenovo.entity.Team;
import com.example.lenovo.entity.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import cn.jpush.android.api.CustomMessage;
import cn.jpush.android.api.NotificationMessage;
import cn.jpush.android.service.JPushMessageReceiver;

public class MyMessageReceiver extends JPushMessageReceiver {
    @Override
    public void onNotifyMessageArrived(Context context, NotificationMessage notificationMessage) {
        super.onNotifyMessageArrived(context, notificationMessage);
        //通知消息到达时回调
        Log.e("接收到通知", "通知标题：" + notificationMessage.notificationTitle +
                "通知内容：" + notificationMessage.notificationContent
                + "附加字段：" + notificationMessage.notificationExtras);
    }

    @Override
    public void onNotifyMessageOpened(Context context, NotificationMessage notificationMessage) {
        super.onNotifyMessageOpened(context, notificationMessage);
        //点击通知时回调：启动固定界面
        Log.e("状态1", notificationMessage.notificationExtras);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        Type listType = new TypeToken<Map<String, String>>() {
        }.getType();
        Map<String, String> map = gson.fromJson(notificationMessage.notificationExtras, listType);
        if (map.get("invite").equals("inviteJoin")) {

            Log.e("test",map.get("team").toString());
            Gson gsonTeam = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            Type listTypeTeam = new TypeToken<Team>() {
            }.getType();
            Team team=gsonTeam.fromJson(map.get("team"),listTypeTeam);

            Log.e("test teamstr",team.getTeam_name());

            Intent intent=new Intent();
            intent.setClass(context, TeamVisitDetailActivity.class);
            intent.putExtra("team",team);
            intent.putExtra("invite","invite");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            //return;

        } else if (!(map.get("invite").equals("inviteJoin"))){

//            Intent intent = new Intent(context, ManageMessageActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            context.startActivity(intent);
        }
//        Intent intent = new Intent(context, ManageMessageActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);

    }

    @Override
    public void onMessage(Context context, CustomMessage customMessage) {
        super.onMessage(context, customMessage);
        //接收自定义消息时回调
        Log.e("test", "11111111111");
    }
}
