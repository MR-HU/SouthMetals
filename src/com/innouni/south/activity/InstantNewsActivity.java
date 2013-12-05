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

import com.innouni.south.adapter.CommentAdapter;
import com.innouni.south.app.MainApplication;
import com.innouni.south.base.BaseActivity;
import com.innouni.south.entity.MarketNews;
import com.innouni.south.net.HttpPostRequest;
import com.innouni.south.net.ParseJsonToList;
import com.innouni.south.util.NetUtil;
import com.innouni.south.widget.PullToRefreshView;
import com.innouni.south.widget.PullToRefreshView.OnHeaderRefreshListener;

/**
 * 即时新闻页面
 * 
 * @author HuGuojun
 * @date 2013-11-29 上午11:06:17
 * @modify
 * @version 1.0.0
 */
public class InstantNewsActivity extends BaseActivity implements OnClickListener, OnItemClickListener {

	private PullToRefreshView refreshView;
	private ListView mListView;
	private View footView;
	private ViewSwitcher switcherView;
	private RelativeLayout loadFailLayout;
	private ImageView loadFailView;
	
	private CommentAdapter adapter;
	private GetMarketNewsTask task;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		application = MainApplication.getApplication();
		application.setActivity(this);
		initView();
	}
	
	private void initView() {
		titleLeftBtn = (Button) findViewById(R.id.btn_title_left);
		titleRightBtn = (Button) findViewById(R.id.btn_title_right);
		titleContentView = (TextView) findViewById(R.id.txt_title_content);
		titleRefreshBar = (ProgressBar) findViewById(R.id.progress_title_right);
		titleLeftBtn.setOnClickListener(this);
		titleRightBtn.setOnClickListener(this);
		titleContentView.setText("即时新闻");
		
		refreshView = (PullToRefreshView) findViewById(R.id.refresh_view);
		mListView = (ListView) findViewById(R.id.my_listview);
		mListView.setHeaderDividersEnabled(false);
		mListView.setFooterDividersEnabled(false);
		footView = getLayoutInflater().inflate(R.layout.app_loadmore_layout, null);
		switcherView = (ViewSwitcher) footView.findViewById(R.id.switcher_view);
		mListView.addFooterView(footView,null,false);
		switcherView.setVisibility(View.GONE);
		switcherView.setOnClickListener(this);	
		
		adapter = new CommentAdapter(this);
		mListView.setAdapter(adapter);
		adapter.setListView(mListView);
		mListView.setOnItemClickListener(this);
		
		loadFailLayout = (RelativeLayout) findViewById(R.id.lay_load_fail);
		loadFailView = (ImageView) findViewById(R.id.imageview_load_fail);
		loadFailView.setOnClickListener(this);
		
		refreshView.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {
			@Override
			public void onHeaderRefresh(PullToRefreshView view) {
				if (NetUtil.isNetworkAvailable(InstantNewsActivity.this)){
					if(null != task) task.cancel(true);
					task = new GetMarketNewsTask();
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
				task = new GetMarketNewsTask();
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
		MarketNews news = (MarketNews) adapter.getItem(position);
		Intent intent = new Intent(InstantNewsActivity.this, NewsDetailActivity.class);
		intent.putExtra("title", news.getTitle().toString());
		intent.putExtra("content", news.getContent().toString());
		intent.putExtra("time", news.getTime().toString());
		intent.putExtra("source", news.getSource().toString());
		startActivity(intent);
	}
	
	private class GetMarketNewsTask extends AsyncTask<String, Void, ArrayList<Object>>{
		
		@Override
		protected void onPreExecute() {
			titleRightBtn.setVisibility(View.GONE);
			titleRefreshBar.setVisibility(View.VISIBLE);
			switcherView.setEnabled(false);
		}

		@Override
		protected ArrayList<Object> doInBackground(String... params) {
			String json = HttpPostRequest.getDataFromWebServer(InstantNewsActivity.this, "getNews", null);
			System.out.println("请求即时新闻返回: " + json);
			ParseJsonToList parser = ParseJsonToList.getInstance();
			return parser.parseWebDataToList(json, MarketNews.class);
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
					if(null != adapter) 
						adapter.clear();
					adapter.setList(result, true);
				}
			}
			switcherView.setEnabled(true);
			switcherView.setVisibility(View.GONE);
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
