package com.innouni.south.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.innouni.south.app.MainApplication;
import com.innouni.south.base.BaseActivity;

/**
 * 开户申请页面
 * 
 * @author HuGuojun
 * @date 2013-11-29 上午11:37:39
 * @modify
 * @version 1.0.0
 */
public class AccountApplyActivity extends BaseActivity implements OnClickListener {
	
	private static final String url = "http://192.168.1.100/nfgjs_app_server/index.php?c=apply&a=index";
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
		titleLeftBtn = (Button) findViewById(R.id.btn_title_left);
		titleRightBtn = (Button) findViewById(R.id.btn_title_right);
		titleContentView = (TextView) findViewById(R.id.txt_title_content);
		titleLeftBtn.setOnClickListener(this);
		titleRightBtn.setOnClickListener(this);
		titleContentView.setText("开户申请");
		
		webView = (WebView) findViewById(R.id.webview_apply_account);
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
