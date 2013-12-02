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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.innouni.south.app.MainApplication;
import com.innouni.south.base.BaseActivity;
import com.innouni.south.net.HttpPostRequest;
import com.innouni.south.util.NetUtil;
import com.innouni.south.util.Util;

/**
 * 找回密码页面
 * @author HuGuojun
 * @data 2013-09-07
 */
public class GetPwdBackActivity extends BaseActivity implements OnClickListener {
	
	private EditText emailOrPhoneText;
	private Button nextButton;
	private GetPwdTask task;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pwd_back);
		application = MainApplication.getApplication();
		application.setActivity(this);
		initView();
	}

	private void initView() {
		titleLeftBtn = (Button) findViewById(R.id.btn_title_left);
		titleRightBtn = (Button) findViewById(R.id.btn_title_right);
		titleContentView = (TextView) findViewById(R.id.txt_title_content);
		titleLeftBtn.setOnClickListener(this);
		titleRightBtn.setVisibility(View.GONE);
		titleContentView.setText(R.string.find_back);	
		
		emailOrPhoneText = (EditText) findViewById(R.id.edit_email_phone);
		nextButton = (Button) findViewById(R.id.btn_back_pwd);
		nextButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_title_left:
			finish();
			break;
		case R.id.btn_back_pwd:
			handleBackPwdBtn();
			break;
		default:
			break;
		}
	}

	private void handleBackPwdBtn() {
		if (!NetUtil.isNetworkAvailable(GetPwdBackActivity.this)) {
			showToast(R.string.net_unavailable_current);
		} else {
			String text = emailOrPhoneText.getText().toString();
			if (text.equals("")) {
				showToast(R.string.no_input);
			} else if (!Util.checkEmail(text) && !Util.checkMobile(text)) {
				showToast("邮箱或者手机号码格式不正确");
			} else {
				if (task != null) task.cancel(true);
				task = new GetPwdTask();
				task.execute(text);
			}
		}
	}

	private class GetPwdTask extends AsyncTask<String, Void, String> {

		private ProgressDialog dialog;
		
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(GetPwdBackActivity.this);
			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); 
			dialog.setMessage(getString(R.string.data_submit));
			dialog.setIndeterminate(false); 
			dialog.setCancelable(true);
			dialog.show();
		}
		
		@Override
		protected String doInBackground(String... params) {
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("email", params[0]));
			String json = HttpPostRequest.getDataFromWebServer(GetPwdBackActivity.this, "getPwdBack", pairs);
			System.out.println("找回密码返回: " + json);
			String message = null;
			try {
				JSONObject object = new JSONObject(json);
				message = object.getString("message");
			} catch (JSONException e) {
				message = "";
			}
			return message;
		}

		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
			task = null;
			showToast(result);
			GetPwdBackActivity.this.finish();
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if (null != task) {
			task.cancel(true);
		}
	}
}
