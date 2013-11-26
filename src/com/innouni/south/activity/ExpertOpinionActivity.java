package com.innouni.south.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.innouni.south.app.MainApplication;
import com.innouni.south.base.BaseActivity;


/**
 * 专家解盘页面
 * @author HuGuojun
 * @data 2013-09-03
 */
public class ExpertOpinionActivity extends BaseActivity implements OnClickListener {
	
	private RelativeLayout questionLay, buyLay, payLay;
	private RelativeLayout voiceClassLay;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_expert_opinion);
		application = MainApplication.getApplication();
		application.setActivity(this);
		initView();
	}

	private void initView() {
		titleLeftBtn = (Button) findViewById(R.id.btn_title_left);
		titleRightBtn = (Button) findViewById(R.id.btn_title_right);
		titleContentView = (TextView) findViewById(R.id.txt_title_content);
		titleLeftBtn.setOnClickListener(this);
		titleRightBtn.setVisibility(View.GONE);
		titleContentView.setText(R.string.profession_opinion);
		
		questionLay = (RelativeLayout) findViewById(R.id.lay_online_question);
		buyLay = (RelativeLayout) findViewById(R.id.lay_buy_info);
		payLay = (RelativeLayout) findViewById(R.id.lay_online_pay);
		voiceClassLay = (RelativeLayout) findViewById(R.id.lay_voice_class);
		questionLay.setOnClickListener(this);
		buyLay.setOnClickListener(this);
		payLay.setOnClickListener(this);
		voiceClassLay.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.btn_title_left:
			finish();
			break;
		case R.id.lay_online_question:
			intent = new Intent(ExpertOpinionActivity.this, OnLineQuestionActivity.class);
			startActivity(intent);
			break;
		case R.id.lay_voice_class:
			intent = new Intent(ExpertOpinionActivity.this, VoiceClassActivity.class);
			startActivity(intent);
			break;
//		case R.id.lay_buy_info:
//			if (null == user) {
//				showToast(R.string.load_first);
//			} else {
//				intent = new Intent(ExpertOpinionActivity.this, SubscribeInfoActivity.class);
//				startActivity(intent);
//			}
//			break;
//		case R.id.lay_online_pay:
//			if (null == user) {
//				showToast(R.string.load_first);
//			} else {
//				intent = new Intent(ExpertOpinionActivity.this, InputMoneyActivity.class);
//				startActivity(intent);
//			}
//			break;
		default:
			break;
		}
	}

}
