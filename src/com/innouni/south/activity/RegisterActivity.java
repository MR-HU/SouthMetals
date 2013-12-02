package com.innouni.south.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.innouni.south.app.MainApplication;
import com.innouni.south.base.BaseActivity;
import com.innouni.south.net.HttpPostRequest;
import com.innouni.south.util.MD5;
import com.innouni.south.util.NetUtil;
import com.innouni.south.util.Util;


/**
 * 账号注册页面
 * @author HuGuojun
 * @data 2013-08-13
 */
public class RegisterActivity extends BaseActivity implements OnClickListener {
	
	private EditText usernameText, passwordText, checkPwdText;
	private EditText emailText, phoneText, captchaText;
	private Button submitButton, captchaButton;
	private ProgressDialog dialog;
	
	private RegisterTask registerTask;
	private int type;
	private String token, expires;
	private String name, password, doublePwd, email, phone, captcha;
	private MyCounterUtil counter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		application = MainApplication.getApplication();
		application.setActivity(this);
		Bundle bundle = getIntent().getExtras();
		if (null != bundle) {
			token = bundle.getString("token");
			expires = bundle.getString("expires");
			type = bundle.getInt("type");
		} else {
			token = "";
			expires = "";
			type = LoadActivity.REGISTER_CUSTOM;
		}
		initView();
		if (!NetUtil.isNetworkAvailable(this)) {
			showToast(R.string.net_unavailable_current);
		}
	}

	private void initView() {
		titleLeftBtn = (Button) findViewById(R.id.btn_title_left);
		titleRightBtn = (Button) findViewById(R.id.btn_title_right);
		titleContentView = (TextView) findViewById(R.id.txt_title_content);
		titleLeftBtn.setOnClickListener(this);
		titleRightBtn.setVisibility(View.GONE);
		if (type != LoadActivity.REGISTER_CUSTOM) {
			//type等于2或3表示用Sina或者QQ授权后进入注册页绑定一个账户
			titleContentView.setText(R.string.complement_info);
		} else {
			titleContentView.setText(R.string.user_reg);
		}
		
		usernameText = (EditText) findViewById(R.id.edit_reg_username);
		passwordText = (EditText) findViewById(R.id.edit_reg_pwd);
		checkPwdText = (EditText) findViewById(R.id.edit_reg_again_pwd);
		emailText = (EditText) findViewById(R.id.edit_reg_email);
		phoneText = (EditText) findViewById(R.id.edit_reg_phone);
		captchaText = (EditText) findViewById(R.id.edit_reg_captcha);
		submitButton = (Button) findViewById(R.id.btn_reg_submit);
		captchaButton = (Button) findViewById(R.id.btn_reg_captcha);
		submitButton.setOnClickListener(this);
		captchaButton.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_title_left:
			finish();
			break;
		case R.id.btn_reg_submit:
			handleSubmitBtn();
			break;
		case R.id.btn_reg_captcha:
			//获取验证码
			if (counter != null) {
				counter.cancel();
			}
			counter = new MyCounterUtil(60000, 1000, captchaButton);
			counter.start();
			break;
		}
	}

	private void handleSubmitBtn() {
		name = usernameText.getText().toString();
		password = passwordText.getText().toString();
		doublePwd = checkPwdText.getText().toString();
		email = emailText.getText().toString();
		phone = phoneText.getText().toString();
		captcha = captchaText.getText().toString();
		if("".equals(name)||"".equals(password)||"".equals(doublePwd)
				||"".equals(email)||"".equals(phone)||"".equals(captcha)) {
			showToast(R.string.register_info_incomplete);
			return;
		}
		if(!password.equals(doublePwd)) {
			showToast(R.string.pwd_not_same);
			return;
		}
		if(!Util.checkEmail(email)) {
			showToast(R.string.email_error);
			return;
		}
		if(!Util.checkMobile(phone)) {
			showToast(R.string.phone_error);
			return;
		}
		if(null == registerTask) {
			registerTask = new RegisterTask();
			registerTask.execute();
		}
	}
	
	private class RegisterTask extends AsyncTask<Void, Void, Boolean>{
		
		private List<NameValuePair> pairs;
		private String message;
		
		@Override
		protected void onPreExecute() {
			pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("username", name));
			pairs.add(new BasicNameValuePair("password", MD5.getMD5(password)));
			pairs.add(new BasicNameValuePair("email", email));
			pairs.add(new BasicNameValuePair("phone", phone));
			pairs.add(new BasicNameValuePair("token", token));
			pairs.add(new BasicNameValuePair("expires", expires));
			pairs.add(new BasicNameValuePair("type", String.valueOf(type)));
			pairs.add(new BasicNameValuePair("captcha", captcha));
			showDialog();
			if (counter != null) counter.cancel();
			captchaButton.setEnabled(true);
		}
		
		@Override
		protected Boolean doInBackground(Void... params) {
			String json = HttpPostRequest.getDataFromWebServer(RegisterActivity.this, "register", pairs);
			try {
				JSONObject object = new JSONObject(json);
				System.out.println("注册返回: " + json);
				boolean isSuccess = object.getBoolean("isSuccess");
				if (!isSuccess) {
					message = object.getString("message");
				}
				return isSuccess;
			} catch (JSONException e) {
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			dialog.dismiss();
			registerTask = null;
			if (result) {
				showToast(R.string.register_success);
				RegisterActivity.this.finish();
			} else {
				showToast(message);
			}
		}
	}
	
	private void showDialog() {
		dialog = new ProgressDialog(RegisterActivity.this);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); 
		dialog.setTitle(getResources().getString(R.string.register_state)); 
		dialog.setMessage(getResources().getString(R.string.registering)); 
		dialog.setIndeterminate(false); 
		dialog.setCancelable(true); 
		dialog.show();
	}
	
	/**
	 * 计时器
	 */
	private class MyCounterUtil extends CountDownTimer {
		
		private Button button;
		
		public MyCounterUtil(long millisInFuture, long countDownInterval, Button button) {
			super(millisInFuture, countDownInterval);
			this.button = button;
			this.button.setEnabled(false);
		}

		@Override
		public void onFinish() {
			this.button.setEnabled(true);
			this.button.setText("获取验证码");
		}

		@Override
		public void onTick(long millisUntilFinished) {
			this.button.setText((millisUntilFinished / 1000) + "");
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if(null != registerTask)
			registerTask.cancel(true);
		if (null != counter) 
			counter.cancel();
	}
}

