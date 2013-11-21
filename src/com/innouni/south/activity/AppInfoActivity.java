package com.innouni.south.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.innouni.south.app.MainApplication;
import com.innouni.south.base.BaseActivity;
import com.innouni.south.net.HttpPostRequest;
import com.innouni.south.util.NetUtil;
import com.innouni.south.util.ShareUtil;
import com.innouni.south.util.UpdateVersionUtil;


/**
 * 软件信息页面
 * @author HuGuojun
 * @data 2013-09-04
 */
public class AppInfoActivity extends BaseActivity implements OnClickListener {
	
	private Button updateButton;
	private TextView titleView, contentView;
	private ShareUtil util;
	private GetVersionInfoTask task;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_info);
		application = MainApplication.getApplication();
		application.setActivity(this);
		util = ShareUtil.getInstance(this);
		initview();
		if (NetUtil.isNetworkAvailable(this)) {
			if (task != null) task.cancel(true);
			task = new GetVersionInfoTask();
			task.execute();
		} else {
			showToast(R.string.net_unavailable_current);
		}
	}

	private void initview() {
		titleLeftBtn = (Button) findViewById(R.id.btn_title_left);
		titleRightBtn = (Button) findViewById(R.id.btn_title_right);
		titleContentView = (TextView) findViewById(R.id.txt_title_content);
		titleLeftBtn.setOnClickListener(this);
		titleRightBtn.setVisibility(View.GONE);
		titleContentView.setText(R.string.app_info);
		
		//先从配置文件读取软件信息
		titleView = (TextView) findViewById(R.id.txt_version_title);
		titleView.setText(util.getStringValues(ShareUtil.VERSION_TITLE));
		contentView = (TextView) findViewById(R.id.txt_version_content);
		contentView.setText(util.getStringValues(ShareUtil.VERSION_CONTENT));
		
		updateButton = (Button) findViewById(R.id.btn_test_update);
		updateButton.setOnClickListener(this);
	}
	
	private class GetVersionInfoTask extends AsyncTask<Void, Void, Void> {

		private String title, content;
		
		@Override
		protected Void doInBackground(Void... params) {
			String json = HttpPostRequest.getDataFromWebServer(AppInfoActivity.this, "getAppInfo", null);
			System.out.println("请求软件信息返回: " + json);
			try {
				JSONObject object = new JSONObject(json);
				title = object.getString("version");
				content = object.getString("content");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			task = null;
			titleView.setText(title);
			contentView.setText(content);
			util.setStringValues(ShareUtil.VERSION_TITLE, title);
			util.setStringValues(ShareUtil.VERSION_CONTENT, content);
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_title_left:
			finish();
			break;
		case R.id.btn_test_update:
			UpdateVersionUtil versionUtil = UpdateVersionUtil.getInstance(AppInfoActivity.this);
			versionUtil.setShowLoading(true);
			versionUtil.startCheckVersion();
			break;
		default:
			break;
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
