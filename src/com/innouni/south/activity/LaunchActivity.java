package com.innouni.south.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.innouni.south.base.BaseActivity;

/**
 * 页面分发中心，程序启动项。<br>
 * 判断是推送消息还是直接启动程序，根据判断分发到不同页面
 * @author HuGuojun
 * @data 2013-11-18
 */
public class LaunchActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);  
		super.onCreate(savedInstanceState);
		Intent intent = new Intent();
		Bundle bundle = getIntent().getExtras();
		boolean pushTag = bundle == null ? false : bundle.getBoolean("push");
		if(pushTag) {
			//是推送则根据推送的信息跳转到指定页面
//			String expertId = bundle.getString("expertId");
//			String expertName = bundle.getString("expertName");
//			String userId = bundle.getString("userId");
//			intent.putExtra("push", true);
//			intent.putExtra("expertId", expertId);
//			intent.putExtra("expertName", expertName);
//			intent.putExtra("userId", userId);
//			intent.setClass(LaunchActivity.this, MessageActivity.class);
//			NotificationManagerUnits.getInstance(this).cancel(Integer.valueOf(expertId));
		} else {
			//不是推送就直接跳转到欢迎页面
			intent.setClass(LaunchActivity.this, WelcomeActivity.class);
		}
		startActivity(intent);
		finish();
	}

}

