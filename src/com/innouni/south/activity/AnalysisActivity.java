package com.innouni.south.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.innouni.south.adapter.AvgIndexAdapter;
import com.innouni.south.adapter.PivotAdapter;
import com.innouni.south.adapter.TechIndexAdapter;
import com.innouni.south.app.MainApplication;
import com.innouni.south.base.BaseActivity;
import com.innouni.south.entity.AnalyseIndex;
import com.innouni.south.entity.PivotIndex;
import com.innouni.south.net.HttpPostRequest;

/**
 * 技术分析页面
 * 
 * @author HuGuojun
 * @date 2013-12-2 上午11:54:47
 * @modify
 * @version 1.0.0
 */
public class AnalysisActivity extends BaseActivity implements OnClickListener {
	
	private String URL = "";
	private String[] periods = {"300", "900", "1800", "3600", "18000", "86400"};
	private String[] types = {"gold", "silver", "usd"};
	private String type = types[0];
	private String period = periods[0];
	
	private ListView pivotListView, avgListView, techListView;
	private List<Object> list1; //pivotList
	private List<Object> list2; //avgList
	private List<Object> list3; //techList
	
	private GetDataTask task;
	private PivotAdapter pivotAdapter;
	private TechIndexAdapter techIndexAdapter;
	private AvgIndexAdapter avgIndexAdapter;
	
	private Button goldButton, silverButton, dollarButton;
	private Button techButton, avgButton;
	private Button button1, button2, button3, button4, button5, button6;
	
