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
import com.innouni.south.util.MD5;
import com.innouni.south.util.NetUtil;


/**
 * –ﬁ∏ƒ√‹¬Î“≥√Ê
 * @author HuGuojun
 * @data 2013-09-10
 */
public class ModifyPwdActivity extends BaseActivity implements OnClickListener {

	private EditText originalView, newPwdView, againPwdView;
	private Button submitPwdBtn;
	
	private String originalPwd, newPwd, againPwd;
	
	private ModifyPwdTask task;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_pwd);
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
		titleContentView.setText(R.string.modify_pwd);
		
		originalView = (EditText) findViewById(R.id.edit_original_pwd);
		newPwdView = (EditText) findViewById(R.id.edit_new_pwd);
		againPwdView = (EditText) findViewById(R.id.edit_again_pwd);
		submitPwdBtn = (Button) findViewById(R.id.btn_submit_pwd);
		submitPwdBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_title_left:
			finish();
			break;
		case R.id.btn_submit_pwd:
			handleSubmitBtn();
			break;
		}
	}

	private void handleSubmitBtn() {
		originalPwd = originalView.getText().toString();
		newPwd = newPwdView.getText().toString();
		againPwd = againPwdView.getText().toString();
		if("".equals(originalPwd) || "".equals(newPwd) || "".equals(againPwd)){
			showToast(R.string.register_info_incomplete);
			return;
		}
		if (!newPwd.equals(againPwd)) {
			showToast(R.string.pwd_not_same);
			return;
		}
		if (!NetUtil.isNetworkAvailable(ModifyPwdActivity.this)) {
			showToast(R.string.net_unavailable_current);
			return;
		}
		if (null != task) task.cancel(true);
		task = new ModifyPwdTask();
		task.execute();
	}
	
	private class ModifyPwdTask extends AsyncTask<Void, Void, Boolean> {

		private ProgressDialog dialog;
		
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(ModifyPwdActivity.this);
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
			pairs.add(new BasicNameValuePair("oldPwd", MD5.getMD5(originalPwd)));
			pairs.add(new BasicNameValuePair("newPwd", MD5.getMD5(newPwd)));
			String json = HttpPostRequest.getDataFromWebServer(ModifyPwdActivity.this, "modifyPwd", pairs);
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
			originalView.setText(null);
			newPwdView.setText(null);
			againPwdView.setText(null);
			if (result) {
				showToast(R.string.modify_success);
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
