package com.innouni.south.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.innouni.south.app.MainApplication;
import com.innouni.south.base.BaseActivity;
import com.innouni.south.util.ShareUtil;
import com.renn.rennsdk.RennClient;
import com.renn.rennsdk.RennClient.LoginListener;
import com.renn.rennsdk.RennExecutor.CallBack;
import com.renn.rennsdk.RennResponse;
import com.renn.rennsdk.param.PutFeedParam;

/**
 * 人人授权分享页面
 * @author HuGuojun
 * @data 2013-08-20
 */
public class ShareRenrenActivity extends BaseActivity implements OnClickListener{
	
	private RennClient rennClient;
	private EditText contentView;
	private String content;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		rennClient = RennClient.getInstance(this);
		rennClient.init(ShareUtil.RENREN_APP_ID, ShareUtil.RENREN_API_KEY, ShareUtil.RENREN_SECRET_KEY);
		rennClient.setScope("read_user_album read_user_status publish_share publish_feed status_update");
		rennClient.setTokenType("bearer");
		setContentView(R.layout.activity_renren_share);
		application = MainApplication.getApplication();
		application.setActivity(this);
		initView();
	}

	private void initView() {
		titleLeftBtn = (Button) findViewById(R.id.btn_title_left);
		titleRightBtn = (Button) findViewById(R.id.btn_title_right);
		titleContentView = (TextView) findViewById(R.id.txt_title_content);
		titleLeftBtn.setOnClickListener(this);
		titleRightBtn.setBackgroundResource(R.drawable.share_btn_selector);
		titleRightBtn.setOnClickListener(this);
		titleContentView.setText(R.string.renn);
		
		contentView = (EditText) findViewById(R.id.edit_share_content);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_title_left:
			finish();
			break;
		case R.id.btn_title_right:
			content = contentView.getText().toString().trim();
			if (content.equals("")) {
				showToast(R.string.no_input);
			} else {
				handleOauthBtn();
			}
			break;
		default:
			break;
		}
	}
	 
	/**
	 * 发布新鲜事到人人网
	 */
	private void handleFeedBtn(String content) {
		try {
			PutFeedParam param = new PutFeedParam();
			param.setTitle("app下载地址");
			param.setMessage(content);
			param.setDescription("南方贵金属是个不错的软件");
			param.setTargetUrl("http://www.innouni.com");
			rennClient.getRennService().sendAsynRequest(param, new CallBack() {
	
				@Override
				public void onFailed(String errorCode, String errorMessage) {
					showToast(errorMessage);
				}
	
				@Override
				public void onSuccess(RennResponse response) {
					showToast(R.string.share_success);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 授权人人登陆
	 */
	private void handleOauthBtn() {
		rennClient.setLoginListener(new LoginListener() {
			
			@Override
			public void onLoginSuccess() {
				showToast(R.string.oauth_success);
				handleFeedBtn(content);
			}
			
			@Override
			public void onLoginCanceled() {
				showToast("取消授权");
			}
		});
		rennClient.login(this);
	}

}
