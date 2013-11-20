package com.innouni.south.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.innouni.south.app.MainApplication;
import com.innouni.south.base.BaseActivity;
import com.innouni.south.entity.QQAccessToken;
import com.innouni.south.entity.UserInfo;
import com.innouni.south.net.HttpPostRequest;
import com.innouni.south.util.QQAccessTokenKeeper;
import com.innouni.south.util.ShareUtil;
import com.innouni.south.util.SinaAccessTokenKeeper;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.sso.SsoHandler;

/**
 * 个人中心
 * @author HuGuojun
 * @data 2013-08-14
 */
public class UserCenterActivity extends BaseActivity implements OnClickListener {
	
	private static final String BIND_QQ = "1";
	private static final String BIND_WEIBO = "2";
	
	private RelativeLayout modifyLayout, emailLayout, phoneLayout, qqLayout, sinaLayout;
	private TextView nameView, vipView;
	private TextView emailView, phoneView, qqView, sinaView;
	private Button exitBtn;
	
	private Tencent tencent;
	private Weibo weibo;
    private SsoHandler ssoHandler;
	private BindTask task;
	
	private ShareUtil shareUtil;
	private GetUserTask userTask;
	private UserInfo user;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_center);
		application = MainApplication.getApplication();
		application.setActivity(this);
		shareUtil = ShareUtil.getInstance(this);
		initView();
	}

	private void initView() {
		titleLeftBtn = (Button) findViewById(R.id.btn_title_left);
		titleRightBtn = (Button) findViewById(R.id.btn_title_right);
		titleContentView = (TextView) findViewById(R.id.txt_title_content);
		titleLeftBtn.setOnClickListener(this);
		titleRightBtn.setVisibility(View.GONE);
		titleContentView.setText(R.string.user_center);
		
		modifyLayout = (RelativeLayout) findViewById(R.id.lay_modify_pwd);
		emailLayout = (RelativeLayout) findViewById(R.id.lay_email_address);
		phoneLayout = (RelativeLayout) findViewById(R.id.lay_phone_num);
		qqLayout = (RelativeLayout) findViewById(R.id.lay_bind_qq);
		sinaLayout = (RelativeLayout) findViewById(R.id.lay_bind_sina);
		modifyLayout.setOnClickListener(this);
		emailLayout.setOnClickListener(this);
		phoneLayout.setOnClickListener(this);
		qqLayout.setOnClickListener(this);
		sinaLayout.setOnClickListener(this);
		
		nameView = (TextView) findViewById(R.id.txt_name);
		vipView = (TextView) findViewById(R.id.txt_vip);
		emailView = (TextView) findViewById(R.id.txt_email_address);
		phoneView = (TextView) findViewById(R.id.txt_phone_num);
		qqView = (TextView) findViewById(R.id.txt_bind_qq);
		sinaView = (TextView) findViewById(R.id.txt_bind_sina);
		
		user = application.getUserInfo();
		if (null != user && !"".equals(user.getUserId().toString())) {
			nameView.setText(user.getUserName().toString());
			vipView.setText(Boolean.valueOf(user.getVip().toString())?"vip":"普通会员");
			emailView.setText(user.getEmail().toString());
			phoneView.setText(user.getPhone().toString());
			qqView.setText(Boolean.valueOf(user.getIsBindQQ().toString())?"已绑定":"未绑定");
			sinaView.setText(Boolean.valueOf(user.getIsBindWeibo().toString())?"已绑定":"未绑定");
		}
		
		exitBtn = (Button) findViewById(R.id.btn_exit);
		exitBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.btn_title_left:
			finish();
			break;
		case R.id.btn_exit:
			application.clearPreference();
			application.setUserInfo(null);
			//用户退出后停止后台推送服务
			//stopService(new Intent(UserCenterActivity.this, MessageService.class));
			finish();
			break;
		case R.id.lay_modify_pwd:
			intent = new Intent(UserCenterActivity.this, ModifyPwdActivity.class);
			startActivity(intent);
			break;
		case R.id.lay_email_address:
			intent = new Intent(UserCenterActivity.this, ModifyEmailActivity.class);
			startActivity(intent);
			break;
		case R.id.lay_phone_num:
			intent = new Intent(UserCenterActivity.this, ModifyPhoneActivity.class);
			startActivity(intent);
			break;
		case R.id.lay_bind_qq:
			if (Boolean.valueOf(application.getUserInfo().getIsBindQQ().toString())) {
				showToast(R.string.binded);
				return;
			}
			tencentOauth();
			break;
		case R.id.lay_bind_sina:
			if (Boolean.valueOf(application.getUserInfo().getIsBindWeibo().toString())) {
				showToast(R.string.binded);
				return;
			}
			weiboOauth();
			break;
		default:
			break;
		}
	}

	//QQ授权
	private void tencentOauth() {
		tencent = Tencent.createInstance(ShareUtil.QQ_APP_ID, this.getApplicationContext());
		if (!tencent.isSessionValid()) {
			IUiListener listener = new BaseUiListener(){
				@Override
				protected void doComplete(JSONObject values) {
					try {
						String openId = values.getString("openid");
						String accessToken = values.getString("access_token");
						//计算token的失效日期
						long expiresIn = System.currentTimeMillis() + Long.parseLong(values.getString("expires_in")) * 1000;
						QQAccessToken token = new QQAccessToken(openId, accessToken, expiresIn);
						QQAccessTokenKeeper.keepAccessToken(UserCenterActivity.this, token);
						if (null != task) task.cancel(true);
						task = new BindTask();
						task.execute(accessToken, String.valueOf(expiresIn), BIND_QQ);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			};
			tencent.login(this, "all", listener);
		}
	}

	private class BaseUiListener implements IUiListener {

		@Override
		public void onComplete(JSONObject response) {
			doComplete(response);
		}
		
		protected void doComplete(JSONObject values) {}
		
		@Override
		public void onCancel() {
			showToast("取消授权");
		}
		
		@Override
		public void onError(UiError e) {
			showToast("错误码:" + e.errorCode + ", 错误信息:"
                    + e.errorMessage + ", 详情:" + e.errorDetail);
		}
	}
	
	//新浪微博授权
	private void weiboOauth() {
		weibo = Weibo.getInstance(ShareUtil.SINA_APP_KEY, ShareUtil.SINA_REDIRECT_URL, ShareUtil.SINA_SCOPE);
		ssoHandler = new SsoHandler(UserCenterActivity.this, weibo);
		ssoHandler.authorize(new AuthDialogListener(),null);
	}
	
	private class BindTask extends AsyncTask<String, Void, String> {
		
		//区分绑定QQ还是微博
		private String type;

		@Override
		protected String doInBackground(String... params) {
			type = params[2];
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("userId", application.getUserInfo().getUserId().toString()));
			pairs.add(new BasicNameValuePair("token", params[0]));
			pairs.add(new BasicNameValuePair("expires", params[1]));
			pairs.add(new BasicNameValuePair("type", params[2]));
			String json = HttpPostRequest.getDataFromWebServer(UserCenterActivity.this, "bind", pairs);
			System.out.println("绑定账户返回: " + json);
			return json;
		}

		@Override
		protected void onPostExecute(String result) {
			task = null;
			String message = null;
			try {
				JSONObject object = new JSONObject(result);
				message = object.getString("message");
				if(object.getBoolean("isSuccess")){
					if (type.equals(BIND_QQ)) {
						application.getUserInfo().setIsBindQQ(true);
						shareUtil.setBooleanValues(ShareUtil.ISBINDQQ, true);
						qqView.setText(R.string.binded);
					} else {
						application.getUserInfo().setIsBindWeibo(true);
						shareUtil.setBooleanValues(ShareUtil.ISBINDWEIBO, true);
						sinaView.setText(R.string.binded);
					}
				}else {
					if (type.equals(BIND_QQ)) {
						QQAccessTokenKeeper.clear(UserCenterActivity.this);
					}else {
						SinaAccessTokenKeeper.clear(UserCenterActivity.this);
					}
				}
			} catch (JSONException e) {
				if (type.equals(BIND_QQ)) {
					QQAccessTokenKeeper.clear(UserCenterActivity.this);
				}else {
					SinaAccessTokenKeeper.clear(UserCenterActivity.this);
				}
			} 
			showToast(message);
		}
	}
	
	class AuthDialogListener implements WeiboAuthListener {

        @Override
        public void onComplete(Bundle values) {
        	String code = values.getString("code");
        	if(code != null){
	        	return;
        	} 
            String token = values.getString("access_token");
            String expires_in = values.getString("expires_in");
            Oauth2AccessToken accessToken = new Oauth2AccessToken(token, expires_in);
            if (accessToken.isSessionValid()) {
                SinaAccessTokenKeeper.keepAccessToken(UserCenterActivity.this, accessToken);
	            if (null != task) task.cancel(true);
				task = new BindTask();
				task.execute(accessToken.getToken(), String.valueOf(accessToken.getExpiresTime()), BIND_WEIBO);
            }
        }

        @Override
        public void onError(WeiboDialogError e) {
        	showToast("授权失败 : " + e.getMessage());
        }

        @Override
        public void onCancel() {
        	showToast("取消授权");
        }

        @Override
        public void onWeiboException(WeiboException e) {
        	showToast("授权失败 : " + e.getMessage());
        }
    }
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null != ssoHandler) {
        	ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
        if (null != tencent) {
            tencent.onActivityResult(requestCode, resultCode, data);
		}
    }
	
	private class GetUserTask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("userId", user.getUserId().toString()));
			String json = HttpPostRequest.getDataFromWebServer(UserCenterActivity.this, "getUserById", pairs);
			System.out.println("根据用户ID获取用户信息返回: " + json);
			return initAndSaveUser(json);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			userTask = null;
			if (result) {
				nameView.setText(user.getUserName().toString());
				vipView.setText(Boolean.valueOf(user.getVip().toString())?"vip":"普通会员");
				emailView.setText(user.getEmail().toString());
				phoneView.setText(user.getPhone().toString());
				qqView.setText(Boolean.valueOf(user.getIsBindQQ().toString())?"已绑定":"未绑定");
				sinaView.setText(Boolean.valueOf(user.getIsBindWeibo().toString())?"已绑定":"未绑定");
			}
		}
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (null != userTask) {
			userTask.cancel(true);
		}
		userTask = new GetUserTask();
		userTask.execute();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if(null != task)
			task.cancel(true);
		if(null != userTask)
			userTask.cancel(true);
	}
	
	@SuppressWarnings("finally")
	private boolean initAndSaveUser(String json) {
		boolean isSuccess = false;
		try {
			JSONObject object = new JSONObject(json);
			isSuccess = object.getBoolean("isSuccess");
			if (isSuccess) {
				int userId = object.getInt("userId");
				String nick = object.getString("nick");
				boolean vip = object.getBoolean("vip");
				String email = object.getString("email");
				String phone = object.getString("phone");
				boolean isBindQQ = object.getBoolean("isBindQQ");
				boolean isBindWeibo = object.getBoolean("isBindWeibo");
				
				shareUtil.setIntValues(ShareUtil.USERID, userId);
				shareUtil.setStringValues(ShareUtil.USERNAME, nick);
				shareUtil.setBooleanValues(ShareUtil.VIP, vip);
				shareUtil.setStringValues(ShareUtil.EMAIL, email);
				shareUtil.setStringValues(ShareUtil.PHONE, phone);
				shareUtil.setBooleanValues(ShareUtil.ISBINDQQ, isBindQQ);
				shareUtil.setBooleanValues(ShareUtil.ISBINDWEIBO, isBindWeibo);
		
				user.setUserId(userId);
				user.setUserName(nick);
				user.setVip(vip);
				user.setPhone(phone);
				user.setEmail(email);
				user.setIsBindQQ(isBindQQ);
				user.setIsBindWeibo(isBindWeibo);
				application.setUserInfo(user);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} finally {
			return isSuccess;
		}
	}
}
