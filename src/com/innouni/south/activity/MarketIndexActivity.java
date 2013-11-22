package com.innouni.south.activity;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.innouni.south.adapter.FinancialDataAdapter;
import com.innouni.south.app.MainApplication;
import com.innouni.south.base.BaseActivity;
import com.innouni.south.entity.FinancialData;
import com.innouni.south.net.HttpPostRequest;
import com.innouni.south.net.ParseJsonToList;
import com.innouni.south.util.NetUtil;
import com.innouni.south.widget.PullToRefreshView;
import com.innouni.south.widget.PullToRefreshView.OnHeaderRefreshListener;

/**
 * 财经指数页面
 * @author HuGuojun
 * @data 2013-09-10
 */
public class MarketIndexActivity extends BaseActivity implements OnClickListener{

	private PullToRefreshView refreshView;
	private View headView;
	private ListView mListView;
	private RelativeLayout loadFailLayout;
	private ImageView loadFailView;
	
	private FinancialDataAdapter adapter;
	private GetFinancialDataTask task;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_market_index);
		application = MainApplication.getApplication();
		application.setActivity(this);
		adapter = new FinancialDataAdapter(this);
		initView();
	}

	private void initView() {
		refreshView = (PullToRefreshView) findViewById(R.id.refresh_view);
		mListView = (ListView) findViewById(R.id.my_listview);
		mListView.setHeaderDividersEnabled(false);
		mListView.setFooterDividersEnabled(false);
		headView = getLayoutInflater().inflate(R.layout.app_financial_data_header, null);
		mListView.addHeaderView(headView, null, false);
		
		mListView.setAdapter(adapter);
		adapter.setListView(mListView);
		
		loadFailLayout = (RelativeLayout) findViewById(R.id.lay_load_fail);
		loadFailView = (ImageView) findViewById(R.id.imageview_load_fail);
		loadFailView.setOnClickListener(this);
		
		refreshView.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {
			@Override
			public void onHeaderRefresh(PullToRefreshView view) {
				if (NetUtil.isNetworkAvailable(MarketIndexActivity.this)){
					if(null != task) task.cancel(true);
					task = new GetFinancialDataTask();
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
		case R.id.imageview_load_fail:
			mListView.setEnabled(false);
			refreshView.headerRefreshing();
			break;
		default:
			break;
		}
	}

	private class GetFinancialDataTask extends AsyncTask<String, Void, ArrayList<Object>>{
		
		@Override
		protected ArrayList<Object> doInBackground(String... params) {
			String json = HttpPostRequest.getDataFromWebServer(MarketIndexActivity.this, "getFinancialData", null);
			System.out.println("请求资讯相关->市场指数返回: " + json);
			ParseJsonToList parser = ParseJsonToList.getInstance();
			return parser.parseWebDataToList(json, FinancialData.class);
		}

		@Override
		protected void onPostExecute(ArrayList<Object> result) {
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
			}
			refreshView.onHeaderRefreshComplete();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if(null != task){
			task.cancel(true);
			refreshView.onHeaderRefreshComplete();
		}
	}
	
}
