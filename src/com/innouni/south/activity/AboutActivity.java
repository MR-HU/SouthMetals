package com.innouni.south.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.innouni.south.app.MainApplication;
import com.innouni.south.base.BaseActivity;


/**
 * 关于我们页面
 * @author HuGuojun
 * @data 2013-09-04
 */
public class AboutActivity extends BaseActivity implements OnClickListener {
	
	private RelativeLayout shareAppLayout, appInfoLayout, feedBackLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
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
		titleContentView.setText(R.string.about_us);
		
		shareAppLayout = (RelativeLayout) findViewById(R.id.lay_share_app);
		appInfoLayout = (RelativeLayout) findViewById(R.id.lay_app_info);
		feedBackLayout = (RelativeLayout) findViewById(R.id.lay_feed_back);
		shareAppLayout.setOnClickListener(this);
		appInfoLayout.setOnClickListener(this);
		feedBackLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.btn_title_left:
			finish();
			break;
		case R.id.lay_share_app:
			intent = new Intent(AboutActivity.this, ShareAppActivity.class);
			startActivity(intent);
			break;
		case R.id.lay_app_info:
			intent = new Intent(AboutActivity.this, AppInfoActivity.class);
			startActivity(intent);
			break;
		case R.id.lay_feed_back:
			intent = new Intent(AboutActivity.this, FeedBackActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

}
