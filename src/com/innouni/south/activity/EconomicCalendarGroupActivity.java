package com.innouni.south.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.innouni.south.app.MainApplication;
import com.innouni.south.base.BaseActivityGroup;

/**
 * 财经日历板块<br>
 * 包括财经日历和财经指数
 * @author HuGuojun
 * @date 2013-11-21 下午4:47:49
 * @modify
 * @version 1.0.0
 */
public class EconomicCalendarGroupActivity extends BaseActivityGroup implements OnClickListener, OnCheckedChangeListener {

	private RadioGroup radioGroup;
	private RadioButton calendarButton, indexButton;
	public static Handler handler;
	
	@Override
	protected FrameLayout getContainer() {
		return (FrameLayout)findViewById(R.id.lay_info_related_frame);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_economic_group);
		appClication = MainApplication.getApplication();
		appClication.setActivity(this);
		initView();
	}
	
	private void initView() {
		titleLeftBtn = (Button) findViewById(R.id.btn_title_left);
		titleRightBtn = (Button) findViewById(R.id.btn_title_right);
		titleContentView = (TextView) findViewById(R.id.txt_title_content);
		titleLeftBtn.setOnClickListener(this);
		titleRightBtn.setVisibility(View.GONE);
		titleContentView.setText("财经日历");
		
		radioGroup = (RadioGroup) findViewById(R.id.radiogroup_info_related);
		radioGroup.setOnCheckedChangeListener(this);
		calendarButton = (RadioButton) findViewById(R.id.radio_calendar);
		indexButton = (RadioButton) findViewById(R.id.radio_index);		
		
		openView(EconomicCalendaActivity.class);
		
		handler = new Handler(){
			
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case R.id.radio_calendar:
					calendarButton.setChecked(true);
					break;
				default:
					break;
				}
			}
			
		};
	}
	
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.radio_calendar:
			openView(EconomicCalendaActivity.class);
			break;
		case R.id.radio_index:
			openView(MarketIndexActivity.class);
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_title_left) {
			finish();
		}
	}

}
