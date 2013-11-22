package com.innouni.south.util;

import android.annotation.SuppressLint;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * ����������
 * 
 * @author HuGuojun
 * @date 2013-11-22 ����10:28:33
 * @modify
 * @version 1.0.0
 */
@SuppressLint("SimpleDateFormat")
public class CalendaUtil {
	
	/**
	 * ��ȡ��ǰ����
	 * @return String
	 * @exception
	 */
	public static String GetCurrentDate(){   
	    Date date = new Date();   
	    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");   
	    return format.format(date);   
	} 

	/**
	 * ��ȡĳһ���ǰһ��
	 * @param specifiedDay
	 * @return
	 * @throws Exception
	 */
	public static String getSpecifiedDayBefore(String specifiedDay){
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day-1);
		String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		return dayBefore;
	}
	
	/**
	 * ��ȡĳһ��ĺ�һ��
	 * @param specifiedDay
	 * @return
	 */
	public static String getSpecifiedDayAfter(String specifiedDay){
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day+1);
		String dayAfter = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		return dayAfter;
	}
	 
	/**
	 * �������»�ȡ��������
	 * @param year
	 * @param month
	 * @return int
	 */
	public static int getDaysOfMonth(int year, int month){  
        Calendar cal = Calendar.getInstance();  
        cal.set(Calendar.YEAR, year);  
        cal.set(Calendar.MONTH, month-1);  
        int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);  
        return days;  
    }  
	
	/**
	 * ��ʽ��ʱ��
	 * @param year
	 * @param month
	 * @param day
	 * @return Date
	 */
	public static Date getFormatDate(int year, int month, int day) {
		String strDate = year + "-" + month + "-" + day;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return format.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * ��ȡĳһ�������ڼ�
	 * @param specifiedDay
	 * @return String
	 * @exception
	 */
	public static String getWeek(String specifiedDay){ 
		Date date = null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
        SimpleDateFormat format = new SimpleDateFormat("EEEE");  
        String week = format.format(date);  
        return week;  
    }  
	
}
