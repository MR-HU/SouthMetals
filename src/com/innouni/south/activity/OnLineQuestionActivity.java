package com.innouni.south.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.innouni.south.adapter.ExpertOnlineAdapter;
import com.innouni.south.app.MainApplication;
import com.innouni.south.base.BaseActivity;
import com.innouni.south.entity.ExpertInfo;
import com.innouni.south.entity.UserInfo;
import com.innouni.south.net.HttpPostRequest;
import com.innouni.south.net.ParseJsonToList;
import com.innouni.south.util.NetUtil;
import com.innouni.south.widget.PullToRefreshView;
import com.innouni.south.widget.PullToRefreshView.OnHeaderRefreshListener;


/**
 * 在线提问页面 专家列表
 * @author HuGuojun
 * @data 2013-09-03
 */
public class OnLineQuestionActivity extends BaseActivity implements OnClickListener, OnItemClickListener {
	
	private PullToRefreshView refreshView;
	private ListView mListView;
	private View footView;
	private ViewSwitcher switcherView;
	private RelativeLayout loadFailLayout;
	private ImageView loadFailView;
	
	private ExpertOnlineAdapter adapter;
	private GetExpertTask task;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_online_question);
		application = MainApplication.getApplication();
		application.setActivity(this);
		adapter = new ExpertOnlineAdapter(this);
		initView();
	}

	private void initView() {
		titleLeftBtn = (Button) findViewById(R.id.btn_title_left);
		titleRightBtn = (Button) findViewById(R.id.btn_title_right);
		titleContentView = (TextView) findViewById(R.id.txt_title_content);
		titleRefreshBar = (ProgressBar) findViewById(R.id.progress_title_right);
		titleLeftBtn.setOnClickListener(this);
		titleRightBtn.setOnClickListener(this);
		titleContentView.setText(R.string.online_ask);
		
		refreshView = (PullToRefreshView) findViewById(R.id.refresh_view);
		mListView = (ListView) findViewById(R.id.my_listview);
		mListView.setHeaderDividersEnabled(false);
		mListView.setFooterDividersEnabled(false);
		footView = getLayoutInflater().inflate(R.layout.app_loadmore_layout, null);
		switcherView = (ViewSwitcher) footView.findViewById(R.id.switcher_view);
		mListView.addFooterView(footView,null,false);
		switcherView.setVisibility(View.GONE);
		switcherView.setOnClickListener(this);	
		
		mListView.setAdapter(adapter);
		adapter.setAbsListView(mListView);
		mListView.setOnItemClickListener(this);
		
		loadFailLayout = (RelativeLayout) findViewById(R.id.lay_load_fail);
		loadFailView = (ImageView) findViewById(R.id.imageview_load_fail);
		loadFailView.setOnClickListener(this);
		
		refreshView.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {
			@Override
			public void onHeaderRefresh(PullToRefreshView view) {
				if (NetUtil.isNetworkAvailable(OnLineQuestionActivity.this)){
					if(null != task) task.cancel(true);
					task = new GetExpertTask();
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
		case R.id.switcher_view:
			if (NetUtil.isNetworkAvailable(this)) {
				switcherView.showNext();
				if(null != task) task.cancel(true);
				task = new GetExpertTask();
				task.execute();
			} else {
				showToast(R.string.net_unavailable_current);
			}
			break;
		case R.id.imageview_load_fail:
			mListView.setEnabled(false);
			refreshView.headerRefreshing();
			break;
		case R.id.btn_title_left:
			finish();
			break;
		case R.id.btn_title_right:
			mListView.setEnabled(false);
			refreshView.headerRefreshing();
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		UserInfo user = application.getUserInfo();
		if (null == user) {
			showToast(R.string.load_first);
			return;
		}
		ExpertInfo expert = (ExpertInfo) adapter.getItem(position);
		Intent intent = new Intent(OnLineQuestionActivity.this, MessageActivity.class);
		intent.putExtra("push", false);
		intent.putExtra("expertId", expert.getId().toString());
		intent.putExtra("userId", user.getUserId().toString());
		intent.putExtra("expertName", expert.getName().toString());
		startActivity(intent);
	}

	private class GetExpertTask extends AsyncTask<String, Void, ArrayList<Object>>{

		@Override
		protected void onPreExecute() {
			titleRightBtn.setVisibility(View.GONE);
			titleRefreshBar.setVisibility(View.VISIBLE);
			switcherView.setEnabled(false);
		}

		@Override
		protected ArrayList<Object> doInBackground(String... params) {
			String json = HttpPostRequest.getDataFromWebServer(OnLineQuestionActivity.this, "getExperts", null);
			System.out.println("请求专家列表返回: " + json);
			ParseJsonToList parser = ParseJsonToList.getInstance();
			return parser.parseWebDataToList(json, ExpertInfo.class);
		}

		@Override
		protected void onPostExecute(ArrayList<Object> result) {
			titleRightBtn.setVisibility(View.VISIBLE);
			titleRefreshBar.setVisibility(View.GONE);
			task = null;
			if(null != result){
				mListView.setEnabled(true);
				loadFailLayout.setVisibility(View.GONE);
				if(result.size() > 0){
					if(null != adapter){
						adapter.clear();
					}
					adapter.setList(result, true);
				}
				switcherView.setVisibility(View.GONE);
			}
			switcherView.setEnabled(true);
			refreshView.onHeaderRefreshComplete();
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		titleRightBtn.setVisibility(View.VISIBLE);
		titleRefreshBar.setVisibility(View.GONE);
		if(null != task){
			task.cancel(true);
			refreshView.onHeaderRefreshComplete();
		}
	}

}
