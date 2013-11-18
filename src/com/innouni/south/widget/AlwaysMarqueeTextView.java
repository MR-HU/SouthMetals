package com.innouni.south.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 总是获得焦点,持续跑马灯效果
 * @author Huguojun
 * @data 2013-09-27
 */
public class AlwaysMarqueeTextView extends TextView {

	public AlwaysMarqueeTextView(Context context) {
		super(context);
	}

	public AlwaysMarqueeTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AlwaysMarqueeTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean isFocused() {
		return true;
	}
}