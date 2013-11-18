package com.innouni.south.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

/**
 * {@code NetUtils}���繤����<br>
 * <li></li>�ж�����״̬��ģʽ<br>
 * <li></li>���ⷽ����
 * {@link #isAirplaneMode},
 * {@link #isNetworkAvailable},
 * {@link #isURLAvailable}
 * 
 * @author HuGuojun
 * @data 2013-08-12
 */
public class NetUtil {
	
	/**
	 * �жϷ���ģʽ;
	 * @param context ������
	 * @return true����ģʽ   false�Ƿ���ģʽ
	 * @category ��̬���÷��� 
	 */
	public static boolean isAirplaneMode(Context context){
		return Settings.System.getInt(context.getContentResolver(),
				Settings.System.AIRPLANE_MODE_ON, 0) == 1;
	}
	
	/**
	 * �ж������Ƿ����;
	 */
	public static boolean isNetworkAvailable(Context context){
		ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo = manager.getActiveNetworkInfo();
		if(networkinfo == null)
			return false;
		return networkinfo.isAvailable() && networkinfo.isConnected();
	}
	
	/**
	 * �ж�URL�Ƿ����;
	 */
	public static boolean isURLAvailable(Context context,String strUrl){
		boolean result = false;
		if(isNetworkAvailable(context)){
			try {
				URL url = new URL(strUrl);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(3*1000);
				if (conn.getResponseCode() != HttpURLConnection.HTTP_NOT_FOUND) {
					result = true;
				}
			} catch (IOException e) {
				result = false;
			}
		}
		return result;
	}
}
