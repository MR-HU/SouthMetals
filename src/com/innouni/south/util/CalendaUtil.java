package com.innouni.south.util;

import android.annotation.SuppressLint;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日历工具类
 * 
 * @author HuGuojun
 * @date 2013-11-22 上午10:28:33
 * @modify
 * @version 1.0.0
 */
@SuppressLint("SimpleDateFormat")
public class CalendaUtil {
	
	/**
	 * 获取当前日期
	 * @return String
	 * @exception
	 */
	public static String GetCurrentDate(){   
	    Date date = new Date();   
	    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");   
	    return format.format(date);   
	} 

	/**
	 * 获取某一天的前一天
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
	 * 获取某一天的后一天
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
	 * 根據年月获取当月天数
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
	 * 格式化时间
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
	 * 获取某一天是星期几
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
