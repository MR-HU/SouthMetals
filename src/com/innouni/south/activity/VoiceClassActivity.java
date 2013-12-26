package com.innouni.south.activity;

import uk.co.senab.photoview.PhotoViewAttacher;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.innouni.south.app.MainApplication;
import com.innouni.south.base.BaseActivity;

/**
 * ”Ô“ÙΩÃ ““≥√Ê
 * 
 * @author HuGuojun
 * @date 2013-11-26 œ¬ŒÁ3:33:23
 * @modify
 * @version 1.0.0
 */
public class VoiceClassActivity extends BaseActivity implements OnClickListener {	
	
	private ImageView imageView;
	private PhotoViewAttacher attacher;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_voice_class);
		application = MainApplication.getApplication();
		application.setActivity(this);
		initView();
	}

	private void initView() {
		titleLeftBtn = (TextView) findViewById(R.id.btn_title_left);
		titleRightBtn = (TextView) findViewById(R.id.btn_title_right);
		titleContentView = (TextView) findViewById(R.id.txt_title_content);
		titleLeftBtn.setOnClickListener(this);
		titleRightBtn.setVisibility(View.GONE);
		titleContentView.setText("”Ô“ÙΩÃ “");
		
		imageView = (ImageView) findViewById(R.id.image_voice_class);
		attacher = new PhotoViewAttacher(imageView);
		attacher.canZoom();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_title_left:
			finish();
			break;
		}
	}
}
