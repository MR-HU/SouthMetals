package com.innouni.south.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.innouni.south.adapter.MessageAdapter;
import com.innouni.south.app.MainApplication;
import com.innouni.south.base.BaseActivity;
import com.innouni.south.entity.MessageInfo;
import com.innouni.south.net.HttpPostRequest;
import com.innouni.south.net.ParseJsonToList;
import com.innouni.south.util.NetUtil;
import com.innouni.south.widget.PullToRefreshView;
import com.innouni.south.widget.PullToRefreshView.OnHeaderRefreshListener;

/**
 * 留言页面
 * @author HuGuojun
 * @data 2013-09-04
 */
public class MessageActivity extends BaseActivity implements OnClickListener {
	
	private PullToRefreshView refreshView;
	private ListView mListView;
	private RelativeLayout loadFailLayout;
	private ImageView loadFailView;
	
	private MessageAdapter adapter;
	private GetMessageTask task;
	private SendMessageTask sendTask;
	
	private Button sendButton;
	private EditText contentText;
	
	private int currentPage = 0;
	
	private boolean push;
	private String userImage, expertImage;
	private String expertId, userId, expertName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);
		application = MainApplication.getApplication();
		application.setActivity(this);
		adapter = new MessageAdapter(this);
		Intent intent = getIntent();
		if (null != intent) {
			push = intent.getBooleanExtra("push", false);
			expertId = intent.getStringExtra("expertId");
			userId = intent.getStringExtra("userId");
			expertName = intent.getStringExtra("expertName");
		}
		initView();
	}

	private void initView() {
		titleLeftBtn = (TextView) findViewById(R.id.btn_title_left);
		titleRightBtn = (TextView) findViewById(R.id.btn_title_right);
		titleContentView = (TextView) findViewById(R.id.txt_title_content);
		titleRefreshBar = (ProgressBar) findViewById(R.id.progress_title_right);
		titleLeftBtn.setOnClickListener(this);
		titleRightBtn.setOnClickListener(this);
		titleContentView.setText(expertName);
		
		sendButton = (Button) findViewById(R.id.btn_send_message);
		sendButton.setOnClickListener(this);
		contentText = (EditText) findViewById(R.id.edit_message_write);
		
		refreshView = (PullToRefreshView) findViewById(R.id.refresh_view);
		mListView = (ListView) findViewById(R.id.my_listview);
		mListView.setHeaderDividersEnabled(false);
		mListView.setFooterDividersEnabled(false);
		//设置ListView从底部开始显示
		mListView.setStackFromBottom(true);
		
		mListView.setAdapter(adapter);
		adapter.setAbsListView(mListView);
		
		loadFailLayout = (RelativeLayout) findViewById(R.id.lay_load_fail);
		loadFailView = (ImageView) findViewById(R.id.imageview_load_fail);
		loadFailView.setOnClickListener(this);
		
		refreshView.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {
			@Override
			public void onHeaderRefresh(PullToRefreshView view) {
				if (NetUtil.isNetworkAvailable(MessageActivity.this)){
					if(null != task) task.cancel(true);
					task = new GetMessageTask();
					task.execute();
				} else {
					loadFailLayout.setVisibility(View.VISIBLE);
					refreshView.onHeaderRefreshComplete();
				}
			}
		});
		
    	refreshView.headerRefreshing();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_title_left:
			backByTag();
			break;
		case R.id.btn_title_right:
			currentPage = 0;
			mListView.setEnabled(false);
			refreshView.headerRefreshing();
			break;
		case R.id.imageview_load_fail:
			currentPage = 0;
			mListView.setEnabled(false);
			refreshView.headerRefreshing();
			break;
		case R.id.btn_send_message:
			handleSendBtn();
			break;
		default:
			break;
		}
	}

	private void handleSendBtn() {
		String content = contentText.getText().toString();
		if (null != sendTask) sendTask.cancel(true);
		sendTask = new SendMessageTask();
		sendTask.execute(content);
	}

	private class GetMessageTask extends AsyncTask<String, Void, ArrayList<Object>>{
		
		@Override
		protected void onPreExecute() {
			titleRightBtn.setVisibility(View.GONE);
			titleRefreshBar.setVisibility(View.VISIBLE);
			currentPage += 1;
		}

		@Override
		protected ArrayList<Object> doInBackground(String... params) {
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("userId", userId));
			pairs.add(new BasicNameValuePair("expertId", expertId));
			pairs.add(new BasicNameValuePair("pageCount", String.valueOf(currentPage)));
			String json = HttpPostRequest.getDataFromWebServer(MessageActivity.this, "getMessages", pairs);
			System.out.println("请求留言返回: " + json);
			try {
				userImage = new JSONObject(json).getString("userImage");
				expertImage = new JSONObject(json).getString("expertImage");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			ParseJsonToList parser = ParseJsonToList.getInstance();
			return parser.parseWebDataToList(json, MessageInfo.class);
		}

		@Override
		protected void onPostExecute(ArrayList<Object> result) {
			titleRightBtn.setVisibility(View.VISIBLE);
			titleRefreshBar.setVisibility(View.GONE);
			task = null;
			if(null != result){
				adapter.setUserImage(userImage);
				adapter.setExpertImage(expertImage);
				mListView.setEnabled(true);
				loadFailLayout.setVisibility(View.GONE);
				if(result.size() > 0){
					if(currentPage == 1 && null != adapter){
						adapter.clear();
					}
					//显示历史留言的时候,将更多加载的留言加在原列表的顶部
					adapter.setListToFirst(result, true);
					mListView.setSelection(adapter.getCount());
				}else {
					showToast(R.string.no_more_data);
				}
			}
			refreshView.onHeaderRefreshComplete();
		}
	}
	
	private class SendMessageTask extends AsyncTask<String, Void, Boolean> {
		
		private String jsonResult;
		
		@Override
		protected Boolean doInBackground(String... params) {
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("content", params[0]));
			pairs.add(new BasicNameValuePair("userId", userId));
			pairs.add(new BasicNameValuePair("expertId", expertId));
			jsonResult = HttpPostRequest.getDataFromWebServer(MessageActivity.this, "sendMessage", pairs);
			System.out.println("发送留言返回: " + jsonResult);
			try { 
				return new JSONObject(jsonResult).getBoolean("isSuccess");
			} catch (JSONException e) {
				e.printStackTrace();
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			sendTask = null;
			if (!result) {
				showToast(R.string.send_exception);
				return;
			}
			ParseJsonToList parser = ParseJsonToList.getInstance();
			ArrayList<Object> list = parser.parseWebDataToList(jsonResult, MessageInfo.class);
			adapter.setList(list, true);
			mListView.setSelection(adapter.getCount());
			contentText.setText(null);
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		titleRightBtn.setVisibility(View.VISIBLE);
		titleRefreshBar.setVisibility(View.GONE);
		if (null != task) {
			task.cancel(true);
			refreshView.onHeaderRefreshComplete();
		}
		if(null != sendTask)
			sendTask.cancel(true);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			backByTag();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * 根据是否推送进入留言页面来处理返回操作
	 */
	private void backByTag(){
		if (push) {
			if (application.isInActivity()) {
				finish();
			} else {
				Intent intent = new Intent();
				intent.setClass(this, WelcomeActivity.class);
				startActivity(intent);
				finish();
			}
		} else {
			finish();
		}
	}
}

