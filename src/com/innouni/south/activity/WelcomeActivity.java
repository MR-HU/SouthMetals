package com.innouni.south.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.innouni.south.app.MainApplication;
import com.innouni.south.base.BaseActivity;
import com.innouni.south.entity.UserInfo;
import com.innouni.south.push.SouthMessageService;
import com.innouni.south.util.NetUtil;
import com.innouni.south.util.ShareUtil;

/**
 * 欢迎页面 <br>
 * 加载程序启动图片
 * @author HuGuojun
 * @data 2013-08-12
 */
public class WelcomeActivity extends BaseActivity {

	private ShareUtil shareUtil;
	private int install = -1;
	
	private Handler handler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		handler = new Handler();
		
		shareUtil = ShareUtil.getInstance(this);
		application = MainApplication.getApplication();
		application.setInActivity(true);
		
		//判断是否首次安装
		install = shareUtil.getIntValues(ShareUtil.FIRSTINSTALL);
		
		if(install == -1){
			shareUtil.setIntValues(ShareUtil.FIRSTINSTALL, 1);
			install = 1;
		} else {
			shareUtil.setIntValues(ShareUtil.FIRSTINSTALL, 0);
			install = 0;
		}
		
		//开启推送信息监听的服务
		UserInfo user = application.getUserInfo();
		if (null != user) {
			startService(new Intent(this, SouthMessageService.class));
		}
	}


	@Override
	protected void onResume() {
		super.onResume();
		if(NetUtil.isAirplaneMode(this)) {
			alertMsg(this.getResources().getString(R.string.air_mode));
		} else if(!NetUtil.isNetworkAvailable(this)) {
			alertNetwork();
		} else {
			//启动画面暂停5秒
			handler.postDelayed(new splashHandler(),5000); 
		}
	}
	
	private class splashHandler implements Runnable {
		public void run() {
			Intent intent = new Intent();
			if(install == -1 || install == 1) {
				intent.setClass(WelcomeActivity.this, GuideActivity.class);
			} else {
				intent.setClass(WelcomeActivity.this, MainActivity.class);
			}
			startActivity(intent);
			finish();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			System.exit(0);
			finish();
		}
		return false;
	}
	
	private void alertMsg(String msg) {
		new AlertDialog.Builder(this)
			.setTitle(R.string.alter_title).setMessage(msg)
			.setMessage(msg).setIcon(R.drawable.logo72)
			.setPositiveButton(R.string.dialog_positive, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,	int whichButton) {
					finish();
				}
			}).show();
	}
	
	private void alertNetwork(){
        AlertDialog.Builder builder = new Builder(WelcomeActivity.this);
        builder.setTitle(R.string.alter_title)
        	.setMessage(R.string.net_unavailable)
        	.setNegativeButton(R.string.net_setting, 
    			new DialogInterface.OnClickListener() {
            		@Override
		            public void onClick(DialogInterface dialog, int which) {
		                Intent intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
		                startActivity(intent);
		            }
        		}
        	)
	        .setPositiveButton(R.string.dialog_negative, 
        		new DialogInterface.OnClickListener() {
            		@Override
		            public void onClick(DialogInterface dialog, int which) {
						android.os.Process.killProcess(android.os.Process.myPid());
						System.exit(0);
						dialog.dismiss();
		            }
        		}
	        ).show();
    }
	
}
