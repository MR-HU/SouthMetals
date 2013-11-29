package com.innouni.south.activity;

import uk.co.senab.photoview.PhotoViewAttacher;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.innouni.south.app.MainApplication;
import com.innouni.south.base.BaseActivity;
import com.innouni.south.net.HttpPostRequest;
import com.innouni.south.util.Util;

/**
 * 美元指数详情图片
 * 
 * @author HuGuojun
 * @date 2013-11-29 下午7:09:46
 * @modify
 * @version 1.0.0
 */
public class MeizhiImageActivity extends BaseActivity {

	private ImageView view;
	private PhotoViewAttacher attacher;
	
	private GetUrlTask task;
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {			case 1:
				if (msg.obj != null) {
					view.setImageBitmap((Bitmap)msg.obj);
				}
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_meizhi_iamge);
		application = MainApplication.getApplication();
		application.setActivity(this);
		
		view = (ImageView) findViewById(R.id.image_meizhi);
		attacher = new PhotoViewAttacher(view);
		attacher.canZoom();
		
		if (task != null) 
			task.cancel(true);
		task = new GetUrlTask();
		task.execute();
	}
	
	private class GetUrlTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			return HttpPostRequest.getDataFromWebServer(MeizhiImageActivity.this, "http://zhj8.sinaapp.com/Mobi/Pic/url/id/100");
		}

		@Override
		protected void onPostExecute(final String url) {
			super.onPostExecute(url);
			if (url != null && !url.equals("net_err")) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						Bitmap map = Util.downloadImage(url);
						if (map != null) {
							Message msg = handler.obtainMessage(1, map);
							handler.sendMessage(msg);
						}
					}
				}).start();
			}
		}
		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if (task != null) 
			task.cancel(true);
	}
}
