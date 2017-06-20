package com.zhizun.zhizunwifi.service;

import java.util.Iterator;
import java.util.List;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.os.Build;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;

public class RobMoneyService extends AccessibilityService {


	private static RobMoneyService service;

	@Override
	public void onCreate() {
		this.service = this;
		super.onCreate();
	}

	@Override
	public void onAccessibilityEvent(AccessibilityEvent event) {
		int eventType = event.getEventType();
		switch (eventType) {
			//第一步：监听通知栏消息
			case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
				List<CharSequence> texts = event.getText();
				if (!texts.isEmpty()) {
					for (CharSequence text : texts) {
						String content = text.toString();
						if (content.contains("[微信红包]")) {
							//模拟打开通知栏消息
							if (event.getParcelableData() != null
									&&
									event.getParcelableData() instanceof Notification) {
								Notification notification = (Notification) event.getParcelableData();
								PendingIntent pendingIntent = notification.contentIntent;
								try {
									pendingIntent.send();
								} catch (CanceledException e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
				break;
		}
	}

	@Override
	public void onInterrupt() {
	}


	/**
	 * 判断当前服务是否正在运行
	 * */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public static boolean isRunning() {
		if(service == null) {
			System.out.println("service ============ null");
			return false;
		}
		AccessibilityManager accessibilityManager = (AccessibilityManager) service.getSystemService(Context.ACCESSIBILITY_SERVICE);
		AccessibilityServiceInfo info = service.getServiceInfo();
		if(info == null) {
			System.out.println("info ============ null");
			return false;
		}
		List<AccessibilityServiceInfo> list = accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
		Iterator<AccessibilityServiceInfo> iterator = list.iterator();

		boolean isConnect = false;
		while (iterator.hasNext()) {
			AccessibilityServiceInfo i = iterator.next();
			if(i.getId().equals(info.getId())) {
				isConnect = true;
				break;
			}
		}
		if(!isConnect) {
			System.out.println("isConnect ============ false");
			return false;
		}
		return true;
	}


}
