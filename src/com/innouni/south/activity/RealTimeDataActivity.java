package com.innouni.south.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.innouni.south.adapter.AllRealTimeDataAdapter;
import com.innouni.south.app.MainApplication;
import com.innouni.south.base.BaseActivity;
import com.innouni.south.net.HttpPostRequest;
import com.innouni.south.util.NetUtil;
import com.innouni.south.util.ShareUtil;

public class RealTimeDataActivity extends BaseActivity implements OnClickListener {
	
	//按钮
	private TextView goldBtn, chargeBtn, tiantongBtn, yueguiBtn;
	private TextView goldHistoryBtn, usdHistoryBtn;
	
	//列表头部
	private LinearLayout app_list_title_bar_1;
	private LinearLayout app_list_title_bar_2;
	private LinearLayout app_list_title_bar_3;
	
	//列表数据和适配器
	private ListView mListView;
	private List<HashMap<String, String>> mListData;
	private AllRealTimeDataAdapter adapter;
	
	//用于标示实时数据的类别(0到5对应纸金 现货 天通 粤贵 T+D 期货)
	private String[] titles = {"纸金", "现货", "天通", "粤贵", "T+D", "期货"};
	private int position = 1;
	
	private GetRealtimeDataTask getRealtimeDataTask;
	private ShareUtil shareUtil;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_realtime_data);
		application = MainApplication.getApplication();
		application.setActivity(this);
		shareUtil = ShareUtil.getInstance(this);
		shareUtil.setIntValues("type_position", position);
		mListData  = new ArrayList<HashMap<String,String>>();
		initBar();
		initBtn();
		initListTitle();
		initData();
	}

	private void initData() {
		mListView = (ListView) findViewById(R.id.listview_realtime_data);
		adapter = new AllRealTimeDataAdapter(this, mListData, R.layout.item_data_list, 
				new String[] { "name", "now", "start", "high", "low", "range", "percent" }, 
				new int[] { R.id.tvName, R.id.tvNow, R.id.tvStart, R.id.tvHigh, 
							R.id.tvLow, R.id.tvRange, R.id.tvPercent });
		mListView.setAdapter(adapter);
		if (NetUtil.isNetworkAvailable(RealTimeDataActivity.this)) {
			if(getRealtimeDataTask != null) 
				getRealtimeDataTask.cancel(true);
			getRealtimeDataTask = new GetRealtimeDataTask();
			getRealtimeDataTask.execute();
		}
	}

	private void initBar() {
		titleLeftBtn = (Button) findViewById(R.id.btn_title_left);
		titleRightBtn = (Button) findViewById(R.id.btn_title_right);
		titleContentView = (TextView) findViewById(R.id.txt_title_content);
		titleContentView.setText(titles[position]);
		titleRefreshBar = (ProgressBar) findViewById(R.id.progress_title_right);
		titleLeftBtn.setOnClickListener(this);
		titleRightBtn.setOnClickListener(this);
	}

	private void initBtn() {
		goldBtn = (TextView) findViewById(R.id.btn_gold);
		chargeBtn = (TextView) findViewById(R.id.btn_cahrge);
		tiantongBtn = (TextView) findViewById(R.id.btn_tiantong);
		yueguiBtn = (TextView) findViewById(R.id.btn_yuegui);
		usdHistoryBtn = (TextView) findViewById(R.id.btn_gold_usd_history);
		goldHistoryBtn = (TextView) findViewById(R.id.btn_gold_history);
		goldBtn.setOnClickListener(this);
		chargeBtn.setOnClickListener(this);
		tiantongBtn.setOnClickListener(this);
		yueguiBtn.setOnClickListener(this);
		usdHistoryBtn.setOnClickListener(this);
		goldHistoryBtn.setOnClickListener(this);
	}
	
	private void initListTitle() {
		//纸金
		app_list_title_bar_1 = (LinearLayout) findViewById(R.id.app_list_title_bar_1);
		//天通 粤贵
		app_list_title_bar_2 = (LinearLayout) findViewById(R.id.app_list_title_bar_2);
		//现货 期货 T+D
		app_list_title_bar_3 = (LinearLayout) findViewById(R.id.app_list_title_bar_3);
		app_list_title_bar_3.setVisibility(View.VISIBLE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_title_left:
			finish();
			break;
		case R.id.btn_title_right:
			if (NetUtil.isNetworkAvailable(RealTimeDataActivity.this)) {
				if(getRealtimeDataTask != null)
					getRealtimeDataTask.cancel(true);
				getRealtimeDataTask = new GetRealtimeDataTask();
				getRealtimeDataTask.execute();
			}
			break;
		case R.id.btn_gold:
			//现货
			position = 1;
			shareUtil.setIntValues("type_position", position);
			app_list_title_bar_1.setVisibility(View.GONE);
			app_list_title_bar_2.setVisibility(View.GONE);
			app_list_title_bar_3.setVisibility(View.VISIBLE);
			if (NetUtil.isNetworkAvailable(RealTimeDataActivity.this)) {
				if(getRealtimeDataTask != null)
					getRealtimeDataTask.cancel(true);
				getRealtimeDataTask = new GetRealtimeDataTask();
				getRealtimeDataTask.execute();
			}
			break;
		case R.id.btn_cahrge:
			break;
		case R.id.btn_tiantong:
			position = 2;
			shareUtil.setIntValues("type_position", position);
			app_list_title_bar_1.setVisibility(View.GONE);
			app_list_title_bar_2.setVisibility(View.VISIBLE);
			app_list_title_bar_3.setVisibility(View.GONE);
			if (NetUtil.isNetworkAvailable(RealTimeDataActivity.this)) {
				if(getRealtimeDataTask != null)
					getRealtimeDataTask.cancel(true);
				getRealtimeDataTask = new GetRealtimeDataTask();
				getRealtimeDataTask.execute();
			}
			break;
		case R.id.btn_yuegui:
			position = 3;
			shareUtil.setIntValues("type_position", position);
			app_list_title_bar_1.setVisibility(View.GONE);
			app_list_title_bar_2.setVisibility(View.VISIBLE);
			app_list_title_bar_3.setVisibility(View.GONE);
			if (NetUtil.isNetworkAvailable(RealTimeDataActivity.this)) {
				if(getRealtimeDataTask != null)
					getRealtimeDataTask.cancel(true);
				getRealtimeDataTask = new GetRealtimeDataTask();
				getRealtimeDataTask.execute();
			}
			break;
		case R.id.btn_gold_usd_history:
			break;
		case R.id.btn_gold_history:
			break;
		}
	}
	
	private class GetRealtimeDataTask extends AsyncTask<String, Void, String>{
    	
    	@Override
		protected void onPreExecute() {
    		mListView.setEnabled(false);
			titleRightBtn.setVisibility(View.GONE);
			titleRefreshBar.setVisibility(View.VISIBLE);
		}
    	
		@Override
		protected String doInBackground(String... params) {
			switch (position) {
			//case 0: getPaperData(); break;//获取纸金
			//case 4: getTDData(); break;//获取T+D
			//case 5: getQHData();break;//获取期货
			case 1: getSpotData(); break;//获取现货
			case 2: getTtjData(); break;//获取天通
			case 3: getYgyData(); break;//获取粤贵
			
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			titleContentView.setText(titles[position]);
			getRealtimeDataTask = null;
			mListView.setEnabled(true);
			titleRightBtn.setVisibility(View.VISIBLE);
			titleRefreshBar.setVisibility(View.GONE);
			if(mListData.size() != 0){
				adapter.notifyDataSetChanged();
			} 
		}
	}
	
	//获取粤贵
    private void getYgyData() {
    	String getstr = HttpPostRequest.getDataFromWebServer(this, "http://zhj8.sinaapp.com/Mobi/Data/ygy");
    	try {
	    	mListData.clear();
	    	HashMap<String, String> map;
	    	String[] strArray = getstr.split("@");
	    	String[] arrayOfString;
	    	String str2,str3,str4,str5,str6,str7,str8;
	    	for (int i = 0; i < strArray.length; i++) {
	    		map =  new HashMap<String, String>();
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
    	} catch (Exception e) {}
    }
    
    //获取天通
    private void getTtjData(){
    	String getstr = HttpPostRequest.getDataFromWebServer(this, "http://zhj8.sinaapp.com/Mobi/Data/ttj");
    	try {
	    	mListData.clear();
	    	HashMap<String, String> map;
	    	String[] strArray = getstr.split("@");
	    	String[] arrayOfString;
	    	String str2,str3,str4,str5,str6,str7,str8;
	    	for (int i = 0; i < strArray.length; i++) {
	    		map =  new HashMap<String, String>();
	    		arrayOfString = strArray[i].split(",");
	    		str2 = arrayOfString[6];
	            str3 = arrayOfString[0];
	            str4 = arrayOfString[1];
	            str5 = arrayOfString[2];
	            str6 = arrayOfString[4];
	            str7 = arrayOfString[5];
	            str8 = formatFloat(100.0F * Float.valueOf(str6).floatValue() / Float.valueOf(str3).floatValue()) + "%";
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
    	} catch (Exception e) {}
    }
    
    //获取现货
    private void getSpotData() {
    	String getstr = HttpPostRequest.getDataFromWebServer(this, "http://zhj8.sinaapp.com/Mobi/Data/spot");
    	mListData.clear();
    	try {
			JSONObject localJSONObject = new JSONObject(getstr);
			String str2 = localJSONObject.getString("1");
		    if (str2.length() > 0)
		        setSpotList(str2, "黄金");
		    String str3 = localJSONObject.getString("3");
		    if (str3.length() > 0)
		        setSpotList(str3, "白银");
		    String str4 = localJSONObject.getString("4");
		    if (str4.length() > 0)
		        setSpotList(str4, "铂金");
		    String str5 = localJSONObject.getString("5");
		    if (str5.length() > 0)
		        setSpotList(str5, "钯金");
		    String str6 = localJSONObject.getString("2");
		    if (str6.length() > 0)
		        setSpotList(str6, "港金");
		} catch (JSONException e) {}
    }
    
    private void setSpotList(String data, String name){
    	try {
    		HashMap<String, String> map;
    		map =  new HashMap<String, String>();
    		String[] arrayOfString = data.split(",");
    		String str1 = formatMoney_ex2(arrayOfString[0]);
    	    String str2 = formatMoney_ex2(arrayOfString[1]);
    	    String str3 = formatMoney_ex2(arrayOfString[2]);
    	    String str4 = formatMoney_ex2(arrayOfString[3]);
    	    String str5 = "0";
    	    String str6 = "0%";
    	    if (!str1.equals("0")){
    	        float f = Float.parseFloat(str1) - Float.parseFloat(str2);
    	        str5 = formatFloat(f);
    	        str6 = formatFloat(100.0F * (f / Float.parseFloat(str2))) + "%";
    	    }
    	    map.put("name", name);
    	    map.put("now", str1);
    	    map.put("start", str2);
    	    map.put("high", str3);
    	    map.put("low", str4);
    	    map.put("range", str5);
    	    map.put("percent", str6);
    	    mListData.add(map);
    	} catch (Exception e) {}
    }
    
    public String formatFloat(float paramFloat) {
	    DecimalFormat localDecimalFormat = new DecimalFormat("########.00");
	    String localObject = "0";
	    double d = paramFloat;
	    try {
	    	localObject = String.valueOf(Float.parseFloat(localDecimalFormat.format(d)));
	    	String[] arrayOfString = ((String)localObject).split("\\.");
	    	if (arrayOfString.length == 1) {
	    		localObject = localObject + ".00";
	    	} else if (arrayOfString[1].length() == 1) {
	    		String str = localObject + "0";
	    		localObject = str;
	    	}
	    } catch (Exception localException) {}
	    return localObject;
	}
    
    public String formatMoney_ex2(String paramString) {
		String localObject = "0";
	    try {
	    	float f = Float.parseFloat(paramString);
	    	localObject = String.valueOf(Float.parseFloat(new DecimalFormat("########.00").format(f)));
	    	String[] arrayOfString = ((String)localObject).split("\\.");
	    	if (arrayOfString.length == 1) {
	    		localObject = localObject + ".00";
	    	} else if (arrayOfString[1].length() == 1) {
	    		String str = localObject + "0";
	    		localObject = str;
	    	}
	    } catch (Exception localException) {}
	    return localObject;
	}
    
}
