package com.innouni.south.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.innouni.south.adapter.EconomicCalendaAdapter;
import com.innouni.south.app.MainApplication;
import com.innouni.south.base.BaseActivity;
import com.innouni.south.entity.EconomicCalendar;
import com.innouni.south.net.HttpPostRequest;
import com.innouni.south.net.ParseJsonToList;
import com.innouni.south.util.CalendaUtil;
import com.innouni.south.util.NetUtil;
import com.innouni.south.widget.PullToRefreshView;
import com.innouni.south.widget.PullToRefreshView.OnHeaderRefreshListener;

/**
 * 财经日历页面
 * 
 * @author HuGuojun
 * @date 2013-11-21 下午4:47:27
 * @modify
 * @version 1.0.0
 */
public class EconomicCalendaActivity extends BaseActivity implements OnClickListener {
	
	private String currentDate, currentWeek;
	private Button calLeft, calCenter, calRight;
	
	private PullToRefreshView refreshView;
	private ListView mListView;
	private RelativeLayout loadFailLayout;
	private ImageView loadFailView;
	
	private EconomicCalendaAdapter adapter;
	private GetEconomicCalendaDataTask task;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_economic_calenda);
		application = MainApplication.getApplication();
		application.setActivity(this);
		adapter = new EconomicCalendaAdapter(this);
		initView();
	}

	private void initView() {
		currentDate = CalendaUtil.GetCurrentDate();
		currentWeek = CalendaUtil.getWeek(currentDate);
				
		calLeft = (Button) findViewById(R.id.btn_calenda_left);
		calRight = (Button) findViewById(R.id.btn_calenda_right);
		calCenter = (Button) findViewById(R.id.btn_calenda_center);
		calCenter.setText(currentDate + " " + currentWeek);
		calLeft.setOnClickListener(this);
		calRight.setOnClickListener(this);
		calCenter.setOnClickListener(this);
		
		refreshView = (PullToRefreshView) findViewById(R.id.refresh_view);
		mListView = (ListView) findViewById(R.id.my_listview);
		mListView.setHeaderDividersEnabled(false);
		mListView.setFooterDividersEnabled(false);
		
		mListView.setAdapter(adapter);
		adapter.setListView(mListView);
		
		loadFailLayout = (RelativeLayout) findViewById(R.id.lay_load_fail);
		loadFailView = (ImageView) findViewById(R.id.imageview_load_fail);
		loadFailView.setOnClickListener(this);
		
		refreshView.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {
			@Override
			public void onHeaderRefresh(PullToRefreshView view) {
				if (NetUtil.isNetworkAvailable(EconomicCalendaActivity.this)) {
					if(null != task) task.cancel(true);
					task = new GetEconomicCalendaDataTask();
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
		case R.id.btn_calenda_left:
			handleLeftBtn();
			break;
		case R.id.btn_calenda_right:
			handleRightBtn();
			break;
		case R.id.btn_calenda_center:
			handleCenterBtn();
			break;
		}
	}
	
	private void handleLeftBtn() {
		currentDate = CalendaUtil.getSpecifiedDayBefore(currentDate);
		currentWeek = CalendaUtil.getWeek(currentDate);
		calCenter.setText(currentDate + " " + currentWeek);
		refreshView.headerRefreshing();
	}
	
	private void handleRightBtn() {
		currentDate = CalendaUtil.getSpecifiedDayAfter(currentDate);
		currentWeek = CalendaUtil.getWeek(currentDate);
		calCenter.setText(currentDate + " " + currentWeek);
		refreshView.headerRefreshing();
	}

	private void handleCenterBtn() {
		Calendar calendar = Calendar.getInstance();
		DatePickerDialog dialog = new DatePickerDialog(EconomicCalendaActivity.this, new OnDateSetListener() {
			
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				currentDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
				currentWeek = CalendaUtil.getWeek(currentDate);
				calCenter.setText(currentDate + " " + currentWeek);
				refreshView.headerRefreshing();
			}
		}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		dialog.show();
	}

	private class GetEconomicCalendaDataTask extends AsyncTask<String, Void, ArrayList<Object>>{
		
		@Override
		protected ArrayList<Object> doInBackground(String... params) {
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("date", currentDate));
			String json = HttpPostRequest.getDataFromWebServer(EconomicCalendaActivity.this, "getEconomicCalenda", pairs);
			System.out.println("获取财经日历返回: " + json);
			ParseJsonToList parser = ParseJsonToList.getInstance();
			return parser.parseWebDataToList(json, EconomicCalendar.class);
		}

		@Override
		protected void onPostExecute(ArrayList<Object> result) {
			task = null;
			if(null != result) {
				mListView.setEnabled(true);
				loadFailLayout.setVisibility(View.GONE);
				if(result.size() > 0) {
					if(null != adapter) {
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
		if(null != task) {
			task.cancel(true);
			refreshView.onHeaderRefreshComplete();
		}
	}
}
