package com.innouni.south.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.innouni.south.app.MainApplication;

/**
 * 实时数据详情页面
 * 
 * @author HuGuojun
 * @date 2013-11-28 下午2:25:46
 * @modify
 * @version 1.0.0
 */
public class RealTimeDataActivity extends FragmentActivity implements OnClickListener {
	
	private MainApplication application;
	private TextView titleLeftBtn;
	private TextView titleRightBtn;
	private TextView titleContentView;
	
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
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_title_left:
			finish();
			break;
		}
	}

}
