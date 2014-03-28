package com.innouni.south.fragment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.innouni.south.activity.HistoryImageActivity;
import com.innouni.south.activity.R;
import com.innouni.south.activity.StockChartsActivity;
import com.innouni.south.adapter.AllRealTimeDataTopAdapter;
import com.innouni.south.net.HttpPostRequest;
import com.innouni.south.util.NetUtil;
import com.innouni.south.util.ShareUtil;

/**
 * 综合报价上半部分(外黄金,外汇)
 * 
 * @author HuGuojun
 * @date 2013-12-24 下午8:00:43
 * @modify
 * @version 1.0.0
 */
public class RealTimeFragmentTop extends Fragment implements OnClickListener,
		OnItemClickListener {

	private String mChartCode, mChartName;
	private String[] XHChartName = { "黄金", "白银", "铂金", "钯金", "港金" };
	private String[] SpotChartName = { "现货黄金", "现货白银", "现货铂金", "现货钯金", "现货港金" };
	private String[] SpotChartCode = { "XHAU", "XHAG", "XHPT", "XHPD", "XHHAU" };
	private String[] TtjChartName = { "天通铂金", "天通钯金", "天通白银", "天通镍" };
	private String[] TtjChartCode = { "TTPT", "TTPD", "TTAG", "TTNI" };
	private String[] YgyChartName = { "粤贵银9999", "粤贵银9995", "粤贵银100G" };
	private String[] YgyChartCode = { "YGAG99", "YGAG95", "YGAG10" };

	// 按钮
	private TextView goldBtn, chargeBtn, tiantongBtn, yueguiBtn, usdHistoryBtn;

	// 列表头部
	private LinearLayout app_list_title_bar_1;
	private LinearLayout app_list_title_bar_2;
	private LinearLayout app_list_title_bar_3;

	// 列表数据和适配器
	private ListView mListView;
	private List<HashMap<String, String>> mListData;
	private AllRealTimeDataTopAdapter adapter;

	private int position = 1; // 默认外黄金

	private GetRealtimeDataTask getRealtimeDataTask;
	private ShareUtil shareUtil;
	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_realtime_data_top, container,
				false);
		shareUtil = ShareUtil.getInstance(getActivity());
		shareUtil.setIntValues("type_position", position);
		mListData = new ArrayList<HashMap<String, String>>();
		initBtn();
		initListTitle();
		initData();
		return view;
	}

	private void initData() {
		mListView = (ListView) view.findViewById(R.id.listview_realtime_data);
		adapter = new AllRealTimeDataTopAdapter(getActivity(), mListData,
				R.layout.item_data_list, new String[] { "name", "now", "start",
						"high", "low", "range", "percent" }, new int[] {
						R.id.tvName, R.id.tvNow, R.id.tvStart, R.id.tvHigh,
						R.id.tvLow, R.id.tvRange, R.id.tvPercent });
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(this);
		getData();
	}

	public void getData() {
		if (NetUtil.isNetworkAvailable(getActivity())) {
			if (getRealtimeDataTask != null)
				getRealtimeDataTask.cancel(true);
			getRealtimeDataTask = new GetRealtimeDataTask();
			getRealtimeDataTask.execute();
		}
	}

	private void initBtn() {
		goldBtn = (TextView) view.findViewById(R.id.btn_gold);
		chargeBtn = (TextView) view.findViewById(R.id.btn_cahrge);
		tiantongBtn = (TextView) view.findViewById(R.id.btn_tiantong);
		yueguiBtn = (TextView) view.findViewById(R.id.btn_yuegui);
		usdHistoryBtn = (TextView) view.findViewById(R.id.btn_gold_usd_history);
		goldBtn.setOnClickListener(this);
		chargeBtn.setOnClickListener(this);
		tiantongBtn.setOnClickListener(this);
		yueguiBtn.setOnClickListener(this);
		usdHistoryBtn.setOnClickListener(this);
	}

	private void initListTitle() {
		// 纸金
		app_list_title_bar_1 = (LinearLayout) view
				.findViewById(R.id.app_list_title_bar_1);
		// 天通 粤贵
		app_list_title_bar_2 = (LinearLayout) view
				.findViewById(R.id.app_list_title_bar_2);
		// 现货 期货 T+D
		app_list_title_bar_3 = (LinearLayout) view
				.findViewById(R.id.app_list_title_bar_3);
		app_list_title_bar_3.setVisibility(View.VISIBLE);
	}

	/**
	 * 固定Listview的高度,解决与ScrollView的冲突
	 * 
	 * @description setListViewHeightBasedOnChildren
	 * @param listView
	 */
	public void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_gold:
			goldBtn.setBackgroundResource(R.drawable.title_btn_bg_orange_pressed);
			chargeBtn
					.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
			tiantongBtn
					.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
			yueguiBtn
					.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
			usdHistoryBtn
					.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
			// 现货
			position = 1;
			shareUtil.setIntValues("type_position", position);
			app_list_title_bar_1.setVisibility(View.GONE);
			app_list_title_bar_2.setVisibility(View.GONE);
			app_list_title_bar_3.setVisibility(View.VISIBLE);
			getData();
			break;
		case R.id.btn_cahrge:
			chargeBtn
					.setBackgroundResource(R.drawable.title_btn_bg_orange_pressed);
			goldBtn.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
			tiantongBtn
					.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
			yueguiBtn
					.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
			usdHistoryBtn
					.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
			position = 6;
			shareUtil.setIntValues("type_position", position);
			app_list_title_bar_1.setVisibility(View.GONE);
			app_list_title_bar_2.setVisibility(View.GONE);
			app_list_title_bar_3.setVisibility(View.VISIBLE);
			getData();
			break;
		case R.id.btn_tiantong:
			tiantongBtn
					.setBackgroundResource(R.drawable.title_btn_bg_orange_pressed);
			chargeBtn
					.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
			goldBtn.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
			yueguiBtn
					.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
			usdHistoryBtn
					.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
			position = 2;
			shareUtil.setIntValues("type_position", position);
			app_list_title_bar_1.setVisibility(View.GONE);
			app_list_title_bar_2.setVisibility(View.VISIBLE);
			app_list_title_bar_3.setVisibility(View.GONE);
			getData();
			break;
		case R.id.btn_yuegui:
			yueguiBtn
					.setBackgroundResource(R.drawable.title_btn_bg_orange_pressed);
			chargeBtn
					.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
			tiantongBtn
					.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
			goldBtn.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
			usdHistoryBtn
					.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
			position = 3;
			shareUtil.setIntValues("type_position", position);
			app_list_title_bar_1.setVisibility(View.GONE);
			app_list_title_bar_2.setVisibility(View.VISIBLE);
			app_list_title_bar_3.setVisibility(View.GONE);
			getData();
			break;
		case R.id.btn_gold_usd_history:
			usdHistoryBtn
					.setBackgroundResource(R.drawable.title_btn_bg_orange_pressed);
			chargeBtn
					.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
			tiantongBtn
					.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
			yueguiBtn
					.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
			goldBtn.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
			Intent intent = new Intent(getActivity(),
					HistoryImageActivity.class);
			startActivity(intent);
			break;
		}
	}

	private class GetRealtimeDataTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			mListView.setEnabled(false);
		}

		@Override
		protected String doInBackground(String... params) {
			switch (position) {
			// case 0: getPaperData(); break; //获取纸金
			// case 4: getTDData(); break; //获取T+D
			// case 5: getQHData();break; //获取期货
			case 1:
				getSpotData();
				break; // 获取现货
			case 2:
				getTtjData();
				break; // 获取天通
			case 3:
				getYgyData();
				break; // 获取粤贵
			case 6:
				getMeizhi();
				break; // 获取美元指数
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			getRealtimeDataTask = null;
			mListView.setEnabled(true);
			if (mListData.size() != 0) {
				adapter.notifyDataSetChanged();
			}
			setListViewHeightBasedOnChildren(mListView);
		}
	}

	// 获取美元指数
	private void getMeizhi() {
		String getstr = HttpPostRequest.getDataFromWebServer(getActivity(),
				"http://zhj8.sinaapp.com/Mobi/Data/base");
		try {
			mListData.clear();
			HashMap<String, String> map = new HashMap<String, String>();
			String[] strArray = getstr.split("@");
			String[] arrayOfString;
			String str1, str2, str3, str4, str5, str6;
			arrayOfString = strArray[strArray.length - 1].split(",");

			str1 = formatFloat(Float.valueOf(arrayOfString[0]));
			str2 = formatFloat(Float.valueOf(arrayOfString[1]));
			str3 = formatFloat(Float.valueOf(arrayOfString[2]));
			str4 = formatFloat(Float.valueOf(arrayOfString[3]));
			str5 = String.valueOf(formatFloat(Float.valueOf(str1)
					- Float.valueOf(str2)));
			str6 = String.valueOf(100 * Float.valueOf(formatFloat((Float
					.valueOf(str5) / Float.valueOf(str2))))) + "%";
			map.put("name", "美指");
			map.put("now", str1);
			map.put("start", str2);
			map.put("high", str3);
			map.put("low", str4);
			map.put("range", str5);
			map.put("percent", str6);
			mListData.add(map);
		} catch (Exception e) {
		}
	}

	// 获取粤贵数据
	private void getYgyData() {
		String getstr = HttpPostRequest.getDataFromWebServer(getActivity(),
				"http://zhj8.sinaapp.com/Mobi/Data/ygy");
		try {
			mListData.clear();
			HashMap<String, String> map;
			String[] strArray = getstr.split("@");
			String[] arrayOfString;
			String str2, str3, str4, str5, str6, str7, str8;
			for (int i = 0; i < strArray.length; i++) {
				map = new HashMap<String, String>();
				arrayOfString = strArray[i].split(",");

				str2 = arrayOfString[0];
				str3 = arrayOfString[7];
				str4 = arrayOfString[1];
				str5 = arrayOfString[3];
				str6 = arrayOfString[4];
				str7 = arrayOfString[5];
				str8 = arrayOfString[6];
				map.put("name", str2);
				map.put("now", str4);
				map.put("start", str3);
				map.put("high", str5);
				map.put("low", str6);
				map.put("range", str7);
				map.put("percent", str8);
				mListData.add(map);
			}
		} catch (Exception e) {
		}
	}

	// 获取天通数据
	private void getTtjData() {
		String getstr = HttpPostRequest.getDataFromWebServer(getActivity(),
				"http://zhj8.sinaapp.com/Mobi/Data/ttj");
		try {
			mListData.clear();
			HashMap<String, String> map;
			String[] strArray = getstr.split("@");
			String[] arrayOfString;
			String str2, str3, str4, str5, str6, str7, str8;
			for (int i = 0; i < strArray.length; i++) {
				map = new HashMap<String, String>();
				arrayOfString = strArray[i].split(",");
				str2 = arrayOfString[6];
				str3 = arrayOfString[0];
				str4 = arrayOfString[1];
				str5 = arrayOfString[2];
				str6 = arrayOfString[4];
				str7 = arrayOfString[5];
				str8 = formatFloat(100.0F * Float.valueOf(str6).floatValue()
						/ Float.valueOf(str3).floatValue())
						+ "%";
				if (!str7.equals("银")) {
					str2 = formatMoney_ex2(arrayOfString[6]);
					str3 = formatMoney_ex2(arrayOfString[0]);
					str4 = formatMoney_ex2(arrayOfString[1]);
					str5 = formatMoney_ex2(arrayOfString[2]);
					str6 = formatMoney_ex2(arrayOfString[4]);
				}
				map.put("name", str7);
				map.put("now", str3);
				map.put("high", str4);
				map.put("low", str5);
				map.put("start", str2);
				map.put("range", str6);
				map.put("percent", str8);
				mListData.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 获取现货数据
	// http://apphome.sinaapp.com/dc/A/Api/tdata?os=android&tid=XHHAU&apitoken=
	private void getSpotData() {
		mListData.clear();
		HashMap<String, String> map;
		for (int i = 0; i < SpotChartCode.length; i++) {
			map = new HashMap<String, String>();
			String mUrl = "http://apphome.sinaapp.com/dc/A/Api/tdata?os=android&tid="
					+ SpotChartCode[i] + "&apitoken=";
			String json = HttpPostRequest.getDataFromWebServer(getActivity(),
					mUrl);
			String[] strAarray = json.split(",");
			map.put("name", XHChartName[i]);
			map.put("now", strAarray[3]);
			map.put("start", strAarray[4]); // 今开
			map.put("high", strAarray[5]);
			map.put("low", strAarray[6]);
			map.put("range", strAarray[7]);
			map.put("percent", strAarray[8]);
			mListData.add(map);
		}
	}

	public String formatFloat(float paramFloat) {
		DecimalFormat localDecimalFormat = new DecimalFormat("########.00");
		String localObject = "0";
		double d = paramFloat;
		try {
			localObject = String.valueOf(Float.parseFloat(localDecimalFormat
					.format(d)));
			String[] arrayOfString = ((String) localObject).split("\\.");
			if (arrayOfString.length == 1) {
				localObject = localObject + ".00";
			} else if (arrayOfString[1].length() == 1) {
				String str = localObject + "0";
				localObject = str;
			}
		} catch (Exception localException) {
		}
		return localObject;
	}

	public String formatMoney_ex2(String paramString) {
		String localObject = "0";
		try {
			float f = Float.parseFloat(paramString);
			localObject = String.valueOf(Float.parseFloat(new DecimalFormat(
					"########.00").format(f)));
			String[] arrayOfString = ((String) localObject).split("\\.");
			if (arrayOfString.length == 1) {
				localObject = localObject + ".00";
			} else if (arrayOfString[1].length() == 1) {
				String str = localObject + "0";
				localObject = str;
			}
		} catch (Exception localException) {
		}
		return localObject;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int index, long id) {
		switch (position) {
		// case 0: setIntentPutExtra_Paper(index); break; //纸金
		// case 4: setIntentPutExtra_TD(index); break; //T+D
		// case 5: setIntentPutExtra_QH(index);break; //期货
		case 1:
			setIntentPutExtra_Spot(index);
			break; // 现货
		case 2:
			setIntentPutExtra_Ttj(index);
			break; // 天通
		case 3:
			setIntentPutExtra_Ygy(index);
			break; // 粤贵
		case 6:
			setIntentPutExtra_Meizhi(index);
			break; // 美元指数
		}
	}

	// 现货K线(外黄金)
	private void setIntentPutExtra_Spot(int index) {
		mChartName = SpotChartName[index];
		mChartCode = SpotChartCode[index];
		toIntentStockChart();
	}

	// 美元指数
	private void setIntentPutExtra_Meizhi(int index) {
		mChartName = "美元指数";
		mChartCode = "ZSUSD";
		toIntentStockChart();
	}

	// 天通K线
	private void setIntentPutExtra_Ttj(int index) {
		mChartName = TtjChartName[index];
		mChartCode = TtjChartCode[index];
		toIntentStockChart();
	}

	// 粤贵K线
	private void setIntentPutExtra_Ygy(int index) {
		if (index < 3) {
			mChartName = YgyChartName[index];
			mChartCode = YgyChartCode[index];
			toIntentStockChart();
		}
	}

	/** 跳转到K线图页面 */
	private void toIntentStockChart() {
		Intent intent = new Intent(getActivity(), StockChartsActivity.class);
		intent.putExtra("ChartCode", mChartCode);
		intent.putExtra("ChartName", mChartName);
		startActivity(intent);
	}

	@Override
	public void onPause() {
		super.onPause();
		if (getRealtimeDataTask != null) {
			getRealtimeDataTask.cancel(true);
		}
	}

}
