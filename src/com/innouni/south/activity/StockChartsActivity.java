package com.innouni.south.activity;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.stockchart.StockChartView;
import org.stockchart.core.Appearance;
import org.stockchart.core.Area;
import org.stockchart.core.Axis;
import org.stockchart.core.AxisRange;
import org.stockchart.core.Line;
import org.stockchart.indicators.BollingerBandsIndicator;
import org.stockchart.indicators.EmaIndicator;
import org.stockchart.indicators.EnvelopesIndicator;
import org.stockchart.indicators.MacdIndicator;
import org.stockchart.indicators.RsiIndicator;
import org.stockchart.indicators.SmaIndicator;
import org.stockchart.indicators.StochasticIndicator;
import org.stockchart.points.LinePoint;
import org.stockchart.points.StockPoint;
import org.stockchart.series.BarSeries;
import org.stockchart.series.LinearSeries;
import org.stockchart.series.SeriesBase;
import org.stockchart.series.StockSeries;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.innouni.south.entity.TimeChartBean;
import com.innouni.south.net.HttpPostRequest;
import com.innouni.south.pref.GlobalConfigPreferences;

/**
 * K线图页面
 * 
 * @author HuGuojun
 * @date 2013-11-28 下午2:19:49
 * @modify
 * @version 1.0.0
 */
