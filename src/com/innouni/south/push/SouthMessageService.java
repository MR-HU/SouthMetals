package com.innouni.south.push;

import java.util.Calendar;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import com.innouni.south.activity.R;

/**
 * 消息推送服务 <br>
 * 如果Service已经启动了，当我们再次调用startService时<br>
 * 不会在执行onCreate()方法，而是直接执行onStart()方法
 * @author HuGuojun
 * @data 2013-10-31
 */
public class SouthMessageService extends Service {
	
	public static final String ACTIONTAG = "SouthMetals";
	
	public static NotificationManager notificationManager = null;
	public static Notification notification;
 
	private PushMessageUtils pushUtil;
	private UpdateReceiver updateReceiver;
	 
	@Override
	public void onCreate() {
		super.onCreate();
		Log.v("push", "Service ---->onCreate()");
		
		/**
		 * 获取消息
		 */
		pushUtil = new PushMessageUtils(this);
		pushUtil.startPush();  //开启服务监听消息
		
		/** 
		 * 注册通知广播(代码中注册,也可以在配置文件里注册)
		 */
		IntentFilter updateFilter = new IntentFilter();
		updateFilter.addAction(ACTIONTAG);
		updateReceiver = new UpdateReceiver();
		registerReceiver(updateReceiver, updateFilter);
	}
    
	/**
	 * 外部调用startService时会调用onStartCommand
	 */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    	//START_STICKY：如果service进程被kill掉，保留service的状态为开始状态，但不保留递送的intent对象。
    	//随后系统会尝试重新创建service，由于服务状态为开始状态，所以创建服务后一定会调用onStartCommand(Intent,int,int)方法。
    	//如果在此期间没有任何启动命令被传递到service，那么参数Intent将为null。
    	flags = START_STICKY;
    	return super.onStartCommand(intent, flags, startId);
    }
    
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.v("push", "Service ---->onDestroy()");
		pushUtil.stopPush();
		unregisterReceiver(updateReceiver);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/** 广播接收器 */
	private class UpdateReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.v("push", "push ---->接收到推送消息");
			notification();
		}
	}

	/** 发出广播 <br>
	 *  状态栏和通知栏显示提醒
	 */
	private void notification() {
		notificationManager = NotificationManagerUnits.getInstance(this);
		
		Intent intent = new Intent();
		intent.setComponent(new ComponentName(getPackageName(), getPackageName()+".LaunchActivity"));
		intent.setAction(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
	    
		PushInfo info = (PushInfo) pushUtil.getPushInfo().get(0);
		String expertId = info == null ? "" : info.getExpertId();
	    String expertName = info == null ? "" : info.getExpertName();
	    String content = info == null ? "" : info.getContent();
	    String userId = info == null ? "" : info.getUserId();
	    
	    intent.putExtra("push", true);
	    intent.putExtra("expertId", expertId);
	    intent.putExtra("expertName", expertName);
	    intent.putExtra("userId", userId);
   	    
		PendingIntent pendingIntent = PendingIntent.getActivity(this ,Integer.valueOf(expertId)
				,intent ,PendingIntent.FLAG_UPDATE_CURRENT);
		
		notification = new Notification();
		//提示文字
		notification.icon = R.drawable.logo;
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		notification.contentView = new RemoteViews(getPackageName(),R.layout.notification_message);
		//设置声音
		notification.defaults = Notification.DEFAULT_SOUND;
		//设置参数,最后一个参数类型是Intent类型，是点击状态栏消息后要运行的Activity
		notification.contentView.setTextViewText(R.id.notification_title, getResources().getString(R.string.app_name));
		notification.contentView.setTextViewText(R.id.notification_time, getSystemTime());
		notification.contentView.setTextViewText(R.id.notification_content, content);
		notification.contentIntent = pendingIntent;
		//送出Notification
		notificationManager.notify(Integer.valueOf(expertId), notification);
			 
	}
	
	/**获取系统当前时间*/
	private String getSystemTime(){
		String time = "";
        Calendar c = Calendar.getInstance();
        if(c.get(Calendar.MINUTE) >= 10)
        	time = c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE);
        else
        	time = c.get(Calendar.HOUR_OF_DAY)+":0"+c.get(Calendar.MINUTE);
        return time;
	}
}