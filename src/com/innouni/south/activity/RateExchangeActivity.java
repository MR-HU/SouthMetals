//package com.innouni.south.activity;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//
//import com.innouni.south.app.MainApplication;
//import com.innouni.south.base.BaseActivity;
//import com.innouni.south.net.HttpPostRequest;
//import com.innouni.south.util.NetUtil;
//
///**
// * 汇率转换页面
// * @author HuGuojun
// * @data 2013-09-03
// */
//public class RateExchangeActivity extends BaseActivity implements OnClickListener {
//	
//	private static final char POINT = '.';
//	private static final String SPOINT = ".";
//	
//	private double USD2CNY;
//	private double USD2EUR;
//	private double EUR2CNY;
//	
//	private EditText chinaView, usaView, europeView;
//	private GetRateTask task;
//	
//	private ChinaTextWatcher chinaTextWatcher;
//	private UsaTextWatcher usaTextWatcher;
//	private EuropeTextWatcher europeTextWatcher;
//	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_rate_exchange);
//		application = MainApplication.getApplication();
//		application.setActivity(this);
//		initView();
//		if (NetUtil.isNetworkAvailable(this)) {
//			if (null != task) {
//				task.cancel(true);
//			}
//			task = new GetRateTask();
//			task.execute();
//		} else {
//			showToast(R.string.net_unavailable_current);
//		}
//		registerEvent();
//	}
//
//	private void initView() {
//		titleLeftBtn = (TextView) findViewById(R.id.btn_title_left);
//		titleRightBtn = (TextView) findViewById(R.id.btn_title_right);
//		titleContentView = (TextView) findViewById(R.id.txt_title_content);
//		titleRefreshBar = (ProgressBar) findViewById(R.id.progress_title_right);
//		titleLeftBtn.setOnClickListener(this);
//		titleRightBtn.setOnClickListener(this);
//		titleContentView.setText(R.string.rechange);
//		titleContentView.setFocusable(true);
//		titleContentView.setFocusableInTouchMode(true);
//		titleContentView.requestFocus();
//		
//		chinaView = (EditText) findViewById(R.id.edit_china);
//		usaView = (EditText) findViewById(R.id.edit_usa);
//		europeView = (EditText) findViewById(R.id.edit_europe);
//	}
//	
//	private void registerEvent() {
//		chinaTextWatcher = new ChinaTextWatcher();
//		usaTextWatcher = new UsaTextWatcher();
//		europeTextWatcher = new EuropeTextWatcher();
//		chinaView.addTextChangedListener(chinaTextWatcher);
//		usaView.addTextChangedListener(usaTextWatcher);
//		europeView.addTextChangedListener(europeTextWatcher);
//	}
//	
//	private class GetRateTask extends AsyncTask<Void, Void, Void> {
//		@Override
//		protected void onPreExecute() {
//			titleRightBtn.setVisibility(View.GONE);
//			titleRefreshBar.setVisibility(View.VISIBLE);
//		}
//		
//		@Override
//		protected Void doInBackground(Void... params) {
//			String json = HttpPostRequest.getDataFromWebServer(RateExchangeActivity.this, "getCurrencyRate", null);
//			System.out.println("请求汇率返回: " + json);
//			try {
//				JSONObject object = new JSONObject(json);
//				USD2EUR = object.getDouble("USD2EUR");
//				USD2CNY = object.getDouble("USD2CNY");
//				EUR2CNY = USD2CNY/USD2EUR;
//			} catch (JSONException e) {
//				e.printStackTrace();
//				USD2EUR = 0.0d;
//				USD2CNY = 0.0d;
//				EUR2CNY = 0.0d;
//			}
//			return null;
//		}
//
//		@Override
//		protected void onPostExecute(Void result) {
//			titleRightBtn.setVisibility(View.VISIBLE);
//			titleRefreshBar.setVisibility(View.GONE);
//			task = null;
//			chinaView.setText(String.valueOf(USD2CNY));
//			usaView.setText("1.0");
//			europeView.setText(String.valueOf(USD2EUR));
//		}
//	}
//	
//	/** 
//	 * 截取小数点后4为位 <br>
//	 * 如 2.45678-->2.4567 <br>
//	 * 如果计算结果小于0.001,则不予显示 <br>
//	 * @param str
//	 * @return
//	 */
//	private String getAfter4Point(String value){
//		double result = Double.valueOf(value);
//		if (Math.abs(result - 0) < 0.001d) {
//			value = "0.0";
//		} else {
//			if (value.contains(SPOINT)) {
//				int index = value.indexOf(SPOINT);
//				String before = value.substring(0, index);
//				String after = value.substring(index + 1);
//				if (after.length() > 4) {
//					after = after.substring(0, 4);
//				}
//				value = before + SPOINT + after;
//			}
//		}
//		return value;
//	}
//	
//	private class ChinaTextWatcher implements TextWatcher {
//
//		@Override
//		public void afterTextChanged(Editable s) {
//			usaView.addTextChangedListener(usaTextWatcher);
//			europeView.addTextChangedListener(europeTextWatcher);
//		}
//
//		@Override
//		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//			usaView.removeTextChangedListener(usaTextWatcher);
//			europeView.removeTextChangedListener(europeTextWatcher);
//		}
//
//		@Override
//		public void onTextChanged(CharSequence s, int start, int before, int count) {
//			String str = s.toString();
//			if (str.equals("")) {
//				europeView.setText(null);
//				usaView.setText(null);
//			} else if (s.charAt(0) != POINT && s.charAt(s.length()-1) != POINT) {
//				double rmb = Double.valueOf(str);
//				double doller = rmb / USD2CNY;
//				double europe = rmb / EUR2CNY;
//				usaView.setText(getAfter4Point(String.valueOf(doller)));
//				europeView.setText(getAfter4Point(String.valueOf(europe)));
//			}
//		}
//	}
//	
//	private class UsaTextWatcher implements TextWatcher {
//
//		@Override
//		public void afterTextChanged(Editable s) {
//			chinaView.addTextChangedListener(chinaTextWatcher);
//			europeView.addTextChangedListener(europeTextWatcher);
//		}
//
//		@Override
//		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//			chinaView.removeTextChangedListener(chinaTextWatcher);
//			europeView.removeTextChangedListener(europeTextWatcher);
//		}
//
//		@Override
//		public void onTextChanged(CharSequence s, int start, int before, int count) {
//			String str = s.toString();
//			if (str.equals("")) {
//				chinaView.setText(null);
//				europeView.setText(null);
//			} else if (s.charAt(0) != POINT && s.charAt(s.length()-1) != POINT) {
//				double doller = Double.valueOf(str);
//				double rmb = doller * USD2CNY;
//				double europe = doller * USD2EUR;
//				chinaView.setText(getAfter4Point(String.valueOf(rmb)));
//				europeView.setText(getAfter4Point(String.valueOf(europe)));
//			}
//		}
//	}
//	
//	private class EuropeTextWatcher implements TextWatcher {
//
//		@Override
//		public void afterTextChanged(Editable s) {
//			chinaView.addTextChangedListener(chinaTextWatcher);
//			usaView.addTextChangedListener(usaTextWatcher);
//		}
//
//		@Override
//		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//			chinaView.removeTextChangedListener(chinaTextWatcher);
//			usaView.removeTextChangedListener(usaTextWatcher);
//		}
//
//		@Override
//		public void onTextChanged(CharSequence s, int start, int before, int count) {
//			String str = s.toString();
//			if (s.toString().equals("")) {
//				chinaView.setText(null);
//				usaView.setText(null);
//			} else if (s.charAt(0) != POINT && s.charAt(s.length()-1) != POINT) {
//				double europe = Double.valueOf(str);
//				double rmb = europe * EUR2CNY;
//				double doller = europe / USD2EUR;
//				chinaView.setText(getAfter4Point(String.valueOf(rmb)));
//				usaView.setText(getAfter4Point(String.valueOf(doller)));
//			}
//		}
//	}
//	
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.btn_title_left:
//			finish();
//			break;
//		case R.id.btn_title_right:
//			if (null != task) task.cancel(true);
//			task = new GetRateTask();
//			task.execute();
//			break;
//		default:
//			break;
//		}
//	}
//
//	@Override
//	protected void onPause() {
//		super.onPause();
//		titleRightBtn.setVisibility(View.VISIBLE);
//		titleRefreshBar.setVisibility(View.GONE);
//		if(null != task){
//			task.cancel(true);
//		}
//	}
//}
package com.innouni.south.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.innouni.south.app.MainApplication;
import com.innouni.south.base.BaseActivity;
import com.innouni.south.net.HttpPostRequest;
import com.innouni.south.util.NetUtil;

