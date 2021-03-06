package com.innouni.south.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.innouni.south.app.MainApplication;
import com.innouni.south.base.BaseActivity;
import com.innouni.south.net.HttpPostRequest;
import com.innouni.south.util.NetUtil;
import com.innouni.south.util.ShareUtil;


/**
 * 服务热线页面
 * @author HuGuojun
 * @data 2013-09-09
 */
public class ServerActivity extends BaseActivity implements OnClickListener{
	
	private Button gotoAbotButton;
	private Button gotoChargeButton;
	private Button gotoWeButton;
	private LinearLayout telLayout;
	private TextView serverView;
	private ShareUtil shareUtil;
	private GetServerLineTask task;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_server);
		application = MainApplication.getApplication();
		application.setActivity(this);
		shareUtil = ShareUtil.getInstance(this);
		initView();
		if (NetUtil.isNetworkAvailable(this)) {
			if(null != task) task.cancel(true);
			task = new GetServerLineTask();
			task.execute();
		}
	}

	private void initView() {
		titleLeftBtn = (TextView) findViewById(R.id.btn_title_left);
		titleRightBtn = (TextView) findViewById(R.id.btn_title_right);
		titleContentView = (TextView) findViewById(R.id.txt_title_content);
		titleLeftBtn.setOnClickListener(this);
		titleRightBtn.setVisibility(View.GONE);
		titleContentView.setText(R.string.server_line);
		
		findViewById(R.id.txt_wap).setOnClickListener(this);
		
		serverView = (TextView) findViewById(R.id.txt_server_line);
		serverView.setText(shareUtil.getStringValues(ShareUtil.SERVER_LINE));
		
		telLayout = (LinearLayout) findViewById(R.id.lay_telephone);
		telLayout.setOnClickListener(this);
		
		gotoWeButton = (Button) findViewById(R.id.btn_goto_web);
		gotoWeButton.setOnClickListener(this);
		gotoAbotButton = (Button) findViewById(R.id.btn_goto_about);
		gotoChargeButton = (Button) findViewById(R.id.btn_goto_charge);
		gotoAbotButton.setOnClickListener(this);
		gotoChargeButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_title_left:
			finish();
			break;
		case R.id.lay_telephone:
			Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + serverView.getText().toString()));
			startActivity(intent);
			break;
		case R.id.btn_goto_web:
			Intent intent3 = new Intent(ServerActivity.this, VirtualAccountActivity.class);
			startActivity(intent3);
			break;
		case R.id.btn_goto_about:
			Intent intent2 = new Intent(ServerActivity.this, AboutActivity.class);
			startActivity(intent2);
			break;
		case R.id.btn_goto_charge:
			Intent intent4 = new Intent(ServerActivity.this, RateExchangeActivity.class);
			startActivity(intent4);
			break;
		case R.id.txt_wap:
			Intent intent5 = new Intent();        
            intent5.setAction("android.intent.action.VIEW");   
            String uri = getResources().getString(R.string.app_url) + "index.php?c=wap&a=data";
            Uri url = Uri.parse(uri);   
            intent5.setData(url);  
            startActivity(intent5);
			break;
		default:
			break;
		}
	}
	
	private class GetServerLineTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			String serverLine = null;
			String json = HttpPostRequest.getDataFromWebServer(ServerActivity.this, "getServerLine", null);
			System.out.println("请求服务热线返回: " + json);
			try {
				JSONObject object = new JSONObject(json);
				serverLine = object.getString("phoneNum");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return serverLine;
		}

		@Override
		protected void onPostExecute(String result) {
			if (null == result || result.equals("")) return;
			serverView.setText(result);
			shareUtil.setStringValues(ShareUtil.SERVER_LINE, result);
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
