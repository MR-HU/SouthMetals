package com.innouni.south.push;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.innouni.south.app.MainApplication;
import com.innouni.south.net.HttpPostRequest;
import com.innouni.south.net.ParseJsonToList;

/**
 * 推送消息工具类 <br>
 * 每隔30秒请求服务器接收新消息
 * @author HuGuojun
 * @data 2013-10-31
 */
public class PushMessageUtils {

	private static final int PUSHTIME = 30*1000;
	
	private Handler handler = new Handler();
	private MainApplication application;
	private ParseJsonToList parser;
	private Context mContext;
	
	public PushMessageUtils(Context context) {
		mContext = context;
		application = MainApplication.getApplication();
		parser = ParseJsonToList.getInstance();
	}

	/**
	 * 开启推送
	 */
	public void startPush() {
		Log.v("push", "Push ---->startPush()");
		handler.removeCallbacks(messageRunnable);
		handler.postDelayed(messageRunnable, 0);
	}

	/**
	 * 停止推送
	 */
	public void stopPush(){
		Log.v("push", "Push ---->stopPush()");
		handler.removeCallbacks(messageRunnable);
	}
	
	private Runnable messageRunnable = new Runnable() {

		@Override
		public void run() {
			try {
				List<NameValuePair> pairs = new ArrayList<NameValuePair>();
				pairs.add(new BasicNameValuePair("userId", application.getUserInfo().getUserId().toString()));
				String json = HttpPostRequest.getDataFromWebServer(mContext, "pushMessage", pairs);
				Log.v("push", "推送返回 ---->" + json);
				if (json == null || json.equals("") || json.equals("net_err")) {
					setPushInfo(null);
				} else {
					JSONArray array = new JSONObject(json).getJSONArray("list");
					if (null != array && array.length() != 0) {
						//多个专家都有留言,以列表形式返回
						ArrayList<Object> list = parser.parseWebDataToList(json, PushInfo.class);
						setPushInfo(list);
						Intent intent = new Intent();
						intent.setAction(SouthMessageService.ACTIONTAG);
						mContext.sendBroadcast(intent);
					}
				}
			} catch (Exception e) {
				Log.v("push", " push messageRunnable->"+e.toString());
			}
			handler.postDelayed(messageRunnable, PUSHTIME);
		}
	};

	private List<Object> pushInfo;

	public List<Object> getPushInfo() {
		return pushInfo;
	}

	public void setPushInfo(List<Object> pushInfo) {
		this.pushInfo = pushInfo;
	}
}
