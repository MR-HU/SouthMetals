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
import android.widget.ImageButton;
import android.widget.TextView;

import com.innouni.south.app.MainApplication;
import com.innouni.south.base.BaseActivity;
import com.innouni.south.net.HttpPostRequest;
import com.innouni.south.util.ShareUtil;
import com.innouni.south.util.Util;

/**
 * 修改邮箱页面
 * @author HuGuojun
 * @data 2013-09-10
 */
public class ModifyEmailActivity extends BaseActivity implements OnClickListener {

	private Button submitButton;
	private TextView originalEmailView;
	private EditText newEmailView;
	private String newEmail;
	
	private ModifyEmailTask task;
	private ShareUtil util;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_email);
		application = MainApplication.getApplication();
		application.setActivity(this);
		util = ShareUtil.getInstance(this);
		initView();
	}

	private void initView() {
		titleLeftBtn = (TextView) findViewById(R.id.btn_title_left);
		titleRightBtn = (TextView) findViewById(R.id.btn_title_right);
		titleContentView = (TextView) findViewById(R.id.txt_title_content);
		titleLeftBtn.setOnClickListener(this);
		titleRightBtn.setVisibility(View.GONE);
		titleContentView.setText(R.string.modify_email);
		
		originalEmailView = (TextView) findViewById(R.id.edit_email_address_modify);
		originalEmailView.setText(application.getUserInfo().getEmail().toString());
		newEmailView = (EditText) findViewById(R.id.edit_new_email);
		
		submitButton = (Button) findViewById(R.id.btn_submit_email);
		submitButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_title_left:
			finish();
			break;
		case R.id.btn_submit_email:
			handleSubmitBtn();
			break;
		default:
			break;
		}
	}

	private void handleSubmitBtn() {
		newEmail = newEmailView.getText().toString();
		if (!Util.checkEmail(newEmail)) {
			showToast(R.string.email_error);
			return;
		}
		if (newEmail.equals("")) {
			showToast(R.string.register_info_incomplete);
			return;
		}
		if (null != task) task.cancel(true);
		task = new ModifyEmailTask();
		task.execute();
	}
	
	private class ModifyEmailTask extends AsyncTask<Void, Void, Boolean> {

		private ProgressDialog dialog;
		
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(ModifyEmailActivity.this);
			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); 
			dialog.setMessage(getString(R.string.data_submit));
			dialog.setIndeterminate(false); 
			dialog.setCancelable(true);
			dialog.show();
		}
		
		@Override
		protected Boolean doInBackground(Void... params) {
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("userId", application.getUserInfo().getUserId().toString()));
			pairs.add(new BasicNameValuePair("email", newEmail));
			String json = HttpPostRequest.getDataFromWebServer(ModifyEmailActivity.this, "modifyEmail", pairs);
			try {
				JSONObject object = new JSONObject(json);
				return object.getBoolean("isSuccess");
			} catch (JSONException e) {
				e.printStackTrace();
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			dialog.dismiss();
			task = null;
			newEmailView.setText(null);
			if (result) {
				showToast(R.string.modify_success);
				//当邮箱修改成功以后,在配置文件和内存中都对当前用户的邮箱进行修改。
				util.setStringValues(ShareUtil.EMAIL, newEmail);
				application.getUserInfo().setEmail(newEmail);
				finish();
			} else {
				showToast(R.string.modify_fail);
			}
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
