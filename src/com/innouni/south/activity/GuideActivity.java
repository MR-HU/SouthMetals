package com.innouni.south.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.innouni.south.app.MainApplication;
import com.innouni.south.base.BaseActivity;

/**
 * 新手引导页面
 * 
 * @author HuGuojun
 * @data 2013-08-12
 */
public class GuideActivity extends BaseActivity implements OnClickListener,
		OnPageChangeListener, OnTouchListener {

	private float downX, upX, distanceX;
	private int currentIndex = 0;
	private ViewPager viewPager;
	private List<View> views;
	private List<View> dots;

	private int[] welcomeIcon = { R.drawable.welcome_img1,
			R.drawable.welcome_img2, R.drawable.welcome_img3,
			R.drawable.welcome_img4, R.drawable.welcome_img5, };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		application = MainApplication.getApplication();
		application.setActivity(this);
		initDots();
		initView();
	}

	private void initDots() {
		dots = new ArrayList<View>();
		View one = (ImageView) findViewById(R.id.img_dot_first);
		View two = (ImageView) findViewById(R.id.img_dot_second);
		View three = (ImageView) findViewById(R.id.img_dot_third);
		View four = (ImageView) findViewById(R.id.img_dot_fouth);
		View five = (ImageView) findViewById(R.id.img_dot_fifth);
		dots.add(one);
		dots.add(two);
		dots.add(three);
		dots.add(four);
		dots.add(five);
	}

	private void initView() {
		viewPager = (ViewPager) findViewById(R.id.guide_viewpager);
		views = new ArrayList<View>();
		LayoutInflater layoutInflater = LayoutInflater.from(GuideActivity.this);
		int size = welcomeIcon.length;
		for (int i = 0; i < size; i++) {
			RelativeLayout view = (RelativeLayout) layoutInflater.inflate(
					R.layout.item_guide_viewpage, null);
			ImageView welcomeView = (ImageView) view
					.findViewById(R.id.image_welcome);
			welcomeView.setBackgroundResource(welcomeIcon[i]);
			Button enter = (Button) view.findViewById(R.id.enter_app);
			if (i == size - 1) {
				enter.setOnClickListener(this);
			} else {
			}
			views.add(view);
		}
		viewPager.setAdapter(new ViewPagerAdapter());
		viewPager.setCurrentItem(0);
		viewPager.setOnPageChangeListener(this);
		viewPager.setOnTouchListener(this);
	}

	private class ViewPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return views.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(views.get(arg1));
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(views.get(arg1), 0);
			return views.get(arg1);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.enter_app:
			Intent intent = new Intent();
			intent.setClass(GuideActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (null != views) {
			views.clear();
		}
		if (null != dots) {
			dots.clear();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (viewPager != null) {
			viewPager.removeAllViews();
			viewPager.destroyDrawingCache();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int position) {
		currentIndex = position;
		for (int i = 0; i < dots.size(); i++) {
			if (i == position) {
				dots.get(i).setBackgroundResource(R.drawable.welcome_tab_hover);
			} else {
				dots.get(i).setBackgroundResource(R.drawable.welcome_tab);
			}
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downX = event.getX();
			break;
		case MotionEvent.ACTION_UP:
			upX = event.getX();
			distanceX = Math.abs(upX - downX);
			if (currentIndex == 4 && distanceX < 15) {
				Intent intent = new Intent();
				intent.setClass(GuideActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
			break;
		default:
			break;
		}
		return false;
	}

}
