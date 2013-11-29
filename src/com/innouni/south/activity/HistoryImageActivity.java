package com.innouni.south.activity;

import java.io.DataInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.innouni.south.app.MainApplication;
import com.innouni.south.base.BaseActivity;
import com.innouni.south.util.Util;

/**
 * 金银美元石油比页面
 * 
 * @author HuGuojun
 * @date 2013-11-28 下午3:43:35
 * @modify
 * @version 1.0.0
 */
public class HistoryImageActivity extends BaseActivity {

	private static final String[] urls = {"http://price.zhjtong.com/1_gh_740_500.png", "http://price.zhjtong.com/1_ying_740_500.png", 
							 "http://price.zhjtong.com/1_mz_740_500.png", "http://price.zhjtong.com/1_sy_740_500.png"};
	private LinearLayout layout;
	
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			ImageView view = (ImageView) layout.getChildAt(msg.what);
			view.setImageBitmap((Bitmap)msg.obj);
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history_image);
		application = MainApplication.getApplication();
		application.setActivity(this);
		initView();
		loadingImage();
	}

	private void initView() {
		layout = (LinearLayout) findViewById(R.id.lay_images);
		for (int i = 0; i < urls.length; i++) {
			ImageView view = new ImageView(this);
			view.setImageResource(R.drawable.logo72);
			view.setScaleType(ScaleType.FIT_XY);
			view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 
					Util.dip2px(HistoryImageActivity.this, 250)));
			layout.addView(view);
		}
	}
	
	private void loadingImage() {
		for (int i = 0; i < urls.length; i++) {
			final String url = urls[i];
			final int index = i;
			new Thread(new Runnable() {
				@Override
				public void run() {
					Bitmap map = Util.downloadImage(url);
					if (map != null) {
						Message msg = new Message();
						msg.what = index;
						msg.obj = map;
						handler.sendMessage(msg);
					}
				}
			}).start();
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		for (int i = 0; i < layout.getChildCount(); i++) {
			ImageView view = (ImageView) layout.getChildAt(i);
			Bitmap map = view.getDrawingCache();
			if (map != null) {
				map.recycle();
			}
		}
	}
}
