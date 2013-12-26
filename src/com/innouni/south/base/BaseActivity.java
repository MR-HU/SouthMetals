package com.innouni.south.base;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.innouni.south.app.MainApplication;

/**
 * »ùÀà
 * @author HuGuojun
 * @data 2013-08-12
 */
public class BaseActivity extends Activity {
	
	protected TextView titleLeftBtn;
	protected TextView titleRightBtn;
	protected TextView titleContentView;
	protected ProgressBar titleRefreshBar;
	
	protected MainApplication application;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPostResume() {
		super.onPostResume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	protected void showToast(int resourceId){
		Toast.makeText(this, resourceId, Toast.LENGTH_LONG).show();
	}
	
	protected void showToast(String text){
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	}
}
