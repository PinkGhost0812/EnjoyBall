package com.example.lenovo.enjoyball;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.lenovo.Activity.InviteActivity;
import com.example.lenovo.Activity.ManageMessageActivity;

import cn.jpush.android.api.CustomMessage;
import cn.jpush.android.api.NotificationMessage;
import cn.jpush.android.service.JPushMessageReceiver;

public class MyMessageReceiver extends JPushMessageReceiver {
    @Override
    public void onNotifyMessageArrived(Context context, NotificationMessage notificationMessage) {
        super.onNotifyMessageArrived(context, notificationMessage);
        //通知消息到达时回调
        Log.e("接收到通知","通知标题："+notificationMessage.notificationTitle+
                "通知内容："+ notificationMessage.notificationContent
                +"附加字段："+ notificationMessage.notificationExtras);
    }

    @Override
    public void onNotifyMessageOpened(Context context, NotificationMessage notificationMessage) {
        super.onNotifyMessageOpened(context, notificationMessage);
        //点击通知时回调：启动固定界面
        Log.e("状态1","点击通知");
        Intent intent = new Intent(context, ManageMessageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void onMessage(Context context, CustomMessage customMessage) {
        super.onMessage(context, customMessage);
        //接收自定义消息时回调
        Log.e("test","11111111111");
    }
}
