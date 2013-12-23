package com.innouni.south.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Intent;
import android.content.res.Resources;
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

import com.innouni.south.adapter.MarketNewsAdapter;
import com.innouni.south.app.MainApplication;
import com.innouni.south.base.BaseActivity;
import com.innouni.south.entity.MarketNews;
import com.innouni.south.net.HttpPostRequest;
import com.innouni.south.net.ParseJsonToList;
import com.innouni.south.util.NetUtil;
import com.innouni.south.widget.PullToRefreshView;
import com.innouni.south.widget.PullToRefreshView.OnHeaderRefreshListener;


/** 
 * 新闻资讯页面(包括实时银讯，每日精评，全球市场，外汇要闻，世界财经，名家专栏)
 * @author HuGuojun
 * @data 2013-09-02
 */
public class MarketNewsActivity extends BaseActivity implements OnClickListener, OnItemClickListener{
	
	public static final int FAST_INFO = 0;
	public static final int AGENCY_COMMENT = 1;
	public static final int NEWS = 2;
	public static final int FROEX_NEWS = 3;
	public static final int WORLD_FINANCE = 4;
	public static final int FAMOUS_COLUMN = 5;
	
	private PullToRefreshView refreshView;
	private ListView mListView;
	private View footView;
	private ViewSwitcher switcherView;
	private RelativeLayout loadFailLayout;
	private ImageView loadFailView;
	
	private MarketNewsAdapter adapter;
	private GetMarketNewsTask task;
	private Resources resources;
	
	private TextView fastInfoView, agencyCommentView, marketNewsView;
	private TextView frexNewsView, worldFianceView, famousColumnView;
	
