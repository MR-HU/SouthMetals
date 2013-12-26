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
import com.innouni.south.util.ShareUtil;
import com.sina.weibo.sdk.WeiboSDK;
import com.sina.weibo.sdk.api.BaseResponse;
import com.sina.weibo.sdk.api.IWeiboAPI;
import com.sina.weibo.sdk.api.IWeiboHandler;
import com.sina.weibo.sdk.api.SendMessageToWeiboRequest;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMessage;

/**
 * 分享软件页面
 * @author HuGuojun
 * @data 2013-09-04
 */
public class ShareAppActivity extends BaseActivity implements OnClickListener, IWeiboHandler.Response{
	
	private RelativeLayout sinaWeiboLayout, tencentWeiboLayout, rennLayout;
	private IWeiboAPI weiboAPI;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share_app);
		application = MainApplication.getApplication();
		application.setActivity(this);
		initView();
	}

	private void initView() {
		weiboAPI = WeiboSDK.createWeiboAPI(this, ShareUtil.SINA_APP_KEY);
		weiboAPI.registerApp();
		
		titleLeftBtn = (TextView) findViewById(R.id.btn_title_left);
		titleRightBtn = (TextView) findViewById(R.id.btn_title_right);
		titleContentView = (TextView) findViewById(R.id.txt_title_content);
		titleLeftBtn.setOnClickListener(this);
		titleRightBtn.setVisibility(View.GONE);
		titleContentView.setText(R.string.share_app);
		
		sinaWeiboLayout = (RelativeLayout) findViewById(R.id.lay_sina);
		tencentWeiboLayout = (RelativeLayout) findViewById(R.id.lay_tencnet);
		rennLayout = (RelativeLayout) findViewById(R.id.lay_renn);
		sinaWeiboLayout.setOnClickListener(this);
		tencentWeiboLayout.setOnClickListener(this);
		rennLayout.setOnClickListener(this);
		
		weiboAPI.responseListener(getIntent(), this);
	}

	/**
	 * 分享文本到微博
	 */
	private void reqTextMsg(){
		WeiboMessage weiboMessage = new WeiboMessage();
		weiboMessage.mediaObject = getTextObject();
		SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
		request.transaction = String.valueOf(System.currentTimeMillis());
		request.message = weiboMessage;
		weiboAPI.sendRequest(this, request);
	}
	
	/**
	 * 文本消息构造方法
	 * @return TextObject
	 */
	private TextObject getTextObject(){
		TextObject textObject = new TextObject();
		textObject.text = "我正在使用南方贵金属APP,你也来试试吧!!";
		return textObject;
	}
	
	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.btn_title_left:
			finish();
			break;
		case R.id.lay_sina:
			reqTextMsg();
			break;
		case R.id.lay_tencnet:
			intent = new Intent(ShareAppActivity.this, ShareTencentWeiboActivity.class);
			startActivity(intent);
			break;
		case R.id.lay_renn:
			intent = new Intent(ShareAppActivity.this, ShareRenrenActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		weiboAPI.responseListener(getIntent(), this);
	}
	
	@Override
	public void onResponse(BaseResponse baseResp) {
		switch (baseResp.errCode) {
        case com.sina.weibo.sdk.constant.Constants.ErrorCode.ERR_OK:
            showToast("分享成功");
            break;
        case com.sina.weibo.sdk.constant.Constants.ErrorCode.ERR_CANCEL:
        	showToast("取消分享！！");
            break;
        case com.sina.weibo.sdk.constant.Constants.ErrorCode.ERR_FAIL:
        	showToast(baseResp.errMsg + "分享失败！！");
            break;
        }
	}

}

/*
* 获取微博登录用户的ID <br>
* 通过此ID可以获取微博用户的基本信息
* 
private void getUserID(){
	Oauth2AccessToken token = SinaAccessTokenKeeper.readAccessToken(ShareSinaWeiboActivity.this);
	if(null == token || !token.isSessionValid()) {
		showToast("您需要先授权");
		return;
	}
	AccountAPI accountAPI = new AccountAPI(token);
	accountAPI.getUid(new RequestListener() {
		
		@Override
		public void onIOException(IOException exception) {}
		
		@Override
		public void onError(WeiboException exception) {}
		
		@Override
		public void onComplete4binary(ByteArrayOutputStream stream) {}
		
		@Override
		public void onComplete(String result) {
			try {
				JSONObject object = new JSONObject(result);
				String weiboUid = object.getString("uid");
				shareUtil.setStringValues(ShareUtil.SINAWEIBOUID, weiboUid);
			} catch (Exception e) {}
		}
	});
}

* 获得微博用户的头像和昵称
private void getUserInfo() {
	String weiboUid = shareUtil.getStringValues(ShareUtil.SINAWEIBOUID);
	if(null == weiboUid || "".equals(weiboUid)) return;
	Oauth2AccessToken token = SinaAccessTokenKeeper.readAccessToken(ShareSinaWeiboActivity.this);
	if(null == token || !token.isSessionValid()) {
		showToast("您需要先授权");
		return;
	}
	UsersAPI usersAPI = new UsersAPI(token);
	usersAPI.show(Long.valueOf(weiboUid), new RequestListener(){

		@Override
		public void onComplete(String result) {
			try {
				JSONObject object = new JSONObject(result);
				//获得用户呢称
				String screenName = object.getString("screen_name");
				//获得用户头像
				String profileImageUrl = object.getString("profile_image_url");
				shareUtil.setStringValues(ShareUtil.SINA_SCREENNAME, screenName);
				shareUtil.setStringValues(ShareUtil.SINA_IMAGEURL, profileImageUrl);
			} catch (Exception e) {}
		}

		@Override
		public void onComplete4binary(ByteArrayOutputStream stream) {}

		@Override
		public void onError(WeiboException exception) {}

		@Override
		public void onIOException(IOException exception) {}
		
	});
}*/