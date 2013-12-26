package com.innouni.south.base;

import android.app.ActivityGroup;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.innouni.south.app.MainApplication;

/**
 * Activity����
 * @author HuGuojun
 * @data 2013-09-04
 */
public abstract class BaseActivityGroup extends ActivityGroup {

	/**
	 * ����Activity���ֵ�����
	 * @return
	 */
	protected abstract FrameLayout getContainer(); 
	
	protected MainApplication appClication;
	
	protected TextView titleLeftBtn;
	protected TextView titleRightBtn;
	protected TextView titleContentView;

	/**
	 * ������ͼ
	 * @param cls
	 */
	protected void openView(Class<?> cls) {
		Intent intent = new Intent(this,cls);
		getContainer().removeAllViews();
		View view = getLocalActivityManager().startActivity(
					intent.getComponent().getShortClassName(), intent)
					.getDecorView();
		getContainer().addView(view);
		view.requestFocus();
	}
	
}
