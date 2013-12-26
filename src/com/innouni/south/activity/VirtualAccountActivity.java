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

/**
 * 模拟账户页面
 * 
 * @author HuGuojun
 * @date 2013-11-26 下午4:37:02
 * @modify
 * @version 1.0.0
 */
public class VirtualAccountActivity extends BaseActivity implements OnClickListener {

	private WebView webView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_virtual_account);
		application = MainApplication.getApplication();
		application.setActivity(this);
		initView();
	}

	private void initView() {
		titleLeftBtn = (TextView) findViewById(R.id.btn_title_left);
		titleLeftBtn.setOnClickListener(this);
		titleRightBtn = (TextView) findViewById(R.id.btn_title_right);
		titleRightBtn.setVisibility(View.GONE);
		titleContentView = (TextView) findViewById(R.id.txt_title_content);
		titleContentView.setText("模拟账户");
		
		webView = (WebView) findViewById(R.id.webview_virtual);
		webView.loadUrl("http://192.168.1.100/nfgjs_app_server/index.php?c=apply&a=index");
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
