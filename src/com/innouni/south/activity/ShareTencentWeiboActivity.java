package com.innouni.south.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.innouni.south.app.MainApplication;
import com.innouni.south.base.BaseActivity;
import com.innouni.south.net.HttpPostRequest;
import com.innouni.south.util.ShareUtil;
import com.innouni.south.util.TencentAccessTokenKeeper;
import com.tencent.weibo.api.TAPI;
import com.tencent.weibo.constants.OAuthConstants;
import com.tencent.weibo.oauthv2.OAuthV2;
import com.tencent.weibo.webview.OAuthV2AuthorizeWebView;

/**
 * ������Ѷ΢��ҳ��
 * @author HuGuojun
 * @data 2013-08-19
 */
public class ShareTencentWeiboActivity extends BaseActivity implements OnClickListener {
	
	private EditText contentView;
	private String url;

	private OAuthV2 oAuth;      // ��Ȩ
//	private UserAPI userAPI;    // �û�ģ�鴦�����
	private String response;    // ������Ϣ
	private Message message;
	private TAPI tapi;          // ����΢���������

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				//��ʾ��õ��û���Ϣ
				showToast(msg.obj.toString());
			} else if (msg.what == 2) {
				System.out.println(msg.obj.toString());
				showToast(R.string.share_success);
			} else if (msg.what == 3) {
				showToast(R.string.share_success);
			} else if (msg.what == 0) {
				showToast(R.string.share_fail);
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tencent_share);
		application = MainApplication.getApplication();
		application.setActivity(this);
		url = getIntent().getStringExtra("url");
		if (url.equals("")) {
			new GetApkUrlTask().execute();
		}
		initView();
	}
	
	private void initView() {
		titleLeftBtn = (TextView) findViewById(R.id.btn_title_left);
		titleRightBtn = (TextView) findViewById(R.id.btn_title_right);
		titleContentView = (TextView) findViewById(R.id.txt_title_content);
		titleLeftBtn.setOnClickListener(this);
		titleRightBtn.setBackgroundResource(R.drawable.share_btn_selector);
		titleRightBtn.setOnClickListener(this);
		titleContentView.setText(R.string.tencnet_weibo);
		
		contentView = (EditText) findViewById(R.id.edit_share_content);
		String tip = "";
		if (!url.trim().equals("")) {
			tip = "APP�����ص�ַ:" + url;
		}
		contentView.setText(contentView.getText().toString() + tip);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_title_left:
			finish();
			break;
		case R.id.btn_title_right:
			String content = contentView.getText().toString().trim();
			if (content.equals("")) {
				showToast(R.string.no_input);
			} else {
				sendText(content);
			}
			break;
		default:
			break;
		}
	}
	
	/** ���ʹ��ı���Ϣ */
	private void sendText(String content) {
		if (isExit()) {
			message = new Message();
			tapi = new TAPI(OAuthConstants.OAUTH_VERSION_2_A);
			try {
				oAuth.setClientId(ShareUtil.TENCENT_APP_KEY);
				oAuth.setClientSecret(ShareUtil.Tencent_APP_SECRET);
				response = tapi.add(oAuth, "json", content, "127.0.0.1"); 
				message.what = 2;
				message.obj = response;
			} catch (Exception e) {
				e.printStackTrace();
				message.what = 0;
			}
			tapi.shutdownConnection();
			handler.sendMessage(message);
		} else {
			login();
		}
	}

	/** �ӱ��ػ��OAuth */
	private boolean isExit() {
		oAuth = TencentAccessTokenKeeper.readAccessToken(this); 
		if (oAuth != null && oAuth.getStatus() == 0 && isOauthValid(oAuth)) {
			return true;
		}
		return false;
	}
	
	/** ��Ȩ��Ѷ΢�� */
	private void login() { 
		oAuth = new OAuthV2(ShareUtil.TENCENT_REDIRECT_URL);
		oAuth.setClientId(ShareUtil.TENCENT_APP_KEY);
		oAuth.setClientSecret(ShareUtil.Tencent_APP_SECRET);
		Intent intent = new Intent();
		// ����Intent��ʹ��WebView���û���Ȩ
		intent = new Intent(ShareTencentWeiboActivity.this, OAuthV2AuthorizeWebView.class);
		intent.putExtra("oauth", oAuth);
		startActivityForResult(intent, 2);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) { 
		// ��Ȩ����д���
		if (requestCode == 2) {
			if (resultCode == OAuthV2AuthorizeWebView.RESULT_CODE) {
				oAuth = (OAuthV2) data.getExtras().getSerializable("oauth"); 
				// ��ȡ���ص�oAuth
				if (oAuth.getStatus() == 0) {
					showToast(R.string.oauth_success);
					// ���ر�������
					TencentAccessTokenKeeper.keepAccessToken(this, oAuth);
				}
			}
		}
	}
	
	private boolean isOauthValid(OAuthV2 token) {
		if (!token.getAccessToken().equals("") && !token.getExpiresIn().equals("") &&
				!token.getOpenid().equals("") && !token.getOpenkey().equals("") &&
				!token.getClientId().equals("") && !token.getClientSecret().equals("")) {
			return true;
		} else {
			return false;
		}
	}

	private class GetApkUrlTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			String json = HttpPostRequest.getDataFromWebServer(ShareTencentWeiboActivity.this, "getApkUrl", null);
			try {
				url = new JSONObject(json).optString("url");
			} catch (JSONException e) {
				url = "";
			}
			return null;
		}
		
	}
//	/** ��ȡ��ǰ��Ѷ΢����Ȩ�û���Ϣ */
//	private void getUserInfo() {
//		message = new Message();
//		if (isExit()) {
//			new Thread() {
//				@Override
//				public void run() {
//					userAPI = new UserAPI(OAuthConstants.OAUTH_VERSION_2_A); 
//					try {
//						response = userAPI.info(oAuth, "json"); 
//						message.obj = response;
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//					userAPI.shutdownConnection();
//					super.run();
//					message.what = 1;
//					handler.sendMessage(message);
//				}
//			}.start();
//		} else {
//			login();
//		}
//	}
	
//	/** ���ʹ�ͼƬ���ı���Ϣ */
//	private void sendImage() {
//		String imageUrl = "http://e.hiphotos.baidu.com/album/w%3D2048/sign=86a790652f738bd4c421b53195b386d6/3c6d55fbb2fb4316313195c221a4462309f7d358.jpg"; // ����ͼƬ��ַ����Ҳ�����ñ���ͼƬ
//		if (isExit()) {
//			message = new Message();
//			tapi = new TAPI(OAuthConstants.OAUTH_VERSION_2_A);
//			try {
//				response = tapi.addPic(oAuth, "json", "��Ѷ΢������ƽ̨����", "22.32.1", imageUrl);
//				message.what = 3;
//				message.obj = response;
//			} catch (Exception e) {
//				e.printStackTrace();
//				message.what = 0;
//			}
//			tapi.shutdownConnection();
//			handler.sendMessage(message);
//		} else {
//			login();
//		}
//	}
}
