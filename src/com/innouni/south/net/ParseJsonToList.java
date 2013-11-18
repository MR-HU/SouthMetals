package com.innouni.south.net;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

/**
 * 将Json数组转换成对应实体的List
 * @author HuGuojun
 * @data 2013-09-03
 */
public class ParseJsonToList {
	
	static ParseJsonToList parser = new ParseJsonToList();
	
	public static ParseJsonToList getInstance(){
		return null == parser ? new ParseJsonToList() : parser;
	}
	
 	/**
	 * 解析请求WebServer后返回的数据信息并封装成List<Object>
	 * @param jsonArray 表示Json数组的Json字符串
	 * @param cla 传入的实体类
	 * @return 返回所要的List数组 详细说明 通过反射进行对数据的封装
	 */
	public ArrayList<Object> parseWebDataToList(String jsonArray, Class<?> cla) {
		ArrayList<Object> list = new ArrayList<Object>();
		try {
			if (null != jsonArray && !"".equals(jsonArray)) {
				Field[] fields = cla.getDeclaredFields();
				JSONArray array = new JSONObject(jsonArray).getJSONArray("list");
				if (array.length() > 0) {
					for (int i = 0; i < array.length(); i++) {
						Object object = cla.newInstance();
						JSONObject jsonObject = array.getJSONObject(i);
						for (int j = 0; j < fields.length; j++) {
							Field field = fields[j];
							String fieldName = field.getName();
							// 根据属性名称获得对应的setXXX方法名
							String methodName = "set"
									+ Character.toUpperCase(fieldName
											.charAt(0))
									+ fieldName.substring(1);
							// 根据方法名获得要执行的方法
							Method method = cla.getMethod(methodName,
									new Class[] { field.getType() });
							// 利用反射执行setXX方法，封装数据
							Object filedValue = null;
							try {
								filedValue = jsonObject.get(fieldName);
								if (null != filedValue) {
									method.invoke(object, filedValue);
								}
							} catch (Exception e) {
								Log.v("TAG", "No value for " + filedValue);
							}
						}
						list.add(object);
					}
				}
			}
		} catch (Exception e) {
			Log.v("TAG", " ERROR: JsonParse web data json parse error "
					+ e.getMessage());
		}
		return list;
	}
}
