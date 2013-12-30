package com.innouni.south.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.innouni.south.app.MainApplication;
import com.innouni.south.base.BaseActivity;
import com.innouni.south.entity.ETFEntity;

/**
 * 开户申请页面
 * 
 * @author HuGuojun
 * @date 2013-11-29 上午11:37:39
 * @modify
 * @version 1.0.0
 */
public class AccountApplyActivity extends BaseActivity implements OnClickListener {
	
	private String url;
	private WebView webView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_apply);
		application = MainApplication.getApplication();
		application.setActivity(this);
		initView();
	}

	private void initView() {
		titleLeftBtn = (TextView) findViewById(R.id.btn_title_left);
		titleRightBtn = (TextView) findViewById(R.id.btn_title_right);
		titleContentView = (TextView) findViewById(R.id.txt_title_content);
		titleLeftBtn.setOnClickListener(this);
		titleRightBtn.setOnClickListener(this);
		titleContentView.setText("开户申请");
		
		webView = (WebView) findViewById(R.id.webview_apply_account);
		url = getResources().getString(R.string.app_url) + "index.php?c=apply&a=index";
		webView.loadUrl(url);
		webView.loadUrl(url);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_title_left:
			finish();
			break;
		case R.id.btn_title_right:
			webView.loadUrl(url);
			break;
		}
	}	
}