public class StockChartsActivity extends Activity implements OnClickListener,
		OnTouchListener {
	private LinearLayout time_chart_layout, indicator_layout;
	private TextView time_chart_oneDay, time_chart_twoDay, time_chart_threeDay,
			time_chart_fourDay, tv_title, stock_price_now, stock_price_updown,
			stock_price_yesterday, stock_price_today, stock_price_high,
			stock_price_low, chart_time_tab, chart_k_tab, tv_change_indicator,
			mTitleView, mTimeTitleView, mDataRefreshView;
	private TextView indicator_oneM, indicator_fiveM, indicator_fifM,
			indicator_thirtyM, indicator_sixtyM, indicator_fourHour,
			indicator_oneDay, indicator_oneWeek, indicator_oneMonth;
	private TextView btn_back;
	private TextView btn_refresh;
	private StockChartView mTimeChartView, mStockChartView;
	private String mChartTitle, mCurrentKLineType, mCurrentChartCode;
	private int mCurrentKLineTYPIndex, mShowUpIndicator, mShowDownIndicate,
			mCurrentTimeChartTYP;
	private Area mKChartArea, mIndicatorArea, mTimeChartArea;
	private StockSeries mKChartSeries;
	private LinearSeries mIndicatorSeries;
	private LinearSeries mCloseSeries, m48CloseSeries, m72CloseSeries,
			m96CloseSeries;
	private BarSeries mVolumnSeries;
	private GetGoldTask iGetGoldTask;
	private GetTimeGoldTask iGetTimeGoldTask;
	private String[] mStockIndicatorTiemStrings;

	private SmaIndicator mSMA10Indicator, mSMA20Indicator, mSMA5Indicator;
	private EmaIndicator mEMA10Indicator, mEMA20Indicator, mEMA5Indicator;
	private EnvelopesIndicator mEnvelopesIndicator;
	private BollingerBandsIndicator mBOLLIndicator;
	private StochasticIndicator mKDJIndicator;
	private RsiIndicator mRSI12Indicator, mRSI24Indicator, mRSI6Indicator;
	private String[] mKLineTypes = { "1001", "1005", "1015", "1030", "1060",
			"2004", "3001", "4001", "5001" };
	private String[] mTimChartTypes = { "", "24小时", "48小时", "72小时", "96小时" };
	private ArrayList<LinearSeries> mLinearSeries;
	private ArrayList<TimeChartBean> mChartBeans = new ArrayList<TimeChartBean>();
	private GetMetalPriceTask iGetMetalPriceTask;
	private GlobalConfigPreferences isPreferences;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stockchart);
		isPreferences = new GlobalConfigPreferences(this);
		/** 界面初始化 */
		initContentView();
		mCurrentKLineType = "1015"; // K线默认15分线
		mCurrentKLineTYPIndex = 2;
		mCurrentTimeChartTYP = 3; // 分时图默认72小时
		indicatorBgChange(2);
		try {
			Bundle localBundle = getIntent().getExtras();
			mCurrentChartCode = localBundle.getString("ChartCode");
			mChartTitle = localBundle.getString("ChartName");
		} catch (Exception e) {
			mCurrentChartCode = "XAUUSD";
			mChartTitle = "现货黄金";
		}
		mStockIndicatorTiemStrings = getResources().getStringArray(
				R.array.stock_indicator);
		tv_title.setText(mChartTitle + "-" + "K线" + " "
				+ mStockIndicatorTiemStrings[mCurrentKLineTYPIndex]);
		initTimeChart(); // 分时图初始化
		initKChart(); // k线图初始化
		getMetalPriceData();
		getStockChartData();

	}

	/** 界面头部初始化 **/
	private void initHeaderView() {
		// 标题
		tv_title = (TextView) findViewById(R.id.tv_title);
		// 返回按钮
		btn_back = (TextView) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);
		// 数据刷新按钮
		btn_refresh = (TextView) findViewById(R.id.btn_refresh);
		btn_refresh.setOnClickListener(this);
	}

	/** 时间选择初始化 **/
	private void initTimeOption() {
		/** 分时图的 时间选择 布局 **/
		time_chart_layout = (LinearLayout) findViewById(R.id.time_chart_layout);
		// 分时图的 时间选项 一天 24小时
		time_chart_oneDay = (TextView) findViewById(R.id.time_chart_oneDay);
		// 分时图的 时间选项 一天 48小时
		time_chart_twoDay = (TextView) findViewById(R.id.time_chart_twoDay);
		// 分时图的 时间选项 一天 72小时
		time_chart_threeDay = (TextView) findViewById(R.id.time_chart_threeDay);
		// 分时图的 时间选项 一天 96小时
		time_chart_fourDay = (TextView) findViewById(R.id.time_chart_fourDay);
		time_chart_oneDay.setOnClickListener(this);
		time_chart_twoDay.setOnClickListener(this);
		time_chart_threeDay.setOnClickListener(this);
		time_chart_fourDay.setOnClickListener(this);
		/** k线图的 时间选择 布局 **/
		indicator_layout = (LinearLayout) findViewById(R.id.indicator_layout);
		// k线图的 时间选项 1分 001
		indicator_oneM = (TextView) findViewById(R.id.indicator_oneM);
		// k线图的 时间选项 5分 005
		indicator_fiveM = (TextView) findViewById(R.id.indicator_fiveM);
		// k线图的 时间选项 15分 015
		indicator_fifM = (TextView) findViewById(R.id.indicator_fifM);
		// k线图的 时间选项 30分 030
		indicator_thirtyM = (TextView) findViewById(R.id.indicator_thirtyM);

		// k线图的 时间选项 60分 060
		indicator_sixtyM = (TextView) findViewById(R.id.indicator_sixtyM);
		// k线图的 时间选项 24小时 240
		indicator_fourHour = (TextView) findViewById(R.id.indicator_fourHour);
		// k线图的 时间选项 一日 100
		indicator_oneDay = (TextView) findViewById(R.id.indicator_oneDay);
		// k线图的 时间选项 一周 200
		indicator_oneWeek = (TextView) findViewById(R.id.indicator_oneWeek);
		// k线图的 时间选项 一月 300
		indicator_oneMonth = (TextView) findViewById(R.id.indicator_oneMonth);

		indicator_oneM.setOnClickListener(this);
		indicator_fiveM.setOnClickListener(this);
		indicator_fifM.setOnClickListener(this);
		indicator_thirtyM.setOnClickListener(this);
		indicator_sixtyM.setOnClickListener(this);
		indicator_fourHour.setOnClickListener(this);
		indicator_oneDay.setOnClickListener(this);
		indicator_oneWeek.setOnClickListener(this);
		indicator_oneMonth.setOnClickListener(this);
	}

	/** 最新数据信息显示界面初始化 **/
	private void initStockPriceNow() {
		// 当前最新价
		stock_price_now = (TextView) findViewById(R.id.stock_price_now);
		// 涨跌幅
		stock_price_updown = (TextView) findViewById(R.id.stock_price_updown);
		// 昨收价
		stock_price_yesterday = (TextView) findViewById(R.id.stock_price_yesterday);
		// 今开盘价
		stock_price_today = (TextView) findViewById(R.id.stock_price_today);
		// 最高
		stock_price_high = (TextView) findViewById(R.id.stock_price_high);
		// 最低
		stock_price_low = (TextView) findViewById(R.id.stock_price_low);
	}

	/** 界面底部初始化 **/
	private void initFooterView() {
		// 切换到 分时图 的 触发 控件
		chart_time_tab = (TextView) findViewById(R.id.chart_time_tab);
		chart_time_tab.setOnClickListener(this);
		// 切换到 K线图 的 触发 控件
		chart_k_tab = (TextView) findViewById(R.id.chart_k_tab);
		chart_k_tab.setOnClickListener(this);
		// 指标 切换 触发 控件
		tv_change_indicator = (TextView) findViewById(R.id.tv_change_indicator);
		tv_change_indicator.setOnClickListener(this);
	}

	/** 界面初始化 **/
	private void initContentView() {
		/** 界面头部初始化 **/
		initHeaderView();
		/** 时间选择初始化 **/
		initTimeOption();
		/** 最新数据信息显示界面初始化 **/
		initStockPriceNow();
		/** 界面底部初始化 **/
		initFooterView();
		// 分时图 控件
		mTimeChartView = (StockChartView) findViewById(R.id.timeChartView);
		// K线图 控件
		mStockChartView = (StockChartView) findViewById(R.id.stockChartView);
		// K线图 t
		mTitleView = ((TextView) findViewById(R.id.k_chart_title));
		// 分时图 t
		mTimeTitleView = ((TextView) findViewById(R.id.t_chart_title));
		// 数据刷新 时显示
		mDataRefreshView = ((TextView) findViewById(R.id.metal_data_refresh_textView));
	}

	// 分时图初始化
	private void initTimeChart() {
		initTimeChartArea();// 分时图 k线区域初始化
		initCloseSeries();// 分时图系列初始化
		this.mTimeChartArea.getSeries().add(this.mCloseSeries);
		this.mTimeChartArea.getSeries().add(this.m48CloseSeries);
		this.mTimeChartArea.getSeries().add(this.m72CloseSeries);
		this.mTimeChartArea.getSeries().add(this.m96CloseSeries);
		AxisRange localAxisRange = new AxisRange();
		localAxisRange.setMovable(true);
		localAxisRange.setZoomable(true);
		this.mTimeChartView.enableGlobalAxisRange(Axis.Side.BOTTOM,
				localAxisRange);
		timeWireEvent();
	}

	// 分时图 k线区域初始化
	private void initTimeChartArea() {
		this.mTimeChartArea = new Area();
		this.mTimeChartArea.setName("TimeChartArea");
		this.mTimeChartArea.getLeftAxis().setVisible(false);
		this.mTimeChartArea.getTopAxis().setVisible(false);
		this.mTimeChartArea.getBottomAxis().setVisible(true);
		this.mTimeChartArea.setHorizontalGridVisible(true);
		this.mTimeChartArea.getAppearance().setPrimaryFillColor(-16777216);
		this.mTimeChartArea.getAppearance().setAllColors(-16777216);
		this.mTimeChartView.getAreas().add(this.mTimeChartArea);
	}

	// 分时图系列初始化
	private void initCloseSeries() {
		this.mCloseSeries = new LinearSeries();
		this.mCloseSeries.setName("CloseSeries");
		this.mCloseSeries.setYAxisSide(Axis.Side.RIGHT);
		this.mCloseSeries.getAppearance().setAllColors(-65536);
		this.mCloseSeries.setVisible(false);
		this.m48CloseSeries = new LinearSeries();
		this.m48CloseSeries.setName("48CloseSeries");
		this.m48CloseSeries.setYAxisSide(Axis.Side.RIGHT);
		this.m48CloseSeries.getAppearance().setAllColors(-256);
		this.m48CloseSeries.setVisible(false);
		this.m72CloseSeries = new LinearSeries();
		this.m72CloseSeries.setName("72CloseSeries");
		this.m72CloseSeries.setYAxisSide(Axis.Side.RIGHT);
		this.m72CloseSeries.getAppearance().setAllColors(-16711936);
		this.m72CloseSeries.setVisible(false);
		this.m96CloseSeries = new LinearSeries();
		this.m96CloseSeries.setName("96CloseSeries");
		this.m96CloseSeries.setYAxisSide(Axis.Side.RIGHT);
		this.m96CloseSeries.getAppearance().setAllColors(-1);
		this.m96CloseSeries.setVisible(false);
		this.mLinearSeries = new ArrayList();
		this.mLinearSeries.add(0, this.mCloseSeries);
		this.mLinearSeries.add(1, this.m48CloseSeries);
		this.mLinearSeries.add(2, this.m72CloseSeries);
		this.mLinearSeries.add(3, this.m96CloseSeries);
	}

	/** 时间事件 滚动时间事件 **/
	private void timeWireEvent() {
		this.mTimeChartArea.getRightAxis().getAppearance()
				.setPrimaryFillColor(-16777216);
		this.mTimeChartArea.getRightAxis().getAppearance().setTextColor(-1);
		this.mTimeChartArea.getBottomAxis().getAppearance()
				.setPrimaryFillColor(-16777216);
		this.mTimeChartArea.getBottomAxis().getAppearance().setTextColor(-1);
		this.mTimeChartArea.getBottomAxis().setLabelFormatProvider(
				new Axis.ILabelFormatProvider() {
					public String getAxisLabel(Axis paramAnonymousAxis,
							double paramAnonymousDouble) {
						try {
							Area localArea = paramAnonymousAxis.getParent();

							for (int i = 0; i < localArea.getSeries().size(); i++) {
								SeriesBase localSeriesBase = (SeriesBase) localArea
										.getSeries().get(i);
								int j = localSeriesBase
										.convertToArrayIndex(paramAnonymousDouble);
								if ((j >= 0)
										&& (j < localSeriesBase.getPointCount())) {
									Object localObject2 = localSeriesBase
											.getPointAt(j).getID();
									if (localObject2 != null)
										return localObject2.toString();
								}
							}
						} catch (Exception localException) {
							localException.printStackTrace();
						}
						return null;
					}
				});
		this.mTimeChartArea.getRightAxis().setLabelFormatProvider(
				new Axis.ILabelFormatProvider() {
					public String getAxisLabel(Axis paramAnonymousAxis,
							double paramAnonymousDouble) {
						try {
							return String
									.valueOf(round(paramAnonymousDouble, 2));
						} catch (Exception localException) {
						}
						return null;
					}
				});
	}

	// k线图初始化
	private void initKChart() {
		initKChartArea();// k线图 k线区域初始化
		initIndicatorArea();// K线图下部区域 指标区初始化
		initKChartSeries();// k线图系列初始化
		initVolumnSeries();// K线图下部 指标区系列 全年系列初始化
		initIndicatorSeries();// K线图下部 指标区系列
		mKChartArea.getSeries().add(mKChartSeries);
		initKChartIndicatorLine();// K线图指示符线条初始化
		AxisRange localAxisRange = new AxisRange();
		localAxisRange.setMovable(true);
		localAxisRange.setZoomable(true);
		this.mStockChartView.enableGlobalAxisRange(Axis.Side.BOTTOM,
				localAxisRange);
		stockWireEvent();
	}

	// k线图 k线区域初始化
	private void initKChartArea() {
		mKChartArea = new Area(); // k线区 定义
		mKChartArea.setName("KChartArea");
		mKChartArea.getAppearance().setPrimaryFillColor(-16777216);
		mKChartArea.getAppearance().setSecondaryFillColor(-16777216);
		mKChartArea.getAppearance().setAllColors(-16777216);
		mKChartArea.getLeftAxis().setVisible(false);
		mKChartArea.getTopAxis().setVisible(false);
		mKChartArea.getBottomAxis().setVisible(true);
		mKChartArea.setHorizontalGridVisible(true);
		mStockChartView.getAreas().add(mKChartArea);
	}

	// K线图下部 指标区域 全年系列初始化
	private void initVolumnSeries() {
		this.mVolumnSeries = new BarSeries();
		this.mVolumnSeries.setName("Volumn");
		this.mVolumnSeries.setXAxisSide(Axis.Side.BOTTOM);
		this.mVolumnSeries.setYAxisSide(Axis.Side.RIGHT);
	}

	// K线图下部 指标区域
	private void initIndicatorArea() {
		this.mIndicatorArea = new Area(); // 指标区 定义
		this.mIndicatorArea.setName("IndicatorArea");
		this.mIndicatorArea.getTopAxis().setVisible(false);
		this.mIndicatorArea.getLeftAxis().setVisible(false);
		this.mIndicatorArea.getBottomAxis().setVisible(false);
		this.mIndicatorArea.getAppearance().setAllColors(-16777216);
		this.mIndicatorArea.getAppearance().setTextColor(-1);
		this.mIndicatorArea.getAppearance().setOutlineColor(-16777216);
		this.mIndicatorArea.setHeightInPercents(0.3F);
		this.mIndicatorArea.setAutoHeight(false);
		this.mStockChartView.getAreas().add(this.mIndicatorArea);
	}

	// k线图系列初始化
	private void initKChartSeries() {
		this.mKChartSeries = new StockSeries();
		this.mKChartSeries.setName("KChartSeries");
		this.mKChartSeries.setViewType(StockSeries.ViewType.CANDLESTICK);
		this.mKChartSeries.setYAxisSide(Axis.Side.RIGHT);
		Appearance localAppearance1 = this.mKChartSeries.getRiseAppearance();
		localAppearance1.setOutlineStyle(Appearance.OutlineStyle.SOLID);
		localAppearance1.setOutlineWidth(1.0F);
		localAppearance1.setAllColors(-65536);
		localAppearance1.setOutlineColor(-65536);
		Appearance localAppearance2 = this.mKChartSeries.getFallAppearance();
		localAppearance2.setOutlineStyle(Appearance.OutlineStyle.SOLID);
		localAppearance2.setOutlineWidth(1.0F);
		localAppearance2.setAllColors(-16711936);
		localAppearance2.setOutlineColor(-16711936);
	}

	// K线图下部系列 指示符
	private void initIndicatorSeries() {
		mIndicatorSeries = new LinearSeries();
		mIndicatorSeries.setName("IndicatorSeries");
		mIndicatorSeries.setYAxisSide(Axis.Side.LEFT);
	}

	// 被选中的时间控件
	private void indicatorBgChange(int paramInt) {
		LinearLayout localLinearLayout = (LinearLayout) findViewById(R.id.stock_indicator_select);
		for (int i = 0; i < localLinearLayout.getChildCount(); i++) {
			View localView = localLinearLayout.getChildAt(i);
			if (i == paramInt) {
				localView.setSelected(true);
			} else {
				localView.setSelected(false);
			}
		}
	}

	// K线图指示符线条初始化
	private void initKChartIndicatorLine() {
		LinearSeries localLinearSeries1 = new LinearSeries();
		localLinearSeries1.setName("line1");
		localLinearSeries1.getAppearance().setAllColors(-1);
		LinearSeries localLinearSeries2 = new LinearSeries();
		localLinearSeries2.setName("line2");
		localLinearSeries2.getAppearance().setAllColors(-256);
		LinearSeries localLinearSeries3 = new LinearSeries();
		localLinearSeries3.setName("line3");
		localLinearSeries3.getAppearance().setAllColors(-16711936);
		this.mKChartArea.getSeries().add(localLinearSeries1);
		this.mKChartArea.getSeries().add(localLinearSeries2);
		this.mKChartArea.getSeries().add(localLinearSeries3);
	}

	// 事件处理 设置
	private void stockWireEvent() {
		this.mKChartArea.getRightAxis().getAppearance()
				.setPrimaryFillColor(-16777216);
		this.mKChartArea.getRightAxis().getAppearance().setTextColor(-1);
		this.mKChartArea.getBottomAxis().getAppearance()
				.setPrimaryFillColor(-16777216);
		this.mKChartArea.getBottomAxis().getAppearance().setTextColor(-1);
		this.mIndicatorArea.getRightAxis().getAppearance()
				.setPrimaryFillColor(-16777216);
		this.mIndicatorArea.getRightAxis().getAppearance().setTextColor(-1);
		// 设置X轴 为时间
		this.mKChartArea.getBottomAxis().setLabelFormatProvider(
				new Axis.ILabelFormatProvider() {
					@Override
					public String getAxisLabel(Axis sender, double value) {
						try {
							Area localArea = sender.getParent();
							for (int i = 0; i < localArea.getSeries().size(); i++) {
								SeriesBase localSeriesBase = (SeriesBase) localArea
										.getSeries().get(i);
								int index = localSeriesBase
										.convertToArrayIndex(value);
								if (index < 0)
									index = 0;
								if ((index >= 0)
										&& (index < localSeriesBase
												.getPointCount())) {
									return localSeriesBase.getPointAt(index)
											.getID().toString();
								}
							}
						} catch (Exception e) {
						}

						return null;
					}
				});
		// 设置Y轴 的数值 格式
		this.mKChartArea.getRightAxis().setLabelFormatProvider(
				new Axis.ILabelFormatProvider() {
					public String getAxisLabel(Axis paramAnonymousAxis,
							double paramAnonymousDouble) {
						try {
							return String
									.valueOf(round(paramAnonymousDouble, 2));
						} catch (Exception localException) {
						}
						return null;
					}
				});
		// 设置指示符区的 Y轴 的数值 格式
		this.mIndicatorArea.getRightAxis().setLabelFormatProvider(
				new Axis.ILabelFormatProvider() {
					public String getAxisLabel(Axis paramAnonymousAxis,
							double paramAnonymousDouble) {
						try {
							return String
									.valueOf(round(paramAnonymousDouble, 3));
						} catch (Exception localException) {
						}
						return null;
					}
				});
	}

	private void getStockChartData() {
		if (iGetGoldTask == null) {
			this.mKChartSeries.getPoints().clear();
			this.mIndicatorSeries.getPoints().clear();
			this.mKChartArea.getLines().clear();
			this.mVolumnSeries.getPoints().clear();
			iGetGoldTask = new GetGoldTask();
			iGetGoldTask.execute();
		}
	}

	private void getTimeStockChartData() {
		if (iGetTimeGoldTask == null) {
			this.mCloseSeries.getPoints().clear();
			this.m48CloseSeries.getPoints().clear();
			this.m72CloseSeries.getPoints().clear();
			this.m96CloseSeries.getPoints().clear();
			iGetTimeGoldTask = new GetTimeGoldTask();
			iGetTimeGoldTask.execute();
		}
	}

	private void getMetalPriceData() {
		if (iGetMetalPriceTask == null) {
			iGetMetalPriceTask = new GetMetalPriceTask();
			iGetMetalPriceTask.execute();
		}
	}

	private class GetMetalPriceTask extends
			AsyncTask<Void, Void, ArrayList<Object>> {
		String[] strAarray;
		String mUrl, errorString;
		String p_now, p_updwon, p_updown_str, p_high, p_low, p_today,
				p_yesterday;

		@Override
		protected void onPreExecute() {
			mUrl = "http://apphome.sinaapp.com/dc/A/Api/tdata?os=android&tid="
					+ mCurrentChartCode + "&apitoken=";
			errorString = null;
		}

		@Override
		protected ArrayList<Object> doInBackground(Void... params) {
			try {
				String json = HttpPostRequest.getDataFromWebServer(
						StockChartsActivity.this, mUrl);
				if (json == null) {
					errorString = "nodata";
					return null;
				}
				strAarray = json.split(",");
				p_now = strAarray[2];
				p_updwon = strAarray[7];
				p_updown_str = strAarray[8];
				p_high = strAarray[5];
				p_low = strAarray[6];
				p_today = strAarray[4];
				p_yesterday = strAarray[9];
			} catch (Exception e) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(ArrayList<Object> result) {
			iGetMetalPriceTask = null;
			try {
				if (errorString == null) {
					stock_price_now.setText(p_now);
					if (p_updown_str != null)
						stock_price_updown.setText(p_updwon + "("
								+ p_updown_str + ")");
					stock_price_yesterday.setText(p_yesterday);
					stock_price_today.setText(p_today);
					stock_price_high.setText(p_high);
					stock_price_low.setText(p_low);
					float f = Float.parseFloat(p_updwon);
					if (f > 0.0F) {
						stock_price_now.setTextColor(-65536);
						stock_price_updown.setTextColor(-65536);
					} else if (f == 0.0F) {
						stock_price_now.setTextColor(-1);
						stock_price_updown.setTextColor(-1);
					}
				}
			} catch (Exception e) {
			}

		}
	}

	private class GetTimeGoldTask extends
			AsyncTask<Void, Void, ArrayList<Object>> {
		String errorString, str2, mUrl;
		String[] strAarray2, strAarray1;
		int len;

		@Override
		protected void onPreExecute() {
			mUrl = "http://apphome.sinaapp.com/dc/A/Api/trendData?os=android&tid=ZSUSD&t="
					+ mCurrentTimeChartTYP + "";
			errorString = null;
			mDataRefreshView.setVisibility(View.VISIBLE);
		}

		@Override
		protected ArrayList<Object> doInBackground(Void... params) {
			String json = HttpPostRequest.getDataFromWebServer(
					StockChartsActivity.this, mUrl);
			if (json == null) {
				errorString = "nodata";
				return null;
			}
			strAarray1 = json.split("\n");
			len = strAarray1.length;

			mChartBeans.clear();
			TimeChartBean localTimeChartBean = new TimeChartBean();
			LinePoint localLinePoint2;
			for (int i = 0; i < len; i++) {
				strAarray2 = strAarray1[i].split(",");

				str2 = metalDateFormat3(strAarray2[0], strAarray2[1]);
				localLinePoint2 = new LinePoint(
						Double.parseDouble(strAarray2[2]));
				localLinePoint2.setID(str2);
				localTimeChartBean.getLinePoints().add(localLinePoint2);
				mChartBeans.add(localTimeChartBean);
			}
			return null;
		}

		@Override
		protected void onPostExecute(ArrayList<Object> result) {
			iGetTimeGoldTask = null;
			try {
				mDataRefreshView.setVisibility(View.GONE);
				if (errorString == null) {
					int len = mChartBeans.size();
					int j = string2Int((String) ((LinePoint) ((TimeChartBean) mChartBeans
							.get(0)).getLinePoints().get(0)).getID());
					TimeChartBean localTimeChartBean;
					LinearSeries localLinearSeries;
					boolean bool1 = false;
					boolean bool2;
					if (j == 0)
						bool1 = true;
					for (int i = 0; i < len; i++) {
						bool2 = false;
						localTimeChartBean = (TimeChartBean) mChartBeans.get(i);
						if (i == 0) {
							localLinearSeries = (LinearSeries) mLinearSeries
									.get(i);
							localLinearSeries.getPoints().addAll(
									localTimeChartBean.getLinePoints());
							localLinearSeries.setVisible(true);
						}

						int m = string2Int((String) ((LinePoint) localTimeChartBean
								.getLinePoints().get(0)).getID());
						if (m == 0)
							bool2 = true;
					}
				}

			} catch (Exception e) {
			}

		}
	}

	private String metalDateFormat3(String date, String time) {
		if (Integer.parseInt(time) == 0)
			return date.substring(0, 4) + "-" + date.substring(4, 6) + "-"
					+ date.substring(6, 8);
		switch (time.length()) {
		case 1:
			time = "00000" + time;
			break;
		case 2:
			time = "0000" + time;
			break;
		case 3:
			time = "000" + time;
			break;
		case 4:
			time = "00" + time;
			break;
		case 5:
			time = "0" + time;
			break;
		}
		return date.substring(4, 6) + "-" + date.substring(6, 8) + " "
				+ time.substring(0, 2) + ":" + time.substring(2, 4);
	}

	private class GetGoldTask extends AsyncTask<Void, Void, ArrayList<Object>> {
		StockPoint iStockPoint;
		String errorString, str2, mUrl;
		String[] strAarray2, strAarray1;
		int len;

		@Override
		protected void onPreExecute() {
			mUrl = "http://apphome.sinaapp.com/dc/A/Api/kdata?os=android&tid=ZSUSD&tt="
					+ mCurrentKLineType + "&apitoken=";
			errorString = null;
			mDataRefreshView.setVisibility(View.VISIBLE);
		}

		@Override
		protected ArrayList<Object> doInBackground(Void... params) {
			String json = HttpPostRequest.getDataFromWebServer(
					StockChartsActivity.this, mUrl);
			if (json == null) {
				errorString = "nodata";
				return null;
			}
			strAarray1 = json.split("\n");
			LinePoint iLinePoint;
			len = strAarray1.length;
			for (int i = 2; i < len; i++) {
				strAarray2 = strAarray1[i].split(",");
				str2 = metalDateFormat(strAarray2[0], strAarray2[1]);
				iStockPoint = new StockPoint();
				iStockPoint.setHigh(Double.parseDouble(strAarray2[3]));
				iStockPoint.setLow(Double.parseDouble(strAarray2[4]));
				iStockPoint.setOpen(Double.parseDouble(strAarray2[2]));
				iStockPoint.setClose(Double.parseDouble(strAarray2[5]));
				iStockPoint.setID(str2);
				mKChartSeries.getPoints().add(iStockPoint);
				iLinePoint = mIndicatorSeries.addPoint(iStockPoint.getClose());
				iLinePoint.setID(iStockPoint.getID());

				mCloseSeries.getPoints().add(iLinePoint);

				if (i == (len - 1)) {
					mKChartSeries.setLastValue(iStockPoint.getClose());
					mIndicatorSeries.setLastValue(iStockPoint.getClose());
				}
			}

			return null;
		}

		@Override
		protected void onPostExecute(ArrayList<Object> result) {
			if (len < 1) {
				tv_change_indicator.setEnabled(false);
			} else {
				tv_change_indicator.setEnabled(true);
			}
			iGetGoldTask = null;
			try {
				mDataRefreshView.setVisibility(View.GONE);
				if (errorString == null) {

					if (len < 50) {
						mStockChartView.getGlobalAxisRange(Axis.Side.BOTTOM)
								.setViewValues(
										AxisRange.ViewValues.SCROLL_TO_FIRST);
					} else {
						mStockChartView.getGlobalAxisRange(Axis.Side.BOTTOM)
								.expandAutoValues(len, -50 + len);
						mStockChartView.getGlobalAxisRange(Axis.Side.BOTTOM)
								.setViewValues(len, -50 + len);
					}
					stockViewRecalc();

					// 显示 已经选择指标
					showSelcectedIndicator();
				}

			} catch (Exception e) {
			}

		}
	}

	// K线图表等重新计算
	private void stockViewRecalc() {
		this.mStockChartView.recalcIndicators();
		this.mStockChartView.invalidate();
	}

	// 显示Volumn
	private void showVolumnSeries() {
		this.mIndicatorArea.getSeries().clear();
		if (mVolumnSeries == null)
			initVolumnSeries();
		this.mIndicatorArea.setTitle("VOLUMN");
		this.mIndicatorArea.getSeries().add(this.mVolumnSeries);

		stockViewRecalc();
	}

	// 显示SMA指示符
	private void showSMAIndicator() {
		if (this.mSMA5Indicator == null) {
			LinearSeries localLinearSeries3 = (LinearSeries) this.mKChartArea
					.findSeriesByName("line1");
			localLinearSeries3.getAppearance().setOutlineWidth(1.0F);
			this.mSMA5Indicator = new SmaIndicator(this.mKChartSeries, 0,
					localLinearSeries3);
			this.mSMA5Indicator.setPeriodsCount(5);
		}
		if (this.mSMA10Indicator == null) {
			LinearSeries localLinearSeries2 = (LinearSeries) this.mKChartArea
					.findSeriesByName("line2");
			localLinearSeries2.getAppearance().setOutlineWidth(1.0F);
			this.mSMA10Indicator = new SmaIndicator(this.mKChartSeries, 1,
					localLinearSeries2);
			this.mSMA10Indicator.setPeriodsCount(10);
		}
		if (this.mSMA20Indicator == null) {
			LinearSeries localLinearSeries1 = (LinearSeries) this.mKChartArea
					.findSeriesByName("line3");
			localLinearSeries1.getAppearance().setOutlineWidth(1.0F);
			this.mSMA20Indicator = new SmaIndicator(this.mKChartSeries, 1,
					localLinearSeries1);
			this.mSMA20Indicator.setPeriodsCount(20);
		}
		mKChartArea.findSeriesByName("line1").setVisible(true);
		mKChartArea.findSeriesByName("line2").setVisible(true);
		mKChartArea.findSeriesByName("line3").setVisible(true);
		this.mStockChartView.getIndicators().add(mSMA5Indicator);
		this.mStockChartView.getIndicators().add(mSMA10Indicator);
		this.mStockChartView.getIndicators().add(mSMA20Indicator);
		stockViewRecalc();// K线图表等重新计算
		double d1, d2, d3;
		String str = "";
		if (this.mKChartArea.findSeriesByName("line1").hasPoints()) {
			double[] arrayOfDouble1 = this.mKChartArea
					.findSeriesByName("line1").getLastPoint().getValues();
			double[] arrayOfDouble2 = this.mKChartArea
					.findSeriesByName("line2").getLastPoint().getValues();
			double[] arrayOfDouble3 = this.mKChartArea
					.findSeriesByName("line3").getLastPoint().getValues();
			// Log.i("","tag ss12");
			d1 = round(arrayOfDouble1[0], 2);
			d2 = round(arrayOfDouble2[0], 2);
			d3 = round(arrayOfDouble3[0], 2);
			str = "<font color=\"" + getResources().getColor(R.color.white)
					+ "\">SMA5(" + d1 + ")</font> <font color=\""
					+ getResources().getColor(R.color.yellow) + "\">SMA10("
					+ d2 + ")</font> <font color=\""
					+ getResources().getColor(R.color.green) + "\">SMA20(" + d3
					+ ")</font>";

		} else {
			str = "<font color=\"" + getResources().getColor(R.color.white)
					+ "\">SMA5</font> <font color=\""
					+ getResources().getColor(R.color.yellow)
					+ "\">SMA10</font> <font color=\""
					+ getResources().getColor(R.color.green)
					+ "\">SMA20</font>";// + "(点击该图形切换指标)";
		}
		this.mTitleView.setVisibility(View.VISIBLE);
		this.mTitleView.setText(Html.fromHtml(str));
	}

	// 显示MACD指示符
	private void showMACDIndicator() {
		LinearSeries localLinearSeries1 = new LinearSeries();
		localLinearSeries1.getAppearance().setAllColors(-256);
		localLinearSeries1.getAppearance().setOutlineWidth(1.0F);
		LinearSeries localLinearSeries2 = new LinearSeries();
		localLinearSeries2.getAppearance().setAllColors(-1);
		localLinearSeries2.getAppearance().setOutlineWidth(1.0F);
		BarSeries localBarSeries = new BarSeries();
		this.mIndicatorArea.getSeries().add(localLinearSeries1);
		this.mIndicatorArea.getSeries().add(localLinearSeries2);
		this.mIndicatorArea.getSeries().add(localBarSeries);
		this.mIndicatorArea.getLines().add(new Line(0.0D, Axis.Side.RIGHT));
		MacdIndicator localMacdIndicator = new MacdIndicator(
				this.mIndicatorSeries, 0, localLinearSeries1,
				localLinearSeries2, localBarSeries);
		this.mStockChartView.getIndicators().add(localMacdIndicator);
		stockViewRecalc();
		if (localMacdIndicator.getDstMacd().hasPoints()) {
			double d = round(localMacdIndicator.getDstMacd().getLastPoint()
					.getValues()[0], 3);
			this.mIndicatorArea.setTitle("MACD(12,26,9) DIF(黄" + d + ")");
		} else {
			this.mIndicatorArea.setTitle("MACD(12,26,9) DIF(黄)");
		}
	}

	// 显示EMA指示符
	private void showEMAIndicator() {
		if (this.mEMA5Indicator == null) {
			LinearSeries localLinearSeries3 = (LinearSeries) this.mKChartArea
					.findSeriesByName("line1");
			this.mEMA5Indicator = new EmaIndicator(this.mKChartSeries, 0,
					localLinearSeries3);
			this.mEMA5Indicator.setPeriodsCount(5);
		}
		if (this.mEMA10Indicator == null) {
			LinearSeries localLinearSeries2 = (LinearSeries) this.mKChartArea
					.findSeriesByName("line2");
			this.mEMA10Indicator = new EmaIndicator(this.mKChartSeries, 0,
					localLinearSeries2);
			this.mEMA10Indicator.setPeriodsCount(10);
		}
		if (this.mEMA20Indicator == null) {
			LinearSeries localLinearSeries1 = (LinearSeries) this.mKChartArea
					.findSeriesByName("line3");
			this.mEMA20Indicator = new EmaIndicator(this.mKChartSeries, 0,
					localLinearSeries1);
			this.mEMA20Indicator.setPeriodsCount(20);
		}
		this.mKChartArea.findSeriesByName("line3").setVisible(true);
		this.mKChartArea.findSeriesByName("line1").setVisible(true);
		this.mKChartArea.findSeriesByName("line2").setVisible(true);
		this.mStockChartView.getIndicators().add(this.mEMA5Indicator);
		this.mStockChartView.getIndicators().add(this.mEMA10Indicator);
		this.mStockChartView.getIndicators().add(this.mEMA20Indicator);
		stockViewRecalc();
		double d1, d2, d3;
		String str = "";
		if (this.mKChartArea.findSeriesByName("line1").hasPoints()) {
			double[] arrayOfDouble1 = this.mKChartArea
					.findSeriesByName("line1").getLastPoint().getValues();
			double[] arrayOfDouble2 = this.mKChartArea
					.findSeriesByName("line2").getLastPoint().getValues();
			double[] arrayOfDouble3 = this.mKChartArea
					.findSeriesByName("line3").getLastPoint().getValues();
			d1 = round(arrayOfDouble1[0], 2);
			d2 = round(arrayOfDouble2[0], 2);
			d3 = round(arrayOfDouble3[0], 2);
			str = "<font color=\"" + getResources().getColor(R.color.white)
					+ "\">EMA5(" + d1 + ")</font> <font color=\""
					+ getResources().getColor(R.color.yellow) + "\">EMA10("
					+ d2 + ")</font> <font color=\""
					+ getResources().getColor(R.color.green) + "\">EMA20(" + d3
					+ ")</font>";
		} else {
			str = "<font color=\"" + getResources().getColor(R.color.white)
					+ "\">EMA5</font> <font color=\""
					+ getResources().getColor(R.color.yellow)
					+ "\">EMA10</font> <font color=\""
					+ getResources().getColor(R.color.green)
					+ "\">EMA20</font>";// + "(点击该图形切换指标)")
		}
		this.mTitleView.setVisibility(View.VISIBLE);
		this.mTitleView.setText(Html.fromHtml(str));
	}

	// 显示BOLL指示符
	private void showBOLLIndicator() {
		if (mBOLLIndicator == null) {
			LinearSeries localLinearSeries1 = (LinearSeries) this.mKChartArea
					.findSeriesByName("line1");
			LinearSeries localLinearSeries2 = (LinearSeries) this.mKChartArea
					.findSeriesByName("line2");
			LinearSeries localLinearSeries3 = (LinearSeries) this.mKChartArea
					.findSeriesByName("line3");
			mBOLLIndicator = new BollingerBandsIndicator(mKChartSeries, 0,
					localLinearSeries1, localLinearSeries2, localLinearSeries3);
		}
		this.mKChartArea.findSeriesByName("line1").setVisible(true);
		this.mKChartArea.findSeriesByName("line2").setVisible(true);
		this.mKChartArea.findSeriesByName("line3").setVisible(true);
		this.mStockChartView.getIndicators().add(this.mBOLLIndicator);
		stockViewRecalc();
		double d1, d2, d3;
		String str = "";
		if (this.mBOLLIndicator.getDstSMA().hasPoints()) {
			d1 = round(this.mBOLLIndicator.getDstSMA().getLastPoint()
					.getValues()[0], 2);
			d2 = round(this.mBOLLIndicator.getDstUpperBand().getLastPoint()
					.getValues()[0], 2);
			d3 = round(this.mBOLLIndicator.getDstLowerBand().getLastPoint()
					.getValues()[0], 2);

			str = "<font color=\"" + getResources().getColor(R.color.white)
					+ "\">BOLL (MB" + d1 + ")</font> <font color=\""
					+ getResources().getColor(R.color.yellow) + "\">(UP" + d2
					+ ")</font> <font color=\""
					+ getResources().getColor(R.color.green) + "\">(DN" + d3
					+ ")</font>";
		} else {
			str = "<font color=\"" + getResources().getColor(R.color.white)
					+ "\">BOLL布林线</font>";
		}
		this.mTitleView.setVisibility(View.VISIBLE);
		this.mTitleView.setText(Html.fromHtml(str));
	}

	// 显示ENV指示符
	private void showENVIndicator() {
		if (this.mEnvelopesIndicator == null) {
			LinearSeries localLinearSeries1 = (LinearSeries) this.mKChartArea
					.findSeriesByName("line1");
			LinearSeries localLinearSeries2 = (LinearSeries) this.mKChartArea
					.findSeriesByName("line2");
			this.mEnvelopesIndicator = new EnvelopesIndicator(
					this.mKChartSeries, 0, localLinearSeries1,
					localLinearSeries2);
		}
		this.mKChartArea.findSeriesByName("line1").setVisible(true);
		this.mKChartArea.findSeriesByName("line2").setVisible(true);
		this.mKChartArea.findSeriesByName("line3").setVisible(false);
		this.mStockChartView.getIndicators().add(this.mEnvelopesIndicator);
		stockViewRecalc();
		double d1, d2;
		String str = "";
		if (this.mEnvelopesIndicator.getDstUpperEnvelope().hasPoints()) {
			d1 = round(this.mEnvelopesIndicator.getDstUpperEnvelope()
					.getLastPoint().getValues()[0], 2);
			d2 = round(this.mEnvelopesIndicator.getDstLowerEnvelope()
					.getLastPoint().getValues()[0], 2);
			str = "<font color=\"" + getResources().getColor(R.color.white)
					+ "\"> " + "ENV轨道线" + "UP(" + d1
					+ ")</font> <font color=\""
					+ getResources().getColor(R.color.yellow) + "\">LOW(" + d2
					+ ")</font>";
		} else {

			str = "<font color=\"" + getResources().getColor(R.color.white)
					+ "\"> " + "ENV轨道线" + "</font>";
		}
		this.mTitleView.setVisibility(View.VISIBLE);
		this.mTitleView.setText(Html.fromHtml(str));
	}

	// 显示KDJ指示符
	private void showKDJIndicator() {
		LinearSeries localLinearSeries1 = new LinearSeries();
		localLinearSeries1.getAppearance().setAllColors(-65536);
		localLinearSeries1.getAppearance().setOutlineWidth(1.0F);
		LinearSeries localLinearSeries2 = new LinearSeries();
		localLinearSeries2.getAppearance().setOutlineWidth(1.0F);
		localLinearSeries2.getAppearance().setAllColors(-16711936);
		this.mIndicatorArea.getSeries().add(localLinearSeries2);
		this.mIndicatorArea.getSeries().add(localLinearSeries1);
		this.mKDJIndicator = new StochasticIndicator(this.mKChartSeries, 0,
				localLinearSeries1, localLinearSeries2);
		this.mKDJIndicator.setPeriodsCount(9);
		this.mStockChartView.getIndicators().add(this.mKDJIndicator);
		stockViewRecalc();
		if (this.mKDJIndicator.getDstSlowK().hasPoints()) {
			double d1 = round(this.mKDJIndicator.getDstSlowK().getLastPoint()
					.getValues()[0], 3);
			double d2 = round(this.mKDJIndicator.getDstSlowD().getLastPoint()
					.getValues()[0], 3);
			this.mIndicatorArea
					.setTitle("KDJ(9) K(红" + d1 + ") D(绿" + d2 + ")");
		} else {
			this.mIndicatorArea.setTitle("KDJ(9)");
		}
	}

	// 显示Rsi指示符
	private void showRSIIndicator() {
		LinearSeries localLinearSeries1 = new LinearSeries();
		localLinearSeries1.getAppearance().setAllColors(-1);
		localLinearSeries1.getAppearance().setOutlineWidth(1.0F);
		LinearSeries localLinearSeries2 = new LinearSeries();
		localLinearSeries2.getAppearance().setOutlineWidth(1.0F);
		localLinearSeries2.getAppearance().setAllColors(-256);
		LinearSeries localLinearSeries3 = new LinearSeries();
		localLinearSeries3.getAppearance().setOutlineWidth(1.0F);
		localLinearSeries3.getAppearance().setAllColors(-16711936);
		this.mIndicatorArea.getSeries().add(localLinearSeries1);
		this.mIndicatorArea.getSeries().add(localLinearSeries2);
		this.mIndicatorArea.getSeries().add(localLinearSeries3);
		this.mIndicatorArea.getAppearance().setTextSize(20.0F);
		this.mRSI6Indicator = new RsiIndicator(this.mIndicatorSeries, 0,
				localLinearSeries1);
		this.mRSI6Indicator.setPeriodsCount(6);
		this.mRSI6Indicator.recalc();
		this.mRSI12Indicator = new RsiIndicator(this.mIndicatorSeries, 0,
				localLinearSeries2);
		this.mRSI12Indicator.setPeriodsCount(12);
		this.mRSI24Indicator = new RsiIndicator(this.mIndicatorSeries, 0,
				localLinearSeries3);
		this.mRSI24Indicator.setPeriodsCount(24);
		this.mStockChartView.getIndicators().add(this.mRSI6Indicator);
		this.mStockChartView.getIndicators().add(this.mRSI12Indicator);
		this.mStockChartView.getIndicators().add(this.mRSI24Indicator);
		stockViewRecalc();
		if (this.mRSI6Indicator.getDst().hasPoints()) {
			double d1 = round(this.mRSI6Indicator.getDst().getLastPoint()
					.getValues()[0], 2);
			double d2 = round(this.mRSI12Indicator.getDst().getLastPoint()
					.getValues()[0], 2);
			double d3 = round(this.mRSI24Indicator.getDst().getLastPoint()
					.getValues()[0], 2);
			this.mIndicatorArea.setTitle("RSI(6(白 " + d1 + "),12(黄 " + d2
					+ "),24(绿 " + d3 + "))");
		} else {
			this.mIndicatorArea.setTitle("RSI(6(白),12(黄),24(绿))");
		}
	}

	// 显示 已经选择指标
	private void showSelcectedIndicator() {
		switch (isPreferences.getSp().getInt("mShowDownIndicator", 1)) {
		case 0:
			showVolumnSeries();
			break;// VOL成交量 下区 0
		case 1:// MACD指标 下区 1
			mIndicatorArea.getSeries().clear();
			mIndicatorArea.getLines().clear();
			showMACDIndicator();
			break;
		case 2:// KDJ随机指标 下区 2
			mIndicatorArea.getSeries().clear();
			mIndicatorArea.getLines().clear();
			showKDJIndicator();
			break;
		case 3:// RSI强弱指标 下区 3
			mIndicatorArea.getSeries().clear();
			mIndicatorArea.getLines().clear();
			showRSIIndicator();
			break;
		}
		switch (isPreferences.getSp().getInt("mShowUpIndicator", 1)) {
		case 0:
			showSMAIndicator();
			break;// SMA均线 上区 0
		case 1:// BOLL布林线 上区 1
			showBOLLIndicator();
			break;
		case 2:// EMA均线 上区 2
			showEMAIndicator();
			break;
		case 3:// ENV指标 上区 3
			showENVIndicator();
			break;
		}
	}

	// 指标选择
	private void changeIndicator() {
		// 新建AlertDialog对话框 指标选择 对话框
		new AlertDialog.Builder(this).setItems(R.array.indicator_type,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:// MACD指标 下区 1
							if (isPreferences.getSp().getInt(
									"mShowDownIndicator", 1) != 1) {
								mIndicatorArea.getSeries().clear();
								mIndicatorArea.getLines().clear();
								showMACDIndicator();
								isPreferences.updateSp("mShowDownIndicator", 1);
							}
							break;
						case 1:// BOLL布林线 上区 1
							if (isPreferences.getSp().getInt(
									"mShowUpIndicator", 0) != 1) {
								showBOLLIndicator();
								isPreferences.updateSp("mShowUpIndicator", 1);
							}
							break;
						case 2:// KDJ随机指标 下区 2
							if (isPreferences.getSp().getInt(
									"mShowDownIndicator", 1) != 2) {
								mIndicatorArea.getSeries().clear();
								mIndicatorArea.getLines().clear();
								showKDJIndicator();
								isPreferences.updateSp("mShowDownIndicator", 2);
							}
							break;
						case 3:// RSI强弱指标 下区 3
							if (isPreferences.getSp().getInt(
									"mShowDownIndicator", 1) != 3) {
								mIndicatorArea.getSeries().clear();
								mIndicatorArea.getLines().clear();
								showRSIIndicator();
								isPreferences.updateSp("mShowDownIndicator", 3);
							}
							break;
						case 4:// SMA均线 上区 0
							if (isPreferences.getSp().getInt(
									"mShowUpIndicator", 0) != 0) {
								showSMAIndicator();
								isPreferences.updateSp("mShowUpIndicator", 0);
							}
							break;
						case 5:// EMA均线 上区 2
							if (isPreferences.getSp().getInt(
									"mShowUpIndicator", 0) != 2) {
								showEMAIndicator();
								isPreferences.updateSp("mShowUpIndicator", 2);
							}
							break;
						case 6:// ENV指标 上区 3
							if (isPreferences.getSp().getInt(
									"mShowUpIndicator", 0) != 3) {
								showENVIndicator();
								isPreferences.updateSp("mShowUpIndicator", 3);
							}
							break;
						}
					}
				}).show();
	}

	// K线图 几分线 时间选择
	private void changeKLineType(int iKLineType) {
		tv_title.setText(mChartTitle + "-" + "K线" + " "
				+ mStockIndicatorTiemStrings[iKLineType]);
		mCurrentKLineType = mKLineTypes[iKLineType];
		mCurrentKLineTYPIndex = iKLineType;
		indicatorBgChange(iKLineType);
		getStockChartData();
	}

	// 被选中的时间控件
	private void indicatorTimeBgChange(int paramInt) {
		LinearLayout localLinearLayout = (LinearLayout) findViewById(R.id.time_chart_layout);
		for (int i = 0; i < localLinearLayout.getChildCount(); i++) {
			View localView = localLinearLayout.getChildAt(i);
			if (i == paramInt) {
				localView.setSelected(true);
			} else {
				localView.setSelected(false);
			}
		}
	}

	// 分时图 几分线 时间选择
	private void changeTLineType(int iTLineType) {
		tv_title.setText(mChartTitle + "-" + "实时走势" + " "
				+ mTimChartTypes[iTLineType]);
		mCurrentTimeChartTYP = iTLineType;
		getTimeStockChartData();
	}

	private String metalDateFormat(String date, String time) {
		if (Integer.parseInt(time) == 0)
			return date.substring(0, 4) + "-" + date.substring(4, 6) + "-"
					+ date.substring(6, 8);
		switch (time.length()) {
		case 1:
			time = "000" + time;
			break;
		case 2:
			time = "00" + time;
			break;
		case 3:
			time = "0" + time;
			break;
		}
		return date.substring(4, 6) + "-" + date.substring(6, 8) + " "
				+ time.substring(0, 2) + ":" + time.substring(2, 4);
	}

	private String metalDateFormat2(String date, String time) {
		if (Integer.parseInt(time) == 0)
			return date.substring(0, 4) + "-" + date.substring(4, 6) + "-"
					+ date.substring(6, 8);
		switch (time.length()) {
		case 3:
			time = "000" + time;
			break;
		case 4:
			time = "00" + time;
			break;
		case 5:
			time = "0" + time;
			break;
		}
		return date.substring(4, 6) + "-" + date.substring(6, 8) + " "
				+ time.substring(0, 2) + ":" + time.substring(2, 4);
	}

	/** 小数点 四舍五入1 **/
	private static double round(double paramDouble, int paramInt) {
		return round(paramDouble, paramInt, 6);
	}

	/** 小数点 四舍五入2 **/
	private static double round(double paramDouble, int paramInt1, int paramInt2) {
		if (paramInt1 < 0)
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		return new BigDecimal(Double.toString(paramDouble)).setScale(paramInt1,
				paramInt2).doubleValue();
	}

	private int string2Int(String paramString) {
		String str = paramString.substring(0, 1);
		if (paramString.length() % 2 == 1)
			str = paramString.substring(0, 2);
		return Integer.parseInt(str);
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		return false;
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_refresh:
			getMetalPriceData();
			getStockChartData();
			getTimeStockChartData();
			break;
		case R.id.time_chart_oneDay:
			changeTLineType(1);
			break;
		case R.id.time_chart_twoDay:
			changeTLineType(2);
			break;
		case R.id.time_chart_threeDay:
			changeTLineType(3);
			break;
		case R.id.time_chart_fourDay:
			changeTLineType(4);
			break;
		case R.id.indicator_oneM:
			changeKLineType(0);
			break;
		case R.id.indicator_fiveM:
			changeKLineType(1);
			break;
		case R.id.indicator_fifM:
			changeKLineType(2);
			break;
		case R.id.indicator_thirtyM:
			changeKLineType(3);
			break;
		case R.id.indicator_sixtyM:
			changeKLineType(4);
			break;
		case R.id.indicator_fourHour:
			changeKLineType(5);
			break;
		case R.id.indicator_oneDay:
			changeKLineType(6);
			break;
		case R.id.indicator_oneWeek:
			changeKLineType(7);
			break;
		case R.id.indicator_oneMonth:
			changeKLineType(8);
			break;

		case R.id.chart_time_tab:
			time_chart_layout.setVisibility(View.VISIBLE);
			indicator_layout.setVisibility(View.GONE);
			mTimeTitleView.setVisibility(View.VISIBLE);
			mTitleView.setVisibility(View.GONE);
			mStockChartView.setVisibility(View.GONE);
			mTimeChartView.setVisibility(View.VISIBLE);
			changeTLineType(mCurrentTimeChartTYP);
			break;
		case R.id.chart_k_tab:
			tv_title.setText(mChartTitle + "-" + "K线" + " "
					+ mStockIndicatorTiemStrings[mCurrentKLineTYPIndex]);
			time_chart_layout.setVisibility(View.GONE);
			indicator_layout.setVisibility(View.VISIBLE);
			mTimeTitleView.setVisibility(View.GONE);
			mTitleView.setVisibility(View.VISIBLE);
			mStockChartView.setVisibility(View.VISIBLE);
			mTimeChartView.setVisibility(View.GONE);
			break;
		case R.id.tv_change_indicator:
			changeIndicator();
			break;
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (iGetGoldTask != null) {
			iGetGoldTask.cancel(true);
		}
		if (iGetMetalPriceTask != null) {
			iGetMetalPriceTask.cancel(true);
		}
		if (iGetTimeGoldTask != null) {
			iGetTimeGoldTask.cancel(true);
		}
	}
}
