package com.innouni.south.pref;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * ÅäÖÃÎÄ¼þ
 * 
 * @author HuGuojun
 * @date 2013-11-28 ÏÂÎç2:26:49
 * @modify
 * @version 1.0.0
 */
public class GlobalConfigPreferences {
	
	private  SharedPreferences sp;
	
	public GlobalConfigPreferences(Context context) {
		sp = context.getSharedPreferences("globalconfig", Context.MODE_PRIVATE);
	}
	
	public SharedPreferences getSp(){
		return sp;
	}
	
	public boolean updateSp(String key, Object value) {
		SharedPreferences.Editor editor = sp.edit();
		if(value instanceof String) {
			editor.putString(key, value.toString());
		} else if(value instanceof Integer) {
			editor.putInt(key, ((Integer)value).intValue());
		} else if(value instanceof Boolean) {
			editor.putBoolean(key, ((Boolean)value).booleanValue());
		} else if(value instanceof Long) {
			editor.putLong(key, ((Long)value).longValue());
		} else if(value instanceof Float) {
			editor.putFloat(key, ((Float)value).floatValue());
		} else{
			editor.putString(key, value.toString());
		}
		return editor.commit();
	}
}
