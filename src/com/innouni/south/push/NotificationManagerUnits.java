package com.innouni.south.push;

import android.app.NotificationManager;
import android.content.Context;

/**
 * {@link NotificationManager}管理
 * @author HuGuojun
 * @data 2013-10-31
 */
public class NotificationManagerUnits {
	
	private static NotificationManager notificationManager;
	
	public static NotificationManager getInstance(Context context){
		if (null == notificationManager) {
			notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		}
		return notificationManager;
	}
}
