package com.innouni.south.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.innouni.south.app.MainApplication;
import com.innouni.south.base.BaseActivity;
import com.innouni.south.net.HttpPostRequest;

/**
 * 独家策略页面
 * 
 * @author HuGuojun
 * @data 2013-09-03
 */
public class ExpertOpinionActivity extends BaseActivity implements
		OnClickListener {

	private RelativeLayout questionLay, buyLay, payLay;
	private RelativeLayout voiceClassLay;
	private RelativeLayout commentLay;
	private TextView announcementView;
	private LinearLayout announceLay;
	private GetAnnouncementTask task;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_expert_opinion);
		application = MainApplication.getApplication();
		application.setActivity(this);
		initView();
		if (task != null)
			task.cancel(true);
		task = new GetAnnouncementTask();
		task.execute();
	}

	private void initView() {
		titleLeftBtn = (TextView) findViewById(R.id.btn_title_left);
		titleRightBtn = (TextView) findViewById(R.id.btn_title_right);
		titleContentView = (TextView) findViewById(R.id.txt_title_content);
		titleLeftBtn.setOnClickListener(this);
		titleRightBtn.setVisibility(View.GONE);
		titleContentView.setText(R.string.profession_opinion);

		announceLay = (LinearLayout) findViewById(R.id.lay_announce);
		announcementView = (TextView) findViewById(R.id.txt_announcement);
		questionLay = (RelativeLayout) findViewById(R.id.lay_online_question);
		buyLay = (RelativeLayout) findViewById(R.id.lay_buy_info);
		payLay = (RelativeLayout) findViewById(R.id.lay_online_pay);
		voiceClassLay = (RelativeLayout) findViewById(R.id.lay_voice_class);
		commentLay = (RelativeLayout) findViewById(R.id.lay_commemt);
		questionLay.setOnClickListener(this);
		buyLay.setOnClickListener(this);
		payLay.setOnClickListener(this);
		voiceClassLay.setOnClickListener(this);
		commentLay.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.btn_title_left:
			finish();
			break;
		case R.id.lay_online_question:
			intent = new Intent(ExpertOpinionActivity.this,
					OnLineQuestionActivity.class);
			startActivity(intent);
			break;
		case R.id.lay_voice_class:
			// if (application.getUserInfo() == null) {
			// startActivity(new Intent(ExpertOpinionActivity.this,
			// LoadActivity.class));
			// }else {
			intent = new Intent(ExpertOpinionActivity.this,
					VoiceClassActivity.class);
			startActivity(intent);
			// }
			break;
		case R.id.lay_commemt:
			if (application.getUserInfo() == null) {
				showToast(R.string.load_first);
				startActivity(new Intent(ExpertOpinionActivity.this,
						LoadActivity.class));
			} else {
				Intent innIntent = new Intent(ExpertOpinionActivity.this,
						CommentActivity.class);
				startActivity(innIntent);
			}
			break;
		}
	}

	private class GetAnnouncementTask extends
			AsyncTask<Void, Void, List<String>> {

		@Override
		protected List<String> doInBackground(Void... params) {
			String json = HttpPostRequest.getDataFromWebServer(
					ExpertOpinionActivity.this, "getAnnouncement", null);
			System.out.println("请求通知返回: " + json);
			try {
				if (json == null) {
					return null;
				}
				List<String> list = new ArrayList<String>();
				JSONObject object = new JSONObject(json);
				JSONArray array = object.getJSONArray("list");
				for (int i = 0; i < array.length(); i++) {
					JSONObject jsonObject = array.getJSONObject(i);
					list.add(jsonObject.getString("result"));
				}
				return list;
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onPostExecute(List<String> result) {
			task = null;
			if (null == result)
				return;
			if (result.size() > 0) {
				announceLay.setVisibility(View.VISIBLE);
				announcementView.setText(result.get(0));
			}
		}
	}

}
