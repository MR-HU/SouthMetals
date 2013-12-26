package com.innouni.south.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.innouni.south.app.MainApplication;
import com.innouni.south.base.BaseActivity;
import com.innouni.south.entity.QQAccessToken;
import com.innouni.south.entity.UserInfo;
import com.innouni.south.net.HttpPostRequest;
import com.innouni.south.push.SouthMessageService;
import com.innouni.south.util.MD5;
import com.innouni.south.util.NetUtil;
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
 * 登陆页面
 * @author HuGuojun
 * @date 2013-11-18 下午5:13:04
 * @modify
 * @version 1.0.0
 */
public class LoadActivity extends BaseActivity implements OnClickListener{
	
	public static final int REGISTER_CUSTOM = 1;
	public static final int REGISTER_WEIBO = 2;
	public static final int REGISTER_QQ = 3;
	
	private ProgressDialog dialog;
	private EditText usernameText, pwdText;
	private Button loadButton, regButton, weiboButton, qqButton, getPwdBackButton;
	private LoadTask loadTask;
	private ShareUtil shareUtil;
	private String username, password;
	
	private GetUserByTokenTask task; 
	
	private Weibo weibo;
	
    /** SsoHandler 仅当sdk支持sso时有效   */
    private SsoHandler ssoHandler;
    
