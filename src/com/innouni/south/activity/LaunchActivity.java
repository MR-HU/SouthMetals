package com.innouni.south.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.innouni.south.base.BaseActivity;

/**
 * ҳ��ַ����ģ����������<br>
 * �ж���������Ϣ����ֱ���������򣬸����жϷַ�����ͬҳ��
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
			//��������������͵���Ϣ��ת��ָ��ҳ��
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
			//�������;�ֱ����ת����ӭҳ��
			intent.setClass(LaunchActivity.this, WelcomeActivity.class);
		}
		startActivity(intent);
		finish();
	}

}