	private int currentPage = 0;
	private int type = FAST_INFO;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_market_news);
		application = MainApplication.getApplication();
		application.setActivity(this);
		adapter = new MarketNewsAdapter(this);
		resources = getResources();
		initView();
	}

	private void initView() {
		titleLeftBtn = (Button) findViewById(R.id.btn_title_left);
		titleRightBtn = (Button) findViewById(R.id.btn_title_right);
		titleContentView = (TextView) findViewById(R.id.txt_title_content);
		titleRefreshBar = (ProgressBar) findViewById(R.id.progress_title_right);
		titleLeftBtn.setOnClickListener(this);
		titleRightBtn.setOnClickListener(this);
		titleContentView.setText(R.string.market_news);
		
		fastInfoView = (TextView) findViewById(R.id.txt_fast_info);
		agencyCommentView = (TextView) findViewById(R.id.txt_agency_comment);
		marketNewsView = (TextView) findViewById(R.id.txt_market_news);
		frexNewsView = (TextView) findViewById(R.id.txt_forex_news);
		worldFianceView = (TextView) findViewById(R.id.txt_world_finance);
		famousColumnView = (TextView) findViewById(R.id.txt_famous_column);
		fastInfoView.setOnClickListener(new MyOnClickListener(0));
		agencyCommentView.setOnClickListener(new MyOnClickListener(1));
		marketNewsView.setOnClickListener(new MyOnClickListener(2));
		frexNewsView.setOnClickListener(new MyOnClickListener(3));
		worldFianceView.setOnClickListener(new MyOnClickListener(4));
		famousColumnView.setOnClickListener(new MyOnClickListener(5));
		
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
		adapter.setListView(mListView);
		mListView.setOnItemClickListener(this);
		
		loadFailLayout = (RelativeLayout) findViewById(R.id.lay_load_fail);
		loadFailView = (ImageView) findViewById(R.id.imageview_load_fail);
		loadFailView.setOnClickListener(this);
		
		refreshView.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {
			@Override
			public void onHeaderRefresh(PullToRefreshView view) {
				currentPage = 0;
				if (NetUtil.isNetworkAvailable(MarketNewsActivity.this)){
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
		Intent intent = new Intent(MarketNewsActivity.this, NewsDetailActivity.class);
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
			currentPage += 1;
			switcherView.setEnabled(false);
		}

		@Override
		protected ArrayList<Object> doInBackground(String... params) {
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("type", String.valueOf(type)));
			pairs.add(new BasicNameValuePair("pageCount", String.valueOf(currentPage)));
			pairs.add(new BasicNameValuePair("pageSize", String.valueOf(MainApplication.PAGE_SIZE)));
			String json = HttpPostRequest.getDataFromWebServer(pairs);
			System.out.println("请求新闻资讯返回: " + json);
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
					if(currentPage == 1 && null != adapter){
						adapter.clear();
					}
					if(result.size() < MainApplication.PAGE_SIZE){
						switcherView.setVisibility(View.GONE);
					}else {
						switcherView.setVisibility(View.VISIBLE);
						switcherView.setDisplayedChild(0);
					}
					adapter.setList(result, true);
				}else {
					switcherView.setVisibility(View.GONE);
				}
			}
			switcherView.setEnabled(true);
			if(currentPage == 1){
				refreshView.onHeaderRefreshComplete();
			}
		}
	}
	
	private class MyOnClickListener implements OnClickListener{
		
		private int index;
		
		public MyOnClickListener(int index){
			this.index = index;
		}
		
		@Override
		public void onClick(View v) {
			if(index == 0){
				fastInfoView.setBackgroundResource(R.drawable.title_btn_bg_orange_pressed);
				agencyCommentView.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
				marketNewsView.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
				frexNewsView.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
				worldFianceView.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
				famousColumnView.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
//				fastInfoView.setTextColor(resources.getColor(R.color.dark_orange));
//				agencyCommentView.setTextColor(resources.getColor(R.color.white));
//				marketNewsView.setTextColor(resources.getColor(R.color.white));
//				frexNewsView.setTextColor(resources.getColor(R.color.white));
//				worldFianceView.setTextColor(resources.getColor(R.color.white));
//				famousColumnView.setTextColor(resources.getColor(R.color.white));
				type = FAST_INFO;
			}
			if(index == 1){
				fastInfoView.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
				agencyCommentView.setBackgroundResource(R.drawable.title_btn_bg_orange_pressed);
				marketNewsView.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
				frexNewsView.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
				worldFianceView.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
				famousColumnView.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
//				fastInfoView.setTextColor(resources.getColor(R.color.white));
//				agencyCommentView.setTextColor(resources.getColor(R.color.dark_orange));
//				marketNewsView.setTextColor(resources.getColor(R.color.white));
//				frexNewsView.setTextColor(resources.getColor(R.color.white));
//				worldFianceView.setTextColor(resources.getColor(R.color.white));
//				famousColumnView.setTextColor(resources.getColor(R.color.white));
				type = AGENCY_COMMENT;
			}
			if(index == 2){
				fastInfoView.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
				agencyCommentView.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
				marketNewsView.setBackgroundResource(R.drawable.title_btn_bg_orange_pressed);
				frexNewsView.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
				worldFianceView.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
				famousColumnView.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
//				fastInfoView.setTextColor(resources.getColor(R.color.white));
//				agencyCommentView.setTextColor(resources.getColor(R.color.white));
//				marketNewsView.setTextColor(resources.getColor(R.color.dark_orange));
//				frexNewsView.setTextColor(resources.getColor(R.color.white));
//				worldFianceView.setTextColor(resources.getColor(R.color.white));
//				famousColumnView.setTextColor(resources.getColor(R.color.white));
				type = NEWS;
			}
			if(index == 3){
				fastInfoView.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
				agencyCommentView.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
				marketNewsView.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
				frexNewsView.setBackgroundResource(R.drawable.title_btn_bg_orange_pressed);
				worldFianceView.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
				famousColumnView.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
//				fastInfoView.setTextColor(resources.getColor(R.color.white));
//				agencyCommentView.setTextColor(resources.getColor(R.color.white));
//				marketNewsView.setTextColor(resources.getColor(R.color.white));
//				frexNewsView.setTextColor(resources.getColor(R.color.dark_orange));
//				worldFianceView.setTextColor(resources.getColor(R.color.white));
//				famousColumnView.setTextColor(resources.getColor(R.color.white));
				type = FROEX_NEWS;
			}
			if(index == 4){
				fastInfoView.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
				agencyCommentView.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
				marketNewsView.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
				frexNewsView.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
				worldFianceView.setBackgroundResource(R.drawable.title_btn_bg_orange_pressed);
				famousColumnView.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
//				fastInfoView.setTextColor(resources.getColor(R.color.white));
//				agencyCommentView.setTextColor(resources.getColor(R.color.white));
//				marketNewsView.setTextColor(resources.getColor(R.color.white));
//				frexNewsView.setTextColor(resources.getColor(R.color.white));
//				worldFianceView.setTextColor(resources.getColor(R.color.dark_orange));
//				famousColumnView.setTextColor(resources.getColor(R.color.white));
				type = WORLD_FINANCE;
			}
			if(index == 5){
				fastInfoView.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
				agencyCommentView.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
				marketNewsView.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
				frexNewsView.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
				worldFianceView.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
				famousColumnView.setBackgroundResource(R.drawable.title_btn_bg_orange_pressed);
//				fastInfoView.setTextColor(resources.getColor(R.color.white));
//				agencyCommentView.setTextColor(resources.getColor(R.color.white));
//				marketNewsView.setTextColor(resources.getColor(R.color.white));
//				frexNewsView.setTextColor(resources.getColor(R.color.white));
//				worldFianceView.setTextColor(resources.getColor(R.color.white));
//				famousColumnView.setTextColor(resources.getColor(R.color.dark_orange));
				type = FAMOUS_COLUMN;
			}
			if (null != adapter && adapter.getCount() > 0) {
				adapter.clear();
			}
			switcherView.setVisibility(View.GONE);
			mListView.setEnabled(false);
			refreshView.headerRefreshing();
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
