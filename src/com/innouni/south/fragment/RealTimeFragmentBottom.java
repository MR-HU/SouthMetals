package com.innouni.south.fragment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.ListView;
import android.widget.TextView;

import com.innouni.south.activity.HistoryImageActivity;
import com.innouni.south.activity.R;
import com.innouni.south.activity.StockChartsActivity;
import com.innouni.south.adapter.AllRealTimeDataAdapter;
import com.innouni.south.net.HttpPostRequest;
import com.innouni.south.util.NetUtil;
import com.innouni.south.util.ShareUtil;

/**
 * �ۺϱ����°벿��(��ͨ Խ��)
 * 
 * @author HuGuojun
 * @date 2013-12-24 ����8:00:43
 * @modify
 * @version 1.0.0
 */
public class RealTimeFragmentBottom extends Fragment implements OnClickListener, OnItemClickListener {

	private String mChartCode, mChartName;
	private String[] SpotChartName = {"�ֻ��ƽ�", "�ֻ�����", "�ֻ�����", "�ֻ��ٽ�"};
	private String[] SpotChartCode = {"XAUUSD", "XAGUSD", "XPTUSD", "XPDUSD"};
	private String[] TtjChartName = {"��ͨ����", "��ͨ�ٽ�", "��ͨ����"};
	private String[] TtjChartCode = {"XPT", "XPD", "XAG"};
	
	//��ť
	private TextView goldBtn, chargeBtn, tiantongBtn, yueguiBtn, usdHistoryBtn;
	
	//�б�ͷ��
	private LinearLayout app_list_title_bar_1;
	private LinearLayout app_list_title_bar_2;
	private LinearLayout app_list_title_bar_3;
	
	//�б����ݺ�������
	private ListView mListView;
	private List<HashMap<String, String>> mListData;
	private AllRealTimeDataAdapter adapter;
	
	private int position = 2;
	