	private TextView nameView, priceView, timeView, suggestView;
	private TextView avgBuyView, avgSellView, techBuyView, techSellView;
	private String name, price, time;
	private String suggest; 
	private int avgBuy, avgSell;
	private int techBuy, techSell;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_analysis);
		application = MainApplication.getApplication();
		application.setActivity(this);
		initView();
		initListView();
		getData();
	}

	private void getData() {
		if (null != task) 
			task.cancel(true);
		task = new GetDataTask();
		task.execute();
	}

	private void initView() {
		list1 = new ArrayList<Object>();
		list2 = new ArrayList<Object>();
		list3 = new ArrayList<Object>();
		
		titleLeftBtn = (Button) findViewById(R.id.btn_title_left);
		titleRightBtn = (Button) findViewById(R.id.btn_title_right);
		titleContentView = (TextView) findViewById(R.id.txt_title_content);
		titleRefreshBar = (ProgressBar) findViewById(R.id.progress_title_right);
		titleLeftBtn.setOnClickListener(this);
		titleRightBtn.setOnClickListener(this);
		titleContentView.setText("技术分析");
		
		goldButton = (Button) findViewById(R.id.btn_analyse_gold);
		silverButton = (Button) findViewById(R.id.btn_analyse_silver);
		dollarButton = (Button) findViewById(R.id.btn_analyse_dollar);
		techButton = (Button) findViewById(R.id.btn_analyse_tech);
		avgButton = (Button) findViewById(R.id.btn_analyse_avg);
		goldButton.setOnClickListener(this);
		silverButton.setOnClickListener(this);
		dollarButton.setOnClickListener(this);
		techButton.setOnClickListener(this);
		avgButton.setOnClickListener(this);
		
		button1 = (Button) findViewById(R.id.btn_5m);
		button2 = (Button) findViewById(R.id.btn_15m);
		button3 = (Button) findViewById(R.id.btn_30m);
		button4 = (Button) findViewById(R.id.btn_60m);
		button5 = (Button) findViewById(R.id.btn_300m);
		button6 = (Button) findViewById(R.id.btn_1440m);
		button1.setOnClickListener(this);
		button2.setOnClickListener(this);
		button3.setOnClickListener(this);
		button4.setOnClickListener(this);
		button5.setOnClickListener(this);
		button6.setOnClickListener(this);
		
		nameView = (TextView) findViewById(R.id.txt_analyse_name);
		priceView = (TextView) findViewById(R.id.txt_analyse_price);
		timeView = (TextView) findViewById(R.id.txt_analyse_time);
		suggestView = (TextView) findViewById(R.id.txt_analyse_jianyi);
		avgBuyView = (TextView) findViewById(R.id.txt_analyse_move_index_buy);
		avgSellView = (TextView) findViewById(R.id.txt_analyse_move_index_sell);
		techBuyView = (TextView) findViewById(R.id.txt_analyse_tec_index_buy);
		techSellView = (TextView) findViewById(R.id.txt_analyse_tec_index_sell);
	}

	private void initListView() {
		pivotListView = (ListView) findViewById(R.id.listview_pivot);
		View headerView1 = LayoutInflater.from(this).inflate(R.layout.analyse_pivot_header, null);
		pivotListView.addHeaderView(headerView1);
		pivotAdapter = new PivotAdapter(this);
		pivotListView.setAdapter(pivotAdapter);
		
		techListView = (ListView) findViewById(R.id.listview_tech);
		View headerView2 = LayoutInflater.from(this).inflate(R.layout.analyse_tech_header, null);
		techListView.addHeaderView(headerView2);
		techIndexAdapter = new TechIndexAdapter(this);
		techListView.setAdapter(techIndexAdapter);
		
		avgListView = (ListView) findViewById(R.id.listview_avg);
		View headerView3 = LayoutInflater.from(this).inflate(R.layout.analyse_avg_header, null);
		avgListView.addHeaderView(headerView3);
		avgIndexAdapter = new AvgIndexAdapter(this);
		avgListView.setAdapter(avgIndexAdapter);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_title_left:
			finish();
			break;
		case R.id.btn_title_right:
			getData();
			break;
		case R.id.btn_5m:
			changeBtnBg(0);
			period = periods[0];
			getData();
			break;
		case R.id.btn_15m:
			changeBtnBg(1);
			period = periods[1];
			getData();
			break;
		case R.id.btn_30m:
			changeBtnBg(2);
			period = periods[2];
			getData();
			break;
		case R.id.btn_60m:
			changeBtnBg(3);
			period = periods[3];
			getData();
			break;
		case R.id.btn_300m:
			changeBtnBg(4);
			period = periods[4];
			getData();
			break;
		case R.id.btn_1440m:
			changeBtnBg(5);
			period = periods[5];
			getData();
			break;
		case R.id.btn_analyse_gold:
			goldButton.setBackgroundResource(R.drawable.title_btn_bg_orange_pressed);
			silverButton.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
			dollarButton.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
			type = types[0];
			getData();
			break;
		case R.id.btn_analyse_silver:
			goldButton.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
			silverButton.setBackgroundResource(R.drawable.title_btn_bg_orange_pressed);
			dollarButton.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
			type = types[1];
			getData();
			break;
		case R.id.btn_analyse_dollar:
			goldButton.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
			silverButton.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
			dollarButton.setBackgroundResource(R.drawable.title_btn_bg_orange_pressed);
			type = types[2];
			getData();
			break;
		case R.id.btn_analyse_tech:
			techButton.setBackgroundResource(R.drawable.title_btn_bg_orange_pressed);
			avgButton.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
			techListView.setVisibility(View.VISIBLE);
			avgListView.setVisibility(View.GONE);
			break;
		case R.id.btn_analyse_avg:
			techButton.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
			avgButton.setBackgroundResource(R.drawable.title_btn_bg_orange_pressed);
			techListView.setVisibility(View.GONE);
			avgListView.setVisibility(View.VISIBLE);
			break;
		}
	}
	
	private class GetDataTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			titleRightBtn.setVisibility(View.GONE);
			titleRefreshBar.setVisibility(View.VISIBLE);
			URL = "http://i.sojex.net/FinanceQuoteServer/client.action?" +
					"rtp=TechAnalysis&type="+type+"&period="+period;
			avgBuy = 0;
			avgSell = 0;
			techBuy = 0;
			techSell = 0;
			list1.clear();
			list2.clear();
			list3.clear();
		}

		@Override
		protected Void doInBackground(Void... params) {
			String json = HttpPostRequest.getDataFromWebServerByGet(URL);
			if (json == null || json.equals("net_err")) {
				return null;
			} else {
				try {
					JSONObject object = new JSONObject(json);
					if (1000 != object.optInt("status")) {
						return null;
					}
					JSONObject quotes = object.optJSONObject("data").optJSONObject("quotes");
					name = quotes.optString("name");
					time = quotes.optString("time");
					price = quotes.optDouble("sell")+"";
					JSONObject tech = object.optJSONObject("data").optJSONObject("tech");
					suggest = tech.optString("suggest");
					JSONArray pivotArray = tech.optJSONObject("pivotIndex").optJSONArray("pivotList");
					for (int i = 0; i < pivotArray.length(); i++) {
						JSONObject pivotoObject = pivotArray.optJSONObject(i);
						PivotIndex pivotIndex = new PivotIndex();
						pivotIndex.setR1(pivotoObject.optString("r1"));
						pivotIndex.setR2(pivotoObject.optString("r2"));
						pivotIndex.setR3(pivotoObject.optString("r3"));
						pivotIndex.setS1(pivotoObject.optString("s1"));
						pivotIndex.setS2(pivotoObject.optString("s2"));
						pivotIndex.setS3(pivotoObject.optString("s3"));
						pivotIndex.setSr(pivotoObject.optString("sr"));
						list1.add(pivotIndex);
					}
					JSONArray avgArray = tech.optJSONObject("avgIndex").optJSONArray("avgList");
					for (int i = 0; i < avgArray.length(); i++) {
						JSONObject avgObject = avgArray.optJSONObject(i);
						AnalyseIndex analyseIndex = new AnalyseIndex();
						analyseIndex.setName(avgObject.optString("name"));
						analyseIndex.setPrice1(avgObject.optString("price1"));
						analyseIndex.setPrice2(avgObject.optString("price2"));
						analyseIndex.setSuggest1(avgObject.optString("suggest1"));
						analyseIndex.setSuggest2(avgObject.optString("suggest2"));
						list2.add(analyseIndex);
						if (avgObject.optString("suggest1").equals("购买")) {
							avgBuy++;
						} else if (avgObject.optString("suggest1").equals("出售")) {
							avgSell++;
						}
						if (avgObject.optString("suggest2").equals("购买")) {
							avgBuy++;
						} else if (avgObject.optString("suggest2").equals("出售")) {
							avgSell++;
						}
					}
					JSONArray techArray = tech.optJSONObject("techIndex").optJSONArray("techList");
					for (int i = 0; i < techArray.length(); i++) {
						JSONObject techObject = techArray.optJSONObject(i);
						AnalyseIndex analyseIndex = new AnalyseIndex();
						analyseIndex.setName(techObject.optString("name"));
						analyseIndex.setPrice1(techObject.optString("price1"));
						analyseIndex.setPrice2(techObject.optString("price2"));
						analyseIndex.setSuggest1(techObject.optString("suggest1"));
						analyseIndex.setSuggest2(techObject.optString("suggest2"));
						list3.add(analyseIndex);
						if (techObject.optString("suggest1").equals("购买")) {
							techBuy++;
						} else if (techObject.optString("suggest1").equals("出售")) {
							techSell++;
						}
					}
				} catch (Exception e) {
					
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			task = null;
			titleRightBtn.setVisibility(View.VISIBLE);
			titleRefreshBar.setVisibility(View.GONE);
			nameView.setText(name);
			priceView.setText(price);
			timeView.setText(time);
			suggestView.setText(suggest);
			if (suggest.equals("负面") ||suggest.equals("出售")) {
				suggestView.setBackgroundColor(Color.GREEN);
			} else if (suggest.equals("正面") || suggest.equals("购买")) {
				suggestView.setBackgroundColor(Color.RED);
			}
			avgBuyView.setText("购买("+avgBuy+")");
			avgSellView.setText("出售("+avgSell+")");
			techBuyView.setText("购买("+techBuy+")");
			techSellView.setText("出售("+techSell+")");
			if (pivotAdapter.getCount() > 0) {
				pivotAdapter.clear();
			}
			pivotAdapter.setList(list1, true);
			if (avgIndexAdapter.getCount() > 0) {
				avgIndexAdapter.clear();
			}
			avgIndexAdapter.setList(list2, true);
			if (techIndexAdapter.getCount() > 0) {
				techIndexAdapter.clear();
			}
			techIndexAdapter.setList(list3, true);
		}
		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		titleRightBtn.setVisibility(View.VISIBLE);
		titleRefreshBar.setVisibility(View.GONE);
		if (null != task) {
			task.cancel(true);
		}
	}
	
	private void changeBtnBg(int index) {
		Button array[] = {button1, button2, button3 ,button4 ,button5 ,button6};
		for (int i = 0; i < array.length; i++) {
			if (index == i) {
				array[i].setBackgroundResource(R.drawable.title_btn_bg_orange_pressed);
			} else {
				array[i].setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
			}
		}
	}
}

//http://i.sojex.net/FinanceQuoteServer/client.action?rtp=TechAnalysis&type=gold&period=3600