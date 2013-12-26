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
 * ��½ҳ��
 * @author HuGuojun
 * @date 2013-11-18 ����5:13:04
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
	
    /** SsoHandler ����sdk֧��ssoʱ��Ч   */
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
	
	/** ע�� */
	private void handleRegBtn() {
		Intent intent = new Intent(LoadActivity.this, RegisterActivity.class);
		startActivity(intent);
	}
	
	/** �һ�����  */
	private void handleBackPwdBtn() {
		Intent intent = new Intent(LoadActivity.this, GetPwdBackActivity.class);
		startActivity(intent);
	}
	
	/** ʹ��QQ�˺ŵ�¼ */
	private void handleQqBtn() {
		if (!tencent.isSessionValid()) {
			IUiListener listener = new BaseUiListener(){
				@Override
				protected void doComplete(JSONObject values) {
					try {
						String openId = values.getString("openid");
						String accessToken = values.getString("access_token");
						//����token��ʧЧ����
						long expiresIn = System.currentTimeMillis() + Long.parseLong(values.getString("expires_in")) * 1000;
						QQAccessToken token = new QQAccessToken(openId, accessToken, expiresIn);
						QQAccessTokenKeeper.keepAccessToken(LoadActivity.this, token);
						showToast("QQ��Ȩ�ɹ�");
						//����QQ��token��ȡ��Ӧ�û���Ϣ
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
	 * ʹ������΢���˺ŵ�¼ <br>
	 * ��¼��ʽΪSSO�����¼ <br>
	 * ���û�а�װ�汾֧�ֵĿͻ������Զ�תΪOAuth2.0��֤
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
			showToast("ȡ����Ȩ");
		}
		
		@Override
		public void onError(UiError e) {
			showToast("������:" + e.errorCode + ", ������Ϣ:"
                    + e.errorMessage + ", ����:" + e.errorDetail);
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
            	//�����Ʊ����ڱ��������ļ�
                SinaAccessTokenKeeper.keepAccessToken(LoadActivity.this, accessToken);
                showToast("����΢����Ȩ�ɹ�");
	            //ͨ������Token��ö�Ӧ�û���Ϣ
	            //���ȡ�ö�Ӧ�û���Ϣ,���ʼ���û�,�����������
	            //����,����ע��ҳ���һ���˺�;
	            if (null != task) task.cancel(true);
				task = new GetUserByTokenTask("getUserBySinaToken", "sinaToken", "sinaExpires", REGISTER_WEIBO);
				task.execute(accessToken.getToken(), String.valueOf(accessToken.getExpiresTime()));
            }
        }

        @Override
        public void onError(WeiboDialogError e) {
        	showToast("��Ȩʧ�� : " + e.getMessage());
        }
        
        @Override
        public void onCancel() {
        	showToast("ȡ����Ȩ");
        }

        @Override
        public void onWeiboException(WeiboException e) {
        	showToast("��Ȩʧ�� : " + e.getMessage());
        }
    }
	
	/** ��ͨ��¼  */
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
			System.out.println("��½����: " + json);
			return initAndSaveUser(json);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			dialog.dismiss();
			loadTask = null;
			if(result) {
				showToast(R.string.load_success);
				//��¼�ɹ��Ժ�����Ϣ���ͷ���
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
			System.out.println("��Ȩ��¼����: " + json);
			return initAndSaveUser(json);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			dialog.dismiss();
			task = null;
			if (result) {
				showToast(R.string.load_success);
				//��¼�ɹ��Ժ�����Ϣ���ͷ���
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
        //sso��Ȩ�ص�
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
