package com.innouni.south.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.innouni.south.app.MainApplication;
import com.innouni.south.fragment.RealTimeFragmentBottom;
import com.innouni.south.fragment.RealTimeFragmentTop;

/**
 * 综合报价页面
 * 
 * @author HuGuojun
 * @date 2013-11-28 下午2:25:46
 * @modify
 * @version 1.0.0
 */
public class RealTimeDataActivity extends FragmentActivity implements
		OnClickListener {

	private MainApplication application;
	private TextView titleLeftBtn;
	private TextView titleRightBtn;
	private TextView titleContentView;
	private TextView userNameView;

	private MyCounterUtil counter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_realtime_data);
		application = MainApplication.getApplication();
		application.setActivity(this);
		initView();
	}

	private void initView() {
		titleLeftBtn = (TextView) findViewById(R.id.btn_title_left);
		titleRightBtn = (TextView) findViewById(R.id.btn_title_right);
		titleContentView = (TextView) findViewById(R.id.txt_title_content);
		titleLeftBtn.setOnClickListener(this);
		titleRightBtn.setVisibility(View.GONE);
		titleContentView.setText("综合报价");
		userNameView = (TextView) findViewById(R.id.txt_user_name_show);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(0, 0, 15, 0);
		userNameView.setLayoutParams(params);
		userNameView.setVisibility(View.VISIBLE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_title_left:
			finish();
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (counter != null) {
			counter.cancel();
		}
		counter = new MyCounterUtil(6000, 1000);
		counter.start();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		userNameView.setVisibility(View.GONE);
		if (counter != null) {
			counter.cancel();
		}
	}

	private class MyCounterUtil extends CountDownTimer {

		public MyCounterUtil(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			FragmentManager manager = getSupportFragmentManager();
			RealTimeFragmentTop topFragment = (RealTimeFragmentTop) manager
					.findFragmentById(R.id.fragment_top);
			RealTimeFragmentBottom bottomFragment = (RealTimeFragmentBottom) manager
					.findFragmentById(R.id.fragment_bottom);
			topFragment.getData();
			bottomFragment.getData();
			if (counter != null) {
				counter.cancel();
			}
			counter = new MyCounterUtil(6000, 1000);
			counter.start();
		}

		@Override
		public void onTick(long millisUntilFinished) {
			userNameView.setText((millisUntilFinished / 1000) + "");
		}
	}

}
