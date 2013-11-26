package com.innouni.south.push;

import com.innouni.south.app.MainApplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 接收手机Android手机在启动的过程中会触发一个Standard Broadcast Action <br>
 * 名字叫{@link android.intent.action.BOOT_COMPLETED} <br>
 * 实现开机后自动启动{@link SouthMessageService}
 * @author HuGuojun
 * @data 2013-10-31
 */
public class BootBroadCastReceiver extends BroadcastReceiver {  
	
	@Override  
	public void onReceive(Context context, Intent intent) { 
		Log.v("push", "Push ---->开机");
		MainApplication application = MainApplication.getApplication();
		if (application != null && null != application.getUserInfo()) {
			context.startService(new Intent(context, SouthMessageService.class));
		}
    }  
} 
