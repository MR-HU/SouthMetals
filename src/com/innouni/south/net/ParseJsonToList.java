package com.innouni.south.net;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

/**
 * ��Json����ת���ɶ�Ӧʵ���List
 * @author HuGuojun
 * @data 2013-09-03
 */
public class ParseJsonToList {
	
	static ParseJsonToList parser = new ParseJsonToList();
	
	public static ParseJsonToList getInstance(){
		return null == parser ? new ParseJsonToList() : parser;
	}
	
 	/**
	 * ��������WebServer�󷵻ص�������Ϣ����װ��List<Object>
	 * @param jsonArray ��ʾJson�����Json�ַ���
	 * @param cla �����ʵ����
	 * @return ������Ҫ��List���� ��ϸ˵�� ͨ��������ж����ݵķ�װ
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
							// �����������ƻ�ö�Ӧ��setXXX������
							String methodName = "set"
									+ Character.toUpperCase(fieldName
											.charAt(0))
									+ fieldName.substring(1);
							// ���ݷ��������Ҫִ�еķ���
							Method method = cla.getMethod(methodName,
									new Class[] { field.getType() });
							// ���÷���ִ��setXX��������װ����
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
