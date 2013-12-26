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
import com.innouni.south.util.NetUtil;


/**
 * 意见反馈页面
 * @author HuGuojun
 * @data 2013-09-04
 */
public class FeedBackActivity extends BaseActivity implements OnClickListener {
	
	private TextView feedView;
	private EditText suggestView;
	private Button submitButton;
	
	private String suggestion;
	private FeedBackTask task;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed_back);
		application = MainApplication.getApplication();
		application.setActivity(this);
		initView();
	}

	private void initView() {
		titleLeftBtn = (TextView) findViewById(R.id.btn_title_left);
		titleRightBtn = (TextView) findViewById(R.id.btn_title_right);
		titleContentView = (TextView) findViewById(R.id.txt_title_content);
		titleLeftBtn.setOnClickListener(this);
		titleRightBtn.setVisibility(View.GONE);
		titleContentView.setText(R.string.feed_back_que);
		
		feedView = (TextView) findViewById(R.id.txt_feed_tip);
		feedView.requestFocus();
		suggestView = (EditText) findViewById(R.id.edit_feed_back_suggestion);
		submitButton = (Button) findViewById(R.id.btn_submit_suggestion);
		submitButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_title_left:
			finish();
			break;
		case R.id.btn_submit_suggestion:
			handleSubmitBtn();
			break;
		default:
			break;
		}
	}

	private void handleSubmitBtn() {
		suggestion = suggestView.getText().toString();
		if ("".equals(suggestion.trim())) {
			showToast(R.string.no_input);
			return;
		} 
		if (NetUtil.isNetworkAvailable(FeedBackActivity.this)) {
			if (null != task) task.cancel(true);
			task = new FeedBackTask();
			task.execute(suggestion);
		} else {
			showToast(R.string.net_unavailable_current);
		}
	}
	
	private class FeedBackTask extends AsyncTask<String, Void, Boolean> {
		
		private ProgressDialog dialog;
		
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(FeedBackActivity.this);
			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); 
			dialog.setMessage(getString(R.string.data_submit));
			dialog.setIndeterminate(false); 
			dialog.setCancelable(true);
			dialog.show();
		}
		
		@Override
		protected Boolean doInBackground(String... params) {
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("content", params[0]));
			String json = HttpPostRequest.getDataFromWebServer(FeedBackActivity.this, "feedBack", pairs);
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
			if (result) {
				suggestView.setText(null);
				showToast(R.string.feed_success);
			} else {
				showToast(R.string.feed_fail);
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
