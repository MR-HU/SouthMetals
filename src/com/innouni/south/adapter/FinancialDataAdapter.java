package com.innouni.south.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.innouni.south.activity.R;
import com.innouni.south.base.ArrayListAdapter;
import com.innouni.south.entity.FinancialData;

/**
 * 财经指数的适配器
 * @author HuGuojun
 * @data 2013-09-04
 */
public class FinancialDataAdapter extends ArrayListAdapter<Object> {

	private LayoutInflater inflater = null;
	private Resources resources;
	
	public FinancialDataAdapter(Context context) {
		super(context);
		inflater = LayoutInflater.from(context);
		resources = context.getResources();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		FinancialData data = null;
		ViewHolder viewHolder = null;
		if (null == convertView) {
			convertView = inflater.inflate(R.layout.item_financial_data_listview, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.nameView = (TextView) convertView.findViewById(R.id.txt_financial_name);
			viewHolder.currentView = (TextView) convertView.findViewById(R.id.txt_financial_current);
			viewHolder.openView = (TextView) convertView.findViewById(R.id.txt_financial_open);
			viewHolder.closeView = (TextView) convertView.findViewById(R.id.txt_financial_close);
			viewHolder.upView = (TextView) convertView.findViewById(R.id.txt_financial_up);
			viewHolder.percentView = (TextView) convertView.findViewById(R.id.txt_financial_percent);
			convertView.setTag(viewHolder);
		} else { 
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		data = (FinancialData) mList.get(position);
		viewHolder.nameView.setText(data.getName().toString());
		viewHolder.currentView.setText(data.getCurrent().toString());
		viewHolder.openView.setText(data.getOpen().toString());
		viewHolder.closeView.setText(data.getClose().toString()); 
		viewHolder.upView.setText(data.getUp().toString());
		viewHolder.percentView.setText(data.getPercent().toString()); 
		//根据涨跌设置字体颜色,上涨为红色,下跌为绿色
		if(Float.valueOf(data.getUp().toString()) < 0.0f){
			viewHolder.currentView.setTextColor(resources.getColor(R.color.green));
			viewHolder.openView.setTextColor(resources.getColor(R.color.green));
			viewHolder.closeView.setTextColor(resources.getColor(R.color.green));
			viewHolder.upView.setTextColor(resources.getColor(R.color.green));
			viewHolder.percentView.setTextColor(resources.getColor(R.color.green));
		} else {
			viewHolder.currentView.setTextColor(resources.getColor(R.color.red));
			viewHolder.closeView.setTextColor(resources.getColor(R.color.red));
			viewHolder.openView.setTextColor(resources.getColor(R.color.red));
			viewHolder.upView.setTextColor(resources.getColor(R.color.red));
			viewHolder.percentView.setTextColor(resources.getColor(R.color.red));
		}
		
		return convertView;
	}
	
	static class ViewHolder {
		TextView nameView;
		TextView currentView;
		TextView openView;
		TextView closeView;
		TextView upView;
		TextView percentView;
	}
}
