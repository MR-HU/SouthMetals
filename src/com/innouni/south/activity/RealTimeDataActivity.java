package com.innouni.south.activity;

import com.innouni.south.app.MainApplication;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

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
	private Button titleLeftBtn, titleRightBtn;
	private TextView titleContentView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_realtime_data);
		application = MainApplication.getApplication();
		application.setActivity(this);
		initView();
//		FragmentManager fragmentManager = getSupportFragmentManager();  
//		FragmentTransaction fragmentTransaction =fragmentManager.beginTransaction();
//		RealTimeFragment fragment = new RealTimeFragment();
//		fragmentTransaction.add(R.id.fragment_container, fragment);
//		fragmentTransaction.commit();
	}

	private void initView() {
		titleLeftBtn = (Button) findViewById(R.id.btn_title_left);
		titleRightBtn = (Button) findViewById(R.id.btn_title_right);
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
