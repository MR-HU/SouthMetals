package com.innouni.south.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.innouni.south.app.MainApplication;
import com.innouni.south.base.BaseActivity;


/**
 * 新闻资讯详情页面
 * @author HuGuojun
 * @data 2013-09-03
 */
public class NewsDetailActivity extends BaseActivity implements OnClickListener{
	
	private TextView titleView, sourceView, timeView;
	private WebView webView;
	
	private String title, time, content, source;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_detail);
		application = MainApplication.getApplication();
		application.setActivity(this);
		Intent intent = getIntent();
		if(null != intent){
			title = intent.getStringExtra("title");
			time = intent.getStringExtra("time");
			content = intent.getStringExtra("content");
			source = intent.getStringExtra("source");
		}
		initView();
	}

	private void initView() {
		titleLeftBtn = (TextView) findViewById(R.id.btn_title_left);
		titleRightBtn = (TextView) findViewById(R.id.btn_title_right);
		titleContentView = (TextView) findViewById(R.id.txt_title_content);
		titleLeftBtn.setOnClickListener(this);
		titleRightBtn.setVisibility(View.GONE);
		titleContentView.setVisibility(View.GONE);
		
		titleView = (TextView) findViewById(R.id.txt_news_detail_title);
		sourceView = (TextView) findViewById(R.id.txt_news_detail_source);
		timeView = (TextView) findViewById(R.id.txt_news_detail_time);
		titleView.setText(title);
		sourceView.setText(source);
		timeView.setText(time);
		
		webView = (WebView) findViewById(R.id.webview_news_detail_content);
		WebSettings sets = webView.getSettings();
		sets.setJavaScriptEnabled(true);
		sets.setSupportZoom(false);
		sets.setBuiltInZoomControls(false);
		webView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {       
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {       
            webView.goBack();       
            return true;       
        }       
        return super.onKeyDown(keyCode, event);       
    }   

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.btn_title_left){
			finish();
		}
	}

}
