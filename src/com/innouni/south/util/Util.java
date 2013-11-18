package com.innouni.south.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Environment;

/**
 * {@code Util}������<br>
 * <li></li>Rules:ʹ��ǰ�Ȼ�ȡʵ�� {@link #getInstance},���־�̬��������ֱ����{@code Util}����<br>
 * <li></li>���ⷽ��
 * {@link #IsCanUseSdCard},
 * {@link #getMemoryClass},
 * {@link #hasExternalCacheDir}......
 * 
 * @author HuGuojun
 * @data 2013-08-13
 */
public class Util {
	
	static String CACHESUFFIX = ".CACHEIMG";
	
	static Util utils = new Util();
	
	/**
	 * ��ȡ{@link Util} ʵ��
	 * @param context {@link Context}
	 * @return Utils
	 */
	public static Util getInstance(){
		return utils;
	}
	
	/**
	 * �ж�SDCard�Ƿ����
	 * @return ���÷���true, �����÷���false
	 * @category ��̬�������� 
	 */
	public static boolean IsCanUseSdCard() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}
	
	/**  
	 * ��ȡ�豸�����app�Ŀ����ڴ�
	 * @param context 
	 * @return ����ʹ�õ��ڴ��С
	 * @category ʵ���������� 
	 */
	public int getMemoryClass(Context context) {
        return ((ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE)).getMemoryClass();
	}
	
	/**
	 * �ж��Ƿ������õĻ�ȡ�ⲿ�洢����(Context.getExternalCacheDir())<br>
	 * �����ж�ϵͳ�汾�Ƿ����2.2
	 * @return �Ƿ���true,�񷵻�false
	 * @category ʵ����������
	 */
	public boolean hasExternalCacheDir() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
	}
	
	/**
	 * ���������Ҫ������������ͼƬʱ�򵥵Ĵ���һ�»����ļ�����<br>
	 * <br>�ַ���ת��
	 * @param str ��Ҫת�����ַ�
	 * @return ת�����ַ�(��д)
	 */
	public static String convertStr(String str){
		String lowerStr = "";
		try {
			lowerStr = str.toLowerCase().replace("http://", "");
			lowerStr = lowerStr.substring(lowerStr.indexOf("/"), lowerStr.length());
			return lowerStr.replace("/", "")
					.replace("images", "")
					.replace("image", "")
					.replace(":", "")
					.replace(".", "")
					.replace("-", "")
					.replace("jpg", "")
					.replace("png", "").toUpperCase().trim() + CACHESUFFIX;
		} catch (Exception e) {
			lowerStr = "cachetemp" + CACHESUFFIX;
		}
		return lowerStr;
	}
	
	public static String getFormatDate(String strTime) {
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
		 Date date = null;
		 String result = "";
		try {
			if(strTime == null){
				result = "";
			}else{
				date = sdf.parse(strTime);
				result = sdf.format(date); 
			}
		} catch (Exception e) {
			result = strTime;
		}
		return (result.equalsIgnoreCase("null") || result == null) ? "" : result;
	}
   
	public static String getFormatTimeMM(String strTime) {
		 SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss"); 
		 Date date = null;
		 String result = "";
		try {
			if(strTime == null){
				result = "";
			}else{
				date = sdf.parse(strTime);
				result = sdf.format(date); 
			}
		} catch (Exception e) {
			result = strTime;
		}
		return (result.equalsIgnoreCase("null") || result == null) ? "" : result;
	}
	
	public static String getFormatTime(String strTime) {
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		 Date date = null;
		 String result = "";
		try {
			if(strTime == null){
				result = "";
			}else{
				date = sdf.parse(strTime);
				result = sdf.format(date); 
			}
		} catch (Exception e) {
			result = strTime;
		}
		return (result.equalsIgnoreCase("null")||result==null)?"":result;
	}
	
	/**
	 * ��������ʽ
	 * @param email
	 * @return ��ʽ��ȷ����true
	 */
	public static boolean checkEmail(String email){
		String emailPattern = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
		Pattern pattern = Pattern.compile(emailPattern);
		Matcher matcher = pattern.matcher(email);
		return matcher.find();
	}
	
	/**
	 * �������ʽ
	 * @param mobile
	 * @return ��ʽ��ȷ����true
	 */
	public static boolean checkMobile(String mobile){
		String mobilePattern = "^1[3,4,5,8]\\d{9}$";
		return mobile.matches(mobilePattern);
	}
	
	/** 
     * dipת����px 
     */  
    public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
  
    /** 
     * pxת����dip 
     */  
    public static int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }  
    
    /**
     * ��spֵת��Ϊpxֵ����֤���ִ�С����
     * 
     * @param spValue
     * @param fontScale (DisplayMetrics��������scaledDensity)
     * @return
     */
    public static int sp2px(float spValue, float fontScale) {
    	return (int) (spValue * fontScale + 0.5f);
    }
    
    /**
     * dipת����px ��֤���벻�� 
     * @param dpValue
     * @param scale (DisplayMetrics��������density)
     * @return
     */
    public static int dip2px(float dpValue, float scale) {
        return (int) (dpValue * scale + 0.5f);  
    }   
    
}