	private GetRealtimeDataTask getRealtimeDataTask;
	private ShareUtil shareUtil;
	private View view;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_realtime_data_bottom, container, false);
		shareUtil = ShareUtil.getInstance(getActivity());
		shareUtil.setIntValues("type_position", position);
		mListData  = new ArrayList<HashMap<String,String>>();
		initBtn();
		initListTitle();
		initData();
		return view;
	}
	
	private void initData() {
		mListView = (ListView) view.findViewById(R.id.listview_realtime_data);
		adapter = new AllRealTimeDataAdapter(getActivity(), mListData, R.layout.item_data_list, 
				new String[] { "name", "now", "start", "high", "low", "range", "percent" }, 
				new int[] { R.id.tvName, R.id.tvNow, R.id.tvStart, R.id.tvHigh, 
							R.id.tvLow, R.id.tvRange, R.id.tvPercent });
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(this);
		
		if (NetUtil.isNetworkAvailable(getActivity())) {
			if(getRealtimeDataTask != null) 
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
		//ֽ��
		app_list_title_bar_1 = (LinearLayout) view.findViewById(R.id.app_list_title_bar_1);
		//��ͨ ����
		app_list_title_bar_2 = (LinearLayout) view.findViewById(R.id.app_list_title_bar_2);
		//�ֻ� �ڻ� T+D
		app_list_title_bar_3 = (LinearLayout) view.findViewById(R.id.app_list_title_bar_3);
		app_list_title_bar_2.setVisibility(View.VISIBLE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_gold:
			goldBtn.setBackgroundResource(R.drawable.title_btn_bg_orange_pressed);
			chargeBtn.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
			tiantongBtn.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
			yueguiBtn.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
			usdHistoryBtn.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
			//�ֻ�
			position = 1;
			shareUtil.setIntValues("type_position", position);
			app_list_title_bar_1.setVisibility(View.GONE);
			app_list_title_bar_2.setVisibility(View.GONE);
			app_list_title_bar_3.setVisibility(View.VISIBLE);
			if (NetUtil.isNetworkAvailable(getActivity())) {
				if(getRealtimeDataTask != null)
					getRealtimeDataTask.cancel(true);
				getRealtimeDataTask = new GetRealtimeDataTask();
				getRealtimeDataTask.execute();
			}
			break;
		case R.id.btn_cahrge:
			chargeBtn.setBackgroundResource(R.drawable.title_btn_bg_orange_pressed);
			goldBtn.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
			tiantongBtn.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
			yueguiBtn.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
			usdHistoryBtn.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
			position = 6;
			shareUtil.setIntValues("type_position", position);
			app_list_title_bar_1.setVisibility(View.GONE);
			app_list_title_bar_2.setVisibility(View.GONE);
			app_list_title_bar_3.setVisibility(View.VISIBLE);
			if (NetUtil.isNetworkAvailable(getActivity())) {
				if(getRealtimeDataTask != null)
					getRealtimeDataTask.cancel(true);
				getRealtimeDataTask = new GetRealtimeDataTask();
				getRealtimeDataTask.execute();
			}
			break;
		case R.id.btn_tiantong:
			tiantongBtn.setBackgroundResource(R.drawable.title_btn_bg_orange_pressed);
			chargeBtn.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
			goldBtn.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
			yueguiBtn.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
			usdHistoryBtn.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
			position = 2;
			shareUtil.setIntValues("type_position", position);
			app_list_title_bar_1.setVisibility(View.GONE);
			app_list_title_bar_2.setVisibility(View.VISIBLE);
			app_list_title_bar_3.setVisibility(View.GONE);
			if (NetUtil.isNetworkAvailable(getActivity())) {
				if(getRealtimeDataTask != null)
					getRealtimeDataTask.cancel(true);
				getRealtimeDataTask = new GetRealtimeDataTask();
				getRealtimeDataTask.execute();
			}
			break;
		case R.id.btn_yuegui:
			yueguiBtn.setBackgroundResource(R.drawable.title_btn_bg_orange_pressed);
			chargeBtn.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
			tiantongBtn.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
			goldBtn.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
			usdHistoryBtn.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
			position = 3;
			shareUtil.setIntValues("type_position", position);
			app_list_title_bar_1.setVisibility(View.GONE);
			app_list_title_bar_2.setVisibility(View.VISIBLE);
			app_list_title_bar_3.setVisibility(View.GONE);
			if (NetUtil.isNetworkAvailable(getActivity())) {
				if(getRealtimeDataTask != null)
					getRealtimeDataTask.cancel(true);
				getRealtimeDataTask = new GetRealtimeDataTask();
				getRealtimeDataTask.execute();
			}
			break;
		case R.id.btn_gold_usd_history:
			usdHistoryBtn.setBackgroundResource(R.drawable.title_btn_bg_orange_pressed);
			chargeBtn.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
			tiantongBtn.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
			yueguiBtn.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
			goldBtn.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
			Intent intent = new Intent(getActivity(), HistoryImageActivity.class);
			startActivity(intent);
			break;
		}
	}
	
	private class GetRealtimeDataTask extends AsyncTask<String, Void, String>{
    	
    	@Override
		protected void onPreExecute() {
    		mListView.setEnabled(false);
		}
    	
		@Override
		protected String doInBackground(String... params) {
			switch (position) {
			//case 0: getPaperData(); break;  //��ȡֽ��
			//case 4: getTDData(); break;     //��ȡT+D
			//case 5: getQHData();break;      //��ȡ�ڻ�
			case 1: getSpotData(); break;     //��ȡ�ֻ�
			case 2: getTtjData(); break;      //��ȡ��ͨ
			case 3: getYgyData(); break;      //��ȡ����
			case 6: getMeizhi(); break;       //��ȡ��Ԫָ��
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			getRealtimeDataTask = null;
			mListView.setEnabled(true);
			if(mListData.size() != 0){
				adapter.notifyDataSetChanged();
			} 
		}
	}
	
	//��ȡ��Ԫָ��
	private void getMeizhi() {
		String getstr = HttpPostRequest.getDataFromWebServer(getActivity(), "http://zhj8.sinaapp.com/Mobi/Data/base");
		try {
			mListData.clear();
			HashMap<String, String> map = new HashMap<String, String>();
	    	String[] strArray = getstr.split("@");
	    	String[] arrayOfString;
	    	String str1,str2,str3,str4,str5,str6;
    		arrayOfString = strArray[strArray.length -1].split(",");
    		
            str1 = formatFloat(Float.valueOf(arrayOfString[0]));
            str2 = formatFloat(Float.valueOf(arrayOfString[1]));
            str3 = formatFloat(Float.valueOf(arrayOfString[2]));
            str4 = formatFloat(Float.valueOf(arrayOfString[3]));
            str5 = String.valueOf(formatFloat(Float.valueOf(str1) - Float.valueOf(str2)));
            str6 = String.valueOf(100*Float.valueOf(formatFloat((Float.valueOf(str5)/Float.valueOf(str2)))))+"%";
            map.put("name", "��ָ");
            map.put("now", str1);
            map.put("start", str2);
            map.put("high", str3);
            map.put("low", str4);
            map.put("range", str5);
            map.put("percent", str6);
    		mListData.add(map);
		} catch (Exception e) {}
	}
	
	//��ȡ��������
    private void getYgyData() {
    	String getstr = HttpPostRequest.getDataFromWebServer(getActivity(), "http://zhj8.sinaapp.com/Mobi/Data/ygy");
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
    
    //��ȡ��ͨ����
    private void getTtjData(){
    	String getstr = HttpPostRequest.getDataFromWebServer(getActivity(), "http://zhj8.sinaapp.com/Mobi/Data/ttj");
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
	            if (!str7.equals("��")) {
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
    
    //��ȡ�ֻ�����
    private void getSpotData() {
    	String getstr = HttpPostRequest.getDataFromWebServer(getActivity(), "http://zhj8.sinaapp.com/Mobi/Data/spot");
    	mListData.clear();
    	try {
			JSONObject localJSONObject = new JSONObject(getstr);
			String str2 = localJSONObject.getString("1");
		    if (str2.length() > 0)
		        setSpotList(str2, "�ƽ�");
		    String str3 = localJSONObject.getString("3");
		    if (str3.length() > 0)
		        setSpotList(str3, "����");
		    String str4 = localJSONObject.getString("4");
		    if (str4.length() > 0)
		        setSpotList(str4, "����");
		    String str5 = localJSONObject.getString("5");
		    if (str5.length() > 0)
		        setSpotList(str5, "�ٽ�");
		    String str6 = localJSONObject.getString("2");
		    if (str6.length() > 0)
		        setSpotList(str6, "�۽�");
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int index, long id) {
		switch (position) {
		//case 0: setIntentPutExtra_Paper(index); break; //ֽ��
		//case 4: setIntentPutExtra_TD(index); break;    //T+D
		//case 5: setIntentPutExtra_QH(index);break;     //�ڻ�
		case 1: setIntentPutExtra_Spot(index); break;    //�ֻ�
		case 2: setIntentPutExtra_Ttj(index); break;     //��ͨ
		case 3: setIntentPutExtra_Ygy(index); break;     //����
		case 6: setIntentPutExtra_Meizhi(index); break;  //��Ԫָ��
		}
	}
	
	//�ֻ�K��
	private void setIntentPutExtra_Spot(int index) {
		if(index == 4) {
			//�ֻ��еĸ۽�
		} else {
			mChartName = SpotChartName[index];
			mChartCode = SpotChartCode[index];
			toIntentStockChart();
		}
	}
    
	//��ͨK��
	private void setIntentPutExtra_Ttj(int index) {
		if(index != 3) {
			mChartName = TtjChartName[index];
			mChartCode = TtjChartCode[index];
			toIntentStockChart();
		}
	}
		
	//����K��
	private void setIntentPutExtra_Ygy(int index) {
		if(index < 2) {
			mChartName = (index == 0 ? "������9999" : "������9995");
			mChartCode = (index == 0 ? "Ygy9999" : "Ygy9995");
			toIntentStockChart();
		}
	}
		
	/** ��ת��K��ͼҳ�� */
	private void toIntentStockChart() {
		Intent intent = new Intent(getActivity(), StockChartsActivity.class);
		intent.putExtra("ChartCode", mChartCode);
		intent.putExtra("ChartName", mChartName);
		startActivity(intent);
	}
	
	//��Ԫָ��
	private void setIntentPutExtra_Meizhi(int index) {
		mChartName = "��ָ";
		mChartCode = "ZSUSD";
		toIntentStockChart();
	}
	
	
	@Override
	public void onPause() {
		super.onPause();
		if (getRealtimeDataTask != null) {
			getRealtimeDataTask.cancel(true);
		}
	}

}
