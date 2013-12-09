package com.innouni.south.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.innouni.south.adapter.ViewPagerAdapter;
import com.innouni.south.app.MainApplication;
import com.innouni.south.base.BaseActivity;
import com.innouni.south.net.HttpPostRequest;
import com.innouni.south.util.NetUtil;
import com.innouni.south.util.UpdateVersionUtil;
import com.innouni.south.widget.MsgBox;
import com.viewpagerindicator.PageIndicator;
import com.yintong.secure.f.i;

/**
 * 主页  九宫格展示各个模块
 * @author HuGuojun
 * @data 2013-09-02
 */
public class MainActivity extends BaseActivity implements OnClickListener, OnItemClickListener, OnPageChangeListener {

	private PageIndicator indicatorLayout;
	private TextView announcementView;
	private GridView gridView1;
	private GridView gridView2;
	private ViewPager viewPager;
	private List<View> views;
	
	private List<HashMap<String, Object>> data1;
	private List<HashMap<String, Object>> data2;
	private int[] icons;
	private String[] titles;
	private int[] icons2;
	private String[] titles2;
	
	private int currentPage = 0;
	
	private GetAnnouncementTask task;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		application = MainApplication.getApplication();
		application.setActivity(this);
		
		icons = new int[]{
				R.drawable.icon_home_realtime, R.drawable.icon_home_market, R.drawable.icon_home_finance, 
				R.drawable.icon_home_news, R.drawable.icon_home_expert, R.drawable.icon_home_exchange, 
				R.drawable.icon_home_about, R.drawable.icon_home_serve, R.drawable.icon_home_help};
		titles = getResources().getStringArray(R.array.home_icon);
		icons2 = new int[]{R.drawable.icon_home_realtime, R.drawable.icon_home_market, R.drawable.icon_home_finance};
		titles2 = new String[]{"即时新闻", "价格转换", "关于我们"};
		initData();
		initView();
		if (NetUtil.isNetworkAvailable(this)) {
			UpdateVersionUtil versionUtil = UpdateVersionUtil.getInstance(MainActivity.this);
			versionUtil.setShowLoading(false);
			versionUtil.startCheckVersion();
			if (task != null) task.cancel(true);
			task = new GetAnnouncementTask();
			task.execute();
		}
	}

	private void initData() {
		HashMap<String, Object> map1;
		data1 = new ArrayList<HashMap<String,Object>>();
		for (int i = 0; i < icons.length; i++) {
			map1 = new HashMap<String, Object>();
			map1.put("icon", icons[i]);
			map1.put("title", titles[i]);
			data1.add(map1);
			map1 = null;
		}
		HashMap<String, Object> map2;
		data2 = new ArrayList<HashMap<String,Object>>();
		for (int i = 0; i < icons2.length; i++) {
			map2 = new HashMap<String, Object>();
			map2.put("icon", icons2[i]);
			map2.put("title", titles2[i]);
			data2.add(map2);
			map2 = null;
		}
	}

	private void initView() {
		titleLeftBtn = (Button) findViewById(R.id.btn_title_left);
		titleLeftBtn.setVisibility(View.GONE);
		titleRightBtn = (Button) findViewById(R.id.btn_title_right);
		titleRightBtn.setBackgroundResource(R.drawable.toolbar_btn_user);
		titleRightBtn.setOnClickListener(this);
		titleContentView = (TextView) findViewById(R.id.txt_title_content);
		titleContentView.setBackgroundResource(R.drawable.toolbar_logo);
		announcementView = (TextView) findViewById(R.id.txt_announcement);
		
		View view1 = LayoutInflater.from(this).inflate(R.layout.main_pager_gridview1, null);
		gridView1 = (GridView) view1.findViewById(R.id.gridview_main_icon);
		gridView1.setOnItemClickListener(this);
		gridView1.setAdapter(new SimpleAdapter(MainActivity.this, data1,
				R.layout.item_main_gridview, new String[]{"icon", "title"}, 
				new int[]{R.id.image_main_gridview, R.id.txt_main_gridview}));
		View view2 = LayoutInflater.from(this).inflate(R.layout.main_pager_gridview1, null);
		gridView2 = (GridView) view2.findViewById(R.id.gridview_main_icon);
		gridView2.setOnItemClickListener(this);
		gridView2.setAdapter(new SimpleAdapter(MainActivity.this, data2,
				R.layout.item_main_gridview, new String[]{"icon", "title"}, 
				new int[]{R.id.image_main_gridview, R.id.txt_main_gridview}));
		
		views = new ArrayList<View>();
		views.add(view1);
		views.add(view2);
		viewPager = (ViewPager) findViewById(R.id.view_pager);
		viewPager.setAdapter(new ViewPagerAdapter(views));
		viewPager.setCurrentItem(0);
		viewPager.setOnPageChangeListener(this);
		
		indicatorLayout = (PageIndicator) findViewById(R.id.main_page_indicator);
		indicatorLayout.setViewPager(viewPager);
		indicatorLayout.setOnPageChangeListener(this);
	}
	
	private class GetAnnouncementTask extends AsyncTask<Void, Void, List<String>>{

		@Override
		protected List<String> doInBackground(Void... params) {
			String json = HttpPostRequest.getDataFromWebServer(MainActivity.this, "getAnnouncement", null);
			System.out.println("请求通知返回: " + json);
			try {
				if (json == null) {
					return null;
				}
				List<String> list = new ArrayList<String>();
				JSONObject object = new JSONObject(json);
				JSONArray array = object.getJSONArray("list");
				for (int i = 0; i < array.length(); i++) {
					JSONObject jsonObject = array.getJSONObject(i);
					list.add(jsonObject.getString("result"));
				}
				return list;
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onPostExecute(List<String> result) {
			task = null;
			if (null == result) 
				return;
			if (result.size() > 0) {
				announcementView.setText(result.get(0));
			}
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_title_right:
			if (null != application.getUserInfo() && !"-1".equals(application.getUserInfo().getUserId().toString())) {
				Intent intent = new Intent(MainActivity.this, UserCenterActivity.class);
				startActivity(intent);
			} else {
				Intent intent = new Intent(MainActivity.this, LoadActivity.class);
				startActivity(intent);
			}
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent();
		switch (position) {
		case 0:
			if (currentPage == 0) {
				intent.setClass(MainActivity.this, RealTimeDataActivity.class);
			} else {
				intent.setClass(MainActivity.this, InstantNewsActivity.class);
			}
			break;
		case 1:
			if (currentPage == 0) {
				intent.setClass(MainActivity.this, MarketNewsActivity.class);
			} else {
				intent.setClass(MainActivity.this, RateExchangeActivity.class);
			}
			break;
		case 2:
			if (currentPage == 0) {
				intent.setClass(MainActivity.this, EconomicCalendarGroupActivity.class);
			} else {
				intent.setClass(MainActivity.this, AboutActivity.class);
			}
			break;
		case 3:
			intent.setClass(MainActivity.this, ExpertOpinionActivity.class);
			break;
		case 4:
			intent.setClass(MainActivity.this, VirtualAccountActivity.class);
			break;
		case 5:
			intent.setClass(MainActivity.this, ETFActivity.class);
			break;
		case 6:
			intent.setClass(MainActivity.this, AnalysisActivity.class);
			break;
		case 7:
			intent.setClass(MainActivity.this, ServerActivity.class);
			break;
		case 8:
			intent.setClass(MainActivity.this, AccountApplyActivity.class);
			break;
		default:
			break;
		}
		startActivity(intent);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if (task != null) {
			task.cancel(true);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (NetUtil.isNetworkAvailable(this)) {
			if (task != null) task.cancel(true);
			task = new GetAnnouncementTask();
			task.execute();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		application.setInActivity(false);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exitApp();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void exitApp(){
		MsgBox mg = new MsgBox();
		String msg = getString(R.string.exit_app);
		mg.setMessage(msg);
		mg.setPositiveListener(new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				application.exitApp();
				android.os.Process.killProcess(android.os.Process.myPid());
				System.exit(0);
				dialog.dismiss();
			}
		});
		mg.setNegativeListener(new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		mg.show(this);
	}

	/** ViewPager.OnPageChangeListener */
	@Override
	public void onPageScrollStateChanged(int arg0) {}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {}

	@Override
	public void onPageSelected(int position) {
		currentPage = position;
	}
	
}
