package com.innouni.south.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.innouni.south.adapter.ETFAdapter;
import com.innouni.south.app.MainApplication;
import com.innouni.south.base.BaseActivity;
import com.innouni.south.entity.ETFEntity;
import com.innouni.south.net.HttpPostRequest;
import com.innouni.south.net.ParseJsonToList;

/**
 * ETF持仓页面
 * 
 * @author HuGuojun
 * @date 2013-11-29 上午10:57:58
 * @modify
 * @version 1.0.0
 */
public class ETFActivity extends BaseActivity implements OnClickListener {
	
	private static final int TYPE_GOLD = 1;
	private static final int TYPE_SILVER = 2;
	
	private TextView goldBtn, silverBtn;
	
	private ListView listView;
	private ETFAdapter adapter;
	private GetETFTask task;
	private int type = TYPE_GOLD;
	
	private String currentType = "gold";
	private LinearLayout chartView;
	private GraphicalView mChartView;
	private XYSeriesRenderer mCurrentRenderer;
	private XYSeries mCurrentSeries;
	private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
	private String mDateFormat = "MM/dd";
	private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_etf);
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
		titleContentView.setText("ETF持仓");
		
		goldBtn = (TextView) findViewById(R.id.btn_gold_etf);
		silverBtn = (TextView) findViewById(R.id.btn_silver_etf);
		goldBtn.setOnClickListener(this);
		silverBtn.setOnClickListener(this);
		
		listView = (ListView) findViewById(R.id.listview_etf);
		adapter = new ETFAdapter(this);
		listView.setAdapter(adapter);
		
		chartView = ((LinearLayout) findViewById(R.id.chart_layout));
		initChart();
		
		if (task != null) task.cancel(true);
		task = new GetETFTask();
		task.execute();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_title_left:
			finish();
			break;
		case R.id.btn_title_right:
			if (task != null) task.cancel(true);
			task = new GetETFTask();
			task.execute();
			break;
		case R.id.btn_gold_etf:
			goldBtn.setBackgroundResource(R.drawable.title_btn_bg_orange_pressed);
			silverBtn.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
			type = TYPE_GOLD;
			currentType = "gold";
			if (task != null) task.cancel(true);
			task = new GetETFTask();
			task.execute();
			break;
		case R.id.btn_silver_etf:
			silverBtn.setBackgroundResource(R.drawable.title_btn_bg_orange_pressed);
			goldBtn.setBackgroundResource(R.drawable.title_btn_bg_orange_normal);
			type = TYPE_SILVER;
			currentType = "silver";
			if (task != null) task.cancel(true);
			task = new GetETFTask();
			task.execute();
			break;
		}
	}
	
	private class GetETFTask extends AsyncTask<Void, Void, ArrayList<Object>> {
		
		@Override
		protected void onPreExecute() {
			titleRightBtn.setVisibility(View.GONE);
			titleRefreshBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected ArrayList<Object> doInBackground(Void... params) {
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("type", String.valueOf(type)));
			String json = HttpPostRequest.getDataFromWebServer(ETFActivity.this, "getETF", pairs);
			System.out.println(json);
			if (json == null || json.equals("net_err")) {
				return null;
			} else {
				ParseJsonToList parser = ParseJsonToList.getInstance();
				return parser.parseWebDataToList(json, ETFEntity.class);
			}
		}

		@Override
		protected void onPostExecute(ArrayList<Object> result) {
			task = null;
			titleRightBtn.setVisibility(View.VISIBLE);
			titleRefreshBar.setVisibility(View.GONE);
			if (result != null) {
				//画图
				paint(result);
				if (adapter != null && adapter.getCount() > 0) {
					adapter.clear();
				}
				adapter.setList(result, true);
			}
		}
	}
	
	private void initChart() {
		this.mRenderer.setSelectableBuffer(100);
		this.mRenderer.setApplyBackgroundColor(true);
		this.mRenderer.setBackgroundColor(-16777216);
		this.mRenderer.setAxisTitleTextSize(16.0F);
		this.mRenderer.setChartTitleTextSize(20.0F);
		this.mRenderer.setLabelsTextSize(15.0F);
		this.mRenderer.setMargins(new int[4]);
		this.mRenderer.setFitLegend(true);
		this.mRenderer.setPointSize(10.0F);
		this.mRenderer.setLabelsColor(getResources().getColor(R.color.white));
		this.mRenderer.setYLabelsAlign(Paint.Align.LEFT);
		this.mRenderer.setShowGrid(true);
		this.mRenderer.setShowLegend(false);
		this.mRenderer.setLegendTextSize(20.0F);
		this.mRenderer.setClickEnabled(true);
		this.mRenderer.setMarginsColor(-16777216);
		TimeSeries localTimeSeries = new TimeSeries(this.currentType);
		this.mDataset.addSeries(localTimeSeries);
		this.mCurrentSeries = localTimeSeries;
		XYSeriesRenderer localXYSeriesRenderer = new XYSeriesRenderer();
		this.mRenderer.addSeriesRenderer(localXYSeriesRenderer);
		this.mRenderer.setShowLabels(true);
		localXYSeriesRenderer.setPointStyle(PointStyle.POINT);
		localXYSeriesRenderer.setFillPoints(true);
		localXYSeriesRenderer.setFillBelowLine(true);
		localXYSeriesRenderer.setColor(Color.rgb(87, 118, 151));
		localXYSeriesRenderer.setLineWidth(2.0F);
		localXYSeriesRenderer.setFillBelowLineColor(Color.rgb(155, 174, 194));
		this.mCurrentRenderer = localXYSeriesRenderer;
	}

	private void paint(ArrayList<Object> paramArrayList) {
		try {
			this.mCurrentSeries.clear();
			SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Iterator localIterator = paramArrayList.iterator();
			while (true) {
				if (!localIterator.hasNext()) {
					this.mRenderer.setYAxisMax(5.0D + this.mCurrentSeries.getMaxY());
					this.mRenderer.setYAxisMin(this.mCurrentSeries.getMinY() - 5.0D);
					this.mRenderer.setXAxisMax(5.0D + this.mCurrentSeries.getMaxX());
					this.mRenderer.setXAxisMin(this.mCurrentSeries.getMinX() - 5.0D);
					if (this.mChartView != null)
						break;
					this.mChartView = ChartFactory.getTimeChartView(this,this.mDataset, this.mRenderer, this.mDateFormat);
					this.mChartView.setDrawingCacheBackgroundColor(Color.rgb(155, 174, 194));
					this.chartView.addView(this.mChartView,new LinearLayout.LayoutParams(-1, -1));
					return;
				}
				ETFEntity localETFEntity = (ETFEntity) localIterator.next();
				this.mCurrentSeries.add(localSimpleDateFormat.parse(localETFEntity.getTime())
								.getTime(), Double.parseDouble(localETFEntity.getTotal()));
			}
		} catch (Exception localException) {
			localException.printStackTrace();
			return;
		}
		this.mChartView.repaint();
	}

	protected void onRestoreInstanceState(Bundle paramBundle) {
		super.onRestoreInstanceState(paramBundle);
		this.mDataset = ((XYMultipleSeriesDataset) paramBundle.getSerializable("dataset"));
		this.mRenderer = ((XYMultipleSeriesRenderer) paramBundle.getSerializable("renderer"));
		this.mCurrentSeries = ((TimeSeries) paramBundle.getSerializable("current_series"));
		this.mCurrentRenderer = ((XYSeriesRenderer) paramBundle.getSerializable("current_renderer"));
		this.mDateFormat = paramBundle.getString("date_format");
	}

	protected void onSaveInstanceState(Bundle paramBundle) {
		super.onSaveInstanceState(paramBundle);
		paramBundle.putSerializable("dataset", this.mDataset);
		paramBundle.putSerializable("renderer", this.mRenderer);
		paramBundle.putSerializable("current_series", this.mCurrentSeries);
		paramBundle.putSerializable("current_renderer", this.mCurrentRenderer);
		paramBundle.putString("date_format", this.mDateFormat);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		titleRightBtn.setVisibility(View.VISIBLE);
		titleRefreshBar.setVisibility(View.GONE);
		if (task != null) {
			task.cancel(true);
		}
	}
}