/**
 * 汇率转换页面
 * 
 * @author HuGuojun
 * @data 2013-09-03
 */
public class RateExchangeActivity extends BaseActivity implements
		OnClickListener {

	private static final char POINT = '.';
	private static final String SPOINT = ".";

	private double USD2CNY;
	private double USD2EUR;
	private double EUR2CNY;

	private EditText chinaView, usaView, europeView;
	private GetRateTask task;

	private ChinaTextWatcher chinaTextWatcher;
	private UsaTextWatcher usaTextWatcher;
	private EuropeTextWatcher europeTextWatcher;

	private TextView buyView, sellView;
	private TextView rateView;
	private EditText neipanEdit, waipanEdit;
	private NeipanTextWatcher neiWatcher;
	private waipanTextWatcher waiWatcher;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rate_exchange);
		application = MainApplication.getApplication();
		application.setActivity(this);
		initView();
		if (NetUtil.isNetworkAvailable(this)) {
			if (null != task) {
				task.cancel(true);
			}
			task = new GetRateTask();
			task.execute();
		} else {
			showToast(R.string.net_unavailable_current);
		}
		registerEvent();
	}

	private void initView() {
		titleLeftBtn = (TextView) findViewById(R.id.btn_title_left);
		titleRightBtn = (TextView) findViewById(R.id.btn_title_right);
		titleContentView = (TextView) findViewById(R.id.txt_title_content);
		titleRefreshBar = (ProgressBar) findViewById(R.id.progress_title_right);
		titleLeftBtn.setOnClickListener(this);
		titleRightBtn.setOnClickListener(this);
		titleContentView.setText("价格换算");
		titleContentView.setFocusable(true);
		titleContentView.setFocusableInTouchMode(true);
		titleContentView.requestFocus();

		chinaView = (EditText) findViewById(R.id.edit_china);
		usaView = (EditText) findViewById(R.id.edit_usa);
		europeView = (EditText) findViewById(R.id.edit_europe);

		rateView = (TextView) findViewById(R.id.txt_current_rate);
		waipanEdit = (EditText) findViewById(R.id.edit_waipan);
		neipanEdit = (EditText) findViewById(R.id.edit_neipan);

		buyView = (TextView) findViewById(R.id.txt_mairu);
		sellView = (TextView) findViewById(R.id.txt_maichu);
	}

	private void registerEvent() {
		chinaTextWatcher = new ChinaTextWatcher();
		usaTextWatcher = new UsaTextWatcher();
		europeTextWatcher = new EuropeTextWatcher();
		chinaView.addTextChangedListener(chinaTextWatcher);
		usaView.addTextChangedListener(usaTextWatcher);
		europeView.addTextChangedListener(europeTextWatcher);

		neiWatcher = new NeipanTextWatcher();
		waiWatcher = new waipanTextWatcher();
		neipanEdit.addTextChangedListener(neiWatcher);
		waipanEdit.addTextChangedListener(waiWatcher);
	}

	private class GetRateTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			neipanEdit.setEnabled(false);
			waipanEdit.setEnabled(false);
			titleRightBtn.setVisibility(View.GONE);
			titleRefreshBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {
			String json = HttpPostRequest.getDataFromWebServer(
					RateExchangeActivity.this, "getCurrencyRate", null);
			System.out.println("请求汇率返回: " + json);
			try {
				JSONObject object = new JSONObject(json);
				USD2EUR = object.getDouble("USD2EUR");
				USD2CNY = object.getDouble("USD2CNY") / 100;
				EUR2CNY = USD2CNY / USD2EUR;
			} catch (JSONException e) {
				e.printStackTrace();
				USD2EUR = 0.0d;
				USD2CNY = 0.0d;
				EUR2CNY = 0.0d;
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			titleRightBtn.setVisibility(View.VISIBLE);
			titleRefreshBar.setVisibility(View.GONE);
			task = null;
			chinaView.setText(String.valueOf(USD2CNY));
			usaView.setText("1.0");
			europeView.setText(String.valueOf(USD2EUR));
			rateView.setText("当前美元兑人名币的汇率：" + String.valueOf(USD2CNY));
			if (USD2CNY > 0) {
				neipanEdit.setEnabled(true);
				waipanEdit.setEnabled(true);
			} else {
				showToast("获取汇率失败");
			}
		}
	}

	/**
	 * 截取小数点后4为位 <br>
	 * 如 2.45678-->2.4567 <br>
	 * 如果计算结果小于0.001,则不予显示 <br>
	 * 
	 * @param str
	 * @return
	 */
	private String getAfter4Point(String value) {
		double result = Double.valueOf(value);
		if (Math.abs(result - 0) < 0.001d) {
			value = "0.0";
		} else {
			if (value.contains(SPOINT)) {
				int index = value.indexOf(SPOINT);
				String before = value.substring(0, index);
				String after = value.substring(index + 1);
				if (after.length() > 4) {
					after = after.substring(0, 4);
				}
				value = before + SPOINT + after;
			}
		}
		return value;
	}

	private class ChinaTextWatcher implements TextWatcher {

		@Override
		public void afterTextChanged(Editable s) {
			usaView.addTextChangedListener(usaTextWatcher);
			europeView.addTextChangedListener(europeTextWatcher);
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			usaView.removeTextChangedListener(usaTextWatcher);
			europeView.removeTextChangedListener(europeTextWatcher);
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			String str = s.toString();
			if (str.equals("")) {
				europeView.setText(null);
				usaView.setText(null);
			} else if (s.charAt(0) != POINT
					&& s.charAt(s.length() - 1) != POINT) {
				double rmb = Double.valueOf(str);
				double doller = rmb / USD2CNY;
				double europe = rmb / EUR2CNY;
				usaView.setText(getAfter4Point(String.valueOf(doller)));
				europeView.setText(getAfter4Point(String.valueOf(europe)));
			}
		}
	}

	private class UsaTextWatcher implements TextWatcher {

		@Override
		public void afterTextChanged(Editable s) {
			chinaView.addTextChangedListener(chinaTextWatcher);
			europeView.addTextChangedListener(europeTextWatcher);
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			chinaView.removeTextChangedListener(chinaTextWatcher);
			europeView.removeTextChangedListener(europeTextWatcher);
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			String str = s.toString();
			if (str.equals("")) {
				chinaView.setText(null);
				europeView.setText(null);
			} else if (s.charAt(0) != POINT
					&& s.charAt(s.length() - 1) != POINT) {
				double doller = Double.valueOf(str);
				double rmb = doller * USD2CNY;
				double europe = doller * USD2EUR;
				chinaView.setText(getAfter4Point(String.valueOf(rmb)));
				europeView.setText(getAfter4Point(String.valueOf(europe)));
			}
		}
	}

	private class EuropeTextWatcher implements TextWatcher {

		@Override
		public void afterTextChanged(Editable s) {
			chinaView.addTextChangedListener(chinaTextWatcher);
			usaView.addTextChangedListener(usaTextWatcher);
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			chinaView.removeTextChangedListener(chinaTextWatcher);
			usaView.removeTextChangedListener(usaTextWatcher);
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			String str = s.toString();
			if (s.toString().equals("")) {
				chinaView.setText(null);
				usaView.setText(null);
			} else if (s.charAt(0) != POINT
					&& s.charAt(s.length() - 1) != POINT) {
				double europe = Double.valueOf(str);
				double rmb = europe * EUR2CNY;
				double doller = europe / USD2EUR;
				chinaView.setText(getAfter4Point(String.valueOf(rmb)));
				usaView.setText(getAfter4Point(String.valueOf(doller)));
			}
		}
	}

	private class NeipanTextWatcher implements TextWatcher {

		@Override
		public void afterTextChanged(Editable s) {
			waipanEdit.addTextChangedListener(waiWatcher);
			if (!neipanEdit.getText().toString().equals("")) {
				sellView.setText(neipanEdit.getText().toString());
				buyView.setText((Double
						.valueOf(neipanEdit.getText().toString()) + 8) + "");
			} else {
				sellView.setText("");
				buyView.setText("");
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			waipanEdit.removeTextChangedListener(waiWatcher);
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			String str = s.toString();
			if (str.equals("")) {
				waipanEdit.setText(null);
			} else if (s.charAt(0) != POINT
					&& s.charAt(s.length() - 1) != POINT) {
				double doller = Double.valueOf(str); // 获得内盘的值
				double rmb = doller * 31.1035 / USD2CNY / 1000; // 计算外盘的值
				waipanEdit.setText(getAfter4Point(String.valueOf(rmb)));
			}
		}
	}

	private class waipanTextWatcher implements TextWatcher {

		@Override
		public void afterTextChanged(Editable s) {
			neipanEdit.addTextChangedListener(neiWatcher);
			if (!waipanEdit.getText().toString().equals("")) {
				sellView.setText(neipanEdit.getText().toString());
				buyView.setText((Double
						.valueOf(neipanEdit.getText().toString()) + 8) + "");
			} else {
				sellView.setText("");
				buyView.setText("");
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			neipanEdit.removeTextChangedListener(neiWatcher);
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			String str = s.toString();
			if (str.equals("")) {
				neipanEdit.setText(null);
			} else if (s.charAt(0) != POINT
					&& s.charAt(s.length() - 1) != POINT) {
				double doller = Double.valueOf(str);
				double europe = doller * USD2CNY / 31.1035 * 1000;
				neipanEdit.setText(getAfter4Point(String.valueOf(europe)));
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_title_left:
			finish();
			break;
		case R.id.btn_title_right:
			if (null != task)
				task.cancel(true);
			task = new GetRateTask();
			task.execute();
			break;
		default:
			break;
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
}
