package com.enjoyball.util;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;


public class JPushUtils {
//	protected static final String APP_KEY = "3ac9f40df2172b2e86863a7d";
//    protected static final String MASTER_SECRET = "aa1b4c4a1104dc9c9d9dc880";
//    
    protected static final String APP_KEY = "c2da9fbeb135fff410c09ce6";
    protected static final String MASTER_SECRET = "8a9a6550881a7c38082faf55"; 
    
    
    public final static int ANDROID_PLATFORM = 0;
	public final static int IOS_PLATFORM = 1;
	public final static int ANDROID_IOS_PLATFORM = 2;
	public final static int ALL_PLATFORM = -1;
	
	private static JPushClient jPushClient;
	static {
		jPushClient = new JPushClient(MASTER_SECRET, APP_KEY);
	}
	
	public static void sendAllsetNotification(String message)
	{
//		JPushClient jpushClient = new JPushClient("aa1b4c4a1104dc9c9d9dc880",
//				"3ac9f40df2172b2e86863a7d");
		//JPushClient jpushClient = new JPushClient(masterSecret, appKey);//第一个参数是masterSecret 第二个是appKey
//		Map<String, String> extras = new HashMap<String, String>();
		// 添加附加信息
//		extras.put("extMessage", "我是额外的通知");
		PushPayload payload = PushPayload.newBuilder().setPlatform(Platform.all())
				.setAudience(Audience.registrationId("1104a89792c36f7660a"))
				.setNotification(Notification.alert(message))
				.build();
//		PushPayload payload = buildPushObject_all_alert(message, ANDROID_PLATFORM, null);
//		PushPayload payload = buildPushObject_all_alias_alert(message, extras);
		try
		{
			PushResult result = jPushClient.sendPush(payload);
			System.out.println("res = " + result);
		}
		catch (APIConnectionException e)
		{
			System.out.println(e);
		}
		catch (APIRequestException e)
		{
			System.out.println(e);
			System.out.println("Error response from JPush server. Should review and fix it. " + e);
			System.out.println("HTTP Status: " + e.getStatus());
			System.out.println("Error Code: " + e.getErrorCode());
			System.out.println("Error Message: " + e.getErrorMessage());
			System.out.println("Msg ID: " + e.getMsgId());
		}
	}
	
	
	public static void sendAllNotification(String message, Map<String, String> extras, String id)
	{
		System.out.println("jpush");
		PushPayload payload = PushPayload.newBuilder()
				.setPlatform(Platform.all()).setAudience(Audience.registrationId(id))
				.setNotification(Notification.newBuilder()
						.setAlert(message)
						.addPlatformNotification(AndroidNotification.newBuilder().addExtras(extras).build()).build())
				.build();
		try
		{
			PushResult result = jPushClient.sendPush(payload);
			System.out.println("res = " + result);
		}
		catch (APIConnectionException e)
		{
			System.out.println(e);
		}
		catch (APIRequestException e)
		{
			System.out.println(e);
			System.out.println("Error response from JPush server. Should review and fix it. " + e);
			System.out.println("HTTP Status: " + e.getStatus());
			System.out.println("Error Code: " + e.getErrorCode());
			System.out.println("Error Message: " + e.getErrorMessage());
			System.out.println("Msg ID: " + e.getMsgId());
		}
	}
	


	/**
	 * 构造能往设备发送通知(ALERT)的 PushPayload
	 * 
	 * @param content
	 *            发送内容
	 * @param platform
	 *            接受平台
	 * @param title
	 *            安卓端可设置通知标题，若不需要则传入null即可
	 * @return
	 */
	private static PushPayload buildPushObject_all_alert(String content, int platform, String title) {
		if (platform == ANDROID_PLATFORM) {
			if (title != null) {
				return PushPayload.newBuilder().setPlatform(Platform.android())
						.setNotification(Notification.android(content, title, null)).setAudience(Audience.all())
						.build();
			} else {
				return PushPayload.newBuilder().setPlatform(Platform.android())
						.setNotification(Notification.alert(content)).setAudience(Audience.all()).build();
			}
		} else if (platform == IOS_PLATFORM) {
			return PushPayload.newBuilder().setPlatform(Platform.ios()).setNotification(Notification.alert(content))
					.setAudience(Audience.all()).build();
		} else if (platform == ANDROID_IOS_PLATFORM) {
			return PushPayload.newBuilder().setPlatform(Platform.android_ios())
					.setNotification(Notification.alert(content)).setAudience(Audience.all()).build();
		} else {
			return PushPayload.newBuilder().setPlatform(Platform.all()).setNotification(Notification.alert(content))
					.setAudience(Audience.all()).build();
		}
	}
	
	
	
	public static void sendNotification(String message, String title, String id)
	{
		PushPayload payload = PushPayload.newBuilder()
				.setPlatform(Platform.all()).setAudience(Audience.registrationId(id))
				.setNotification(Notification.newBuilder()
						.setAlert(message)
						.addPlatformNotification(AndroidNotification.newBuilder().setTitle(title).build()).build())
				.build();
		try
		{
			PushResult result = jPushClient.sendPush(payload);
			System.out.println("res = " + result);
		}
		catch (APIConnectionException e)
		{
			System.out.println(e);
		}
		catch (APIRequestException e)
		{
			System.out.println(e);
			System.out.println("Error response from JPush server. Should review and fix it. " + e);
			System.out.println("HTTP Status: " + e.getStatus());
			System.out.println("Error Code: " + e.getErrorCode());
			System.out.println("Error Message: " + e.getErrorMessage());
			System.out.println("Msg ID: " + e.getMsgId());
		}
	}
	
}