    private Tencent tencent;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_load);
		application = MainApplication.getApplication();
		application.setActivity(this);
		shareUtil = ShareUtil.getInstance(this);
		tencent = Tencent.createInstance(ShareUtil.QQ_APP_ID, this.getApplicationContext());
		weibo = Weibo.getInstance(ShareUtil.SINA_APP_KEY, ShareUtil.SINA_REDIRECT_URL, ShareUtil.SINA_SCOPE);
		initView();
		if (!NetUtil.isNetworkAvailable(this)) {
			showToast(R.string.net_unavailable_current);
		}
	}
	
	private void initView(){
		titleLeftBtn = (TextView) findViewById(R.id.btn_title_left);
		titleRightBtn = (TextView) findViewById(R.id.btn_title_right);
		titleContentView = (TextView) findViewById(R.id.txt_title_content);
		titleLeftBtn.setOnClickListener(this);
		titleRightBtn.setVisibility(View.GONE);
		titleContentView.setText(R.string.user_login);
		
		usernameText = (EditText) findViewById(R.id.edit_login_username);
		pwdText = (EditText) findViewById(R.id.edit_login_pwd);
		loadButton = (Button) findViewById(R.id.btn_login);
		regButton = (Button) findViewById(R.id.btn_register);
		weiboButton = (Button) findViewById(R.id.btn_sina_weibo);
		qqButton = (Button) findViewById(R.id.btn_qq);
		getPwdBackButton = (Button) findViewById(R.id.btn_get_pwd_back);
		
		loadButton.setOnClickListener(this);
		regButton.setOnClickListener(this);
		weiboButton.setOnClickListener(this);
		qqButton.setOnClickListener(this);
		getPwdBackButton.setOnClickListener(this);
	} 
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:
			handleLoadBtn();
			break;
		case R.id.btn_register:
			handleRegBtn();
			break;
		case R.id.btn_sina_weibo:
			handleWeiboBtn();
			break;
		case R.id.btn_get_pwd_back:
			handleBackPwdBtn();
			break;
		case R.id.btn_qq:
			handleQqBtn();
			break;
		case R.id.btn_title_left:
			finish();
			break;
		default:
			break;
		}
	}
	
	/** 注册 */
	private void handleRegBtn() {
		Intent intent = new Intent(LoadActivity.this, RegisterActivity.class);
		startActivity(intent);
	}
	
	/** 找回密码  */
	private void handleBackPwdBtn() {
		Intent intent = new Intent(LoadActivity.this, GetPwdBackActivity.class);
		startActivity(intent);
	}
	
	/** 使用QQ账号登录 */
	private void handleQqBtn() {
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
						QQAccessTokenKeeper.keepAccessToken(LoadActivity.this, token);
						showToast("QQ授权成功");
						//根据QQ的token获取对应用户信息
						if (null != task) task.cancel(true);
						task = new GetUserByTokenTask("getUserByQQToken", "qqToken", "qqExpires", REGISTER_QQ);
						task.execute(accessToken, String.valueOf(expiresIn));
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			};
			tencent.login(this, "all", listener);
		}
	}

	/**
	 * 使用新浪微博账号登录 <br>
	 * 登录方式为SSO单点登录 <br>
	 * 如果没有安装版本支持的客户端则自动转为OAuth2.0认证
	 */
	private void handleWeiboBtn() {
		ssoHandler = new SsoHandler(LoadActivity.this, weibo);
		ssoHandler.authorize(new AuthDialogListener(),null);
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
            	//将令牌保存在本地配置文件
                SinaAccessTokenKeeper.keepAccessToken(LoadActivity.this, accessToken);
                showToast("新浪微博授权成功");
	            //通过新浪Token获得对应用户信息
	            //如果取得对应用户信息,则初始化用户,进入个人中心
	            //否则,进入注册页面绑定一个账号;
	            if (null != task) task.cancel(true);
				task = new GetUserByTokenTask("getUserBySinaToken", "sinaToken", "sinaExpires", REGISTER_WEIBO);
				task.execute(accessToken.getToken(), String.valueOf(accessToken.getExpiresTime()));
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
	
	/** 普通登录  */
	private void handleLoadBtn() {
		username = usernameText.getText().toString();
		password = pwdText.getText().toString();
		if("".equals(username) || "".equals(password)){
			showToast(R.string.load_tip);
		} else {
			password = MD5.getMD5(password);
			if(null != loadTask){
				loadTask.cancel(true);
			}
			loadTask = new LoadTask();
			loadTask.execute(username, password);
		}
	}
	
	private class LoadTask extends AsyncTask<String, Void, Boolean>{
		
		@Override
		protected void onPreExecute() {
			showDialog(); 
		}
		
		@Override
		protected Boolean doInBackground(String... params) {
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("userName", params[0]));
			pairs.add(new BasicNameValuePair("password", params[1]));
			String json = HttpPostRequest.getDataFromWebServer(LoadActivity.this, "login", pairs);
			System.out.println("登陆返回: " + json);
			return initAndSaveUser(json);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			dialog.dismiss();
			loadTask = null;
			if(result) {
				showToast(R.string.load_success);
				//登录成功以后开启消息推送服务
				startService(new Intent(LoadActivity.this, SouthMessageService.class));
				Intent intent = new Intent(LoadActivity.this, UserCenterActivity.class);
				startActivity(intent);
				finish();
			}else {
				showToast(R.string.load_error);
			}
		}
	}
	
	private class GetUserByTokenTask extends AsyncTask<String, Void, Boolean> {
		
		private String method, tokenKey, expiresKey;
		private String tokenValue, expiresValue;
		private int type;
		
		@Override
		protected void onPreExecute() {
			showDialog(); 
		}
		
		public GetUserByTokenTask(String method, String token, String expires, int type){
			this.method = method;
			this.tokenKey = token;
			this.expiresKey = expires;
			this.type = type;
		}
		
		@Override
		protected Boolean doInBackground(String... params) {
			tokenValue = params[0];
			expiresValue = params[1];
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair(tokenKey, tokenValue));
			pairs.add(new BasicNameValuePair(expiresKey, expiresValue));
			String json = HttpPostRequest.getDataFromWebServer(LoadActivity.this, method, pairs);
			System.out.println("授权登录返回: " + json);
			return initAndSaveUser(json);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			dialog.dismiss();
			task = null;
			if (result) {
				showToast(R.string.load_success);
				//登录成功以后开启消息推送服务
				startService(new Intent(LoadActivity.this, SouthMessageService.class));
				Intent intent = new Intent(LoadActivity.this, UserCenterActivity.class);
				startActivity(intent);
				finish();
			} else {
				Intent intent = new Intent(LoadActivity.this, RegisterActivity.class);
				intent.putExtra("token", tokenValue);
				intent.putExtra("expires", expiresValue);
				intent.putExtra("type", type);
				startActivity(intent);
			}
		}
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
				
				UserInfo user = application.getUserInfo();
				if (null == user) user = new UserInfo();
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
	
	private void showDialog() {
		dialog = new ProgressDialog(LoadActivity.this);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); 
		dialog.setTitle(getResources().getString(R.string.load_state)); 
		dialog.setMessage(getResources().getString(R.string.loading)); 
		dialog.setIndeterminate(false); 
		dialog.setCancelable(true); 
		dialog.show();
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //sso授权回调
        if (null != ssoHandler) {
        	ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
        if (null != tencent) {
        	tencent.onActivityResult(requestCode, resultCode, data);
		}
    }
    
	@Override
	protected void onPause() {
		super.onPause();
		if(null != loadTask)
			loadTask.cancel(true);
		if(null != task)
			task.cancel(true);
	}
}
